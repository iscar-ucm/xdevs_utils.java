package xdevs.lib.general.sinks;

/**
 * ScopeLineXY
 * 
 * Display an X-Y plot of signals using a JFreeChart figure window.
 * 
 * The XY Scope block displays an X-Y plot of its inputs in a JFreeChart figure window. The block 
 * has two scalar inputs (named x and y). The block plots data in the first input (the x direction) against data 
 * in the second input (the y direction). This block is useful for examining limit cycles and other 
 * two-state data. x-devs opens a figure window for each XY Scope block in the model at the start 
 * of the simulation. The XY Scope block accepts real signals of type Double.
 * 
 * @author Jos� L. Risco-Mart�n
 *
 */
public class ScopeLineXY extends ScopeXY {

    public ScopeLineXY(String name) {
		super(name, ScopeViewXY.MODE.XYLine);
	}
}
