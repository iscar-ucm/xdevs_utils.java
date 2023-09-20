package testing.lib.atomic.sources;

import xdevs.kernel.modeling.Atomic;
import testing.kernel.modeling.DevsDessMessage;
import xdevs.kernel.modeling.Port;


/**
 * Pulse generator
 * @author José L. Risco Martín
 */

public class Pulse extends Atomic {
	public static final String outName = "out";
    protected Port<Double> out = new Port<Double>(outName);

	protected Double baseValue;
	protected Double pulseAmplitude;
	protected Double initialStepTime;
	protected Double finalPulseTime;
	protected Integer zone;
	protected Double outValue;

	public Pulse(String name, Double baseValue, Double pulseAmplitude, Double initialStepTime, Double finalPulseTime) {
		super(name);
		addOutport(out);
		this.baseValue = baseValue;
		this.pulseAmplitude = pulseAmplitude;
		this.initialStepTime = initialStepTime;
		this.finalPulseTime = finalPulseTime;
		zone = 0;
		outValue = baseValue;
		super.holdIn("zone0",0);
	}
	
	public void deltint() {
		zone++;
		if(zone==1) {
			super.holdIn("zone1", this.initialStepTime);
			outValue = pulseAmplitude + baseValue;
		}
		else if(zone==2) {
			super.holdIn("zone2", finalPulseTime-initialStepTime);
			outValue = baseValue;
		}
		else {
			super.passivate();
			outValue = null;
		}
	}

	public void deltext(double e) {
        super.resume(e);
    }

	public void lambda() {
		out.setValue(outValue);
	}
}
