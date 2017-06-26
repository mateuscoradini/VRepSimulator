/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.pos.unicamp.vrep.common;

public class Coordinate {

    private final float x;
    private final float y;
    private final float z;

    public Coordinate(final float[] coordinatesArray) {
        this.x = coordinatesArray[0];
        this.y = coordinatesArray[1];
        this.z = coordinatesArray[2];
    }

    public Coordinate(final float x,
                      final float y,
                      final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Coordinate empty() {
        final float maxValue = Float.MAX_VALUE;
        final float[] defaultCoordinatesArray = {maxValue, maxValue, maxValue};

        return new Coordinate(defaultCoordinatesArray);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public String toString() {
        return String.format("Coordinate (x, y, z) [%+.6f, %+.6f, %+.6f]",
                             getX(),
                             getY(),
                             getZ());
    }
}
