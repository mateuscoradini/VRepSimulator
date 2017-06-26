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

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

class RetinaChartFrame extends ChartFrame {

    private final ChartPanel chartPanel;

    RetinaChartFrame(final String title,
                     final JFreeChart chart) {
        super(title,
              chart);
        this.setDefaultCloseOperation(2);
        this.chartPanel = new ChartPanel(chart,
                                         false);
        this.setContentPane(this.chartPanel);
    }

    public ChartPanel getChartPanel() {
        return this.chartPanel;
    }
}
