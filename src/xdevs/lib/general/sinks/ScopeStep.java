package testing.lib.atomic.sinks;

import testing.nondevs.views.ScopeView;


public class ScopeStep extends ScopeLine {

	public ScopeStep(String name, String[] portNames) {
		super(name, portNames);
		chart.setMode(ScopeView.MODE.XYStep);
	}
	
}
