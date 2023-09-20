package testing.lib.atomic.continuous;

import xdevs.kernel.modeling.Port;



public class IntegratorEulerWithReset extends IntegratorEuler {

	public static final String inReset = "reset";
    protected Port<Object> reset;
	
	public IntegratorEulerWithReset(String name, double x0) {
		super(name, x0);
        reset = new Port<Object>(inReset);
		super.addInport(reset);
	}
	
    @Override
	public void deltext(double e) {
        super.resume(e);
		if(!reset.isEmpty()) {
            x = 0.0;
        }
		else
			super.deltext(e);		
	}
	
}
