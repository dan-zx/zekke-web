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
package com.github.danzx.zekke.service;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import com.github.danzx.zekke.base.Buildable;
import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;

/**
 * Waypoint business logic service.
 * 
 * @author Daniel Pedraza-Arcega
 */
public interface WaypointService extends PersistentService<Waypoint> {

    /**
     * Persists the given waypoint into the underlying datastore.
     * 
     * @param waypoint an element to persist.
     * @throws ServiceException if the given waypoint is of type {@value Type#WALKWAY} and a name is
     *         present.
     * @throws ServiceException if the given waypoint is of type {@value Type#POI} and a name is not
     *         present.
     */
    void persist(Waypoint waypoint);

    /**
     * Finds a waypoint by its id.
     * 
     * @param id an id.
     * @return the optional waypoint.
     */
    Optional<Waypoint> findWaypointById(long id);

    /**
     * Finds all waypoints matching the given query.
     * 
     * @param query a query.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findWaypoints(WaypointsQuery query);

    /**
     * Finds all waypoints near a point matching the given query.
     * 
     * @param query a query.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findNearWaypoints(NearWaypointsQuery query);

    /**
     * Finds all POIs with only its id and name initialized matching the given query.
     * 
     * @param bbox the a rectangle bound.
     * @param nameQuery a name query.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findPoisForNameCompletion(BoundingBox bbox, String nameQuery);

    class WaypointsQuery {
        private final Builder builder;

        private WaypointsQuery(Builder builder) {
            this.builder = builder;
        }

        public Optional<BoundingBox> getBoundingBox() {
            return Optional.ofNullable(builder.boundingBox);
        }

        public String getNameQuery() {
            return builder.nameQuery;
        }

        public Type getWaypointType() {
            return builder.waypointType;
        }

        public static class Builder implements Buildable<WaypointsQuery> {
            private BoundingBox boundingBox;
            private String nameQuery;
            private Type waypointType;

            public Builder withinBoundingBox(BoundingBox bbox) {
                boundingBox = bbox;
                return this;
            }

            public Builder withNameContaining(String name) {
                nameQuery = Optional.ofNullable(name).map(String::trim).orElse(null);
                return this;
            }

            public Builder ofType(Type waypointType) {
                this.waypointType = waypointType;
                return this;
            }

            @Override
            public WaypointsQuery build() {
                return new WaypointsQuery(this);
            }
        }
    }

    class NearWaypointsQuery {
        private final Builder builder;

        private NearWaypointsQuery(Builder builder) {
            this.builder = builder;
        }

        public Coordinates getLocation() {
            return builder.location;
        }

        public Integer getMaxDistance() {
            return builder.maxDistance;
        }

        public Integer getLimit() {
            return builder.limit;
        }

        public Type getWaypointType() {
            return builder.waypointType;
        }

        public static class Builder implements Buildable<NearWaypointsQuery> {
            private final Coordinates location;
            private Integer maxDistance;
            private Integer limit;
            private Type waypointType;

            public static Builder nearLocation(Coordinates location) {
                return new Builder(location);
            }

            private Builder(Coordinates location) {
                this.location = requireNonNull(location);
            }

            public Builder maximumSearchDistance(Integer maxDistance) {
                this.maxDistance = maxDistance;
                return this;
            }

            public Builder limitResulsTo(Integer limit) {
                if (limit != null && limit > 0) this.limit = limit;
                return this;
            }

            public Builder byType(Type waypointType) {
                this.waypointType = waypointType;
                return this;
            }

            @Override
            public NearWaypointsQuery build() {
                return new NearWaypointsQuery(this);
            }
        }
    }
}