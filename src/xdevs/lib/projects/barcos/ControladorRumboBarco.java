package ssii2007.barcos;

import java.util.Iterator;
import java.util.Vector;

import testing.kernel.modeling.AtomicState;
import testing.kernel.modeling.DevsDessMessage;

import ssii2007.matematico.IFuncion;
import ssii2007.matematico.IIntegrador;
import ssii2007.matematico.RungeKutta;

public class ControladorRumboBarco extends AtomicState implements IFuncion{

	private IIntegrador integrador;
	
	public static final String puertoInMRef = "puertoInMRef";
	
	public static final String puertoInBarco = "puertoInBarco";
	
	public static final String puertoOut = "puertoOut";
	
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
		addInport(puertoInMRef);
		addInport(puertoInBarco);
		addOutport(puertoOut);
		integrador=new RungeKutta();
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
		setSigma(INFINITY);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deltext(double e, DevsDessMessage x) {
		
		//si llega de avion, almacenar
		//se llega de modelo referencia, enviar a avion, poner sigma a cero
		
			Iterator iteradorBarco = x.getValuesOnPort(ControladorRumboBarco.puertoInBarco).iterator();
			Iterator iteradorModeloRef = x.getValuesOnPort(ControladorRumboBarco.puertoInMRef).iterator();
			while (iteradorBarco.hasNext()) {
				Vector solicitud = ((Vector)iteradorBarco.next());
				Vector estados = (Vector)(solicitud.get(0));

				/*ERROR*/
				
				
				this.setStateValue("psi", (Double)(estados.get(2)));
				this.setStateValue("r",(Double)(estados.get(3)));
			}
			
			while (iteradorModeloRef.hasNext()) {
				//En primer lugar obtenemos el tipo de solicitud
				Vector solicitud = ((Vector)iteradorModeloRef.next());
				
				
				this.setStateValue("psir", (Double)(solicitud.get(0)));
				this.setStateValue("rr", (Double)(solicitud.get(1)));
				this.setStateValue("alfar", (Double)(solicitud.get(2)));
				this.setSigma(0);
			}
	}

	@Override
	public void deltint() {
		setSigma(INFINITY);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public DevsDessMessage lambda() {
		avanzaTiempo();
		//imprimeEstado();
		DevsDessMessage msg = new DevsDessMessage();
		Vector mensaje = new Vector(5,0);
		mensaje.add(new Integer(BarcoState.CambiarAnguloTimon));
		mensaje.add(new Double (getStateValue("deltac").doubleValue()));
		msg.add("puertoOut", mensaje);
		return msg;
		
	}

	public void actualizaEstados(double[] estadosActuales) {
		// TODO Auto-generated method stub
		this.setStateValue("x1",estadosActuales[0]);
		
	}

	public void avanzaTiempo() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		double[] estado=new double[1];
		estado[0]=this.getStateValue("x1").doubleValue();
		return estado;
	}

}
