/*
 * Copyright 2011 Google Inc.
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

package com.google.template.soy.soytree;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.template.soy.base.SourceLocation;
import com.google.template.soy.base.internal.BaseUtils;
import com.google.template.soy.base.internal.LegacyInternalSyntaxException;
import com.google.template.soy.basetree.CopyState;
import com.google.template.soy.exprtree.ExprNode;
import com.google.template.soy.exprtree.ExprRootNode;
import com.google.template.soy.exprtree.GlobalNode;
import com.google.template.soy.exprtree.IntegerNode;
import com.google.template.soy.exprtree.StringNode;
import com.google.template.soy.soytree.SoyNode.ExprHolderNode;
import com.google.template.soy.soytree.defn.TemplateParam;

import java.util.List;

/**
 * Node representing a delegate template.
 *
 * <p>Important: Do not use outside of Soy code (treat as superpackage-private).
 *
 */
public final class TemplateDelegateNode extends TemplateNode implements ExprHolderNode {

  /**
   * Value class for a delegate template key (name and variant).
   *
   * <p> Important: Do not use outside of Soy code (treat as superpackage-private).
   */
  public static final class DelTemplateKey {

    public static DelTemplateKey create(String name, String variant) {
      return new DelTemplateKey(name, variant);
    }

    public String name() { return name; }
    public String variant() { return variant; }

    @Override public String toString() {
      return name() + (variant().isEmpty() ? "" : ":" + variant());
      }

    private final String name;
    private final String variant;

    DelTemplateKey(String name, String variant) {
      this.name = name;
      this.variant = variant;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((variant == null) ? 0 : variant.hashCode());
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
      DelTemplateKey other = (DelTemplateKey) obj;
      if (name == null) {
        if (other.name != null)
          return false;
      } else if (!name.equals(other.name))
        return false;
      if (variant == null) {
        if (other.variant != null)
          return false;
      } else if (!variant.equals(other.variant))
        return false;
      return true;
    }
  }

  /** The delegate template name. */
  private final String delTemplateName;

  /** The delegate template variant. */
  private String delTemplateVariant;

  /** An expression that defines a delegate template variant. */
  private final ExprRootNode delTemplateVariantExpr;

  /** The delegate template key (name and variant). */
  private DelTemplateKey delTemplateKey;

  /** The delegate priority. */
  private final Priority delPriority;

  /**
   * Main constructor. This is package-private because TemplateDelegateNode instances should be
   * built using TemplateDelegateNodeBuilder.
   *
   * @param nodeBuilder Builder containing template initialization params.
   * @param soyFileHeaderInfo Info from the containing Soy file's header declarations.
   * @param delTemplateName The delegate template name.
   * @param delTemplateVariant The delegate template variant.
   * @param delTemplateVariantExpr An expression that references a delegate template variant.
   * @param delTemplateKey The delegate template key (name and variant).
   * @param delPriority The delegate priority.
   * @param params The params from template header or SoyDoc. Null if no decls and no SoyDoc.
   */
  TemplateDelegateNode(
      TemplateDelegateNodeBuilder nodeBuilder,
      SoyFileHeaderInfo soyFileHeaderInfo, String delTemplateName, String delTemplateVariant,
      ExprRootNode delTemplateVariantExpr, DelTemplateKey delTemplateKey, Priority delPriority,
      ImmutableList<TemplateParam> params) {

    super(nodeBuilder, "deltemplate", soyFileHeaderInfo,
        Visibility.PUBLIC /* deltemplate always has public visibility */,
        params);
    this.delTemplateName = delTemplateName;
    this.delTemplateVariant = delTemplateVariant;
    this.delTemplateVariantExpr = delTemplateVariantExpr;
    this.delTemplateKey = delTemplateKey;
    this.delPriority = delPriority;
  }

