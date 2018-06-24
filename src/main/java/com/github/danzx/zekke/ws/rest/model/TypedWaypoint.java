/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.danzx.zekke.ws.rest.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.ws.rest.model.constraint.CheckTypedWaypoint;

/**
 * Waypoint request/response object with type.
 * 
 * @author Daniel Pedraza-Arcega
 */

@CheckTypedWaypoint
public class TypedWaypoint extends BaseWaypoint {

    @NotNull private Type type;

    private String name;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isTypedWaypointEqualTo((TypedWaypoint) obj);
    }

    /**
     * Use this method to complete your equals method.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is the same as the argument; {@code false} otherwise.
     */
    protected boolean isTypedWaypointEqualTo(TypedWaypoint other) {
        return isBaseWaypointEqualTo(other) &&
                Objects.equals(name, other.name) &&
                Objects.equals(type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, type);
    }

    @Override
    public String toString() {
        return "{ id=" + getId() + ", name=" + name + ", location=" + getLocation() + ", type=" + type + " }";
    }
}
