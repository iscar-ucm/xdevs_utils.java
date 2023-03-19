/*
 * Copyright (C) 2014-2016 José Luis Risco Martín <jlrisco@ucm.es>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package xdevs.lib.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author José Luis Risco Martín
 */
public class ScopeView extends JFrame {

	private static final long serialVersionUID = 1L;
	protected XYSeries serie;

  public ScopeView(String windowsTitle, String title, String xTitle, String yTitle) {
    super(windowsTitle);
    XYSeriesCollection dataSet = new XYSeriesCollection();
    serie = new XYSeries(yTitle);
    dataSet.addSeries(serie);
    JFreeChart chart = ChartFactory.createXYStepChart(title, xTitle, yTitle, dataSet, PlotOrientation.VERTICAL, true, false, false);
    chart.getXYPlot().setDomainAxis(new NumberAxis());
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
    RefineryUtilities.centerFrameOnScreen(this);
    this.setVisible(true);
  }

  public void addPoint(double x, double y) {
    serie.add(x, y);
  }
}

