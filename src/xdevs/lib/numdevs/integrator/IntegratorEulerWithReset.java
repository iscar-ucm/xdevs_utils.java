package xdevs.lib.numdevs.integrator;

import xdevs.core.modeling.Port;

public class IntegratorEulerWithReset extends IntegratorEuler {
    protected Port<Object> reset = new Port<Object>("reset");
	
	public IntegratorEulerWithReset(String name, Double x0) {
		super(name, x0);
		super.addInPort(reset);
	}
	
    @Override
	public void deltext(double e) {
		if(!reset.isEmpty()) {
	        super.resume(e);
            x = 0.0;
        }
		else
			super.deltext(e);		
	}
	
}