  /**
   * Copy constructor.
   * @param orig The node to copy.
   */
  private TemplateDelegateNode(TemplateDelegateNode orig, CopyState copyState) {
    super(orig, copyState);
    this.delTemplateName = orig.delTemplateName;
    this.delTemplateVariant = orig.delTemplateVariant;
    this.delTemplateVariantExpr = orig.delTemplateVariantExpr;
    this.delTemplateKey = orig.delTemplateKey;
    this.delPriority = orig.delPriority;
  }

  static void verifyVariantName(String delTemplateVariant, SourceLocation srcLoc) {
    if (delTemplateVariant.length() > 0 && !(BaseUtils.isIdentifier(delTemplateVariant))) {
      throw LegacyInternalSyntaxException.createWithMetaInfo(
          "Invalid variant \""
              + delTemplateVariant
              + "\" in 'deltemplate'"
              + " (when a string literal is used, value must be an identifier).",
          srcLoc);
    }
  }

  @Override public Kind getKind() {
    return Kind.TEMPLATE_DELEGATE_NODE;
  }


  /** Returns the delegate template name. */
  public String getDelTemplateName() {
    return delTemplateName;
  }


  /** Returns the delegate template variant. */
  public String getDelTemplateVariant() {
    if (delTemplateVariant != null) {
      return delTemplateVariant;
    }
    return resolveVariantExpression().variant();
  }


  /** Returns the delegate template key (name and variant). */
  public DelTemplateKey getDelTemplateKey() {
    if (delTemplateKey != null) {
      return delTemplateKey;
    }
    return resolveVariantExpression();
  }


  /** Returns the delegate priority. */
  public Priority getDelPriority() {
    return delPriority;
  }


  @Override public TemplateDelegateNode copy(CopyState copyState) {
    return new TemplateDelegateNode(this, copyState);
  }


  @Override
  public List<ExprUnion> getAllExprUnions() {
    if (delTemplateVariantExpr == null) {
      return ImmutableList.of();
    }
    return ImmutableList.of(new ExprUnion(delTemplateVariantExpr));
  }

  /**
   * When a variant value is not defined at parsing time (e.g. when a global constant is used) the
   * deltemplate variant and deltemplate key fields in this node have null value. To fetch their
   * values, we must lazily resolve the expression, after globals are substituted.
   */
  private DelTemplateKey resolveVariantExpression() {
    if (delTemplateVariantExpr == null || delTemplateVariantExpr.numChildren() != 1) {
      throw invalidExpressionError();
    }
    ExprNode exprNode = delTemplateVariantExpr.getRoot();
    if (exprNode instanceof IntegerNode) {
      // Globals were already substituted: We may now create the definitive variant and key fields
      // on this node.
      int variantValue = ((IntegerNode) exprNode).getValue();
      Preconditions.checkArgument(
          variantValue >= 0,
          "Globals used as deltemplate variants must not evaluate to negative numbers.");
      delTemplateVariant = String.valueOf(variantValue);
      delTemplateKey = DelTemplateKey.create(delTemplateName, delTemplateVariant);
      return delTemplateKey;
    } else if (exprNode instanceof StringNode) {
      // Globals were already substituted: We may now create the definitive variant and key fields
      // on this node.
      delTemplateVariant = ((StringNode) exprNode).getValue();
      TemplateDelegateNode.verifyVariantName(delTemplateVariant, exprNode.getSourceLocation());
      delTemplateKey = DelTemplateKey.create(delTemplateName, delTemplateVariant);
      return delTemplateKey;
    } else if (exprNode instanceof GlobalNode) {
      // This global was not substituted.  This happens when TemplateRegistries are built for
      // message extraction and parseinfo generation.  To make this 'work' we just use the Global
      // name for the variant value.  This is fine and will help catch some errors.
      // Because these nodes won't be used for code generation this should be safe.
      return DelTemplateKey.create(delTemplateName, ((GlobalNode) exprNode).getName());
    } else {
      throw invalidExpressionError();
    }
  }

  private AssertionError invalidExpressionError() {
    return new AssertionError("Invalid expression for deltemplate variant for " + delTemplateName
        + " template");
  }
}
