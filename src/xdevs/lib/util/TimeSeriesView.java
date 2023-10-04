package xdevs.lib.util;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

public class TimeSeriesView extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private String title;						// Título del gráfico.
	private String xTitle;						// Título del eje de abcisas.
	private String yTitle;						// Título del eje de ordenadas.
	private ArrayList<XYSeries> series;			// Series JFreeChart.
	private HashMap<String,XYSeries> seriesMap;	// Series JFreeChart.
	private XYSeriesCollection dataSet;			// Almacenamiento de datos.


	public TimeSeriesView(String topTitle, String title, String xTitle, String yTitle, Collection<String> seriesName) {
		super("");
        this.title = title;
        this.xTitle = xTitle;
        this.yTitle = yTitle;

		this.series = new ArrayList<XYSeries>();
		this.seriesMap = new HashMap<String,XYSeries>();
        for(String serieName : seriesName) {
        	XYSeries serie = new XYSeries(serieName);
        	series.add(serie);
        	seriesMap.put(serieName, serie);
        }
        
        ChartPanel chartPanel = createDemoPanel();
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
        super.setTitle(topTitle);
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
	}

	private JFreeChart createChart(XYDataset dataset) {
    	JFreeChart chart = null;
   		chart = ChartFactory.createXYLineChart(title,xTitle,yTitle,dataset,PlotOrientation.VERTICAL,true,true,false);
        chart.setBackgroundPaint(Color.white);
        return chart;
    }

	private XYDataset createDataset() {
        dataSet = new XYSeriesCollection();
        for(int i=0;i<series.size();i++)
        	dataSet.addSeries(series.get(i));
        return dataSet;
    }

    public ChartPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
    
    public void add(double t, double[] y) {
    	for(int i=0;i<y.length;i++) 
    		series.get(i).add(t,y[i]);
    }
    
    public void add(Double t, Double[] y) {
    	for(int i=0;i<y.length;i++)
    		series.get(i).add(t,y[i]);
    }

    public void add(double t, double y, String serieName) {
    	seriesMap.get(serieName).add(t, y);
    }
    
}
