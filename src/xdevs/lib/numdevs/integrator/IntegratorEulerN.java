package xdevs.lib.numdevs.integrator;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class IntegratorEulerN extends Atomic {
	public static final String inIn = "in";
	public static final String outOut = "out";

    protected Port<Double[]> in;
    protected Port<Double[]> out;
	
	private Double[] dx;		// Entrada
	private Double[] x;			// Estado en el instante t
	
	// Constructor 
	public IntegratorEulerN(String name, double[] x0) {
		super(name);
        in = new Port<Double[]>(IntegratorEuler.inName);
        out = new Port<Double[]>(IntegratorEuler.outName);
		super.addInport(in);
		super.addOutport(out);
		
		this.dx = new Double[x0.length];
		this.x = new Double[x0.length];
		for(int i=0;i<x0.length;i++) {
			this.x[i] = x0[i];
			this.dx[i] = 0.0;
		}

		super.holdIn("active",0.0);
	}

	public void deltint() {
		super.passivate();
	}

	public void deltext(double e) {
        super.resume(e);
        Double[] tempValueAtIn = in.getValue();
        if(tempValueAtIn!=null) {
            dx = tempValueAtIn;
            EulerStep(e);
            super.holdIn("active", 0.0);
        }
    }
	
	public void lambda() {
		out.setValue(x);
	}
	
	public void EulerStep(double h) {
		for(int i=0;i<x.length;i++) {
			x[i] += dx[i]*h;
		}
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'exit'");
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'initialize'");
	}

}
