package xdevs.lib.projects.barcos;

import java.util.Iterator;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.lib.projects.math.IFuncion;
import xdevs.lib.projects.math.IIntegrador;
import xdevs.lib.projects.math.RungeKutta;

public class ModeloReferencia extends AtomicState implements IFuncion {
	
	private IIntegrador integrador;
		
	// TODO DEVUELVE TODOS LOS DATOS POR ESTE PUERTO
	public Port<Vector<Number>> puertoIn = new Port<>("puertoIn");
	public Port<Vector<Number>> puertoOut = new Port<>("puertoOut");
	
	public static final int Reset = 101; //Reset inicializa
	
	/** Constante para la solicitud de cambio de rumbo */
	public static final int CambioRumbo = 102;

	public static final String tactual = "tactual"; 
	public static final String dt = "dt"; 
	//ESTADOS
	public static final String x1 = "x1"; 
	public static final String x2 = "x2"; 
	public static final String x3 = "x3"; 

	public static final String Zetar = "Zetar"; 
	//public static final String Psid = "Psid";
	public static final String Wr = "Wr"; 
	
	public ModeloReferencia(String name,double _Wr,double _Zr,double _dt) {
		super(name);
		integrador = new RungeKutta();
		//Creamos las variables de las que dependeran las ecuaciones
		addState("Wr");
		setStateValue("Wr",_Wr);
		addState("dt");
		addState("tactual");
		
		addState("Zetar");
		setStateValue("Zetar",_Zr);
		//addState("Psid");
		
		addState("x1");
		setStateValue("x1",0);
		addState("x2");
		setStateValue("x2",0);
		addState("x3");
		setStateValue("x3",0);
		// Damos valores a las constantes
		setStateValue("dt",_dt);
		// Puertos de entrada
		addInPort(puertoIn);
		// Puertos de salida
		addOutPort(puertoOut);
		// Ponemos el tiempo a infinito, hasta que se inicie
		super.passivate();
		
	}

	@Override
	public void initialize() {
		super.passivate();
	}

	@Override
	public void exit() {
	}

	public void ponValor(Number valor){
		setStateValue("tactual",0);
		setStateValue("Psid",valor);
		//System.out.println("angulo "+this.getStateValue("Psid").doubleValue());
		
	}
	
	@Override
	public void deltext(double e) {
		super.resume(e);
		// TODO Auto-generated method stub
	//	System.out.println("llegada mensaje modelo referencia");
		Iterator<Vector<Number>> iteradorSolicitud = puertoIn.getValues().iterator();
		while (iteradorSolicitud.hasNext()) {
			//En primer lugar obtenemos el tipo de solicitud
			Vector<Number> solicitud = iteradorSolicitud.next();
			//En función del tipo de solicitud, obtenemos los datos y realizamos la operación requerida
			switch ((Integer)solicitud.get(0)) {
			case ModeloReferencia.Reset: {
				//VALOR DE RUMBO Y INICIALIZAR DT...
				ponValor(((Double)solicitud.get(1)));
				setSigma(0);
			}; break;
			case ModeloReferencia.CambioRumbo:{
			//	System.out.println("cambio rumbo modelo referencia");
				ponValor((Double)solicitud.get(1));
			//	this.setStateValue("x1",0);
			//	this.setStateValue("x2",0);
			//	this.setStateValue("x3",0);
				setSigma(0);
			}
			}
		}
	}
	
	

	@Override
	public void deltint() {
		// TODO Auto-generated method stub
		setSigma(5);
	}
	
	public void imprimeEstado(){
		System.out.println(
		"tactual "+
		this.getStateValue("tactual").doubleValue()+
		"dt "+
		this.getStateValue("dt").doubleValue()+
		"x1 "+
		this.getStateValue("x1").doubleValue()+
		"x2 "+
		this.getStateValue("x2").doubleValue()+
		"x3 "+
		this.getStateValue("x3").doubleValue()+
		"Zetar "+
		this.getStateValue("Zetar").doubleValue()+
		"Psid "+
		this.getStateValue("Psid").doubleValue()+
		"Wr "+
		this.getStateValue("Wr").doubleValue()
		);
	}

	@Override
	public void lambda() {
		// TODO Auto-generated method stub
		avanzaTiempo();
		//imprimeEstado();
		Vector<Number> vector = new Vector<Number>(3,0);
		double psir = this.getStateValue("x1").doubleValue();
		double rr= this.getStateValue("x2").doubleValue();

		double x1,x2,x3,wr,psid,zr;
		x1=getStateValue("x1").doubleValue();
		x2=getStateValue("x2").doubleValue();
		x3=getStateValue("x3").doubleValue();
		wr=getStateValue("Wr").doubleValue();
		psid=getStateValue("Psid").doubleValue();
		zr=getStateValue("Zetar").doubleValue();
		//double alfar = (-wr*x3+psid);
		double alfar = ((-(wr*wr)*x1-wr*zr*x2+x3));
		vector.add(psir);
		vector.add(rr);
		vector.add(alfar);
		puertoOut.addValue(vector);		
		//ORDEN PSI-> 0, RR ->1, ALFA->2
	}

	public double normalizar(double angulo){
		double angulo_normalizado = angulo;
		if(angulo > Math.PI){angulo_normalizado=normalizar(angulo-2*Math.PI);}
		if(angulo < -Math.PI){angulo_normalizado=normalizar(angulo + 2*Math.PI);}
		return angulo_normalizado;
	}
	
	public void actualizaEstados(double[] estadosActuales) {
		// TODO Auto-generated method stub
		setStateValue("x1",estadosActuales[0]);
		
		if(estadosActuales[1]>1){
			setStateValue("x2",1);	
		}
		else if(estadosActuales[1]<-1){
			setStateValue("x2",-1);	
		}
		else{
			setStateValue("x2",estadosActuales[1]);	
		}
		setStateValue("x3",estadosActuales[2]);
		
	}

	public void avanzaTiempo() {
		// TODO Auto-generated method stub
		this.actualizaEstados(integrador.integra(this, 5,this.getStateValue("tactual").doubleValue()));
		this.setStateValue("tactual", this.getStateValue("tactual").doubleValue()+5);
		
		
	}

	public double[] dameControlActual() {
		// TODO Auto-generated method stub
		return null;
	}

	public double[] dameDerivadas(double tiempo, double[] estados,
			double[] control) {
		// TODO Auto-generated method stub
		double x1,x2,x3,wr,zr,psid;
		x1=getStateValue("x1").doubleValue();
		x2=getStateValue("x2").doubleValue();
		x3=getStateValue("x3").doubleValue();
		wr=getStateValue("Wr").doubleValue();
		zr=getStateValue("Zetar").doubleValue();
		psid=getStateValue("Psid").doubleValue();
		
		double[] derivadas=new double[3];
		
		if(x2<-1){derivadas[0]=-1;}
		else if(x2>1){derivadas[0]=1;}
		else{derivadas[0]=x2;}
		derivadas[1]=(-(wr*wr)*x1-2*wr*zr*x2+x3);
		derivadas[2]=(-wr*x3+wr*wr*wr*psid);
		
		return derivadas;
	}

	public double[] dameEstadoActual() {
		// TODO Auto-generated method stub
		double estado[]= new double[3];
		estado[0]=getStateValue("x1").doubleValue();
		estado[1]=getStateValue("x2").doubleValue();
		estado[2]=getStateValue("x3").doubleValue();
		return estado;
	}
}
