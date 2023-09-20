package testing.lib.atomic.general;

import testing.lib.atomic.continuous.IntegratorEuler;
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.modeling.Port;

public class DevsDess extends Coupled {

    public static final String inName = "in";
    public static final String outName = "out";
	public static final String stateName = "state";
    protected Port<Double> in = new Port<Double>(inName);
    protected Port<Double> out = new Port<Double>(outName);
    protected Port<Boolean> state = new Port<Boolean>(stateName);
	
	public DevsDess(String name, IntegratorEuler integrator, double threshold){
		
		super(name);
        super.addInport(in);
        super.addOutport(out);
        super.addOutport(state);
		
		Level level = new Level("Level",threshold);
		
		addComponent(integrator);
		addComponent(level);

        // EIC
        super.addCoupling(this, inName, integrator, IntegratorEuler.inName);
        // IC
        super.addCoupling(level, Level.outName, integrator, IntegratorEuler.resetName);
        super.addCoupling(integrator, IntegratorEuler.outName, level, Level.inName);
        // EOC
        super.addCoupling(integrator, IntegratorEuler.outName, this, outName);
        super.addCoupling(level, Level.outName, this, stateName);
	}
	
}
