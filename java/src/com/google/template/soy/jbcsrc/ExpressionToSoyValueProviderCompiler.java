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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.template.soy.jbcsrc.BytecodeUtils.constant;

import com.google.common.base.Optional;
import com.google.template.soy.data.SoyValueProvider;
import com.google.template.soy.exprtree.DataAccessNode;
import com.google.template.soy.exprtree.ExprNode;
import com.google.template.soy.exprtree.ExprRootNode;
import com.google.template.soy.exprtree.NullNode;
import com.google.template.soy.exprtree.OperatorNodes.ConditionalOpNode;
import com.google.template.soy.exprtree.OperatorNodes.NullCoalescingOpNode;
import com.google.template.soy.exprtree.VarRefNode;
import com.google.template.soy.jbcsrc.ExpressionCompiler.BasicExpressionCompiler;
import com.google.template.soy.soytree.defn.InjectedParam;
import com.google.template.soy.soytree.defn.LocalVar;
import com.google.template.soy.soytree.defn.TemplateParam;

import org.objectweb.asm.Label;

import javax.annotation.Nullable;

/**
 * Attempts to compile an {@link ExprNode} to an {@link Expression} for a {@link SoyValueProvider}
 * in order to preserve laziness.
 * 
 * <p>There are two ways to use this depending on the specific requirements of the caller
 * <ul>
 *     <li>{@link #compileAvoidingBoxing(ExprNode, Label)} attempts to compile the expression to a 
 *         {@link SoyValueProvider} but without introducing any unnecessary boxing operations.  
 *         Generating detach logic is OK.  This case is for print operations, where callers may want
 *         to call {@link SoyValueProvider#renderAndResolve} to incrementally print the value.
 *         However, this is only desirable if the expression is naturally a 
 *         {@link SoyValueProvider}.
 *     <li>{@link #compileAvoidingDetaches(ExprNode)} attempts to compile the expression to a 
 *         {@link SoyValueProvider} with no detach logic.  This is for passing data to templates or 
 *         defining variables with {@code let} statements.  In these cases boxing operations are
 *         fine (because the alternative is to use the {@link LazyClosureCompiler} which 
 *         necessarily boxes the expression into a custom SoyValueProvider.
 * </ul>
 * 
 * <p>This is used as a basic optimization and as a necessary tool to implement template 
 * transclusions.  If a template has a parameter {@code foo} then we want to be able to render it 
 * via {@link SoyValueProvider#renderAndResolve} so that we can render it incrementally.
 */
final class ExpressionToSoyValueProviderCompiler {
  /**
   * Create an expression compiler that can implement complex detaching logic with the given
   * {@link ExpressionDetacher.Factory}
   */
  static ExpressionToSoyValueProviderCompiler create(
      ExpressionCompiler exprCompiler, TemplateParameterLookup variables) {
    return new ExpressionToSoyValueProviderCompiler(exprCompiler, variables);
  }

  private final TemplateParameterLookup variables;
  private final ExpressionCompiler exprCompiler;

  private ExpressionToSoyValueProviderCompiler(
      ExpressionCompiler exprCompiler, TemplateParameterLookup variables) {
    this.exprCompiler = exprCompiler;
    this.variables = variables;
  }

  /**
   * Compiles the given expression tree to a sequence of bytecode in the current method visitor.
   * 
   * <p>If successful, the generated bytecode will resolve to a {@link SoyValueProvider} if it can
   * be done without introducing unnecessary boxing operations.  This is intended for situations
   * (like print operations) where calling {@link SoyValueProvider#renderAndResolve} would be better
   * than calling {@link #toString()} and passing directly to the output.
   */
  Optional<Expression> compileAvoidingBoxing(ExprNode node, Label reattachPoint) {
    checkNotNull(node);
    return new CompilerVisitor(variables, null, exprCompiler.asBasicCompiler(reattachPoint))
        .exec(node);
  }

  /**
   * Compiles the given expression tree to a sequence of bytecode in the current method visitor.
   * 
   * <p>If successful, the generated bytecode will resolve to a {@link SoyValueProvider} if it can
   * be done without introducing any detach operations.  This is intended for situations where we
   * need to model the expression as a SoyValueProvider to satisfy a contract (e.g. let nodes and
   * params), but we also want to preserve any laziness.  So boxing is fine, but detaches are not.
   */
  Optional<Expression> compileAvoidingDetaches(ExprNode node) {
    checkNotNull(node);
    return new CompilerVisitor(variables, exprCompiler, null).exec(node);
  }

