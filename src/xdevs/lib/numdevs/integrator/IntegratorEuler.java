package xdevs.lib.numdevs.integrator;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class IntegratorEuler extends Atomic {
    public Port<Double> in = new Port<Double>("in");
    public Port<Object> reset = new Port<Object>("reset");
    public Port<Double> out = new Port<Double>("out");

	protected double dx;	// Entrada
	protected double x;	// Estado en el instante t
	
	// Constructor 
	public IntegratorEuler(String name, double x0) {
		super(name);
		super.addInPort(in);
		super.addInPort(reset);
		super.addOutPort(out);
		this.dx = 0.0;
		this.x = x0;
		super.activate();
	}

    public IntegratorEuler(String name) {
        this(name, 0);
    }

	public void deltint() {
		super.passivate();
	}

	public void deltext(double e) {
        super.resume(e);
        if(!reset.isEmpty()) {
            x = 0.0;
            super.holdIn("reset", 0);
            return;
        }
        if(!in.isEmpty()) {
            dx = in.getSingleValue();
            EulerStep(e);
            super.holdIn("active", 0.0);
        }
	}
	
	public void lambda() {
        out.addValue(x);
	}
	
	public void EulerStep(double h) {
        x += h*dx;
    }

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.activate();
	}

}
