package xdevs.lib.projects.uavs;
import java.util.Iterator;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;

public class ReceptorState extends AtomicState {

	public Port<Vector<Number>> inPosicion = new Port<>("INPosicion");
	public Port<Vector<Number>> inVelocidad = new Port<>("INVelocidad");
	public Port<Vector<Number>> inAngulos = new Port<>("INAngulos");
	public Port<Vector<Number>> inFuel = new Port<>("InFuel");
	public Port<Vector<Number>> inEstado = new Port<>("InEstado");
	public Port<Vector<Vector<Number>>> inTodo = new Port<>("InTodo");
	public Port<Vector<Number>> out = new Port<>("Out");
	
	public ReceptorState (String name) {
		super(name);
	//	this._controlador=controlador;
		addInPort(inPosicion);
		addInPort(inVelocidad);
		addInPort(inAngulos);
		addInPort(inFuel);
		addInPort(inEstado);
		addInPort(inTodo);
		addOutPort(out);
		super.passivate();
	}
	
	@Override
	public void deltext(double e) {
		super.resume(e);
		if(!inPosicion.isEmpty()) {
			Vector<Number> posicion = inPosicion.getSingleValue();
		}
		if(!inVelocidad.isEmpty()) {
			Vector<Number> velocidad = inVelocidad.getSingleValue();
		}
		if(!inAngulos.isEmpty()) {
			Vector<Number> angulos = inAngulos.getSingleValue();
		}
		if(!inTodo.isEmpty()) {
			Vector<Number> pos = inTodo.getSingleValue().get(0);
		}
	}

	@Override
	public void deltint() {
		
	}

	@Override
	public void lambda() {
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}
}
