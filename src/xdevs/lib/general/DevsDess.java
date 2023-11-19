package xdevs.lib.general;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import xdevs.lib.numdevs.integrator.IntegratorEuler;

public class DevsDess extends Coupled {

    public static final String inName = "in";
    public static final String outName = "out";
	public static final String stateName = "state";
    protected Port<Double> in = new Port<Double>(inName);
    protected Port<Double> out = new Port<Double>(outName);
    protected Port<Boolean> state = new Port<Boolean>(stateName);
	
	public DevsDess(String name, IntegratorEuler integrator, double threshold){
		
		super(name);
        super.addInPort(in);
        super.addOutPort(out);
        super.addOutPort(state);
		
		Level level = new Level("Level",threshold);
		
		addComponent(integrator);
		addComponent(level);

        // EIC
        super.addCoupling(in, integrator.in);
        // IC
        super.addCoupling(level.out, integrator.reset);
        super.addCoupling(integrator.out, level.in);
        // EOC
        super.addCoupling(integrator.out, out);
        super.addCoupling(level.out, state);
	}
	
}
