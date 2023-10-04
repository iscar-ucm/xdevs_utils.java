package xdevs.lib.util;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

public class ScopeSeriesViewXY extends JFrame {

    //private static final long serialVersionUID = 1L;
    public static enum MODE {

        XYLine, XYStep, XYScatter
    };
    protected HashMap<String, XYSeries> series = new HashMap<String, XYSeries>();
    protected XYSeriesCollection dataSet = new XYSeriesCollection();
    protected JFreeChart chart;
    protected MODE mode;

    public ScopeSeriesViewXY(String topTitle, MODE mode) {
        super(topTitle);
        this.mode = mode;
    }

    public void configure() {
        Iterator<String> itr = series.keySet().iterator();
        while (itr.hasNext()) {
            String serieName = itr.next();
            dataSet.addSeries(series.get(serieName));
        }

        if (mode.equals(MODE.XYScatter)) {
            chart = ChartFactory.createScatterPlot("title", "xTitle", "yTitle", dataSet, PlotOrientation.VERTICAL, true, true, false);
        } else if (mode.equals(MODE.XYLine)) {
            chart = ChartFactory.createXYLineChart("title", "xTitle", "yTitle", dataSet, PlotOrientation.VERTICAL, true, true, false);
        } else if (mode.equals(MODE.XYStep)) {
            chart = ChartFactory.createXYStepChart("title", "xTitle", "yTitle", dataSet, PlotOrientation.VERTICAL, true, true, false);
        }
        chart.setBackgroundPaint(Color.white);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        super.pack();
        //super.setTitle(topTitle);
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);

    }

    public void addSerie(String serieName) {
        series.put(serieName, new XYSeries(serieName));
    }

    public void add(String serieName, Double x, Double y) {
        series.get(serieName).add(x, y);
    }

    public void setChartTitle(String title) {
        chart.setTitle(title);
    }

    public void setXTitle(String title) {
        chart.getXYPlot().getDomainAxis().setLabel(title);
    }

    public void setYTitle(String title) {
        chart.getXYPlot().getRangeAxis().setLabel(title);
    }
}
