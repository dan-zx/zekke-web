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
package com.github.danzx.zekke.domain;

import java.util.Objects;

import com.github.danzx.zekke.constraint.FloatRange;

import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.Point;

/**
 * Represents a geographic point in the planet.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class Coordinates {

    public static final double MAX_LATITUDE = 90;
    public static final double MIN_LATITUDE = -MAX_LATITUDE;
    public static final double MAX_LONGITUDE = 180;
    public static final double MIN_LONGITUDE = -MAX_LONGITUDE;

    @FloatRange(min = MIN_LATITUDE,  max = MAX_LATITUDE)  private final double latitude;
    @FloatRange(min = MIN_LONGITUDE, max = MAX_LONGITUDE) private final double longitude;

    private Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /** Factory constructor. */
    public static Coordinates ofLatLng(double latitude, double longitude) {
        return new Coordinates(latitude, longitude);
    }

    public static Coordinates valueOf(Point point) {
        if (point == null) return null;
        return new Coordinates(point.getLatitude(), point.getLongitude());
    }

    public Point toGeoJsonPoint() {
        return GeoJson.point(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return isCoordinatesEqualTo((Coordinates) obj);
    }

    /**
     * Use this method to complete your equals method.
     * 
     * @see {@link #equals(Object)}
     */
    protected boolean isCoordinatesEqualTo(Coordinates other) {
        return Objects.equals(latitude, other.latitude) && 
               Objects.equals(longitude, other.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "{ latitude:" + latitude + ", longitude:" + longitude + " }";
    }
}
