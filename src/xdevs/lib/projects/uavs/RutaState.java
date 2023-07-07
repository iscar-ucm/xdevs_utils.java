package xdevs.lib.projects.uavs;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;


public class RutaState extends AtomicState{
	
	public Port<Object> InRuta = new Port<>("INRuta");
	
	public Port<ArrayList<Vector<Number>>> OutWayPoint = new Port<>("OUTWayPoint");
	
	public Port<Object> InAvion = new Port<>("INAvion");
		
	private static final String puntoActual = "puntoActual";
	
	private static final String turnosAlejandose = "TurnosAlejandose";
	
	private static final String distanciaAnterior = "DistanciaAnterior";
	
	private static final String turnosMinimosRestantes = "TurnosMinimos";
	
	/**
	 * Constante para la entrada de ruta nueva
	 */
	public static final int NRUTA = 0;
	
	private ArrayList<Vector<Number>> queue = new ArrayList<>();
	private ArrayList<Object> _trayectoria;
	
	public RutaState (String nombre) {
		super(nombre);
		addInPort(InRuta);
		addInPort(InAvion);
		addOutPort(OutWayPoint);
		addState(puntoActual);
		setStateValue(puntoActual,-1);
		addState(turnosAlejandose);
		setStateValue(turnosAlejandose,0);
		addState(distanciaAnterior);
		addState(turnosMinimosRestantes);
		setStateValue(turnosMinimosRestantes,-1);
		_trayectoria = new ArrayList<>();
		super.passivate();
	}

	@Override
	public void deltext(double e) {
		// TODO Auto-generated method stub
		Iterator<Object> iteradorSolicitud = InRuta.getValues().iterator();
		while (iteradorSolicitud.hasNext()) {
			//En primer lugar obtenemos el tipo de solicitud
			Vector<Object> solicitud = (Vector<Object>)(iteradorSolicitud.next());
			//En función del tipo de solicitud, obtenemos los datos y realizamos la operación requerida
			switch ((Integer)solicitud.get(0)) {
				case RutaState.NRUTA: {
					this._trayectoria = (ArrayList<Object>)(solicitud.get(1));
					this.pedirPunto();
				}
			}
		}
		//comunicacion recibida por el avion
		Iterator iterador = InAvion.getValues().iterator();
		while (iterador.hasNext()) {
			Vector todo = (Vector) iterador.next();
			if(_trayectoria.size()>0){
				controlRuta(((Double)(((Vector<Double>) todo.get(2)).get(2))),((Vector<Double>) todo.get(0)), ((Vector<Double>) todo.get(1)),(Double) todo.get(5));
			}
		}
		this.setSigma(0);
	}

	private void controlRuta(Double xi, Vector<Double> p, Vector<Double> v, Double dt) {
		// TODO Auto-generated method stub
		if(this._trayectoria.size()>getStateValue(puntoActual).intValue()){
			double x_obj = Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(0))));
			double y_obj = Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(1))));
			double distancia = Math.sqrt(((p.get(0)-x_obj)*(p.get(0)-x_obj))+((p.get(1)-y_obj)*(p.get(1)-y_obj)));
			System.out.println("turnos restantes"+getStateValue(turnosMinimosRestantes));
			System.out.println("turnos alejandose"+getStateValue(turnosAlejandose));
			if(getStateValue(turnosMinimosRestantes).intValue()==-1){

				double velocidad = Math.sqrt((v.get(0)*v.get(0))+(v.get(1)*v.get(1)));
				setStateValue(turnosMinimosRestantes,(int)((distancia/velocidad)/dt));
			}
			if(getStateValue(turnosAlejandose).intValue()>=3){
				this.pedirPunto();
				setStateValue(turnosAlejandose,0);
				setStateValue(turnosMinimosRestantes,-1);
				setStateValue(distanciaAnterior,Double.POSITIVE_INFINITY);
			}
		
			if(getStateValue(turnosMinimosRestantes).intValue()>0){
				setStateValue(turnosMinimosRestantes,getStateValue(turnosMinimosRestantes).intValue()-1);
			}
			else{
				if(getStateValue(distanciaAnterior).doubleValue()<distancia){
					setStateValue(turnosAlejandose,getStateValue(turnosAlejandose).intValue()+1);
				}
			}
			setStateValue(distanciaAnterior,distancia);
		}
	}

	@Override
	public void deltint() {
		queue.clear();
		super.passivate();
	}

	@Override
	public void lambda() {
		this.OutWayPoint.addValue(queue);
	}
	
	@SuppressWarnings("unchecked")
	public void pedirPunto(){
		if(this._trayectoria.size()>getStateValue(puntoActual).intValue()) {
			setStateValue(puntoActual,getStateValue(puntoActual).intValue()+1);
		}
		System.out.println("pedir punto");
		Vector<Number> solicitud= new Vector<>();
		if(_trayectoria.size()>getStateValue(puntoActual).intValue()){
			solicitud.add(ControladorRumboState.CamPosRefRut);
			solicitud.add(Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(0)))));
			solicitud.add(Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(1)))));
			solicitud.add(Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(2)))));
			queue.add(solicitud);
		}

	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}
}
