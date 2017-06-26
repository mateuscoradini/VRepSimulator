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

package br.com.pos.unicamp.vrep.charts;

import java.util.List;

import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.robots.Pioneer;
import br.com.pos.unicamp.vrep.sensors.ultrasonic.UltrasonicSensor;
import br.com.pos.unicamp.vrep.sensors.ultrasonic.UltrasonicSensors;

import static br.com.pos.unicamp.vrep.utils.LoopUtils.sleep;

public class ObstacleChart extends BaseChart {

    public ObstacleChart(final Pioneer pioneer) {
        super(pioneer);
    }

    @Override
    public void start() {
        // TODO: refactor this and use convert the coordinates correctly.
        new Thread(() -> {
            while (true) {

                getSensors()
                        .stream()
                        .filter(UltrasonicSensor::isDetected)
                        .forEach(sensor -> {
                            final Coordinate obstacleCoordinate = sensor.detectedCoordinate();
                            final Coordinate pioneerCoordinate = getPioneer().getCurrentPose().getCoordinate();
                            int sensorNumber = sensor.getSensorNumber();

                            if (sensorNumber == 4 || sensorNumber == 5) {
                                getPlot().add(new Coordinate(sumVet(obstacleCoordinate.getZ(), pioneerCoordinate.getX()),
                                                             sumVet(obstacleCoordinate.getX(), pioneerCoordinate.getY()),
                                                             0));
                            }

                            if (sensorNumber == 1 || sensorNumber == 8) {
                                getPlot().add(new Coordinate(sumVet(obstacleCoordinate.getZ(), pioneerCoordinate.getX()),
                                                             sumVet(obstacleCoordinate.getX(), pioneerCoordinate.getY()),
                                                             0));
                            }
                        });

                sleep(500);
            }
        }).start();
    }

    // TODO: remove me after the refactor above
    public float sumVet(final float a,
                        final float b) {
        if (a < 0) {
            return a - b;
        } else {
            return a + b;
        }
    }

    private List<UltrasonicSensor> getSensors() {
        final UltrasonicSensors ultrasonicSensors = getPioneer().getUltrasonicSensors();

        return ultrasonicSensors.getSensors();
    }

    @Override
    protected Coordinate updatedCoordinate() {
        return null;
    }

    @Override
    protected String chartName() {
        return "Obstacle Chart";
    }

    @Override
    protected String dataName() {
        return "Obstacle";
    }
}
