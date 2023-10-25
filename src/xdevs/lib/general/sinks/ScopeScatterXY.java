package xdevs.lib.general.sinks;

import xdevs.lib.util.ScopeSeriesViewXY;

/**

 * @author José L. Risco-Martín
 *
 */
public class ScopeScatterXY extends ScopeXY {

	public ScopeScatterXY(String name) {
		super(name, ScopeSeriesViewXY.MODE.XYScatter);
	}
	
}
