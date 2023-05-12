package xdevs.lib.projects.barcos;

import java.util.Iterator;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.lib.projects.math.IFuncion;
import xdevs.lib.projects.math.IIntegrador;
import xdevs.lib.projects.math.RungeKutta;

/** Clase que implementa el comportamiento del controlador del barco*/
public class ControladorBarco extends AtomicState implements IFuncion{

	private IIntegrador integrador;
	
	public static final int cambioRumbo = 11;
	
	/** Puerto por el que entra la informacion de la peticion*/
	public static final String puertoIn = "puertoIn";
	
	public static final String puertoInAlgoritmo = "puertoInAlgoritmo";
	
	/** Puerto por el que entra la comunicacion del barco*/
	public static final String puertoInBarco = "puertoInBarco";
	
	/** Puerto por el que sale la peticion del nuevo angulo*/
	public static final String puertoOut = "puertoOut";
	
	/** Puerto por el que sale la peticion del nuevo angulo*/
	public static final String puertoConexionExterior = "puertoCE";
	
	/** Posicion x del barco*/
	public static final String x = "x";
	
	/** Posicion y del barco*/
	public static final String y = "y";
	
	/** Posicion x del barco*/
	public static final String x_objetivo = "x_objetivo";
	
	/** Posicion y del barco*/
	public static final String y_objetivo = "y_objetivo";
	
	/** Velocidad x del barco*/
	public static final String vx = "vx";
	
	/** Velocidad y del barco */
	public static final String vy = "vy";

	/** Velocidad de avance del barco */
	public static final String U = "U";
	
	public static final String tiempoActual="tiempoActual";
	private boolean pedir=false;
	/** Angulo barco */
	public static final String ang_barco = "ang_barco";
	
	public static final String ang_barco2 = "ang_barco2";
	
	/** Angulo objetivo */
	public static final String ang_objetivo = "ang_objetivo";
	
	/** Constante para el nombre del estado phase	*/
	private static final String phase = "phase";
	

	/** El diferencial de tiempo de integracion*/
	public static final String dt = "dt";
	
	private static final String racha ="racha";
	
	private static final String temporizador = "temporizador";
	
	/** Constante para el estado de parada	 */
	public static final int PARADA = 0;
	
	/** Constante para el estado de running	 */
	public static final int RUNNING = 1;
	
