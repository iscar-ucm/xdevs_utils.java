package xdevs.lib.general.sources;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class SquareWave extends Atomic {
	// Ports
	public static final String outName = "out";
    protected Port<Double> out = new Port<Double>(outName);
	// Parameters
	protected Double amplitude; // vHight
	protected Double period; // Frecuency
	protected Double pulseWidth; // Percentage of period
	protected Double delay;
	
	/**
	 * Generate a SquareWave function.
	 * The SquareWave component provides a square wave (Output a square wave). 
	 * @param name Name of the component
	 * @param vL vLow
	 * @param vH vHight
	 * @param w frecuency
	 */
	public SquareWave(String name, Double amplitude, Double period, Double pulseWidth, Double delay){
		super(name);
		addOutPort(out);
		this.amplitude = amplitude;
		this.period = period;
		this.pulseWidth = pulseWidth;
		this.delay = delay;
		super.holdIn("initial",0);
	}
	
	public void deltint() {
		if(super.getPhase().equals("initial")) {
			super.holdIn("high",delay);
		}
		else if(super.getPhase().equals("high")) {
			super.holdIn("low",period*pulseWidth);
		}
		else if(super.getPhase().equals("low")) {
			super.holdIn("high",period-period*pulseWidth);
		}
	}

	public void deltext(double e) {
        super.resume(e);
    }

	public void lambda() {
		if(super.getPhase().equals("initial"))
			out.addValue(0.0);
		else if(super.getPhase().equals("high"))
			out.addValue(amplitude);
		else if(super.getPhase().equals("low"))
			out.addValue(0.0);
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.holdIn("initial",0);
	}

}
