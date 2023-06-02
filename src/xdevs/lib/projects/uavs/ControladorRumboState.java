package xdevs.lib.projects.uavs;

import java.util.Iterator;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.lib.projects.barcos.Controlador;
import xdevs.lib.projects.math.IFuncion;

/**
 * Clase que implementa el comportamiento de un controlador devs del
 * movimiento de un avion
 * @author Propietario
 */
public class ControladorRumboState extends AtomicState implements IFuncion{
	
	//POSIBLE PROBLEMA, HE FIJADO EL PUNTO A 3000 M Y ESTAN A MUCHOS MAS, ALGUN PROBLEMA CON POE, PON
	/**
	 * indica si se acerca, numeros positivos o si se aleja, numeros negativos
	 * asi como el numero de turnos seguidos
	 */
	private static final String racha ="racha";
	
	private static final String phase ="phase";
	
	
	public static final int NO_INICIADO = 1;
	public static final int INICIADO = 2;
	public static final int DESTRUIDO = 3;
	/**
	 * tiempo que lleva el rumbo actual
	 */
	private static final String tiempoRumbo = "tiempoRumbo";
	
	/**
	 * tiempo minimo estimado para llegar al punto de control
	 */
	//private static final String tiempoMinimo = "tminimo";

	/**
	 * Constante para el nombre del estado para saber si el controlador esta activo
	 */
	private static final String activo = "activo";
	
	/**
	 * Constante para el nombre de la coordenada x del origen de la trayectoria
	 */
	private static final String pon = "pon";
	
	/**
	 * Constante para el nombre de la coordenada y del origen de la trayectoria
	 */
	private static final String poe = "poe";
	
	/**
	 * Constante para el nombre de la coordenada x del origen de la trayectoria
	 */
	private static final String poh = "poh";
	
	/**
	 * Constante para el nombre de la coordenada x actual del avion
	 */
	private static final String pn = "pn";
	
	/**
	 * Constante para el nombre de la coordenada y actual del avion
	 */
	private static final String pe = "pe";
	
	/**
	 * Constante para el nombre de la coordenada x actual del avion
	 */
	private static final String ph = "ph";
	
	/**
	 * Constante para el nombre de la velocidad x actual del avion
	 */
	private static final String vn = "vn";
	
	/**
	 * Constante para el nombre de la velocidad y actual del avion
	 */
	private static final String ve = "ve";
	
	/**
	 * Constante para el nombre de la velocidad x actual del avion
	 */
	private static final String vh = "vh";
	
	/**
	 * Constante para el valor del angulo de rumbo
	 */
	private static final String Xi = "Xi";
	
	/**
	 * Constante para el nombre del �ngulo phi para el mensaje
	 */
	private static final String phi = "phi";
	
	/**
	 * Constante para el nombre de la coordenada x actual del avion
	 */
	private static final String temporizador = "temporizador";
	
	
	private static final double wn= 0.5;
	
	
	//PUERTOS DE ENTRADA
	
	//COMUNICACION CON LA VISTA
	
	protected Port<Vector> Inicializar = new Port<Vector>("Inicializar");
	
	protected Port<Vector> InPosRef = new Port<Vector>("INPosRef");

	protected Port<Vector> InPosRefCon = new Port<Vector>("INPosRefCon");
	
	protected Port<Vector> InAvion = new Port<Vector>("INAvion");
	
	//PUERTOS DE SALIDA
	/**
	 * Constante para el puerto de salida para realizar las peticiones al avion
	 */
	protected Port<Vector> OutPeticion = new Port<Vector>("OUTPeticion");
	
	protected Port<Vector> PeticionPunto = new Port<Vector>("PeticionPunto");

	
	
	/**
	 * Constante para la solicitud de cambio de posicion referencia
	 */
	public static final int CamPosRef = 1100;
	
