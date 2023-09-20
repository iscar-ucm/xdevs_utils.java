package testing.lib.atomic.sources;

import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;


/**
 * Modelo atómico que retiene la entrada una determinada cantidad de tiempo
 */

public class Timer extends Atomic {
	// Ports
	public static final String inName = "in";
	public static final String outName = "out";
    protected Port<Double> in = new Port<Double>(inName);
    protected Port<Double> out = new Port<Double>(outName);
	// Parameters
	protected Double _sampleTime; // SampleTime
	
	/**
	 * Generate a Timer function.
	 * The Timer component provides a provides a time to retain an input. 
	 * @param name Name of the component
	 * @param sT Time value
	 */
	public Timer(String name, Double sT){
		super(name);
		addInport(in);
		addOutport(out);
		super.holdIn("initial",0);
		_sampleTime = sT;
	}
	
	public void deltint() {
		if(super.phaseIs("initial"))
            super.holdIn("active", _sampleTime);
	}

	public void deltext(double e) {
        super.resume(e);
    }

	public void lambda() {
		out.setValue(1.0);
	}
	
}
