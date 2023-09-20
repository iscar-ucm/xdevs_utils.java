package testing.lib.atomic.general;


import xdevs.kernel.modeling.Atomic;
import testing.kernel.modeling.DevsDessMessage;
import xdevs.kernel.modeling.Port;


public class Level extends Atomic {

	public static final String inName = "in";
	public static final String outName = "out";
    protected Port<Double> in;
    protected Port<Boolean> out;

	
	protected double threshold;
	protected boolean level_crossing;
	
	public Level(String name, double threshold){
		super(name);
        in = new Port<Double>(inName);
        out = new Port<Boolean>(outName);
		super.addInport(in);
		super.addOutport(out);
		
		this.threshold = threshold;
		level_crossing = false;
		passivate();
	}
	
	public void deltext(double e) {
        super.resume(e);
		if(!in.isEmpty() && in.getValue()>threshold)
			super.holdIn("active", 0);
	}

	public void deltint() {	
		level_crossing = true;
		passivate();
	}

	public void lambda() {
		DevsDessMessage msg = new DevsDessMessage();
		if(level_crossing) 
            out.setValue(level_crossing);
	}

}
