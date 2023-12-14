package xdevs.lib.general.sinks;

import java.util.ArrayList;
import java.util.Collection;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.lib.util.TimeSeriesView;

public class TimeSeriesScope extends Atomic {

    public Port<Double>[] in;

	protected TimeSeriesView chart;
	protected double time;
	protected Collection<String> seriesName;

	public TimeSeriesScope(String name, String topTitle, String title, String xTitle, String yTitle, Collection<String> seriesName) {
		super(name);
        in = new Port[seriesName.size()];
        int i = 0;
		for(String serieName : seriesName) {
            in[i] = new Port<Double>(serieName);
			super.addInPort(in[i++]);
        }
		this.time = 0.0;
		chart = new TimeSeriesView(topTitle, title, xTitle, yTitle, seriesName);
		this.seriesName = seriesName;
	}
	
	public TimeSeriesScope(String name, String topTitle, String title, String xTitle, String yTitle, String serieName) {
        super(name);
		ArrayList<String> seriesNameAux = new ArrayList<String>();
		seriesNameAux.add(serieName);
        in = new Port[1];
        in[0] = new Port<Double>(serieName);
		super.addInPort(in[0]);
		this.time = 0.0;
		chart = new TimeSeriesView(topTitle, title, xTitle, yTitle, seriesNameAux);
		this.seriesName = seriesNameAux;
	}

	public void deltint() {
		time += super.getSigma();
		super.passivate();
	}
	
	public void deltext(double e) {
        super.resume(e);
		time += e;
		for(int i=0; i<in.length; ++i) {
			if(!in[i].isEmpty()) {
				chart.add(time, in[i].getSingleValue(), in[i].getName());
			}
		}
	}

	public void lambda() { }

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}
	
}
