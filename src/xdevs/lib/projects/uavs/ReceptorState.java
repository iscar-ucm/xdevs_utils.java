package xdevs.lib.projects.uavs;
import java.util.Iterator;
import java.util.Vector;


import testing.kernel.modeling.AtomicState;
import testing.kernel.modeling.DevsDessMessage;



public class ReceptorState extends AtomicState{

	public static final String InPosicion = "INPosicion";
	public static final String InVelocidad = "INVelocidad";
	public static final String InAngulos = "INAngulos";
	public static final String InFuel = "INFuel";
	public static final String InEstado = "INEstado";
	public static final String InTodo = "INTodo";

	private int _numAvion;
	//private GestorAplicacion _controlador;
	
	public static final String Out = "out";

	public ReceptorState (String name) {
		super(name);
	//	this._controlador=controlador;
		addInport(InPosicion);
		addInport(InVelocidad);
		addInport(InAngulos);
		addInport(InFuel);
		addInport(InEstado);
		addInport(InTodo);
		addOutport(Out);
		_numAvion = Integer.parseInt(name);
		this.setSigma(INFINITY);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deltext(double arg0, DevsDessMessage arg1) {
		
		Iterator it = arg1.getValuesOnPort(InPosicion).iterator();
		while (it.hasNext()) {
			
			Vector<Double> posicion = (Vector<Double>) it.next();
		//	_controlador.posicionAVista(_numAvion,posicion.get(0),posicion.get(1),posicion.get(2));
		}
		it = arg1.getValuesOnPort(InVelocidad).iterator();
		while (it.hasNext()) {
			Vector<Double> velocidad = (Vector<Double>) it.next();
		//	_controlador.velocidadAVista(_numAvion,velocidad.get(0),velocidad.get(1),velocidad.get(2));
		}
		it = arg1.getValuesOnPort(InAngulos).iterator();
		while (it.hasNext()) {
			Vector<Double> angulos = (Vector<Double>) it.next();
		//	_controlador.angulosAVista(_numAvion,angulos.get(0),angulos.get(1),angulos.get(2));
		}
		it = arg1.getValuesOnPort(InFuel).iterator();
		while (it.hasNext()) {
		//	_controlador.fuelAVista(_numAvion,((Double) it.next()).doubleValue());
		}
		it = arg1.getValuesOnPort(InEstado).iterator();
		while (it.hasNext()) {
		//	_controlador.estadoAVista(_numAvion,((Integer) it.next()).intValue());
		}
		it = arg1.getValuesOnPort(InTodo).iterator();
		while (it.hasNext()) {
			
			Vector<Double> pos = ((Vector<Double>)((Vector)it.next()).get(0));
			
		//	_controlador.añadeNuevaPosicion(_numAvion, pos.get(0), pos.get(1), pos.get(2));
		}
	}

	@Override
	public void deltint() {
		
	}

	@Override
	public DevsDessMessage lambda() {
		return new DevsDessMessage();
	}
}
