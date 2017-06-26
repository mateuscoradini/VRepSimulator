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

import br.com.pos.unicamp.vrep.robots.Pioneer;
import br.com.pos.unicamp.vrep.sensors.ultrasonic.UltrasonicSensor;
import com.fuzzylite.Engine;
import com.fuzzylite.activation.General;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Ramp;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

import static br.com.pos.unicamp.vrep.utils.NumberUtils.toFloat;

public class SimpleAvoidObstacle implements Behavior {

    public static final String STEER = "mSteer";
    public static final String OBSTACLE = "obstacle";
    private Pioneer pioneer;

    @Override
    public void initialize(final Pioneer pioneer) {
        this.pioneer = pioneer;
    }

    @Override
    public void keepGoing() {
        final Double obstacleLevel = obstacleLevel();
        final double leftVelocity;
        final double rightVelocity;

        if (obstacleLevel != null) {
            final Engine engine = makeFuzzyEngine();
            final InputVariable obstacleInput = engine.getInputVariable(OBSTACLE);
            final OutputVariable steer = engine.getOutputVariable(STEER);

            obstacleInput.setValue(obstacleLevel);
            engine.process();

            final double steerValue = steer.getValue();

            leftVelocity = 4 * (1 - steerValue);
            rightVelocity = 4 * steerValue;
        } else {
            leftVelocity = 2F;
            rightVelocity = 2F;
        }

        pioneer.getMotors().setVelocity(toFloat(leftVelocity),
                                        toFloat(rightVelocity));
    }

    /* TODO: Use 'Optional' here.
     *  The level of the obstacle can assume the following range of values:
     * 0    - the higher level for the right
     * 0.5  - the obstacle is in front of the detectors
     * 1    - the higher level for the left
     * null - that there's no obstacle.
     * @return the level of the obstacle.
     */
    public Double obstacleLevel() {
        final Double rightLevel = rateRightLevel();
        final Double leftLevel = rateLeftLevel();

        if (rightLevel == 0 && leftLevel == 0) {
            return null;
        }

        double obstacleLevel = 0.5;

        if (leftLevel < rightLevel) {
            obstacleLevel = leftLevel;
        } else if (rightLevel < leftLevel) {
            obstacleLevel = 1 + rightLevel;
        }

        return obstacleLevel;
    }

    private Double rateLeftLevel() {
        return rateProximityLevel(4);
    }

    private Double rateRightLevel() {
        return rateProximityLevel(5);
    }

    private Double rateProximityLevel(final int seed) {
        Double level = 0.0;

        level += rateSensor(seed,
                            0.7F);
        level += rateSensor(seed + 1,
                            0.2F);
        level += rateSensor(seed + 2,
                            0.1F);
        return level;
    }

    private float rateSensor(final int sensorNumber,
                             final float weight) {
        final UltrasonicSensor sensor = getSensor(sensorNumber);

        if (sensor.isDetected()) {
            return (1 - sensor.detectedCoordinate().getZ()) * weight;
        }

        return 0;
    }

    private Engine makeFuzzyEngine() {
        final Engine engine = new Engine();
        final RuleBlock mamdani = makeMamdaniRuleBlock();

        engine.setName(getClass().getName());
        engine.setDescription("");
        engine.addInputVariable(makeObstacleInput());
        engine.addOutputVariable(makeSteerOutput());

        mamdani.addRule(Rule.parse("if obstacle is left then mSteer is right",
                                   engine));
        mamdani.addRule(Rule.parse("if obstacle is right then mSteer is left",
                                   engine));
        engine.addRuleBlock(mamdani);

        handleErrors(engine);

        return engine;
    }

    private void handleErrors(final Engine engine) {
        StringBuilder status = new StringBuilder();
        if (!engine.isReady(status)) {
            throw new RuntimeException("[engine error] engine is not ready:n" + status);
        }
    }

    private RuleBlock makeMamdaniRuleBlock() {
        final AlgebraicProduct implication = new AlgebraicProduct();

        RuleBlock mamdani = new RuleBlock();
        mamdani.setName("mamdani");
        mamdani.setDescription("");
        mamdani.setEnabled(true);
        mamdani.setConjunction(null);
        mamdani.setDisjunction(null);
        mamdani.setImplication(implication);
        mamdani.setActivation(new General());

        return mamdani;
    }

    private OutputVariable makeSteerOutput() {
        OutputVariable mSteer = new OutputVariable();
        mSteer.setName(STEER);
        mSteer.setDescription("");
        mSteer.setEnabled(true);
        mSteer.setRange(0.000,
                        1.000);
        mSteer.setLockValueInRange(false);
        mSteer.setAggregation(new Maximum());
        mSteer.setDefuzzifier(new Centroid(100));
        mSteer.setDefaultValue(Double.NaN);
        mSteer.setLockPreviousValue(false);
        mSteer.addTerm(new Ramp("left",
                                1.000,
                                0.000));
        mSteer.addTerm(new Ramp("right",
                                0.000,
                                1.000));
        return mSteer;
    }

    private InputVariable makeObstacleInput() {
        InputVariable obstacle = new InputVariable();
        obstacle.setName(OBSTACLE);
        obstacle.setDescription("");
        obstacle.setEnabled(true);
        obstacle.setRange(0.000,
                          1.000);
        obstacle.setLockValueInRange(false);
        obstacle.addTerm(new Ramp("left",
                                  1.000,
                                  0.000));
        obstacle.addTerm(new Ramp("right",
                                  0.000,
                                  1.000));
        return obstacle;
    }

    private UltrasonicSensor getSensor(final int sensorNumber) {
        return pioneer.getUltrasonicSensors().getSensor(sensorNumber);
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
