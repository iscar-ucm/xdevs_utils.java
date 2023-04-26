package xdevs.lib.projects.uavs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import testing.kernel.modeling.AtomicState;
import testing.kernel.modeling.DevsDessMessage;


public class RutaState extends AtomicState{
	
	public static final String InRuta = "INRuta";
	
	public static final String OutWayPoint = "OUTWayPoint";
	
	public static final String InAvion = "INAvion";
	
	private DevsDessMessage _msg;
	
	private static final String puntoActual = "puntoActual";
	
	private static final String turnosAlejandose = "TurnosAlejandose";
	
	private static final String distanciaAnterior = "DistanciaAnterior";
	
	private static final String turnosMinimosRestantes = "TurnosMinimos";
	
	/**
	 * Constante para la entrada de ruta nueva
	 */
	public static final int NRUTA = 0;
	
	private ArrayList _trayectoria;
	
	public RutaState (String nombre) {
		super(nombre);
		addInport(InRuta);
		addInport(InAvion);
		addOutport(OutWayPoint);
		addState(puntoActual);
		setStateValue(puntoActual,-1);
		addState(turnosAlejandose);
		setStateValue(turnosAlejandose,0);
		addState(distanciaAnterior);
		addState(turnosMinimosRestantes);
		setStateValue(turnosMinimosRestantes,-1);
		_trayectoria = new ArrayList();
		_msg = new DevsDessMessage();
		this.setSigma(INFINITY);
	}

	@Override
	public void deltext(double arg0, DevsDessMessage arg1) {
		// TODO Auto-generated method stub
		Iterator iteradorSolicitud = arg1.getValuesOnPort(RutaState.InRuta).iterator();
		while (iteradorSolicitud.hasNext()) {
			//En primer lugar obtenemos el tipo de solicitud
			Vector solicitud = ((Vector)iteradorSolicitud.next());
			//En función del tipo de solicitud, obtenemos los datos y realizamos la operación requerida
			switch ((Integer)solicitud.get(0)) {
				case RutaState.NRUTA: {
					this._trayectoria = (ArrayList)(solicitud.get(1));
					this.pedirPunto();
				}
			}
		}
		//comunicacion recibida por el avion
		Iterator iterador = arg1.getValuesOnPort(RutaState.InAvion).iterator();
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
			double x_obj = new Double(Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(0)))));
			double y_obj = new Double(Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(1)))));
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
		// TODO Auto-generated method stub
		_msg = new DevsDessMessage();
		this.setSigma(INFINITY);
	}

	@Override
	public DevsDessMessage lambda() {
		// TODO Auto-generated method stub
		return (_msg);
	}
	
	@SuppressWarnings("unchecked")
	public void pedirPunto(){
		if(this._trayectoria.size()>getStateValue(puntoActual).intValue()) {
			setStateValue(puntoActual,getStateValue(puntoActual).intValue()+1);
		}
		System.out.println("pedir punto");
		Vector solicitud= new Vector();
		if(_trayectoria.size()>getStateValue(puntoActual).intValue()){
			solicitud.add(new Integer (ControladorRumboState.CamPosRefRut));
			solicitud.add(new Double(Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(0))))));
			solicitud.add(new Double(Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(1))))));
			solicitud.add(new Double(Double.valueOf(((String)(((ArrayList)_trayectoria.get(getStateValue(puntoActual).intValue())).get(2))))));
			_msg.add(RutaState.OutWayPoint,solicitud);
		}

	}
}
