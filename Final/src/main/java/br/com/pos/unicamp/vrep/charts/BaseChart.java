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

import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.robots.Pioneer;

import static br.com.pos.unicamp.vrep.utils.LoopUtils.loop;

public abstract class BaseChart {

    private final Pioneer pioneer;

    private ScatterPlot plot;

    public BaseChart(final Pioneer pioneer) {
        this.pioneer = pioneer;
        this.plot = new ScatterPlot(dataName(),
                                    chartName());
    }

    public void start() {
        new Thread(() -> {
            updatedCoordinate();
            loop(() -> plot.add(updatedCoordinate()),
                 100);
        }).start();
    }

    protected ScatterPlot getPlot() {
        return plot;
    }

    protected Pioneer getPioneer() {
        return pioneer;
    }

    protected abstract Coordinate updatedCoordinate();

    protected abstract String chartName();

    protected abstract String dataName();
}
