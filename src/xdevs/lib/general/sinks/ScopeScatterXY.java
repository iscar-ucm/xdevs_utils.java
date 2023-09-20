package testing.lib.atomic.sinks;

import testing.nondevs.views.ScopeViewXY;


/**

 * @author José L. Risco-Martín
 *
 */
public class ScopeScatterXY extends ScopeXY {

	public ScopeScatterXY(String name) {
		super(name, ScopeViewXY.MODE.XYScatter);
	}
	
}
