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

import static java.util.Objects.requireNonNull;

/**
 * Contains a bottom left coordinates and upper right coordinates of a rectangle.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class BoundingBox {

    private final Coordinates bottomLeftCoordinates;
    private final Coordinates upperRightCoordinates;

    /**
     * Constructor.
     * 
     * @param bottomLeftCoordinates the bottom left coordinates.
     * @param upperRightCoordinates the upper right coordinates.
     */
    public BoundingBox(Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates) {
        this.bottomLeftCoordinates = requireNonNull(bottomLeftCoordinates);
        this.upperRightCoordinates = requireNonNull(upperRightCoordinates);
    }

    public Coordinates getBottomLeftCoordinates() {
        return bottomLeftCoordinates;
    }

    public Coordinates getUpperRightCoordinates() {
        return upperRightCoordinates;
    }

    @Override
    public String toString() {
        return "{ bottomLeftCoordinates=" + bottomLeftCoordinates + ", upperRightCoordinates=" + 
                 upperRightCoordinates + " }";
    }
}