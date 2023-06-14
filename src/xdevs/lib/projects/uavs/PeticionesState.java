package xdevs.lib.projects.uavs;

import java.util.ArrayList;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;


public class PeticionesState extends AtomicState {
	public Port<Object> in = new Port<>("in");

	private static final String estado = "Estado";
	
	public Port<Vector<Number>> peticionAvion = new Port<>("OutPeticionAvion");
	
	public Port<Object> peticionEscenario = new Port<>("OutPeticionEscenario");
	
	public Port<Vector<Number>> peticionControladorRumbo = new Port<>("OutPeticionRumbo");
	
	public Port<Vector<Number>> peticionRuta = new Port<>("OutPeticionRuta");


	
	public PeticionesState (String name) {
		super(name);
		addInPort(in);
		addOutPort(peticionControladorRumbo);
		addOutPort(peticionEscenario);
		addOutPort(peticionAvion);
		addOutPort(peticionRuta);
		addState(estado);
		setStateValue(estado,0);
		super.passivate();
	}

	@Override
	public void deltext(double e) {
		this.activate();
	}

	@Override
	public void deltint() {
		setStateValue(estado,0);
		super.passivate();
	}

	@Override
	public void lambda() {
	}


	@SuppressWarnings("unchecked")
	public void reset (Vector<Double> posicion, Vector<Double> velocidad) {
		try{
			Vector<Number> peticion= new Vector<>();
			peticion.add(AvionState.Reset);
			peticion.add(posicion.get(0));
			peticion.add(posicion.get(1));
			peticion.add(posicion.get(2));
			peticion.add(velocidad.get(0));
			peticion.add(velocidad.get(1));
			peticion.add(velocidad.get(2));
			peticionAvion.addValue(peticion);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,0))/(int)Math.pow(2,0))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,0));
		}
		catch (Exception e) {
		}			
	}
	
	@SuppressWarnings("unchecked")
	public void cambiarVelocidad (double vref) {
		try {
			Vector<Number> a= new Vector<>();
			a.add(AvionState.CambiarVelocidad);
			a.add(vref);
			peticionAvion.addValue(a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,2))/(int)Math.pow(2,2))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,2));
		}
		catch (Exception e) {
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cambiarAnguloPhi (Double phi) {
		try {
			Vector<Number> a= new Vector<>();
			a.add(AvionState.CambiarAnguloPhi);
			a.add(phi);
			peticionAvion.addValue(a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,3))/(int)Math.pow(2,3))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,3));
		}
		catch (Exception e) {
		}		
	}
	
	public void cambiarAltura (Double altura) {
		try {
			Vector<Number> a= new Vector<>();
			a.add(AvionState.CambiarAltura);
			a.add(altura);
			peticionAvion.addValue(a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,4))/(int)Math.pow(2,4))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,4));
		}
		catch (Exception e) {
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void ponerFuel (Double fuel) {
		try {
			Vector<Number> a= new Vector<>();
			a.add(AvionState.PonerFuel);
			a.add(fuel);
			peticionAvion.addValue(a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,5))/(int)Math.pow(2,5))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,5));
		}
		catch (Exception e) {
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void iniciaSimulacion (Double fuel) {
		try {
			Vector<Number> a= new Vector<>();
			a.add(AvionState.IniciarSimulacion);
			a.add(fuel);
			peticionAvion.addValue(a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,6))/(int)Math.pow(2,6))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,6));
		}
		catch (Exception e) {
		}
	}
	
	public void finSimulacion () {
		Vector<Number> a= new Vector<>();
		a.add(AvionState.FinSimulacion);
		peticionAvion.addValue(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,7))/(int)Math.pow(2,7))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,7));
	}
	
	public void destruirAvion () {
		Vector<Number> a= new Vector<>();
		a.add(AvionState.DestruirAvion);
		peticionAvion.addValue(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,8))/(int)Math.pow(2,8))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,8));
	}
	
	public void getPosicion () {
		Vector<Number> a= new Vector<>();
		a.add(AvionState.GetPosicion);
		peticionAvion.addValue(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,9))/(int)Math.pow(2,9))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,9));
	}
	
	public void getVelocidad () {
		Vector<Number> a= new Vector<>();
		a.add(AvionState.GetVelocidad);
		peticionAvion.addValue(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,10))/(int)Math.pow(2,10))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,10));
	}
	
	public void getAngulos () {
		Vector<Number> a= new Vector<>();
		a.add(AvionState.GetAngulos);
		peticionAvion.addValue(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,11))/(int)Math.pow(2,11))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,11));
	}
	
	public void getFuel () {
		Vector<Number> a= new Vector<>();
		a.add(AvionState.GetFuel);
		peticionAvion.addValue(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,12))/(int)Math.pow(2,12))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,12));
	}
	
	public void getEstado () {
		Vector<Number> a= new Vector<>();
		a.add(AvionState.GetEstado);
		peticionAvion.addValue(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,13))/(int)Math.pow(2,13))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,13));
	}

	public void PosicionRef(Double dpn, Double dpe, Double dph) {
		Vector<Number> solicitud= new Vector<>();
		solicitud.add(ControladorRumboState.CamPosRef);
		solicitud.add(dpn);
		solicitud.add(dpe);
		solicitud.add(dph);	
		peticionControladorRumbo.addValue(solicitud);		
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,14))/(int)Math.pow(2,14))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,14));
	}
	
	public void CambiarRuta(ArrayList<Number> ruta) {
		Vector<Number> solicitud= new Vector<>();
		solicitud.add(RutaState.NRUTA);
		solicitud.addAll(ruta);	
		peticionRuta.addValue(solicitud);		
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,15))/(int)Math.pow(2,15))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,15));
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}
}