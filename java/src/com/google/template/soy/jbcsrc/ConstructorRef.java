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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.template.soy.data.SoyRecord;
import com.google.template.soy.data.internal.AugmentedParamStore;
import com.google.template.soy.data.internal.BasicParamStore;
import com.google.template.soy.jbcsrc.Expression.Feature;
import com.google.template.soy.jbcsrc.api.AdvisingStringBuilder;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * A reference to a type that can be constructed at runtime.
 */
final class ConstructorRef {
  /**
   * Returns a new {@link ConstructorRef} that refers to a constructor on the given type with the
   * given parameter types.
   */
  static ConstructorRef create(TypeInfo type, Method init) {
    checkArgument(init.getName().equals("<init>")  && init.getReturnType().equals(Type.VOID_TYPE),
        "'%s' is not a valid constructor", init);
    return new ConstructorRef(
        type,
        init,
        ImmutableList.copyOf(init.getArgumentTypes()));
  }

  /**
   * Returns a new {@link ConstructorRef} that refers to a constructor on the given type with the
   * given parameter types.
   */
  static ConstructorRef create(TypeInfo type, Iterable<Type> argTypes) {
    return create(
        type,
        new Method("<init>", Type.VOID_TYPE, Iterables.toArray(argTypes, Type.class)));
  }

  static ConstructorRef create(Class<?> clazz, Class<?>... argTypes) {
    TypeInfo type = TypeInfo.create(clazz);
    Constructor<?> c;
    try {
      c = clazz.getConstructor(argTypes);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
    Type constructorType = Type.getType(c);

    return new ConstructorRef(
        type,
        Method.getMethod(c),
        ImmutableList.copyOf(constructorType.getArgumentTypes()));
  }

  static final ConstructorRef ARRAY_LIST_SIZE = create(ArrayList.class, int.class);
  static final ConstructorRef LINKED_HASH_MAP_SIZE = create(LinkedHashMap.class, int.class);
  static final ConstructorRef AUGMENTED_PARAM_STORE =
      create(AugmentedParamStore.class, SoyRecord.class, int.class);
  static final ConstructorRef BASIC_PARAM_STORE = create(BasicParamStore.class, int.class);
  static final ConstructorRef ADVISING_STRING_BUILDER = create(AdvisingStringBuilder.class);

  TypeInfo instanceClass(){ return instanceClass; }
  Method method(){ return method; }
  ImmutableList<Type> argTypes() { return argTypes; }

  /**
   * Returns an expression that constructs a new instance of {@link #instanceClass()} by calling
   * this constructor.
   */
  Expression construct(final Expression ...args) {
    return construct(Arrays.asList(args));
  }

  /**
   * Returns an expression that constructs a new instance of {@link #instanceClass()} by calling
   * this constructor.
   */
  Expression construct(final Iterable<? extends Expression> args) {
    Expression.checkTypes(argTypes(), args);
    return new Expression(instanceClass().type(), Feature.NON_NULLABLE) {
      @Override void doGen(CodeBuilder mv) {
        mv.newInstance(instanceClass().type());
        // push a second reference onto the stack so there is still a reference to the new object
        // after invoking the constructor (constructors are void methods)
        mv.dup();
        for (Expression arg : args) {
          arg.gen(mv);
        }
        mv.invokeConstructor(instanceClass().type(), method());
      }
    };
  }

  private final TypeInfo instanceClass;
  private final Method method;
  private final ImmutableList<Type> argTypes;

  ConstructorRef(TypeInfo instanceClass, Method method, ImmutableList<Type> argTypes) {
    this.instanceClass = instanceClass;
    this.method = method;
    this.argTypes = argTypes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((argTypes == null) ? 0 : argTypes.hashCode());
    result = prime * result + ((instanceClass == null) ? 0 : instanceClass.hashCode());
    result = prime * result + ((method == null) ? 0 : method.hashCode());
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
    ConstructorRef other = (ConstructorRef) obj;
    if (argTypes == null) {
      if (other.argTypes != null)
        return false;
    } else if (!argTypes.equals(other.argTypes))
      return false;
    if (instanceClass == null) {
      if (other.instanceClass != null)
        return false;
    } else if (!instanceClass.equals(other.instanceClass))
      return false;
    if (method == null) {
      if (other.method != null)
        return false;
    } else if (!method.equals(other.method))
      return false;
    return true;
  }
}
