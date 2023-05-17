package xdevs.lib.projects.barcos;

import java.util.Iterator;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.lib.projects.math.IFuncion;
import xdevs.lib.projects.math.IIntegrador;
import xdevs.lib.projects.math.RungeKutta;

public class ControladorRumboBarco extends AtomicState implements IFuncion{

	private IIntegrador integrador;
	
	public Port<Vector<Number>> puertoInMRef = new Port<>("puertoInMRef");	
	public Port<Vector<Vector<Number>>> puertoInBarco = new Port<>("puertoInBarco");
	public Port<Vector<Number>> puertoOut = new Port<>("puertoOut");
	
	public static final String dt = "dt";
	
	public static final String tactual = "tactual";
	
	public static final String psir = "psir";
	
	public static final String rr = "rr";
	
	public static final String alfar = "alfar";
	
	public static final String psi = "psi";
	
	public static final String r = "r";
	
	public static final String deltac= "deltac";
	
	public static final String x1 = "x1";
	
	
	//PARAMETROS DEL CONTROLADOR, PASADOS EN EL CONSTRUCTOR
	public static final String Kp = "Kp";
	
	public static final String Kn = "Kn";
	
	public static final String Tn = "Tn";
	
	public static final String Td = "Td";
	
	public static final String Ti = "Ti";
	
	public ControladorRumboBarco(String name,double _kp, double _kn, double _tn, double _td,double _ti) {
		super(name);
		addState("x1");
		addState("deltac");
		addInPort(puertoInMRef);
		addInPort(puertoInBarco);
		addOutPort(puertoOut);
		integrador = new RungeKutta();
		this.addState("Kp");
		this.addState("Kn");
		this.addState("Tn");
		this.addState("Td");
		this.addState("Ti");
		this.addState("tactual");
		this.addState("psir");
		this.addState("rr");
		this.addState("alfar");
		this.addState("psi");
		this.addState("r");
		this.addState("x1");
		this.setStateValue("Kp", _kp);
		this.setStateValue("Kn", _kn);
		this.setStateValue("Tn", _tn);
		this.setStateValue("Td", _td);
		this.setStateValue("Ti", _ti);
		this.setStateValue("x1", 0);
		this.setStateValue("tactual",0);
		super.passivate();
	}

	@Override
	public void initialize() {
		super.passivate();
	}

	@Override
	public void exit() {
	}

	@Override
	public void deltext(double e) {
		
		//si llega de avion, almacenar
		//se llega de modelo referencia, enviar a avion, poner sigma a cero
		
			Iterator<Vector<Vector<Number>>> iteradorBarco = puertoInBarco.getValues().iterator();
			Iterator<Vector<Number>> iteradorModeloRef = puertoInMRef.getValues().iterator();
			while (iteradorBarco.hasNext()) {
				Vector<Vector<Number>> solicitud = iteradorBarco.next();
				Vector<Number> estados = solicitud.get(0);
				this.setStateValue("psi", estados.get(2));
				this.setStateValue("r", estados.get(3));
			}
			
			while (iteradorModeloRef.hasNext()) {
				//En primer lugar obtenemos el tipo de solicitud
				Vector<Number> solicitud = iteradorModeloRef.next();
				this.setStateValue("psir", solicitud.get(0));
				this.setStateValue("rr", solicitud.get(1));
				this.setStateValue("alfar", solicitud.get(2));
				this.setSigma(0);
			}
	}

	@Override
	public void deltint() {
		super.passivate();
		
	}

	@Override
	public void lambda() {
		avanzaTiempo();
		Vector<Number> mensaje = new Vector<>(5,0);
		mensaje.add(BarcoState.CambiarAnguloTimon);
		mensaje.add(getStateValue("deltac"));
		puertoOut.addValue(mensaje);		
	}

	public void actualizaEstados(double[] estadosActuales) {
		this.setStateValue("x1",estadosActuales[0]);
		
	}

	public void avanzaTiempo() {
		this.actualizaEstados(integrador.integra(this, 5,this.getStateValue("tactual").doubleValue()));
		this.setStateValue("tactual", this.getStateValue("tactual").doubleValue()+5);
		double vdeltac=0;
		double valfar,vrr,vr,vx1,vpsir,vpsi;
		double vkp,vkn,vtd,vtn;
		valfar=this.getStateValue("alfar").doubleValue();
		vrr=this.getStateValue("rr").doubleValue();
		vr=this.getStateValue("r").doubleValue();
		vkp=this.getStateValue("Kp").doubleValue();
		vkn=this.getStateValue("Kn").doubleValue();
		vtd=this.getStateValue("Td").doubleValue();
		vtn=this.getStateValue("Tn").doubleValue();
		vx1=this.getStateValue("x1").doubleValue();
		vpsir=this.getStateValue("psir").doubleValue();
		vpsi=this.getStateValue("psi").doubleValue();
		vdeltac=(vx1+
		vkp*vtd*(vrr-vr)+
		(1/vkn)*((vtn*valfar)+vrr)+
		vkp*(normalizar(vpsir-vpsi))
		);
		this.setStateValue("deltac", vdeltac);
	}

	public double[] dameControlActual() {
		return null;
	}
	
	public void imprimeEstado(){

		System.out.println(
		"CONTROLADOR "+
		"psir "+
		this.getStateValue("psir").doubleValue()+
		"rr "+
		this.getStateValue("rr").doubleValue()+
		"alfar "+
		this.getStateValue("alfar").doubleValue()+
		"psi "+
		this.getStateValue("psi").doubleValue()+
		"r "+
		this.getStateValue("r").doubleValue()+
		"deltac "+
		this.getStateValue("deltac").doubleValue()+
		"x1 "+
		this.getStateValue("x1").doubleValue()
		);
	}

	public double[] dameDerivadas(double tiempo, double[] estados,
			double[] control) {
		double[] derivadas= new double[6];
		double vpsir,vpsi;
		double vkp,vti;
		
		vpsir=this.getStateValue("psir").doubleValue();
		vpsi=this.getStateValue("psi").doubleValue();
		vkp=this.getStateValue("Kp").doubleValue();
		vti=this.getStateValue("Ti").doubleValue();
		derivadas[0]=(vkp/vti)*(normalizar(vpsir-vpsi));
		return derivadas;
	}
	
	public double normalizar(double angulo){
		double angulo_normalizado = angulo;
		if(angulo > Math.PI){angulo_normalizado=normalizar(angulo-2*Math.PI);}
		if(angulo < -Math.PI){angulo_normalizado=normalizar(angulo + 2*Math.PI);}
		return angulo_normalizado;
	}
		
	public double[] dameEstadoActual() {
		double[] estado=new double[1];
		estado[0]=this.getStateValue("x1").doubleValue();
		return estado;
	}
}
