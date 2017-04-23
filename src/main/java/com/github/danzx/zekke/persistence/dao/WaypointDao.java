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
package com.github.danzx.zekke.persistence.dao;

import java.util.List;
import java.util.Optional;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;

/**
 * Waypoint CRUD DAO.
 * 
 * @param <W> a waypoint implementation.
 * 
 * @author Daniel Pedraza-Arcega
 */
public interface WaypointDao extends CrudDao<Waypoint, Long> {

    /** 500 meters. */
    int DEFAULT_MAX_DISTANCE = 500;

    /**
     * Filters waypoints by type and/or name containing a string.
     * 
     * @param waypointType the desired waypoint type.
     * @param nameQuery a partial POI name. If specified the paramater waypointType will be ignored
     *        and use POI instead
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findOptionallyByTypeAndNameQuery(Optional<Type> waypointType, Optional<String> nameQuery);

    /**
     * Finds the nearest waypoints to given location.
     * 
     * @param location a location.
     * @param maxDistance limits the results to those Waypoints that are at most the specified
     *        distance from the location (meters). if not present {@value #DEFAULT_MAX_DISTANCE}
     *        will be used.
     * @param limit limits the results to the given number, if not present it will return all
     *        results.
     * @param waypointType the desired waypoint type.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findNear(Coordinates location, Optional<Integer> maxDistance, Optional<Integer> limit, Optional<Type> waypointType);

    /**
     * Finds the waypoints that are within the bounds of a rectangle, you must specify the bottom
     * left and top right corners of the rectangle. Optionally, this list can be filtered by
     * {@link Type} and name containing a string.
     * 
     * @param bottomLeftCoordinates the bottom left coordinates.
     * @param upperRightCoordinates the upper right coordinates.
     * @param waypointType the desired waypoint type.
     * @param nameQuery a partial POI name. If specified the paramater waypointType will be ignored
     *        and use POI instead
     * @param onlyIdAndName if only retrieve the id and the name of the waypoints.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findWithinBox(Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates, Optional<Type> waypointType, Optional<String> nameQuery, boolean onlyIdAndName);
}
