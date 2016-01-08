/*
 * Copyright 2008 Google Inc.
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

package com.google.template.soy.msgs.restricted;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;

/**
 * Represents a part of a message (i.e. raw text or placeholder).
 *
 */
public abstract class SoyMsgPart {
  // TODO(lukes): there is a fair bit of code inspecting this type hierarchy via cascading
  // instanceof tests.  Consider introducing a visitor api or an enum for fast switching.

  /** A case in a plural or 'select' msg part. */
  public static final class Case<T> {
    public static <T> Case<T> create(T spec, Iterable<? extends SoyMsgPart> parts) {
      return new Case<>(spec, ImmutableList.copyOf(parts));
    }

    // null means default case
    @Nullable public T spec() { return spec; }
    public ImmutableList<SoyMsgPart> parts() { return parts; }

    private final T spec;
    private final ImmutableList<SoyMsgPart> parts;

    Case(T spec, ImmutableList<SoyMsgPart> parts) {
      this.spec = spec;
      this.parts = parts;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((parts == null) ? 0 : parts.hashCode());
      result = prime * result + ((spec == null) ? 0 : spec.hashCode());
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
      Case<?> other = (Case<?>) obj;
      if (parts == null) {
        if (other.parts != null)
          return false;
      } else if (!parts.equals(other.parts))
        return false;
      if (spec == null) {
        if (other.spec != null)
          return false;
      } else if (!spec.equals(other.spec))
        return false;
      return true;
    }
  }

  // No methods.
}
