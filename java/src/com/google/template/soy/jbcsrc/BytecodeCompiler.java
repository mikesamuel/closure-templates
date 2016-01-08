/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.template.soy.jbcsrc;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.google.template.soy.error.ErrorReporter;
import com.google.template.soy.error.SoyError;
import com.google.template.soy.jbcsrc.shared.CompiledTemplates;
import com.google.template.soy.soytree.TemplateNode;
import com.google.template.soy.soytree.TemplateRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entry point to the {@code jbcsrc} compiler.
 */
public final class BytecodeCompiler {
  private static final Logger logger = Logger.getLogger(BytecodeCompiler.class.getName());
  /**
   * Compiles all the templates in the given registry.
   *
   * @param registry All the templates to compile
   * @param developmentMode Whether or not we are in development mode.  In development mode we
   *    compile classes lazily
   * @param reporter The error reporter
   * @return CompiledTemplates or {@code absent()} if compilation fails, in which case errors will
   *     have been reported to the error reporter.
   */
  public static Optional<CompiledTemplates> compile(
      TemplateRegistry registry, boolean developmentMode, ErrorReporter reporter) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    ErrorReporter.Checkpoint checkpoint = reporter.checkpoint();
    checkForUnsupportedFeatures(registry, reporter);
    if (reporter.errorsSince(checkpoint)) {
      return Optional.absent();
    }
    CompiledTemplateRegistry compilerRegistry = new CompiledTemplateRegistry(registry);
    if (developmentMode) {
      CompiledTemplates templates = new CompiledTemplates(
          compilerRegistry.getTemplateNames(),
          new CompilingClassLoader(compilerRegistry));
      // TODO(lukes): consider spawning a thread to load all the generated classes in the background
      return Optional.of(templates);
    }

    // TODO(lukes): currently we compile all the classes, but you could easily imagine being
    // configured in such a way that we load the classes from the system class loader.  Then we
    // could add a build phase that writes the compiled templates out to a jar.  Then in the non
    // development mode case we could skip even parsing templates!
    CompilationResult results = compileTemplates(registry, compilerRegistry, reporter);
    if (reporter.errorsSince(checkpoint)) {
      return Optional.absent();
    }
    CompiledTemplates templates =
        new CompiledTemplates(
            compilerRegistry.getTemplateNames(), new MemoryClassLoader(results.classes()));
    logger.log(
        Level.INFO,
        "Compilation took {0}\n"
            + "     templates: {1}\n"
            + "       classes: {2}\n"
            + "         bytes: {3}\n"
            + "        fields: {4}\n"
            + "  detachStates: {5}",
        new Object[] {
          stopwatch.toString(),
          registry.getAllTemplates().size(),
          results.classes().size(),
          results.numBytes(),
          results.numFields(),
          results.numDetachStates()
        });
    stopwatch.reset().start();
    for (String template : compilerRegistry.getTemplateNames()) {
      templates.getTemplateFactory(template);  // triggers classloading
    }
    logger.log(Level.INFO, "Loaded all classes in {0}", stopwatch);
    return Optional.of(templates);
  }

  private static void checkForUnsupportedFeatures(TemplateRegistry registry,
      ErrorReporter errorReporter) {
    UnsupportedFeatureReporter reporter = new UnsupportedFeatureReporter(errorReporter);
    for (TemplateNode node : registry.getAllTemplates()) {
      reporter.check(node);
    }
  }

  @AutoValue
  static final class CompilationResult {
    final List<ClassData> classes;
    final int numBytes;
    final int numFields;
    final int numDetachStates;

    CompilationResult(
        List<ClassData> classes,
        int numBytes,
        int numFields,
        int numDetachStates) {
      this.classes = classes;
      this.numBytes = numBytes;
      this.numFields = numFields;
      this.numDetachStates = numDetachStates;
    }

    List<ClassData> classes() { return classes; }
    int numBytes() { return numBytes; }
    int numFields() { return numFields; }
    int numDetachStates() { return numDetachStates; }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((classes == null) ? 0 : classes.hashCode());
      result = prime * result + numBytes;
      result = prime * result + numDetachStates;
      result = prime * result + numFields;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      CompilationResult other = (CompilationResult) obj;
      if (classes == null) {
        if (other.classes != null)
          return false;
      } else if (!classes.equals(other.classes))
        return false;
      if (numBytes != other.numBytes)
        return false;
      if (numDetachStates != other.numDetachStates)
        return false;
      if (numFields != other.numFields)
        return false;
      return true;
    }
  }

  /**
   * Run the compiler for all templates and return the generated class in a
   * {@link MemoryClassLoader}
   */
  private static CompilationResult compileTemplates(
      TemplateRegistry registry,
      CompiledTemplateRegistry compilerRegistry,
      ErrorReporter errorReporter) {
    List<ClassData> classes = new ArrayList<>();
    int numBytes = 0;
    int numFields = 0;
    int numDetachStates = 0;
    // We generate all the classes and then start loading them.  This 2 phase process ensures that
    // we don't have to worry about ordering (where a class we have generated references a class we
    // haven't generated yet), because none of the classes are loadable until they all are.
    for (TemplateNode template : registry.getAllTemplates()) {
      String name = template.getTemplateName();
      logger.log(Level.FINE, "Compiling template: {0}", name);
      try {
        CompiledTemplateMetadata classInfo = compilerRegistry.getTemplateInfoByTemplateName(name);
        TemplateCompiler templateCompiler = new TemplateCompiler(compilerRegistry, classInfo);
        for (ClassData clazz : templateCompiler.compile()) {
          // This loop is relatively hot, so this actually makes a detectable difference :(
          if (logger.isLoggable(Level.FINE)) {
            logger.log(
                Level.FINE,
                "Generated class {0}.  size: {1}, fields: {2}, detachStates: {3}",
                new Object[] {clazz.type().className(), clazz.data().length,
                    clazz.numberOfFields(), clazz.numberOfDetachStates()});
          }
          numBytes += clazz.data().length;
          numFields += clazz.numberOfFields();
          numDetachStates += clazz.numberOfDetachStates();
          if (Flags.DEBUG) {
            clazz.checkClass();
          }
          classes.add(clazz);
        }
      // Report unexpected errors and keep going to try to collect more.
      } catch (UnexpectedCompilerFailureException e) {
        errorReporter.report(e.getOriginalLocation(),
            SoyError.of("Unexpected error while compiling template: ''{0}''\nSoy Stack:\n{1}"
                + "\nCompiler Stack:{2}"),
            name,
            e.printSoyStack(),
            Throwables.getStackTraceAsString(e));

      } catch (Throwable t) {
        errorReporter.report(template.getSourceLocation(),
            SoyError.of("Unexpected error while compiling template: ''{0}''\n{1}"),
            name,
            Throwables.getStackTraceAsString(t));
      }
    }
    return new CompilationResult(
        classes, numBytes, numFields, numDetachStates);
  }

  private BytecodeCompiler() {}
}
