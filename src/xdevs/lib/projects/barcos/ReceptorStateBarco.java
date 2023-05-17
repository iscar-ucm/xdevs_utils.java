package xdevs.lib.projects.barcos;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;

public class ReceptorStateBarco extends AtomicState {

	public Port<Vector<Double>> inPosicion = new Port<>("INPosicion");
	public Port<Vector<Double>> inVelocidad = new Port<>("INVelocidad");
	public Port<Vector<Double>> inAngulos = new Port<>("INAngulos");
	public Port<Integer> out = new Port<>("out");

	public ReceptorStateBarco (String name) {
		super(name);
	//	this._controlador=controlador;
		addInPort(inPosicion);
		addInPort(inVelocidad);
		addInPort(inAngulos);
		addOutPort(out);
		super.passivate();
	}
	
	@Override
	public void initialize() {
		super.passivate();
	}

	@Override
	public void exit() {
	}

	public void deltext(double e) {
	}

	@Override
	public void deltint() {
		
	}

	@Override
	public void lambda() {
		out.addValue(1);
	}
}