	public ControladorBarco(String name) {
		super(name);
		integrador=new RungeKutta();
		addInport(puertoInAlgoritmo);
		addInport(puertoIn);
		addInport(puertoInBarco);
		addOutport(puertoOut);
		addOutport(puertoConexionExterior);
		addState("x");
		addState("y");
		addState(racha);
		addState(temporizador);
		setStateValue(racha,0);

		addState("x_objetivo");
		addState("y_objetivo");
		addState("tiempoActual");
		addState("vx");
		addState("vy");
		addState("U");
		addState("ang_barco");
		addState("ang_barco2");
		addState("ang_objetivo");
		addState("phase");
		addState("dt");
		
		setStateValue("tiempoActual",0);
		setStateValue(temporizador,10);
		setStateValue("vx",0);
		setStateValue("vy",0);
		setStateValue("U",7.7);
		this.setStateValue("ang_objetivo", 0);
		setStateValue("dt",5);
		setStateValue("phase",ControladorBarco.PARADA);
		setSigma(5);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deltext(double e, DevsDessMessage x) {
	
		Iterator iteradorBarco = x.getValuesOnPort(ControladorBarco.puertoInBarco).iterator();
		
		while (iteradorBarco.hasNext()) {
			Vector solicitud = ((Vector)iteradorBarco.next());
			Vector estados = (Vector)(solicitud.get(0));
			//NOTA:
			//QUEREMOS, POSICION, VELOCIDAD Y ANGULO PARA DESPUES CALCULAR PSI
			this.setStateValue("x", (Double)(estados.get(0)));
			this.setStateValue("y", (Double)(estados.get(1)));
			this.setStateValue("vx", (Double)(estados.get(6)));
			this.setStateValue("vy",(Double)(estados.get(7)));
			this.setStateValue("U", (Double)(estados.get(8)));	
			this.setStateValue("ang_barco2", (Double)(estados.get(2)));
			if(this.getStateValue("phase").intValue()==PARADA){
			}
		}
		
		Iterator iteradorPeticion = x.getValuesOnPort(ControladorBarco.puertoInAlgoritmo).iterator();
		while (iteradorPeticion.hasNext()) {
			Vector solicitud = ((Vector)iteradorPeticion.next());
			if((Integer)solicitud.get(0)==Integer.parseInt(this.getName())){
				if(this.getStateValue("phase").intValue()==PARADA){
					this.setStateValue("phase", RUNNING);
					this.pedir=true;
				}
				
				if((Integer)solicitud.get(1)==ControladorBarco.cambioRumbo){
					double dpoe,dpe;//Esto son las x?
					double dpon,dpn;//Esto son las y?
					dpoe=((Double)solicitud.get(2)).doubleValue();
					dpe=this.getStateValue("x").doubleValue();
					dpon=((Double)solicitud.get(3)).doubleValue();
					dpn=this.getStateValue("y").doubleValue();
					double ve=this.getStateValue("vx").doubleValue();
					//double vh=this.getStateValue("").doubleValue();
					double vn=this.getStateValue("vy").doubleValue();

					double r1 =dpoe-this.getStateValue("x").doubleValue();
					double r12 = r1*r1;
					double r2= dpon-this.getStateValue("y").doubleValue();
					double r22= r2*r2;
					
					double num_tempo=Math.sqrt((r12)+(r22));
					double den_tempo =((Math.sqrt(ve*ve+vn*vn))*5);
					double valor_temporizador =(num_tempo/den_tempo);
					this.setStateValue("temporizador", valor_temporizador);
				
					
					Double Xi;
					Xi=Math.atan2(dpon-dpn, dpoe-dpe);
					//Xi=(Math.PI-Xi);
					this.setStateValue("ang_objetivo", Xi);


					
				}
				setStateValue("x_objetivo",((Double)solicitud.get(2)).doubleValue());
				setStateValue("y_objetivo",((Double)solicitud.get(3)).doubleValue());
				this.setSigma(5);
			}
			
			//setSigma(0);
		}

		//ARRANCAMOS EL FUNCIONAMIENTO
			double angulo= Math.atan2(this.getStateValue("y_objetivo").doubleValue()-this.getStateValue("y").doubleValue(), 
					this.getStateValue("x_objetivo").doubleValue()-this.getStateValue("x").doubleValue());
			
			this.setStateValue("ang_barco",this.getStateValue("ang_barco2").doubleValue());
		
			//setSigma(0);
	}

	@Override
	public void deltint() {
		_msg = new DevsDessMessage();
		avanzaTiempo();
		//if((this.getStateValue("phase").doubleValue()==RUNNING)){
		//imprimeEstado();	
		//System.out.println("temporizador"+this.getStateValue("temporizador").doubleValue());
		this.setStateValue("temporizador", this.getStateValue("temporizador").doubleValue()-1);
		if(pedir){
			this.setStateValue("x_objetivo", this.getStateValue("x").doubleValue());
			this.setStateValue("y_objetivo", this.getStateValue("y").doubleValue());
		}
		if((pedir)||((this.getStateValue("temporizador").doubleValue())<0)){
			pedir=false;

			this.setStateValue("temporizador", Double.MAX_VALUE);
			Vector solicitud = new Vector();
			solicitud.add(new Integer(Controlador.ActualizaPosicion));
			solicitud.add(new Integer(Integer.parseInt(this.getName())));
			_msg.add(ControladorBarco.puertoConexionExterior,solicitud);
		}	
		//}

		

		setSigma(5);
	}
	
	private Vector<Double> transformarEjes(double xi, Vector<Double> punto) {
		Vector<Double> resultado = new Vector<Double>(2,0);
		Vector<Double> t = new Vector<Double>(4,0);
		t.add(new Double(Math.cos(xi)));
		t.add(new Double(Math.sin(xi)));
		t.add(new Double(-Math.sin(xi)));
		t.add(new Double(Math.cos(xi)));
		resultado.add(new Double (t.get(0).doubleValue()*punto.get(0).doubleValue()+t.get(1).doubleValue()*punto.get(1).doubleValue()));
		resultado.add(new Double (t.get(2).doubleValue()*punto.get(0).doubleValue()+t.get(3).doubleValue()*punto.get(1).doubleValue()));
		return resultado;
	}

	public void imprimeEstado(){
		System.out.println(
				"x "+this.getStateValue("x").doubleValue()+
				"y "+this.getStateValue("y").doubleValue()+
				"x_objetivo"+this.getStateValue("x_objetivo").doubleValue()+
				"y_objetivo "+this.getStateValue("y_objetivo").doubleValue()+
				"vx" + this.getStateValue("vx").doubleValue()+
				"vy" + this.getStateValue("vy").doubleValue()+
				"temporizador"+ this.getStateValue("temporizador").doubleValue()
			);
	}
	
	private double calculaControl(){
		
		Vector<Double> aux = new Vector<Double>(2,0);
		aux.add(new Double (this.getStateValue("x").doubleValue()-getStateValue("x_objetivo").doubleValue()));
		aux.add(new Double (this.getStateValue("y").doubleValue()-getStateValue("y_objetivo").doubleValue()));
		Vector<Double> pn = transformarEjes(this.getStateValue("ang_barco").doubleValue(), aux);
		aux = new Vector<Double>(2,0);
		aux.add(this.getStateValue("vx").doubleValue());
		aux.add(this.getStateValue("vy").doubleValue());
		Vector<Double> vn = transformarEjes(this.getStateValue("ang_barco").doubleValue(), aux);
		
		//tenemos ya por tanto la posicion y la velocidad en el nuevo eje de coordenada...
		
		double psi = Math.atan2(vn.get(1),vn.get(0));
		double y = pn.get(1);
		double wmin = 5e-5;
		double wn=0.0113;
		double L=Math.sqrt(2)*this.getStateValue("U").doubleValue()/wn;
		
		double eta1 = Math.atan(y/L);
		double eta2 =psi;
		double w=0;
		//control no lineal
		double eta=-(eta1 +eta2);
		if(Math.abs(eta)<=90*Math.PI/180){

			w=2*this.getStateValue("U").doubleValue()/L*Math.sin(eta);
		}
		else{
			
			w=Math.signum(eta)*2*this.getStateValue("U").doubleValue()/L;
		}
		
		if(Math.abs(w)<wmin){
			
			w=0;
		}
		return w;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DevsDessMessage lambda() {
		if(_msg==null)_msg = new DevsDessMessage();
		Vector peticion =new Vector();
		peticion.add(new Integer (ModeloReferencia.CambioRumbo));
		
		peticion.add(this.getStateValue("ang_objetivo").doubleValue());
		_msg.add(puertoOut, peticion);
		return _msg;
	}
	
	public void deltcon(double e, DevsDessMessage x) {
		//En el caso de que se produzca una transicion externa de manera simultanea a una transici�n interna,
		//realizaremos en primer lugar la funci�n de transicion externa, puesto que tras la funci�n de transici�n
		//interna tendremos calculados los valores de los datos del avion para el instante k+1
		deltext(e,x);	
		deltint();
	}

	public void actualizaEstados(double[] estadosActuales) {
		this.setStateValue("ang_objetivo", estadosActuales[0]);
		
	}

	public void avanzaTiempo() {
		actualizaEstados(integrador.integra(this, 1, this.getStateValue("tiempoActual").doubleValue()));
		this.setStateValue("tiempoActual", this.getStateValue("tiempoActual").doubleValue()+1);
	}

	public double[] dameControlActual() {
		return null;
	}

	public double[] dameDerivadas(double tiempo, double[] estados,
			double[] control) {
		double[] derivadas=new double[1];
		derivadas[0]=this.calculaControl();
		return derivadas;
	}

	public double[] dameEstadoActual() {
		double[] estado= new double[1];
		estado[0]= this.getStateValue("ang_objetivo").doubleValue();
		return estado;
	}
}