  private static final class CompilerVisitor
      extends EnhancedAbstractExprNodeVisitor<Optional<Expression>> {
    final TemplateParameterLookup variables;

    // depending on the mode one or the other of these will be null
    @Nullable final ExpressionCompiler exprCompiler;
    @Nullable final BasicExpressionCompiler detachingExprCompiler;

    CompilerVisitor(
        TemplateParameterLookup variables,
        ExpressionCompiler exprCompiler,
        BasicExpressionCompiler detachingExprCompiler) {
      this.variables = variables;
      checkArgument((exprCompiler == null) != (detachingExprCompiler == null));
      this.exprCompiler = exprCompiler;
      this.detachingExprCompiler = detachingExprCompiler;
    }

    private boolean allowsBoxing() {
      return exprCompiler != null;
    }

    private boolean allowsDetaches() {
      return detachingExprCompiler != null;
    }

    @Override protected final Optional<Expression> visitExprRootNode(ExprRootNode node) {
      return visit(node.getRoot());
    }

    // Primitive value constants

    @Override protected Optional<Expression> visitNullNode(NullNode node) {
      // unlike other primitives, this doesn't really count as boxing, just a read of a static
      // constant field. so we always do it
      return Optional.of(FieldRef.NULL_PROVIDER.accessor());
    }

    @Override protected Optional<Expression> visitNullCoalescingOpNode(NullCoalescingOpNode node) {
      // All non-trivial ?: will require detaches for the left hand side.
      if (allowsDetaches()) {
        // for '$foo ?: $bar' we always have to eagerly evaluate $foo but $bar could be lazy.
        // of course if $foo is not null, then we will need to box it, however the current support
        // for ?: in ExpressionCompiler also always boxes everything so we aren't really losing
        // anything here.
        Optional<Expression> right = visit(node.getRightChild());
        if (!right.isPresent()) {
          return Optional.absent();
        }
        Expression left =
            detachingExprCompiler.compile(node.getLeftChild())
                .box()
                .cast(SoyValueProvider.class);
        return Optional.of(BytecodeUtils.firstNonNull(left, right.get()));
      }
      return visitExprNode(node);
    }

    @Override protected final Optional<Expression> visitConditionalOpNode(ConditionalOpNode node) {
      if (allowsDetaches()) {
        Optional<Expression> trueBranch = visit(node.getChild(1));
        Optional<Expression> falseBranch = visit(node.getChild(2));
        // We only compile as an SVP if both branches are able to be compiled as such.  Technically,
        // we could also support cases where only one branch is compilable to an SVP, but i doubt
        // that would be that important.
        if (!trueBranch.isPresent() || !falseBranch.isPresent()) {
          return Optional.absent();
        }
        Expression condition = detachingExprCompiler.compile(node.getChild(0)).coerceToBoolean();
        return Optional.of(BytecodeUtils.ternary(condition, trueBranch.get(), falseBranch.get()));
      }
      return visitExprNode(node);
    }

    @Override Optional<Expression> visitForeachLoopVar(VarRefNode varRef, LocalVar local) {
      return Optional.of(variables.getLocal(local));
    }

    @Override Optional<Expression> visitParam(VarRefNode varRef, TemplateParam param) {
      return Optional.of(variables.getParam(param));
    }

    @Override Optional<Expression> visitIjParam(VarRefNode node, InjectedParam ij) {
      return Optional.of(
          MethodRef.RUNTIME_GET_FIELD_PROVIDER.invoke(
              variables.getIjRecord(),
              constant(ij.name())));
    }

    @Override Optional<Expression> visitLetNodeVar(VarRefNode varRef, LocalVar local) {
      return Optional.of(variables.getLocal(local));
    }

    @Override protected Optional<Expression> visitDataAccessNode(DataAccessNode node) {
      // TODO(lukes): implement special case for allowsDetaches().  The complex part will be sharing
      // null safety access logic with the ExpressionCompiler
      return visitExprNode(node);
    }

    @Override protected final Optional<Expression> visitExprNode(ExprNode node) {
      if (allowsBoxing()) {
        Optional<SoyExpression> compileWithNoDetaches = exprCompiler.compileWithNoDetaches(node);
        if (compileWithNoDetaches.isPresent()) {
          return Optional.<Expression>of(compileWithNoDetaches.get().box());
        }
      }
      return Optional.absent();
    }
  }
}
