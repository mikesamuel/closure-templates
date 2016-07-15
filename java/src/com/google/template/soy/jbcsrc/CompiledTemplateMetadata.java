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

import static com.google.template.soy.jbcsrc.BytecodeUtils.SOY_RECORD_TYPE;

import com.google.auto.value.AutoValue;
import com.google.template.soy.jbcsrc.api.AdvisingAppendable;
import com.google.template.soy.jbcsrc.shared.CompiledTemplate;
import com.google.template.soy.jbcsrc.shared.Names;
import com.google.template.soy.jbcsrc.shared.RenderContext;
import com.google.template.soy.soytree.TemplateNode;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

/**
 * Information about a compiled template.
 *
 * <p>This should contain basic information about a single template that will be useful for
 * generating that template as well as calls to the template.
 */
@AutoValue final class CompiledTemplateMetadata {
  /**
   * The {@link Method} signature of all generated constructors for the {@link CompiledTemplate}
   * classes.
   */
  private static final Method GENERATED_CONSTRUCTOR =
      new Method(
          "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, SOY_RECORD_TYPE, SOY_RECORD_TYPE));

  /**
   * The {@link Method} signature of the
   * {@link CompiledTemplate#render(AdvisingAppendable, RenderContext)}
   * method.
   */
  private static final Method RENDER_METHOD;

  /**
   * The {@link Method} signature of the {@link CompiledTemplate#kind()} method.
   */
  private static final Method KIND_METHOD;

  static {
    try {
      RENDER_METHOD =
          Method.getMethod(
              CompiledTemplate.class.getMethod(
                  "render", AdvisingAppendable.class, RenderContext.class));
      KIND_METHOD = Method.getMethod(CompiledTemplate.class.getMethod("kind"));
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  static CompiledTemplateMetadata create(String templateName, TemplateNode node) {
    String className = Names.javaClassNameFromSoyTemplateName(templateName);
    TypeInfo type = TypeInfo.create(className);
    return new CompiledTemplateMetadata(
        ConstructorRef.create(type, GENERATED_CONSTRUCTOR),
        MethodRef.createInstanceMethod(type, RENDER_METHOD).asNonNullable(),
        MethodRef.createInstanceMethod(type, KIND_METHOD).asCheap(),
        type,
        node);
  }

  /**
   * The template constructor.
   *
   * <p>The constructor has the same interface as
   * {@link com.google.template.soy.jbcsrc.shared.CompiledTemplate.Factory#create}
   */
  ConstructorRef constructor() { return constructor; }

  /** The {@link CompiledTemplate#render(AdvisingAppendable, RenderContext)} method. */
  MethodRef renderMethod() { return renderMethod; }

  /** The {@link CompiledTemplate#kind()} method. */
  abstract MethodRef kindMethod();

  /** The name of the compiled template. */
  TypeInfo typeInfo() { return typeInfo; }

  /** The actual template. */
  TemplateNode node() { return node; }

  final ConstructorRef constructor;
  final MethodRef renderMethod;
  final TypeInfo typeInfo;
  final TemplateNode node;

  CompiledTemplateMetadata(
      ConstructorRef constructor,
      MethodRef renderMethod,
      TypeInfo typeInfo,
      TemplateNode node) {
    this.constructor = constructor;
    this.renderMethod = renderMethod;
    this.typeInfo = typeInfo;
    this.node = node;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((constructor == null) ? 0 : constructor.hashCode());
    result = prime * result + ((node == null) ? 0 : node.hashCode());
    result = prime * result + ((renderMethod == null) ? 0 : renderMethod.hashCode());
    result = prime * result + ((typeInfo == null) ? 0 : typeInfo.hashCode());
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
    CompiledTemplateMetadata other = (CompiledTemplateMetadata) obj;
    if (constructor == null) {
      if (other.constructor != null)
        return false;
    } else if (!constructor.equals(other.constructor))
      return false;
    if (node == null) {
      if (other.node != null)
        return false;
    } else if (!node.equals(other.node))
      return false;
    if (renderMethod == null) {
      if (other.renderMethod != null)
        return false;
    } else if (!renderMethod.equals(other.renderMethod))
      return false;
    if (typeInfo == null) {
      if (other.typeInfo != null)
        return false;
    } else if (!typeInfo.equals(other.typeInfo))
      return false;
    return true;
  }
}
