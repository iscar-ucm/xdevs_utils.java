package xdevs.lib.projects.barcos;

import java.util.ArrayList;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;

public class PeticionesStateBarco extends AtomicState{
	private static final String estado = "Estado";	
	public Port<?> in = new Port<>("in");
	public Port<ArrayList<Vector<Double>>> peticionBarco = new Port<>("OutPeticionBarco");
	protected ArrayList<Vector<Double>> msg = new ArrayList<>();
	
	//public static final String PeticionControladorRumbo = "OutPeticionRumbo";
	
	//public static final String PeticionRuta = "OutPeticionRuta";

	
	public PeticionesStateBarco (String name) {
		super(name);
		addInPort(in);
		addOutPort(peticionBarco);
		addState(estado);
		setStateValue(estado,0);
		this.passivate();
	}

	@Override
	public void initialize() {
		msg.clear();
		super.passivate();
	}

	@Override
	public void exit() {
	}

	@Override
	public void deltext(double e) {
		super.resume(e);
		this.activate();
	}


	@Override
	public void deltint() {
		msg.clear();
		setStateValue(estado,0);
		this.passivate();
	}

	@Override
	public void lambda() {
		peticionBarco.addValue(msg);
	}


	public void reset (Vector<Double> posicion, Vector<Double> velocidad) {
		Vector<Double> peticion = new Vector<>();
		peticion.add(Double.valueOf(BarcoState.Reset));
		peticion.add(posicion.get(0));
		peticion.add(posicion.get(1));
		peticion.add(velocidad.get(0));
		peticion.add(velocidad.get(1));
		// VELOCIDAD DERIVA
		peticion.add(velocidad.get(2));
		peticion.add(velocidad.get(3));
		msg.add(peticion);
		if (((getStateValue(estado).intValue() & (int) Math.pow(2, 0)) / (int) Math.pow(2, 0)) != 1)
			setStateValue(estado, getStateValue(estado).intValue() + (int) Math.pow(2, 0));
	}
	
	public void cambiarVelocidad (double vref) {
		Vector<Double> a = new Vector<>();
		a.add(Double.valueOf(BarcoState.CambiarVelocidad));
		a.add(vref);
		msg.add(a);
		if (((getStateValue(estado).intValue() & (int) Math.pow(2, 2)) / (int) Math.pow(2, 2)) != 1)
			setStateValue(estado, getStateValue(estado).intValue() + (int) Math.pow(2, 2));
	}

	public void ponRutaReferencia(double ruta){
		// System.out.println("por aqui 3");
		Vector<Double> a = new Vector<>();
		a.add(1.0 * ModeloReferencia.CambioRumbo);
		a.add(ruta);
		msg.add(a);
	}

	public void cambiarAnguloTimon (Double timon) {
		Vector<Double> a = new Vector<>();
		a.add(1.0 * BarcoState.CambiarAnguloTimon);
		a.add(timon);
		msg.add(a);
		if (((getStateValue(estado).intValue() & (int) Math.pow(2, 3)) / (int) Math.pow(2, 3)) != 1)
			setStateValue(estado, getStateValue(estado).intValue() + (int) Math.pow(2, 3));
	}
	
//	@SuppressWarnings("unchecked")
//	public void cambiarAltura (Double altura) {
//		try {
//			Vector a= new Vector();
//			a.add(new Integer (AvionState.CambiarAltura));
//			a.add(altura);
//			msg.add(PeticionAvion, a);
//			if (((getStateValue(estado).intValue()&(int)Math.pow(2,4))/(int)Math.pow(2,4))!=1)
//				setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,4));
//		}
//		catch (Exception e) {
//		}		
//	}
	
	public void ponerFuel (Double fuel) {
		Vector<Double> a = new Vector<>();
		a.add(1.0*BarcoState.PonerFuel);
		a.add(fuel);
		msg.add(a);
		if (((getStateValue(estado).intValue() & (int) Math.pow(2, 5)) / (int) Math.pow(2, 5)) != 1)
			setStateValue(estado, getStateValue(estado).intValue() + (int) Math.pow(2, 5));
	}
	
	public void iniciaSimulacion (Double fuel) {
		Vector<Double> a = new Vector<>();
		a.add(1.0*BarcoState.IniciarSimulacion);
		a.add(fuel);
		msg.add(a);
		if (((getStateValue(estado).intValue() & (int) Math.pow(2, 6)) / (int) Math.pow(2, 6)) != 1)
			setStateValue(estado, getStateValue(estado).intValue() + (int) Math.pow(2, 6));
	}
	
	public void finSimulacion () {
		Vector<Double> a= new Vector<>();
		a.add(1.0*BarcoState.FinSimulacion);
		msg.add(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,7))/(int)Math.pow(2,7))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,7));
	}
	
	public void destruirBarco () {
		Vector<Double> a= new Vector<>();
		a.add(1.0*BarcoState.DestruirBarco);
		msg.add(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,8))/(int)Math.pow(2,8))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,8));
	}
	
	public void getPosicion () {
		Vector<Double> a= new Vector<>();
		a.add(1.0*BarcoState.GetPosicion);
		msg.add(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,9))/(int)Math.pow(2,9))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,9));
	}
	
	public void getVelocidad () {
		Vector<Double> a= new Vector<>();
		a.add(1.0*BarcoState.GetVelocidad);
		msg.add(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,10))/(int)Math.pow(2,10))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,10));
	}
	
	public void getAngulos () {
		Vector<Double> a= new Vector<>();
		a.add(1.0*BarcoState.GetAngulos);
		msg.add(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,11))/(int)Math.pow(2,11))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,11));
	}
	
	public void getFuel () {
		Vector<Double> a= new Vector<>();
		a.add(1.0*BarcoState.GetFuel);
		msg.add(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,12))/(int)Math.pow(2,12))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,12));
	}
	
	public void getEstado () {
		Vector<Double> a= new Vector<>();
		msg.add(a);
		if (((getStateValue(estado).intValue()&(int)Math.pow(2,13))/(int)Math.pow(2,13))!=1)
			setStateValue(estado,getStateValue(estado).intValue()+(int)Math.pow(2,13));
	}
}