package xdevs.lib.general.sinks;

import java.util.Collection;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.lib.util.TimeSeriesView;

public class TimeSeriesScopeN extends Atomic {
	public static final String inName = "in";

    protected Port<Double[]> in = new Port<Double[]>(inName);

	private TimeSeriesView chart; 
	private double time;

	public TimeSeriesScopeN(String name, String topTitle, String title, String xTitle, String yTitle, Collection<String> seriesName) {
		super(name);
		super.addInPort(in);
		this.time = 0.0;
		chart = new TimeSeriesView(topTitle, title, xTitle, yTitle, seriesName);
	}
	
	public void deltint() {
		time += super.getSigma();
		super.passivate();
	}
	
	public void deltext(double e) {
        super.resume(e);
		time += e;
		if(!in.isEmpty()) {
			chart.add(time, in.getSingleValue());
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
