package xdevs.lib.general.sinks;

import xdevs.lib.util.ScopeSeriesView;

public class ScopeStep extends ScopeLine {

	public ScopeStep(String name, String[] portNames) {
		super(name, portNames);
		chart.setMode(ScopeSeriesView.MODE.XYStep);
	}
	
}