	public static final int CamPosRefRut = 2100;
	public static final int Iniciar = 3234;
	public static final int Destruir = 4234;

	
	/**
	 * booleano que indica si en el proximo ciclo va a pedir valores al avion.
	 */
	//private boolean _pide;
	
	
	public ControladorRumboState (String nombre){
		super(nombre);
		addInPort(InAvion);
		addInPort(Inicializar);
		addInPort(InPosRef);
		addInPort(InPosRefCon);
		addOutPort(OutPeticion);
		addOutPort(PeticionPunto);
		addState(pon);
		addState(poe);
		addState(poh);
		addState(pn);
		addState(pe);
		addState(ph);
		addState(vn);
		addState(ve);
		addState(vh);
		addState(Xi);
		addState(temporizador);
		addState(phi);
		addState(activo);
		addState(racha);
		addState(tiempoRumbo);
		//addState(tiempoMinimo);
		setStateValue(activo,0);
		setStateValue(temporizador,0);
		setStateValue(racha,Double.NEGATIVE_INFINITY);
		setStateValue(phase,ControladorRumboState.NO_INICIADO);
		super.passivate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deltext(double e) {
            super.resume(e);
		/*
		 * VAMOS A VER:
		 * CREADO ESTADO
		 * ANGULO X -> EXISTE?
		 * SI:
		 * 	LLEGA NUEVO PUNTO:
		 * 	SI: CREAMOS NUEVO ANGULO, DESDE PUNTO ACTUAL HASTA PUNTO NUEVO
		 * 	NO: SEGUIMOS CON X.
		 * NO:
		 * LO CREAMOS . arcotangente(POX-PX,POY-PY) ---- REVISAR
		 * 
		 */
		
		if(!Inicializar.isEmpty()){
			Vector desechar=Inicializar.getSingleValue();
			this.setStateValue("phase", ControladorRumboState.INICIADO);
			this.setStateValue("racha", -100);
			this.setStateValue("temporizador", -100);
			super.holdIn("active",0);
		}
		// TODO Auto-generated method stub
		if (!InPosRef.isEmpty()) {
			//En primer lugar obtenemos el tipo de solicitud
			Vector solicitud = InPosRef.getSingleValue();
			if(((solicitud!=null)&&(solicitud.size()<5))||((solicitud.size()==5)&&((Integer)solicitud.get(4)==Integer.parseInt(this.getName())))){

			//En funci�n del tipo de solicitud, obtenemos los datos y realizamos la operaci�n requerida
				
			switch ((Integer)solicitud.get(0)) {
				case ControladorRumboState.CamPosRef: {
					double Xi=0;
					if(this.getStateValue(activo).doubleValue()==0){
						
						double dpoe,dpe;//Esto son las x?
						double dpon,dpn;//Esto son las y?
						dpoe=((Double)solicitud.get(1)).doubleValue();
						dpe=this.getStateValue("pe").doubleValue();
						dpon=((Double)solicitud.get(2)).doubleValue();
						dpn=this.getStateValue("pn").doubleValue();
						if(dpoe != Double.MAX_VALUE){

							double ve=this.getStateValue("ve").doubleValue();
							double vh=this.getStateValue("vh").doubleValue();
							double vn=this.getStateValue("vn").doubleValue();

							double r1 =dpoe-this.getStateValue("pe").doubleValue();
							double r12 = r1*r1;
							double r2= dpon-this.getStateValue("pn").doubleValue();
							double r22= r2*r2;
						
							double num_tempo=Math.sqrt((r12)+(r22));
							double den_tempo =((Math.sqrt(ve*ve+vn*vn))/10);
							double valor_temporizador =(num_tempo/den_tempo);
							this.setStateValue("temporizador", valor_temporizador);
					
			
						
						
							Xi=Math.atan2(dpon-dpn, dpoe-dpe);
							//Xi=(Math.PI-Xi);
							this.setStateValue("Xi", Xi);
						}
							setStateValue(activo,1);
							
						}
						else{
							
							double dpoe,dpe;//Esto son las x?
							double dpon,dpn;//Esto son las y?
							dpoe=((Double)solicitud.get(1));
							dpe=this.getStateValue("pe").doubleValue();
							dpon=((Double)solicitud.get(2));
							if(dpon!= Double.MAX_VALUE){
							dpn=this.getStateValue("pn").doubleValue();
							double ve=this.getStateValue("ve").doubleValue();
							double vh=this.getStateValue("vh").doubleValue();
							double vn=this.getStateValue("vn").doubleValue();
							double r1 =dpoe-this.getStateValue("pe").doubleValue();
							double r12 = r1*r1;
							double r2= dpon-this.getStateValue("pn").doubleValue();
							double r22= r2*r2;
						
							double num_tempo=Math.sqrt((r12)+(r22));
							double den_tempo =((Math.sqrt(ve*ve+vn*vn))/10);
							double valor_temporizador =(num_tempo/den_tempo);
							
							this.setStateValue("temporizador", valor_temporizador);

							Xi=Math.atan2(dpon-dpn, dpoe-dpe);
						//Xi=(Math.PI-Xi);
							this.setStateValue("Xi", Xi);
							}
					}
					if(((Double)solicitud.get(2)).doubleValue()!=Double.MAX_VALUE){
						setStateValue("pon",((Double)solicitud.get(2)).doubleValue());
						setStateValue("poe",((Double)solicitud.get(1)).doubleValue());
						setStateValue("poh",((Double)solicitud.get(3)).doubleValue());
					}
					
					Vector a= new Vector();
					double altura = getStateValue("poh").doubleValue();
					a.add(new Integer (AvionState.CambiarAltura));
					a.add(altura);
					OutPeticion.addValue(a);
	//			 fijarPos(pos);
				}; break;
			}
			
			//this.setSigma(0);
			
			}
			
		}

		Iterator iterador = InPosRefCon.getValues().iterator();
		while (iterador.hasNext()) {
			//En primer lugar obtenemos el tipo de solicitud
			Vector solicitud = ((Vector)iterador.next());
			//En función del tipo de solicitud, obtenemos los datos y realizamos la operaci�n requerida
			switch ((Integer)solicitud.get(0)) {
				case ControladorRumboState.CamPosRefRut: {
					double Xi=0;
					
					if(this.getStateValue("activo").doubleValue()==0){
						
						double dpoe,dpe;//Esto son las x?
						double dpon,dpn;//Esto son las y?
						dpoe=((Double)solicitud.get(1)).doubleValue();
						dpe=this.getStateValue("pe").doubleValue();
						dpon=((Double)solicitud.get(2)).doubleValue();
						dpn=this.getStateValue("pn").doubleValue();

						Xi=Math.atan2(dpon-dpn, dpoe-dpe);
						this.setStateValue("Xi", Xi);
						Xi=Math.PI-Xi;
						setStateValue("activo",1);
					}
					else{
						
						double dpoe,dpe;//Esto son las x?
						double dpon,dpn;//Esto son las y?
						dpoe=((Double)solicitud.get(1)).doubleValue();
						dpe=this.getStateValue("poe").doubleValue();
						dpon=((Double)solicitud.get(2)).doubleValue();
						dpn=this.getStateValue("pon").doubleValue();
						Xi=Math.atan2(dpon-dpn, dpoe-dpe );
						Xi=Math.PI-Xi;
						this.setStateValue("Xi", Xi);
					}
					setStateValue("pon",((Double)solicitud.get(2)).doubleValue());
					setStateValue("poe",((Double)solicitud.get(1)).doubleValue());
					setStateValue("poh",((Double)solicitud.get(3)).doubleValue());
					
					
					Vector a= new Vector();
					double altura = getStateValue(poh).doubleValue();
					a.add(new Integer (AvionState.CambiarAltura));
					a.add(altura);
					OutPeticion.addValue(a);
	//			 fijarPos(pos);
				}; break;
			}
		//	this.setSigma(0);
		}

		
		
		//comunicacion recibida por el avion
		iterador = InAvion.getValues().iterator();
		boolean llega_avion=false;
		while (iterador.hasNext()) {
			llega_avion=true;
			Vector todo = (Vector) iterador.next();
			//ahora le estamos volviendo loco... le estamos cambiando el rumbo objetivo continuamente
			double pea=((Vector<Double>)(todo.get(0))).get(0);
			double pna=((Vector<Double>)(todo.get(0))).get(1);
			double pha=((Vector<Double>)(todo.get(0))).get(2);
			double peo=this.getStateValue("poe").doubleValue();
			double pno=this.getStateValue("pon").doubleValue();
			double pho=this.getStateValue("poh").doubleValue();
			double dactual=Math.sqrt((pea-peo)*(pea-peo)+(pna-pno)*(pna-pno));
			
			if(this.getStateValue("pe")!=null){
				
				pea=this.getStateValue("pe").doubleValue();
				pna=this.getStateValue("pn").doubleValue();
				pha=this.getStateValue("ph").doubleValue();
				double dantigua=Math.sqrt((pea-peo)*(pea-peo)+(pna-pno)*(pna-pno));
				double ve=this.getStateValue("ve").doubleValue();
				double vh=this.getStateValue("vh").doubleValue();
				double vn=this.getStateValue("vn").doubleValue();
				double distancia_recorrible=Math.sqrt(ve*ve+vn*vn);
				
				if(dantigua+0.03*(Math.sqrt(ve*ve+vn*vn))<dactual){
					
					this.setStateValue("racha", this.getStateValue("racha").doubleValue()-1);
				}
				else{
					this.setStateValue("racha", 0);
				}
			}
			this.setStateValue("ve", ((Vector<Double>)(todo.get(1))).get(0));
			this.setStateValue("vn", ((Vector<Double>)(todo.get(1))).get(1));
			this.setStateValue("vh", ((Vector<Double>)(todo.get(1))).get(2));
			this.setStateValue("pe", ((Vector<Double>)(todo.get(0))).get(0));
			this.setStateValue("pn", ((Vector<Double>)(todo.get(0))).get(1));
			this.setStateValue("ph", ((Vector<Double>)(todo.get(0))).get(2));
			//controlUAV(((((Vector<Double>) todo.get(2)).get(2))),((Vector<Double>) todo.get(0)), ((Vector<Double>) todo.get(1)));
			this.setSigma(0);
		}
	}

    @Override
	public void deltcon(double e) {
		//En el caso de que se produzca una transición externa de manera simultanea a una transici�n interna,
		//realizaremos en primer lugar la función de transición externa, puesto que tras la funci�n de transici�n
		//interna tendremos calculados los valores de los datos del avión para el instante k+1
		deltint();
		deltext(e);
		
			
	}
	
	public void imprimeEstado(){
		System.out.println(
				"racha "+this.getStateValue("racha").doubleValue()+
				"temporizador "+this.getStateValue("temporizador").doubleValue()+
				"phase"+this.getStateValue("phase").doubleValue()+
				"activo "+this.getStateValue("activo").doubleValue()+
				"poe" + this.getStateValue("poe").doubleValue()+
				"pon" + this.getStateValue("pon").doubleValue()+
				"pe"+ this.getStateValue("pe").doubleValue()+
				"pn"+ this.getStateValue("pn").doubleValue()
			);
	}
	
	@Override
	public void deltint() {
		controlUAV();
		//imprimeEstado();
		this.setStateValue(temporizador, this.getStateValue(temporizador).doubleValue()-1);
		if((this.getStateValue("phase").intValue()==ControladorRumboState.INICIADO)&&
				(((this.getStateValue("racha").doubleValue()<-1)&&
				(this.getStateValue("temporizador").doubleValue()<0))||(this.getStateValue("racha").doubleValue()<-20))){
			Vector solicitud = new Vector();
			solicitud.add(new Integer(Controlador.ActualizaPosicion));
			solicitud.add(new Integer(Integer.parseInt(this.getName())));
			PeticionPunto.addValue(solicitud);
			this.setStateValue("racha", 10);
			//this.setStateValue(temporizador, 10);
		}
		this.passivate();
	}
	
	@Override
	public void lambda() {
	}
	
	@SuppressWarnings("unchecked")
	public void controlUAV() {
		if (getStateValue(activo).intValue()==1) {
			Vector<Double> aux = new Vector<Double>(2,0);
			aux.add(new Double (getStateValue("pe").doubleValue()-getStateValue("poe").doubleValue()));
			aux.add(new Double (getStateValue("pn").doubleValue()-getStateValue("pon").doubleValue()));
			Vector<Double> pn = transformarEjes(this.getStateValue("Xi").doubleValue(), aux);{
			aux = new Vector<Double>(2,0);
			aux.add(getStateValue("ve").doubleValue());
			aux.add(getStateValue("vn").doubleValue());
			Vector<Double> vn = transformarEjes(this.getStateValue("Xi").doubleValue(), aux);
			double eta2 = Math.atan2(vn.get(1),vn.get(0));
			double va = Math.sqrt(
					getStateValue("ve").doubleValue()*getStateValue("ve").doubleValue()
					+
					getStateValue("vn").doubleValue()*getStateValue("vn").doubleValue()
					);
			
			double l = Math.sqrt(2)*va/(wn);
			double y = pn.get(1).doubleValue();
			double eta1 = Math.atan(y/l);
			double eta = -(eta1+eta2);
			double w = 2*va/l*Math.sin(eta);
			//Máximo factor de carga
			double nmax = nmaxcarga(getStateValue("ph").doubleValue());
			double wmax = 9.8*Math.sqrt((nmax*nmax)-1)/va;
			double wmin = 0.0;
			
			//Mientras el error en dirección sea mayor de 90� máxima w
			if (Math.abs(eta2)>=90*Math.PI/180) {
				w = Math.signum(eta)*wmax;
			}
				
			if (Math.abs(w)>wmax) {		//Saturaci�n
				w = Math.signum(eta)*wmax;
			}
			
			if (Math.abs(w)<wmin) {		//Zona muerta
				w = 0;
			}
			//El avión no admite w como entrada por lo adecuamos los datos para darle la señal de control
			
			double phiValue=0;
			phiValue = Math.atan(va*w/9.8);
			//_rumbo= xi+ eta;
//	if(Math.signum(eta)<0){
//		phi= -(Math.PI)+phi;
//	}
//	double rumbo = xi + eta;
//	if (Math.abs(eta)>=Math.PI/2) {
//		rumbo = xi;
//	} 
			
			setStateValue("phi",phiValue);

			Vector solicitud = new Vector(2,0);
			solicitud.add(new Integer(AvionState.CambiarAnguloPhi));
			solicitud.add(new Double (phiValue));
			OutPeticion.addValue(solicitud);
			}
		}
	}

	public Vector<Double> transformarEjes(double xi, Vector<Double> punto) {
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
	
	public double nmaxcarga(double h) {
		double nMax = 5.3820e-9*h*h-4.4291e-4*h+6.1000e+0;				//Factor n máximo para esa altura
		return nMax;
	}

	public void actualizaEstados(double[] estadosActuales) {
		// TODO Auto-generated method stub
		
	}

	public void avanzaTiempo() {
		// TODO Auto-generated method stub
		
	}

	public double[] dameControlActual() {
		// TODO Auto-generated method stub
		return null;
	}

	public double[] dameDerivadas(double tiempo, double[] estados,
			double[] control) {
		// TODO Auto-generated method stub
		return null;
	}

	public double[] dameEstadoActual() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}

}
