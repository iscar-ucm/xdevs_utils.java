package xdevs.lib.general;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class FlowGenerator extends Atomic {

	// Ports
	public static final String stopName = "stop";
	public static final String outName = "out";
    protected Port<Object> stop;
    protected Port<Double> out;
	
	// State
	protected double period;
	protected double flow;
	

	protected double _amplitude;
	protected double _bias;
	protected double _phase;
	protected double _frecuency;
	
	
	protected boolean tend = true;
	protected double time; 
	
	
	public FlowGenerator(String name, double period, double time, double _amplitude, double _phase, double _frecuency, double _bias){
		super(name);
        stop = new Port<Object>(stopName);
        out = new Port<Double>(outName);
		addInPort(stop);
		addOutPort(out);
		this.period = period;
		this.holdIn("active",period);
		this.time = time;
		this._amplitude = _amplitude;
		this._phase = _phase;
		this._frecuency = _frecuency;
		this._bias = _bias;
		
	}

	public void deltint() {
		time+=period;
		flow = _amplitude*Math.sin(_frecuency*time+_phase)+_bias;
		this.holdIn("active",period);
	}

	public void deltext(double e) {
        super.resume(e);
		if(!stop.isEmpty())
            super.passivate();
	}

	public void lambda() {
		out.addValue(flow);
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.holdIn("active", this.period);
	}

}
