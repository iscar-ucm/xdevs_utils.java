package xdevs.lib.numdevs.integrator;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class IntegratorEulerN extends Atomic {
    public Port<Double[]> in = new Port<Double[]>("in");
    public Port<Double[]> out = new Port<Double[]>("out");
	
	private Double[] dx;		// Entrada
	private Double[] x;			// Estado en el instante t
	private Double[] x0;			// Estado inicial
	
	// Constructor 
	public IntegratorEulerN(String name, Double[] x0) {
		super(name);                
		super.addInPort(in);
		super.addOutPort(out);
		
		this.dx = new Double[x0.length];
		this.x = new Double[x0.length];
		this.x0 = x0;
	}

	public void deltint() {
		super.passivate();
	}

	public void deltext(double e) {
        super.resume(e);
        Double[] tempValueAtIn = in.getSingleValue();
        if(tempValueAtIn!=null) {
            dx = tempValueAtIn;
            EulerStep(e);
            super.holdIn("active", 0.0);
        }
    }
	
	public void lambda() {
		out.addValue(x);
	}
	
	public void EulerStep(double h) {
		for(int i=0;i<x.length;i++) {
			x[i] += dx[i]*h;
		}
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		for(int i=0;i<x0.length;i++) {
			this.x[i] = x0[i];
			this.dx[i] = 0.0;
		}
		super.holdIn("active",0.0);
	}

}
