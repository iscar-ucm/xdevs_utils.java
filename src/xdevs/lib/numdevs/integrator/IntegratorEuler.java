package testing.lib.atomic.continuous;


import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;


public class IntegratorEuler extends Atomic {
	public static final String inName = "in";
    public static final String resetName = "reset";
	public static final String outName = "out";

    protected Port<Double> in = new Port<Double>(inName);
    protected Port<Object> reset = new Port<Object>(resetName);
    protected Port<Double> out = new Port<Double>(outName);

	protected double dx;	// Entrada
	protected double x;	// Estado en el instante t
	
	// Constructor 
	public IntegratorEuler(String name, double x0) {
		super(name);
		super.addInport(in);
		super.addInport(reset);
		super.addOutport(out);
		this.dx = 0.0;
		this.x = x0;
		super.holdIn("initial",0.0);
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
            dx = in.getValue();
            EulerStep(e);
            super.holdIn("active", 0.0);
        }
	}
	
	public void lambda() {
        out.setValue(x);
	}
	
	public void EulerStep(double h) {
        x += h*dx;
    }

}
