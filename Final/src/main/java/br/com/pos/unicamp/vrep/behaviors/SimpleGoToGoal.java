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

package br.com.pos.unicamp.vrep.behaviors;

import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.common.Pose;
import br.com.pos.unicamp.vrep.robots.Pioneer;

class SimpleGoToGoal implements Behavior {

    private static final int X_DISTANCE = 2;

    private Pioneer pioneer;

    private boolean isComplete;

    private float lastDistance;

    private float goal;

    @Override
    public void initialize(final Pioneer pioneer) {
        this.pioneer = pioneer;
        this.isComplete = false;
        this.lastDistance = currentPioneerX();
        this.goal = X_DISTANCE;
    }

    @Override
    public void keepGoing() {
        float currentPioneerX = currentPioneerX();
        float distanceVariation = currentPioneerX - lastDistance;
        lastDistance = currentPioneerX;

        if (goal < 0.1) {
            isComplete = true;
        } else {
            goal -= Math.abs(distanceVariation);

            pioneer.getMotors().setVelocity(2F,
                                            2F);
        }
    }

    private float currentPioneerX() {
        final Coordinate currentCoordinate = currentPosition().getCoordinate();

        return currentCoordinate.getX();
    }

    private Pose currentPosition() {
        return pioneer.getCurrentPose();
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
