package xdevs.lib.projects.uavs;

import java.util.ArrayList;
import java.util.Vector;


import testing.kernel.modeling.AtomicState;
import testing.kernel.modeling.DevsDessMessage;


public class PeticionesState extends AtomicState{
	public static final String In = "in";

	private DevsDessMessage msg;
	
	private static final String estado = "Estado";
	
	public static final String PeticionAvion = "OutPeticionAvion";
	
	public static final String PeticionEscenario = "OutPeticionEscenario";
	
	public static final String PeticionControladorRumbo = "OutPeticionRumbo";
	
	public static final String PeticionRuta = "OutPeticionRuta";


	
	public PeticionesState (String name) {
		super(name);
		msg = new DevsDessMessage();
		addInport(In);
		addOutport(PeticionControladorRumbo);
		addOutport(PeticionEscenario);
		addOutport(PeticionAvion);
		addOutport(PeticionRuta);
		addState(estado);
		setStateValue(estado,0);
		this.setSigma(INFINITY);
	}

	@Override
	public void deltext(double arg0, DevsDessMessage arg1) {
		this.setSigma(0);
	}

	@Override
	public void deltint() {
		msg = new DevsDessMessage();
		setStateValue(estado,0);
		this.setSigma(INFINITY);
	}

	@Override
	public DevsDessMessage lambda() {
		return msg;
	}


	@SuppressWarnings("unchecked")
	public void reset (Vector<Double> posicion, Vector<Double> velocidad) {
		try{
			Vector peticion= new Vector();
			peticion.add(new Integer (AvionState.Reset));
			peticion.add(posicion.get(0));
			peticion.add(posicion.get(1));
			peticion.add(posicion.get(2));
			peticion.add(velocidad.get(0));
			peticion.add(velocidad.get(1));
			peticion.add(velocidad.get(2));
			msg.add(PeticionAvion,peticion);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,0))/(int)Math.pow(2,0))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,0));
		}
		catch (Exception e) {
		}			
	}
	
	@SuppressWarnings("unchecked")
	public void cambiarVelocidad (double vref) {
		try {
			Vector a= new Vector();
			a.add(new Integer (AvionState.CambiarVelocidad));
			a.add(vref);
			msg.add(PeticionAvion, a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,2))/(int)Math.pow(2,2))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,2));
		}
		catch (Exception e) {
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cambiarAnguloPhi (Double phi) {
		try {
			Vector a= new Vector();
			a.add(new Integer (AvionState.CambiarAnguloPhi));
			a.add(phi);
			msg.add(PeticionAvion,a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,3))/(int)Math.pow(2,3))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,3));
		}
		catch (Exception e) {
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void cambiarAltura (Double altura) {
		try {
			Vector a= new Vector();
			a.add(new Integer (AvionState.CambiarAltura));
			a.add(altura);
			msg.add(PeticionAvion, a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,4))/(int)Math.pow(2,4))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,4));
		}
		catch (Exception e) {
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void ponerFuel (Double fuel) {
		try {
			Vector a= new Vector();
			a.add(new Integer (AvionState.PonerFuel));
			a.add(fuel);
			msg.add(PeticionAvion,a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,5))/(int)Math.pow(2,5))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,5));
		}
		catch (Exception e) {
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void iniciaSimulacion (Double fuel) {
		try {
			Vector a= new Vector();
			a.add(new Integer (AvionState.IniciarSimulacion));
			a.add(fuel);
			msg.add(PeticionAvion, a);
			if (((getStateValue(estado).intValue()&(int)Math.pow(2,6))/(int)Math.pow(2,6))!=1)
				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,6));
		}
		catch (Exception e) {
		}
	}
	
	@SuppressWarnings("unchecked")
	public void finSimulacion () {
		Vector a= new Vector();
		a.add(new Integer (AvionState.FinSimulacion));
		msg.add(PeticionAvion, a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,7))/(int)Math.pow(2,7))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,7));
	}
	
	@SuppressWarnings("unchecked")
	public void destruirAvion () {
		Vector a= new Vector();
		a.add(new Integer (AvionState.DestruirAvion));
		msg.add(PeticionAvion, a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,8))/(int)Math.pow(2,8))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,8));
	}
	
	@SuppressWarnings("unchecked")
	public void getPosicion () {
		Vector a= new Vector();
		a.add(new Integer (AvionState.GetPosicion));
		msg.add(PeticionAvion, a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,9))/(int)Math.pow(2,9))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,9));
	}
	
	@SuppressWarnings("unchecked")
	public void getVelocidad () {
		Vector a= new Vector();
		a.add(new Integer (AvionState.GetVelocidad));
		msg.add(PeticionAvion, a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,10))/(int)Math.pow(2,10))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,10));
	}
	
	@SuppressWarnings("unchecked")
	public void getAngulos () {
		Vector a= new Vector();
		a.add(new Integer (AvionState.GetAngulos));
		msg.add(PeticionAvion, a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,11))/(int)Math.pow(2,11))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,11));
	}
	
	@SuppressWarnings("unchecked")
	public void getFuel () {
		Vector a= new Vector();
		a.add(new Integer (AvionState.GetFuel));
		msg.add(PeticionAvion, a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,12))/(int)Math.pow(2,12))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,12));
	}
	
	@SuppressWarnings("unchecked")
	public void getEstado () {
		Vector a= new Vector();
		a.add(new Integer (AvionState.GetEstado));
		msg.add(PeticionAvion, a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,13))/(int)Math.pow(2,13))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,13));
	}

	@SuppressWarnings("unchecked")
	public void PosicionRef(Double dpn, Double dpe, Double dph) {
		Vector solicitud= new Vector();
		solicitud.add(new Integer (ControladorRumboState.CamPosRef));
		solicitud.add(new Double(dpn));
		solicitud.add(new Double(dpe));
		solicitud.add(new Double(dph));	
		msg.add(PeticionControladorRumbo,solicitud);		
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,14))/(int)Math.pow(2,14))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,14));
	}
	
	@SuppressWarnings("unchecked")
	public void CambiarRuta(ArrayList ruta) {
		Vector solicitud= new Vector();
		solicitud.add(new Integer (RutaState.NRUTA));
		solicitud.add(ruta);	
		msg.add(PeticionRuta,solicitud);		
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,15))/(int)Math.pow(2,15))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,15));
	}
}