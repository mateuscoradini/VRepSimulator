package br.com.pos.unicamp.vrep.charts;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

import br.com.pos.unicamp.vrep.common.Coordinate;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ScatterPlot {

    private final XYSeries series;

    public ScatterPlot(final String dataName,
                       final String chartName) {
        this.series = new XYSeries(dataName);

        makeFrame(chartName,
                  makeChart(chartName));
    }

    private XYSeriesCollection dataSet() {
        final XYSeriesCollection result = new XYSeriesCollection();

        result.addSeries(series);

        return result;
    }

    private RetinaChartFrame makeFrame(final String chartName,
                                       final JFreeChart chart) {
        final RetinaChartFrame frame = new RetinaChartFrame(chartName,
                                                            chart);
        frame.pack();
        frame.setVisible(true);
        frame.setBackground(Color.WHITE);

        return frame;
    }

    public void add(final Coordinate coordinate) {
        series.add(coordinate.getX(),
                   coordinate.getY());
    }

    private JFreeChart makeChart(final String title) {
        final JFreeChart chart = ChartFactory.createScatterPlot(title,
                                                                "x",
                                                                "y",
                                                                dataSet(),
                                                                PlotOrientation.VERTICAL,
                                                                true,
                                                                true,
                                                                false);

        applyStyle(chart);

        return chart;
    }

    private void applyStyle(final JFreeChart chart) {
        final XYPlot plot = chart.getXYPlot();
        final XYItemRenderer renderer = plot.getRenderer();

        plot.getDomainAxis().setRange(-8.00,
                                      8.00);
        plot.getRangeAxis().setRange(-4.0,
                                     4.0);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        renderer.setSeriesShape(0,
                                circle());
        renderer.setSeriesPaint(0,
                                Color.BLACK);
    }

    private Ellipse2D.Double circle() {
        return new Ellipse2D.Double(0,
                                    0,
                                    3,
                                    3);
    }
}
