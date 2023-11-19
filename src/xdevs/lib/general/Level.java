package xdevs.lib.general;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class Level extends Atomic {
    public Port<Double> in;
    public Port<Boolean> out;

	
	protected double threshold;
	protected boolean level_crossing;
	
	public Level(String name, double threshold){
		super(name);
        in = new Port<Double>("in");
        out = new Port<Boolean>("out");
		super.addInPort(in);
		super.addOutPort(out);
		
		this.threshold = threshold;
		level_crossing = false;
		passivate();
	}
	
	public void deltext(double e) {
        super.resume(e);
		if(!in.isEmpty() && in.getSingleValue()>threshold)
			super.holdIn("active", 0);
	}

	public void deltint() {	
		level_crossing = true;
		passivate();
	}

	public void lambda() {
		if(level_crossing) 
            out.addValue(level_crossing);
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}

}
