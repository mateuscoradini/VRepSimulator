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

import br.com.pos.unicamp.vrep.actuators.Motors;
import br.com.pos.unicamp.vrep.robots.Pioneer;
import br.com.pos.unicamp.vrep.sensors.ultrasonic.UltrasonicSensor;

import static br.com.pos.unicamp.vrep.utils.LoopUtils.sleep;

public class BehaviorHandler {

    private final Pioneer pioneer;

    private BehaviorType currentBehaviorType;

    public BehaviorHandler(final Pioneer pioneer) {
        this.pioneer = pioneer;

        setCurrentBehavior(BehaviorType.AVOID_OBSTACLE);
    }

    public void evaluate() {
        if (isPioneerInDanger()) {
            log("in trouble!!");
            changeContext();
        }

        keepGoing();
    }

    public BehaviorType getCurrentBehaviorType() {
        return currentBehaviorType;
    }

    private void goToGoal() {
        setCurrentBehavior(BehaviorType.GO_TO_GOAL);

        keepGoing();
    }

    private boolean isSuperSafe() {
        return !getSensor(3).isDetected() && !getSensor(4).isDetected() && !getSensor(5).isDetected() && !getSensor(6).isDetected();
    }

    private boolean isPioneerInDanger() {
        final UltrasonicSensor s3 = getSensor(3);
        final UltrasonicSensor s4 = getSensor(4);
        final UltrasonicSensor s5 = getSensor(5);
        final UltrasonicSensor s6 = getSensor(6);

        return (isDangarousDistance(s4) && isDangarousDistance(s5))
                || isSuperDangarousDistance(s3)
                || isSuperDangarousDistance(s6);
    }

    private boolean isSuperDangarousDistance(final UltrasonicSensor sensor) {
        final float safeDistance = 0.2F;

        return sensor.isDetected() && distance(sensor) < safeDistance;
    }

    private boolean isDangarousDistance(final UltrasonicSensor sensor) {
        final float safeDistance = 0.4F;

        return sensor.isDetected() && distance(sensor) < safeDistance;
    }

    private UltrasonicSensor getSensor(final int sensorNumber) {
        return pioneer.getUltrasonicSensors().getSensor(sensorNumber);
    }

    private float distance(final UltrasonicSensor sensor) {
        return sensor.detectedCoordinate().getZ();
    }

    private void setVelocity(final float motorLeftVelocity,
                             final float motorRightVelocity) {
        final Motors motors = pioneer.getMotors();

        motors.setVelocity(motorLeftVelocity,
                           motorRightVelocity);
    }

    private void changeContext() {
        stop();
        findASafeDirection();
        goToGoal();
    }

    private void stop() {
        setVelocity(0F,
                    0F);
        sleep(500);

        log("stopping...");
    }

    private void findASafeDirection() {
        setVelocity(-1F,
                    -1F);
        sleep(500);
        setVelocity(-2F,
                    2F);

        log("looking for a safe direction...");

        while (!isSuperSafe()) {
            sleep(100);
        }
    }

    private void keepGoing() {
        if (currentBehavior().isComplete()) {
            toggleBehavior();
        }

        currentBehavior().keepGoing();
    }

    private void toggleBehavior() {
        if (currentBehaviorType == BehaviorType.GO_TO_GOAL) {
            setCurrentBehavior(BehaviorType.AVOID_OBSTACLE);
        } else {
            setCurrentBehavior(BehaviorType.GO_TO_GOAL);
        }
    }

    public void setCurrentBehavior(final BehaviorType currentBehaviorType) {
        this.currentBehaviorType = currentBehaviorType;

        currentBehavior().initialize(pioneer);

        log(currentBehaviorType.getDescription());
    }

    private Behavior currentBehavior() {
        return currentBehaviorType.behavior();
    }

    public void log(final String action) {
        System.out.println("Hey! Pioneer is " + action);
    }

    private enum BehaviorType {
        GO_TO_GOAL(new SimpleGoToGoal(),
                   "going to some goal!"),
        AVOID_OBSTACLE(new SimpleAvoidObstacle(),
                       "avoiding obstacles!");

        private final Behavior behavior;

        private final String description;

        BehaviorType(final Behavior behavior,
                     final String description) {
            this.behavior = behavior;
            this.description = description;
        }

        public Behavior behavior() {
            return behavior;
        }

        public String getDescription() {
            return description;
        }
    }
}
