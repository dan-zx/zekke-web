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

import static java.util.stream.Collectors.toList;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.test.mongo.BaseSpringMongoTest;

import org.assertj.core.groups.Tuple;

import org.junit.Test;

public class WaypointMorphiaCrudDaoTest extends BaseSpringMongoTest {

    private static final long CDMX_ID = 1L;
    private static final long QRO_ID = 2L;
    private static final long GDL_ID = 4L;
    private static final long LEON_ID = 5L;
    private static final long NOT_EXITING_WAYPOINT = Long.MAX_VALUE;
    private static final Map<Long, Waypoint> DATA = buildStoredData();

    @Inject private WaypointMorphiaCrudDao waypointDao;

    @Override
    public void before() throws Exception {
        super.before();
        assertThat(waypointDao).isNotNull();
        assertThat(waypointDao.getDatastore()).isNotNull();
        assertThat(waypointDao.getCollectionClass()).isNotNull();
    }

    @Test
    public void shouldReturnWaypointWhenFindByAnExistingId() {
        Waypoint waypoint = DATA.get(3L);
        Optional<Waypoint> waypointGot = waypointDao.findById(3L);
        assertThat(waypointGot.isPresent()).isTrue();
        assertThat(waypointGot.get()).isEqualTo(waypoint);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenFindByIdHasInvalidId() {
        Optional<Waypoint> waypoint = waypointDao.findById(NOT_EXITING_WAYPOINT);
        assertThat(waypoint.isPresent()).isFalse();
    }

    @Test
    public void shouldDeleteWaypointAndReturnAnEmptyOptionalWhenFindByTheDeletedId() {
        assertThat(waypointDao.deleteById(CDMX_ID)).isTrue();
        Optional<Waypoint> waypoint = waypointDao.findById(CDMX_ID);
        assertThat(waypoint.isPresent()).isFalse();
    }

    @Test
    public void shouldPerformDeleteWithUnexistingWaypointAndNotFailing() {
        assertThat(waypointDao.deleteById(NOT_EXITING_WAYPOINT)).isFalse();
    }

    @Test
    public void shouldFindAll() {
        Collection<Waypoint> waypoints = DATA.values();
        assertThat(waypointDao.findAll()).isNotNull().isNotEmpty().hasSameSizeAs(waypoints).containsOnlyElementsOf(waypoints);
    }

    @Test
    public void shouldSaveWaypointAndReturnTheSameWaypointWhenFindByTheNewId() {
        Waypoint newWalkway = newWaypoint(666L, null, Type.WALKWAY, 55.3691, -70.1439);
        waypointDao.saveOrUpdate(newWalkway);
        Optional<Waypoint> waypoint = waypointDao.findById(newWalkway.getId());
        assertThat(waypoint.isPresent()).isTrue();
        assertThat(waypoint.get()).isEqualTo(newWalkway);
    }

    @Test
    public void shouldSaveWaypointAndSetAutogeneratedId() {
        Waypoint newWalkway = newWaypoint(null, "Somewhere", Type.POI, 55.3691, -70.1439);
        assertThat(newWalkway.getId()).isNull();
        waypointDao.saveOrUpdate(newWalkway);
        assertThat(newWalkway.getId()).isNotNull();
        Optional<Waypoint> waypoint = waypointDao.findById(newWalkway.getId());
        assertThat(waypoint.isPresent()).isTrue();
        assertThat(waypoint.get()).isEqualTo(newWalkway);
    }

    @Test
    public void shouldUpdateExistingWaypoint() {
        Waypoint waypointToUpdate = waypointDao.findById(CDMX_ID).get();
        waypointToUpdate.setName("other Name");
        waypointToUpdate.setLocation(Coordinates.ofLatLng(12.63, 85.243));
        waypointDao.saveOrUpdate(waypointToUpdate);

        Waypoint updatedWaypoint = waypointDao.findById(waypointToUpdate.getId()).get();
        assertThat(updatedWaypoint).isEqualTo(waypointToUpdate);
    }

    @Test
    public void shouldFilterByType() {
        Type filter = Type.WALKWAY;
        List<Waypoint> waypoints = DATA.values().stream()
                .filter(waypoint -> waypoint.getType() == filter)
                .collect(toList());
        assertThat(waypointDao.findOptionallyByTypeAndNameQuery(filter, null, null)).isNotNull().isNotEmpty().hasSameSizeAs(waypoints).containsOnlyElementsOf(waypoints);
    }

    @Test
    public void shouldFilterByPoiName() {
        String filter = "ara";
        List<Waypoint> waypoints = DATA.values().stream()
                .filter(waypoint -> waypoint.getType() == Type.POI)
                .filter(waypoint -> waypoint.getName().map(name -> name.contains(filter)).orElse(false))
                .collect(toList());
        assertThat(waypointDao.findOptionallyByTypeAndNameQuery(Type.POI, filter, null)).isNotNull().isNotEmpty().hasSameSizeAs(waypoints).containsOnlyElementsOf(waypoints);
    }

    @Test
    public void shouldFilterNothing() {
        Collection<Waypoint> waypoints = DATA.values();
        assertThat(waypointDao.findOptionallyByTypeAndNameQuery(null, null, null)).isNotNull().isNotEmpty().hasSameSizeAs(waypoints).containsOnlyElementsOf(waypoints);
    }

    @Test
    public void shouldFilterNothingLimitingTo2Results() {
        List<Waypoint> waypoints = Arrays.asList(DATA.get(1L), DATA.get(2L));
        assertThat(waypointDao.findOptionallyByTypeAndNameQuery(null, null, 2)).isNotNull().isNotEmpty().hasSameSizeAs(waypoints).containsOnlyElementsOf(waypoints);
    }

    @Test
    public void shouldFindNearestFindOnePoi() {
        List<Waypoint> actualWaypoints = waypointDao.findNear(Coordinates.ofLatLng(19.758788, -99.437256), 180_000, 1, Type.POI);
        listShouldContainGivenWaypoints(actualWaypoints, DATA.get(CDMX_ID));
    }

    @Test
    public void shouldFindNearestFindOneWalkway() {
        List<Waypoint> actualWaypoints = waypointDao.findNear(Coordinates.ofLatLng(19.758788, -99.437256), 180_000, 1, Type.WALKWAY);
        listShouldContainGivenWaypoints(actualWaypoints, DATA.get(7L));
    }

    @Test
    public void shouldFindNearestFindTwoWaypoints() {
        List<Waypoint> actualWaypoints = waypointDao.findNear(Coordinates.ofLatLng(19.758788, -99.437256), 100_000, null, null);
        listShouldContainGivenWaypoints(actualWaypoints, DATA.get(CDMX_ID), DATA.get(7L));
    }

    @Test
    public void shouldFindNearestFindNothing() {
        List<Waypoint> actualWaypoints = waypointDao.findNear(Coordinates.ofLatLng(19.387585, -99.050937), 1, null, null);
        listShouldContainGivenWaypoints(actualWaypoints);
    }

    @Test
    public void shouldFindWithinBox() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(19.437943, -104.018555), Coordinates.ofLatLng(21.373642, -100.832520)), null, null, false, null);
        listShouldContainGivenWaypoints(actualPois, DATA.get(GDL_ID), DATA.get(LEON_ID), DATA.get(3L), DATA.get(6L));
    }

    @Test
    public void shouldFindWithinBoxFindNothing() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(20.794313, -99.052734), Coordinates.ofLatLng(21.347903, -98.591309)), null, null, false, null);
        listShouldContainGivenWaypoints(actualPois);
    }

    @Test
    public void shouldFindWithinBoxByType() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(19.437943, -104.018555), Coordinates.ofLatLng(21.373642, -100.832520)), Type.WALKWAY, null, false, null);
        listShouldContainGivenWaypoints(actualPois, DATA.get(3L), DATA.get(6L));
    }

    @Test
    public void shouldFindWithinBoxByTypeFindNothing() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(20.794313, -99.052734), Coordinates.ofLatLng(21.347903, -98.591309)), Type.WALKWAY, null, false, null);
        listShouldContainGivenWaypoints(actualPois);
    }

    @Test
    public void shouldFindPoisWithinBox() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(20.465294, -103.491211), Coordinates.ofLatLng(21.204578, -101.491699)), Type.POI, null, false, null);
        listShouldContainGivenWaypoints(actualPois, DATA.get(GDL_ID), DATA.get(LEON_ID));
    }

    @Test
    public void shouldFindPoisWithinBoxAndQueryName() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(20.465294, -103.491211), Coordinates.ofLatLng(21.204578, -101.491699)), Type.POI, "ara", false, null);
        listShouldContainGivenWaypoints(actualPois, DATA.get(GDL_ID));
    }

    @Test
    public void shouldFindPoisWithinBoxFindNothing() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(20.794313, -99.052734), Coordinates.ofLatLng(21.347903, -98.591309)), Type.POI, null, false, null);
        listShouldContainGivenWaypoints(actualPois);
    }

    @Test
    public void shouldFindBasicPoisWithinBox() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(20.465294, -103.491211), Coordinates.ofLatLng(21.204578, -101.491699)), Type.POI, null, true, null);
        listShouldContainGivenWaypointsWithOnlyIdAndName(actualPois, DATA.get(GDL_ID), DATA.get(LEON_ID));
    }

    @Test
    public void shouldFindBasicPoisWithinBoxAndQueryName() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(20.465294, -103.491211), Coordinates.ofLatLng(21.204578, -101.491699)), Type.POI, "ara", true, null);
        listShouldContainGivenWaypointsWithOnlyIdAndName(actualPois, DATA.get(GDL_ID));
    }

    @Test
    public void shouldFindBasicPoisWithinBoxFindNothing() {
        List<Waypoint> actualPois = waypointDao.findWithinBox(BoundingBox.ofBottomTop(Coordinates.ofLatLng(20.794313, -99.052734), Coordinates.ofLatLng(21.347903, -98.591309)), Type.POI, "ara", false, null);
        assertThat(actualPois).isNotNull().isEmpty();
    }

    private void listShouldContainGivenWaypoints(List<Waypoint> actualWaypoints, Waypoint...waypoints) {
        if (isEmpty(waypoints)) assertThat(actualWaypoints).isNotNull().isEmpty();
        else assertThat(actualWaypoints).isNotNull().isNotEmpty().hasSameSizeAs(waypoints).containsOnly(waypoints);
    }

    private void listShouldContainGivenWaypointsWithOnlyIdAndName(List<Waypoint> actualWaypoints, Waypoint...waypoints) {
        if (isEmpty(waypoints)) assertThat(actualWaypoints).isNotNull().isEmpty();
        else {
            List<Tuple> expectedIdAndNameList = Arrays.stream(waypoints)
                    .map(waypoint -> tuple(waypoint.getId(), waypoint.getName(), null, null))
                    .collect(toList());
            assertThat(actualWaypoints).isNotNull().isNotEmpty().hasSameSizeAs(waypoints)
                .extracting(Waypoint::getId, Waypoint::getName, Waypoint::getType, Waypoint::getLocation)
                .containsOnlyElementsOf(expectedIdAndNameList);
        }
    }

    private static Map<Long, Waypoint> buildStoredData() {
        Map<Long, Waypoint> data = new HashMap<>();
        data.put(1L, newWaypoint(CDMX_ID, "Ciudad de Mexico", Type.POI, 19.387591, -99.052734));
        data.put(2L, newWaypoint(QRO_ID, "Queretaro", Type.POI, 20.579760, -100.371094));
        data.put(3L, newWaypoint(3L, null, Type.WALKWAY, 19.672009, -101.151123));
        data.put(4L, newWaypoint(GDL_ID, "Guadalajara", Type.POI, 20.651740, -103.337402));
        data.put(5L, newWaypoint(LEON_ID, "Leon", Type.POI, 21.123896, -101.667480));
        data.put(6L, newWaypoint(6L, null, Type.WALKWAY, 20.651740, -101.359863));
        data.put(7L, newWaypoint(7L, null, Type.WALKWAY, 19.920099, -99.788818));
        return data;
    }

    private static Waypoint newWaypoint(Long id, String name, Type type, double lat, double lng) {
        Waypoint waypoint = new Waypoint();
        if (id != null) waypoint.setId(id);
        waypoint.setName(name);
        waypoint.setType(type);
        waypoint.setLocation(Coordinates.ofLatLng(lat, lng));
        return waypoint;
    }
}
