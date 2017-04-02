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
package com.github.danzx.zekke.persistence.dao.morphia;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;

import com.github.danzx.zekke.domain.Path;
import com.github.danzx.zekke.domain.Point;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.test.mongo.BaseSpringMongoTest;

public class WaypointMorphiaCrudDaoTest extends BaseSpringMongoTest {

    private static final long CDMX_ID = 1L;
    private static final long NOT_EXITING_WAYPOINT = Long.MAX_VALUE;
    private static final Map<Long, Waypoint> DATA = buildStoredData();

    @Inject private WaypointMorphiaCrudDao waypointDao;

    @Override
    public void before() throws Exception {
        super.before();
        assertThat(waypointDao).isNotNull();
    }

    @Test
    public void shouldDatastoreNotBeNull() {
        assertThat(waypointDao.getDatastore()).isNotNull();
    }

    @Test
    public void shouldCollectionClassBeWaypointClass() {
        assertThat(waypointDao.getCollectionClass()).isNotNull().isEqualTo(Waypoint.class);
    }

    @Test
    public void shouldReturnWaypointWhenFindByAnExistingId() {
        Waypoint cdmx = DATA.get(CDMX_ID);
        Optional<Waypoint> waypoint = waypointDao.findById(CDMX_ID);
        assertThat(waypoint.isPresent()).isTrue();
        assertThat(waypoint.get()).isEqualTo(cdmx);
        assertThat(waypoint.get().getPaths()).isNotNull().isNotEmpty().hasSameSizeAs(cdmx.getPaths()).containsOnlyElementsOf(cdmx.getPaths());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenFindByIdHasInvalidId() {
        Optional<Waypoint> waypoint = waypointDao.findById(NOT_EXITING_WAYPOINT);
        assertThat(waypoint.isPresent()).isFalse();
    }

    @Test
    public void shouldDeleteWaypointAndReturnAnEmptyOptionalWhenFindByTheDeletedId() {
        shouldReturnWaypointWhenFindByAnExistingId();
        waypointDao.deleteById(CDMX_ID);
        Optional<Waypoint> waypoint = waypointDao.findById(CDMX_ID);
        assertThat(waypoint.isPresent()).isFalse();
    }

    @Test
    public void shouldFindAll() {
        Collection<Waypoint> waypoints = DATA.values();
        assertThat(waypointDao.findAll()).isNotNull().isNotEmpty().hasSameSizeAs(waypoints).containsOnlyElementsOf(waypoints);
    }

    @Test
    public void shouldSaveWaypointAndReturnTheSameWaypointWhenFindByTheNewId() {
        Waypoint newWalkway = newWaypoint(666L, null, Type.WALKWAY, 55.3691, -70.1439);
        waypointDao.save(newWalkway);
        Optional<Waypoint> waypoint = waypointDao.findById(newWalkway.getId());
        assertThat(waypoint.isPresent()).isTrue();
        assertThat(waypoint.get()).isEqualTo(newWalkway);
        assertThat(waypoint.get().getPaths()).isNull();
    }

    private static Map<Long, Waypoint> buildStoredData() {
        Map<Long, Waypoint> data = new HashMap<>();
        data.put(1L, newWaypoint(1L, "Ciudad de México", Type.POI, 19.387591, -99.052734, newPath(1L, 7L, 94431.859)));
        data.put(2L, newWaypoint(2L, "Querétaro", Type.POI, 20.579760, -100.371094, newPath(2L, 6L, 98951.731), newPath(2L, 7L, 95695.093)));
        data.put(3L, newWaypoint(3L, null, Type.WALKWAY, 19.672009, -101.151123, newPath(3L, 6L, 110561.394), newPath(3L, 7L, 144887.222), newPath(3L, 4L, 249563.935)));
        data.put(4L, newWaypoint(4L, "Guadalajara", Type.POI, 20.651740, -103.337402, newPath(4L, 5L, 181890.199), newPath(4L, 3L, 249563.935)));
        data.put(5L, newWaypoint(5L, "León", Type.POI, 21.123896, -101.667480, newPath(5L, 4L, 181890.199), newPath(5L, 6L, 64956.585)));
        data.put(6L, newWaypoint(6L, null, Type.WALKWAY, 20.651740, -101.359863, newPath(6L, 5L, 64956.585), newPath(6L, 2L, 98951.731), newPath(6L, 3L, 110561.394)));
        data.put(7L, newWaypoint(7L, null, Type.WALKWAY, 19.920099, -99.788818, newPath(7L, 2L, 95695.093), newPath(7L, 1L, 94431.859), newPath(7L, 3L, 144887.222)));
        return data;
    }

    private static Waypoint newWaypoint(long id, String name, Type type, double lat, double lng, Path... paths) {
        Waypoint waypoint = new Waypoint();
        waypoint.setId(id);
        waypoint.setName(name);
        waypoint.setType(type);
        waypoint.setLocation(new Point());
        waypoint.getLocation().setLatitude(lat);
        waypoint.getLocation().setLongitude(lng);
        waypoint.setPaths(new HashSet<>(asList(paths)));
        return waypoint;
    }

    private static Path newPath(long fromWaypoint, long toWaypoint, double distance) {
        Path path = new Path();
        path.setFromWaypoint(fromWaypoint);
        path.setToWaypoint(toWaypoint);
        path.setDistance(distance);
        return path;
    }
}
