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

import com.google.auto.value.AutoValue;

import org.objectweb.asm.Type;

/**
 * A wrapper around {@link Type} that provides some additional methods and accessor caching.
 *
 * <p>Also, unlike {@link Type} this only represents the name of a class, it doesn't attempt to
 * represent primitive or array types or method descriptors.
 */
@AutoValue final class TypeInfo {
  static TypeInfo create(Class<?> clazz) {
    Type type = Type.getType(clazz);
    return new TypeInfo(clazz.getName(), clazz.getSimpleName(), type.getInternalName(),
        type);
  }

  static TypeInfo create(String className) {
    // Translates a java class name (foo.bar.Baz$Quux) to a java 'internal' name and then translates
    // that to a Type object
    Type type = Type.getObjectType(className.replace('.', '/'));
    // This logic is specified by Class.getSimpleName()
    String packageLessName = className.substring(className.lastIndexOf('.') + 1);
    String simpleName = packageLessName.substring(packageLessName.lastIndexOf('$') + 1);
    return new TypeInfo(
        className,
        simpleName,
        type.getInternalName(),
        type);
  }

  String className() { return className; }
  String simpleName() { return simpleName; }
  String internalName() { return internalName; }
  Type type() { return type; }

  private final String className;
  private final String simpleName;
  private final String internalName;
  private final Type type;

  TypeInfo(
      String className,
      String simpleName,
      String internalName,
      Type type) {
    this.className = className;
    this.simpleName = simpleName;
    this.internalName = internalName;
    this.type = type;
  }
  /** Returns a new {@link TypeInfo} for an inner class of this class. */
  final TypeInfo innerClass(String simpleName) {
    checkArgument(simpleName.indexOf('$') == -1,
        "Simple names shouldn't contain '$': %s", simpleName);
    String className = className() + '$' + simpleName;
    String internalName = internalName() + '$' + simpleName;
    Type type = Type.getObjectType(internalName);
    return new TypeInfo(className, simpleName, internalName, type);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((className == null) ? 0 : className.hashCode());
    result = prime * result + ((internalName == null) ? 0 : internalName.hashCode());
    result = prime * result + ((simpleName == null) ? 0 : simpleName.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    TypeInfo other = (TypeInfo) obj;
    if (className == null) {
      if (other.className != null)
        return false;
    } else if (!className.equals(other.className))
      return false;
    if (internalName == null) {
      if (other.internalName != null)
        return false;
    } else if (!internalName.equals(other.internalName))
      return false;
    if (simpleName == null) {
      if (other.simpleName != null)
        return false;
    } else if (!simpleName.equals(other.simpleName))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    return true;
  }
}
