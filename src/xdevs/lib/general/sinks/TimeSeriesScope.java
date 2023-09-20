package testing.lib.atomic.sinks;

import java.util.ArrayList;
import java.util.Collection;
import testing.nondevs.views.TimeSeriesView;

import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;


public class TimeSeriesScope extends Atomic {

    protected Port<Double>[] input;

	protected TimeSeriesView chart;
	protected double time;
	protected Collection<String> seriesName;

	public TimeSeriesScope(String name, String topTitle, String title, String xTitle, String yTitle, Collection<String> seriesName) {
		super(name);
        input = new Port[seriesName.size()];
        int i = 0;
		for(String serieName : seriesName) {
            input[i] = new Port<Double>(serieName);
			super.addInport(input[i++]);
        }
		this.time = 0.0;
		chart = new TimeSeriesView(topTitle, title, xTitle, yTitle, seriesName);
		this.seriesName = seriesName;
	}
	
	public TimeSeriesScope(String name, String topTitle, String title, String xTitle, String yTitle, String serieName) {
        super(name);
		ArrayList<String> seriesNameAux = new ArrayList<String>();
		seriesNameAux.add(serieName);
        input = new Port[1];
        input[0] = new Port<Double>(serieName);
		super.addInport(input[0]);
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
		for(int i=0; i<input.length; ++i) {
			if(!input[i].isEmpty()) {
				chart.add(time, input[i].getValue(), input[i].getName());
			}
		}
	}

	public void lambda() { }
	
}
