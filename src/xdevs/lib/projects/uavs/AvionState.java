package xdevs.lib.projects.uavs;
import java.util.Vector;

import javax.media.opengl.GL;

import ssii2007.matematico.IFuncion;
import ssii2007.matematico.IIntegrador;
import ssii2007.matematico.RungeKutta;

import ssii2007.grafico.Dibujable;
import ssii2007.grafico.Dibujante;
import ssii2007.grafico.ModeloAvion;
import ssii2007.grafico.Seguible;
import ssii2007.grafico.estructura.PV3D;
import ssii2007.grafico.estructura.TAfin;
import ssii2007.grafico.estructura.terreno.Terreno;

import testing.kernel.modeling.AtomicState;
import xdevs.kernel.modeling.Port;



/**
 * Clase que implementa el comportamiento de un avi�n seg�n el modelo at�mico de DEVS
 * @author Sergio Gonz�lez Sanz;David Rodilla Rodriguez;Carlos Sanchez Garcia
 */
public class AvionState extends AtomicState implements IFuncion,Dibujable,Seguible{
	
	/**
	 * Controlador 
	 */
//	protected GestorAplicacion _controlador;
	//Puertos de entrada
	/**
	 * Constante para el puerto de entrada de las solicitudes 1
	 */
	protected Port<Vector> InSolicitud1 = new Port<Vector>("INsolicitud1");
	
	/**
	 * Constante para el puerto de entrada de las solicitudes 2
	 */
	protected Port<Vector> InSolicitud2 = new Port<Vector>("INsolicitud2");
	
	
	//Puertos de salida
	/**
	 * Constante para el puerto de salida del vector posicion
	 */
	public static final String OutPosicion = "OUTposicion";
	
	/**
	 * Constante para el puerto de salida del vector velocidad
	 */
	public static final String OutVelocidad = "OUTVelocidad";
	
	/**
	 * Constante para el puerto de salida del vector de angulos
	 */
	public static final String OutAngulos = "OUTAngulos";
	
	/**
	 * Constante para el puerto de salida del nivel de fuel
	 */
	public static final String OutFuel = "OUTFuel";
	
	/**
	 * Constante para el puerto de salida del estado
	 */
	public static final String OutEstado = "OUTEstado";
	
	/**
	 * Constante para el puerto de salida para el controlador
	 */
	protected Port<Vector> OutTodo = new Port<Vector>("OUTTodo");
	
	protected Port<Vector> controlador = new Port<Vector>("controlador");
	
	/**
	 * Constante para el puerto de salida para los valores precisos en ese instante
	 */
	public static final String OutActualizados = "OUTActualizados";
	
	/** Variable que contiene el tiempo actual*/
	public static final String tactual = "tactual";
	
	
	public static final String iniciarControlador = "iniciarControlador";
	
	//Constantes para el estado
	/**
	 * Constante para el estado de parada
	 */
	public static final int PARADA = 0;
	
	/**
	 * Constante para el estado de running
	 */
	public static final int RUNNING = 1;
	
	/**
	 * Constante para el estado de destruido
	 */
	public static final int DESTRUIDO = -1;
	
	//Constantes para las solicitudes
	/**
	 * Constante para la solicitud de reset
	 */
	public static final int Reset = 331;
	
	/**
	 * Constante para la solicitud de cambio de velocidad
	 */
	public static final int CambiarVelocidad = 332;
	
	/**
	 * Constante para la solicitud de cambio de �ngulo Phi
	 */
	public static final int CambiarAnguloPhi = 333;
	
	/**
	 * Constante para la solicitud de cambio de �ngulo Theta
	 */
	public static final int CambiarAltura = 334;
	
	/**
	 * Constante para la solicitud de repostaje
	 */
	public static final int PonerFuel = 335;
	
	/**
	 * Constante para la solicitud de inicio de simulaci�n
	 */
	public static final int IniciarSimulacion = 336;
	
	/**
	 * Constante para la solicitud de fin de simulaci�n
	 */
	public static final int FinSimulacion = 337;
	
	/**
	 * Constante para la solicitud de destrucci�n del avi�n
	 */
	public static final int DestruirAvion = 338;
	
	/**
	 * Constante para la solicitud de posicion
	 */
	public static final int GetPosicion = 339;
	
	/**
	 * Constante para la solicitud de velocidad
	 */
	public static final int GetVelocidad = 3310;
	
	/**
	 * Constante para la solicitud de angulos
	 */
	public static final int GetAngulos = 3311;
	
	/**
	 * Constante para la solicitud de combustible
	 */
	public static final int GetFuel = 3312;
	
	/**
	 * Constante para la solicitud de estado
	 */
	public static final int GetEstado = 3313;
	
	/**
	 * Constante para la solicitud de los datos actualizados
	 */
	public static final int GetActualizados = 3314;
	
	//Valor de la gravedad g
	/**
	 * Constante para el valor de la gravedad
	 */
	public static final double g = 9.8;
	
	//Constantes para los nombre de las variables de estado
	/**
	* Constante para el nombre del estado phase
	*/
	private static final String phase = "Phase";
	
	/**
	* Constante para el nombre del estado pn
	*/
	private static final String pn = "Pn";
	
	/**
	* Constante para el nombre del estado pe
	*/
	private static final String pe = "Pe";
	
	/**
	* Constante para el nombre del estado ph
	*/
	private static final String ph = "Ph";

	/**
	* Constante para el nombre del estado Vn
	*/
	private static final String v = "V";
	
	/**
	* Constante para el nombre del estado velocidad en altura
	*/
	private static final String vh = "vh";
	
	/**
	* Constante para el nombre del estado alabeo
	*/
	private static final String alabeo = "alabeo";
	
	/**
	* Constante para el nombre del estado cabeceo
	*/
	private static final String cabeceo = "cabeceo";
	
	/**
	* Constante para el nombre del estado rumbo
	*/
	private static final String rumbo = "rumbo";
	
	/**
	* Constante para el nombre del estado phase
	*/
	private static final String kp = "kp";		
	
	/**
	* Constante para el nombre del estado phase
	*/
	private static final String dt = "dt";
	
	/**
	* Constante para el nombre del estado phase
	*/
	private static final String dtRestante = "dtRestante";
	
	/**
	* Constante para el nombre del estado fuel
	*/
	private static final String fuel = "Fuel";
	
	/**
	* Constante para el nombre del estado velocidad de referencia
	*/
	private static final String vReferencia = "vReferencia";
	
	/**
	* Constante para el nombre del estado altura de referencia
	*/
	private static final String hck = "hck";
	
	/**
	* Constante para el nombre del estado solicitud de posicion
	*/
	private static final String solicitudPosicion = "solicitudPosicion";	//Variables para conocer si se ha producido una petici�n de alguno de los datos del avi�n
	
	/**
	* Constante para el nombre del estado solicitud de velocidad
	*/
	private static final String solicitudVelocidad = "solicitudVelocidad";
	
	/**
	* Constante para el nombre del estado solicitud de angulos
	*/
	private static final String solicitudAngulos = "solicitudAngulos";
	
	/**
	* Constante para el nombre del estado solicitud de fuel
	*/
	private static final String solicitudFuel = "solicitudFuel";
	
	/**
	* Constante para el nombre del estado solicitud de estado
	*/
	private static final String solicitudEstado = "solicitudEstado";
	
	/**
	 * Constante para el nombre del estado de la solicitud de datos actualizados
	 */
	private static final String solicitudActualizados = "solicitudActualizados";
	
	/**
	 * Constante entera para el valor booleano True
	 */
	private static final int cierto = 1;
	
	/**
	 * Constante entera para el valor booleano False
	 */
	private static final int falso = 0;
	
	/**Integrador*/
	private IIntegrador integrador;
	
	private ModeloAvion _modeloAvion;
	
	private Terreno _terreno;
	
	/**
	 * Constructor del avi�n.
	 * Inicializamos tanto sus puertos de entrada como sus puertos de salida.
	 * Inicializamos los vectores de posici�n, �ngulos y velocidad a 0, y las variables de solicitud a falso
	 * Inicializamos la constante kp al valor pasado como par�metro
	 * @param name
	 * @param kp
	 */
	public AvionState (String name, double kp, Terreno terreno) {
		super(name);
		integrador=new RungeKutta();
		addInport(InSolicitud1);			//Establecemos los puertos de entrada
		addInport(InSolicitud2);			//Establecemos los puertos de entrada
//		addOutport(AvionState.OutPosicion); 			//Establecemos los puertos de salida
		addOutport(controlador); 
		//addOutport(AvionState.OutVelocidad);
		//addOutport(AvionState.OutAngulos);
		//addOutport(AvionState.OutFuel);
		//addOutport(AvionState.OutEstado);
		addOutport(OutTodo);
		//addOutport(AvionState.OutActualizados);
		addState(phase);						//A�adimos los estados
		addState(AvionState.kp);
		addState(dt);
		addState(iniciarControlador);
		addState(dtRestante);
		addState(tactual);
		addState(fuel);
		addState(vReferencia);
		addState(hck);
		addState(solicitudPosicion);
		addState(solicitudVelocidad);
		addState(solicitudAngulos);
		addState(solicitudFuel);
		addState(solicitudEstado);
		addState(solicitudActualizados);
		addState(pn);
		addState(pe);
		addState(ph);
		addState(v);
		addState(alabeo);
		addState(cabeceo);
		addState(rumbo);
		setStateValue(phase,AvionState.PARADA);		//Configuramos el avi�n como detenido
		setStateValue(solicitudPosicion,falso);	//Inicializamos a falso las solicitudes de datos
		setStateValue(solicitudVelocidad,falso);
		setStateValue(solicitudAngulos,falso);
		setStateValue(solicitudFuel,falso);
		setStateValue(solicitudEstado,falso);
		setStateValue(solicitudActualizados,falso);
		setStateValue(AvionState.kp,kp);
		setStateValue("tactual",0);
		setStateValue("iniciarControlador",0);
		setStateValue("pn",0);
		setStateValue("pe",0);
		setStateValue("ph",0);
		setStateValue("vh",0);
		setStateValue("alabeo",0);
		setStateValue("cabeceo",0);
		setStateValue("rumbo",0);
		setStateValue("v",100);
		this.passivate();			//Permanecer� para siempre en este estado hasta que no halla una transici�n externa
		this._terreno = terreno;
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * Funci�n de transici�n externa para el modelo at�mico del avi�n
	 * @param arg0
	 * @param arg1
	 */
	public void deltext(double e) {
		if (getStateValue(phase).intValue() != AvionState.DESTRUIDO) {
			if (InSolicitud1.getValue()!=null) {
				//En primer lugar obtenemos el tipo de solicitud
				Vector solicitud = InSolicitud1.getValue();
				//En funci�n del tipo de solicitud, obtenemos los datos y realizamos la operaci�n requerida
				switch ((Integer)solicitud.get(0)) {
				case AvionState.Reset: {
					
					if(solicitud.size()==8){
					if((Integer)(solicitud.get(7))==Integer.parseInt(this.getName())){
						Vector pos=new Vector();
					 	pos.add(solicitud.get(1));
					 	pos.add(solicitud.get(2));
					 	pos.add(solicitud.get(3));
					 	Vector vel=new Vector();
					 	vel.add(solicitud.get(4));
					 	vel.add(solicitud.get(5));
					 	vel.add(solicitud.get(6));
					 	reset(pos,vel);
					}
					}
					else{
						Vector pos=new Vector();
					 	pos.add(solicitud.get(1));
					 	pos.add(solicitud.get(2));
					 	pos.add(solicitud.get(3));
					 	Vector vel=new Vector();
					 	vel.add(solicitud.get(4));
					 	vel.add(solicitud.get(5));
					 	vel.add(solicitud.get(6));
					 	reset(pos,vel);
					}
				}; break;
				case AvionState.CambiarVelocidad: {
					 cambiarVelocidad((Double)solicitud.get(1));
				}; break;
				case AvionState.CambiarAnguloPhi: {
					cambiarAnguloPhi((Double)solicitud.get(1));
				}; break;
				case AvionState.CambiarAltura: {
					cambiarAltura((Double)solicitud.get(1));
				};break;
				case AvionState.PonerFuel: {
					ponerFuel((Double)solicitud.get(1));
				}; break;
				case AvionState.IniciarSimulacion: {
				
					iniciarSimulacion((Double)solicitud.get(1));
				}; break;
				case AvionState.FinSimulacion: {
					finSimulacion();
				}; break;
				case AvionState.DestruirAvion: {
					destruirAvion();
				}; break;
				case AvionState.GetPosicion: getPosicion(); break;
				case AvionState.GetVelocidad: getVelocidad(); break;
				case AvionState.GetAngulos: getAngulos(); break;
				case AvionState.GetFuel: getFuel(); break;
				case AvionState.GetEstado: getEstado(); break;
				case AvionState.GetActualizados: getActualizados(); break;
				}
			}
			
			if (InSolicitud2.getValue()!=null) {
				
				//En primer lugar obtenemos el tipo de solicitud
				Vector solicitud = InSolicitud2.getValue();
				//En funci�n del tipo de solicitud, obtenemos los datos y realizamos la operaci�n requerida
				
				switch ((Integer)solicitud.get(0)) {
				case AvionState.Reset: {
					 Vector pos=new Vector();
					 pos.add(solicitud.get(1));
					 pos.add(solicitud.get(2));
					 pos.add(solicitud.get(3));
					 Vector vel=new Vector();
					 vel.add(solicitud.get(4));
					 vel.add(solicitud.get(5));
					 vel.add(solicitud.get(6));
					 reset(pos,vel);
				}; break;
				case AvionState.CambiarVelocidad: {
					 cambiarVelocidad((Double)solicitud.get(1));
				}; break;
				case AvionState.CambiarAnguloPhi: {		
					cambiarAnguloPhi((Double)solicitud.get(1));
				}; break;
				case AvionState.CambiarAltura: {
					cambiarAltura((Double)solicitud.get(1));
				};break;
				case AvionState.PonerFuel: {
					ponerFuel((Double)solicitud.get(1));
				}; break;
				case AvionState.IniciarSimulacion: {
					iniciarSimulacion((Double)solicitud.get(1));
				}; break;
				case AvionState.FinSimulacion: {
					finSimulacion();
				}; break;
				case AvionState.DestruirAvion: {
					destruirAvion();
				}; break;
				case AvionState.GetPosicion: getPosicion(); break;
				case AvionState.GetVelocidad: getVelocidad(); break;
				case AvionState.GetAngulos: getAngulos(); break;
				case AvionState.GetFuel: getFuel(); break;
				case AvionState.GetEstado: getEstado(); break;
				}
			}
		}
	}


	/**
	 * Inicializa la posición y la velocidad del avi�n a los valores dados
	 * @param posicion
	 * @param velocidad
	 */
	private void reset (Vector<Double> posicion, Vector<Double> velocidad) {
		
		if (getStateValue(phase).intValue() == AvionState.PARADA) {
			setStateValue("pn",posicion.get(0).doubleValue());
			setStateValue("pe",posicion.get(1).doubleValue());
			setStateValue("ph",posicion.get(2).doubleValue());
			
			setStateValue("v",velocidad.get(0).doubleValue());
			setStateValue("hck",posicion.get(2).doubleValue());
			setStateValue("vReferencia",modulo(velocidad));
			//Phi
		//	this._angulos.get(0)posicion.set();
			//Theta
			setStateValue("cabeceo",(Math.asin(velocidad.get(2)/velocidad.get(0).doubleValue())));
			//Chi
			setStateValue("rumbo",(Math.acos(velocidad.get(1)/modulo(velocidad))));
			
			//Enviamos todos los datos para actualizar los diferentes m�dulos
			this.holdIn("active", 0);
		}
	}
	
	private void cambiarVelocidad (double vReferenciaValue) {
		setStateValue(vReferencia,vReferenciaValue);
	}
	
	private void cambiarAnguloPhi (double phi) {
		setStateValue(alabeo,phi);
	}
	
	private void cambiarAltura (double altura) {
		setStateValue(hck,altura);
	}
	
	private void ponerFuel (double fuelValue) {
		setStateValue(fuel,fuelValue);
	}
	
	private void iniciarSimulacion (double dtValue) {
		setStateValue(dt,dtValue);
		setStateValue(phase,AvionState.RUNNING);
		setStateValue(iniciarControlador,1);
		this.holdIn("active", dtValue);
	}
	
	private void finSimulacion () {
		setStateValue(phase,AvionState.PARADA);
		this.passivate();
	}
	
	private void destruirAvion() {
		setStateValue(phase,AvionState.DESTRUIDO);
		this.passivate();
	}
	
	private void getPosicion() {
		setStateValue(solicitudPosicion,cierto);
		setStateValue(dtRestante,this.getSigma());
		this.holdIn("active", 0);
	}
	
	private void getVelocidad() {
		setStateValue(solicitudVelocidad,cierto);
		setStateValue(dtRestante,this.getSigma());
		this.holdIn("active", 0);
	}
	
	private void getAngulos() {
		setStateValue(solicitudAngulos,cierto);
		setStateValue(dtRestante,this.getSigma());
		this.holdIn("active", 0);
	}
	
	private void getFuel() {
		setStateValue(solicitudFuel,cierto);
		setStateValue(dtRestante,this.getSigma());
		this.holdIn("active", 0);
	}
	
	private void getEstado() {
		setStateValue(solicitudEstado,cierto);
		setStateValue(dtRestante,this.getSigma());
		this.holdIn("active", 0);
	}
	
	private void getActualizados() {
		setStateValue(solicitudActualizados,cierto);
		setStateValue(dtRestante,this.getSigma());
		this.holdIn("active", 0);
	}
	
	public static double modulo(Vector<Double> datos) {
		double suma = 0;
		for (int i = 0; i<datos.size(); i++) suma+=datos.get(i).doubleValue()*datos.get(i).doubleValue();
		return (Math.sqrt(suma));
	}

	public void imprimeEstado(){
		System.out.println(
			"AVIONSTATE "+
			"pe "+
			this.getStateValue("pe").doubleValue()+
			"pn "+
			this.getStateValue("pn").doubleValue()+
			"ph "+
			this.getStateValue("ph").doubleValue()+
			"alabeo "+
			this.getStateValue("alabeo").doubleValue()+
			"cabeceo "+
			this.getStateValue("cabeceo").doubleValue()+
			"rumbo "+
			this.getStateValue("rumbo").doubleValue()+
			"v "+
			this.getStateValue("v").doubleValue()
			);
	}
	public void deltint() {
		//imprimeEstado();
		if ((getStateValue(solicitudPosicion).intValue()==cierto)||
			(getStateValue(solicitudVelocidad).intValue()==cierto)||
			(getStateValue(solicitudAngulos).intValue()==cierto)||
			(getStateValue(solicitudFuel).intValue()==cierto)||
			(getStateValue(solicitudEstado).intValue()==cierto)||
			(getStateValue(solicitudActualizados).intValue()==cierto)) {
			//Se ha producido una transici�n interna por el paso del estado transitorio de respuesta
			//Actualizamos todas las solicitudes a falso y restauramos el dt restante
			setStateValue(solicitudPosicion,falso);
			setStateValue(solicitudVelocidad,falso);
			setStateValue(solicitudAngulos,falso);
			setStateValue(solicitudFuel,falso);
			setStateValue(solicitudEstado,falso);
			setStateValue(solicitudActualizados,falso);
			if (getStateValue(phase).intValue() == AvionState.PARADA) this.passivate();
			else this.holdIn("active",getStateValue(dtRestante).doubleValue());
		}
		else {
			
			//Se ha producido una transici�n interna por la finalizaci�n del tiempo dt (sigma)
			//Actualizamos los par�metros del avi�n y restauramos el mismo estado con sigma = dt
			if (getStateValue(phase).intValue()==AvionState.RUNNING) {
				
				//Vector<Double> avance = avanzarAvion();
				//setStateValue(pn,avance.get(0).doubleValue());
				//setStateValue(pe,avance.get(1).doubleValue());
				//setStateValue(ph,avance.get(2).doubleValue());
				//setStateValue(alabeo,avance.get(3).doubleValue());
				//setStateValue(rumbo,avance.get(4).doubleValue());
				//setStateValue(vn,avance.get(5).doubleValue());
				//setStateValue(ve,avance.get(6).doubleValue());
				//setStateValue(v,avance.get(5).doubleValue());
				
				this.holdIn("active",getStateValue(dt).doubleValue());
			}
			else {
				super.passivate();
			}
		}
	}


	@SuppressWarnings("unchecked")
	public void lambda() {
		avanzaTiempo();
		//imprimeEstado();

		

		if(this.getStateValue(iniciarControlador).doubleValue()==1){
			Vector iniciar = new Vector();
			iniciar.add(ControladorRumboState.Iniciar);
			this.setStateValue(iniciarControlador, 0);
			controlador.setValue(iniciar);
		}
		
		//else{
		Vector<Number> posicion = new Vector<Number>(3,0);
		
		posicion.add(new Double(getStateValue("pe").doubleValue()));
		posicion.add(new Double(getStateValue("pn").doubleValue()));
		posicion.add(new Double(getStateValue("ph").doubleValue()));
		Vector<Number> velocidad = new Vector<Number>(3,0);	
		velocidad.add(new Double( dameComponentesV(1)));
		velocidad.add(new Double( dameComponentesV(2)));
		velocidad.add(new Double( dameComponentesV(3)));
		Vector<Number> angulos = new Vector<Number>(3,0);
		angulos.add(getStateValue("alabeo"));
		angulos.add(getStateValue("cabeceo"));
		angulos.add(getStateValue("rumbo"));		
		Vector todo = new Vector();
		todo.add(posicion);
		todo.add(velocidad);
		todo.add(angulos);
		todo.add(getStateValue(fuel).doubleValue());
		todo.add(getStateValue(phase).doubleValue());
		todo.add(getStateValue(dt).doubleValue());
		todo.add((Integer)Integer.parseInt(this.getName()));
		OutTodo.setValue(todo);
		//}

		
	}
	
    @Override
	public void deltcon(double e) {
		//En el caso de que se produzca una transici�n externa de manera simultanea a una transici�n interna,
		//realizaremos en primer lugar la funci�n de transici�n externa, puesto que tras la funci�n de transici�n
		//interna tendremos calculados los valores de los datos del avi�n para el instante k+1
		
		deltint();
		deltext(e);	
	}


	public void actualizaEstados(double[] estadosActuales) {
		// TODO Auto-generated method stub
		double h= getStateValue(ph).doubleValue();
		double nMax = 5.3820e-9*h*h-4.4291e-4*h+6.1000e+0;				//Factor n m�ximo para esa altura
		double n = 1.0/(Math.cos(getStateValue(alabeo).doubleValue()));	//Factor n actual
		double v = getStateValue("v").doubleValue();
		double w = (g*Math.tan(getStateValue(alabeo).doubleValue())/v);		//Velocidad angular
		
		double vmin = 7.8038e-7*h*h-8.2021e-4*h+5.9000e+1;				//Velocidad m�nima para esa altura
		double vmax = -1.0764e-7*h*h-8.2021e-4*h+3.3500e+2;				//Velocidad m�xima para esa altura
		
		double amax = -4.0365e-9*h*h-2.0505e-4*h+3.8000e+0;				//Aceleraci�n m�xima para esa altura
		
		double vmaxs = 1.7403e-11*h*h*h-4.8328e-7*h*h+3.1700e-3*h+9.3257e+1;				//Velocidad maxima de subida
		double vmaxb = 2.8064e-12*h*h*h-2.5433e-7*h*h+4.7390e-3*h-4.6005e+1;//Velocidad maxima de bajada

		double phi = getStateValue(alabeo).doubleValue();

		if (n>nMax) {													
			n = nMax;									//En el caso de que el factor de carga sobrepase el
		}
			w = (g*Math.signum(w)*Math.sqrt(n*n-1.0))/v;				//m�ximo, recalculamos la velocidad angular y el
			phi = Math.atan((v*w)/g);			//�ngulo de alabeo
			if (phi>Math.PI) phi -= 2*Math.PI;
			else if (phi<-Math.PI) phi += 2*Math.PI;	
		
		this.setStateValue("alabeo", phi);
		
		//primera derivada x
		this.setStateValue("pe", estadosActuales[0]);
		//segunda derivada y
		this.setStateValue("pn", estadosActuales[1]);
		//tercera derivada h
		this.setStateValue("ph", estadosActuales[2]);
		
		
		//cuarta derivada vh
		double estado= estadosActuales[3];
		if(estado>vmaxs){estado=vmaxs;}
		else if(estado<vmaxb){estado=vmaxb;}
		this.setStateValue("vh", estado);
		//quinta derivada psi
		this.setStateValue("rumbo",normalizar(estadosActuales[4]));
		//sexta derivada v
		estado=estadosActuales[5];
		if(estado>vmax){estado=vmax;}
		else if(estado<vmin){estado=vmin;}
		this.setStateValue("v", estado);
	}
	
	/** Funcion que dado un angulo, lo convierte a 
	 * un angulo entre [-Pi,Pi]*/
	public double normalizar(double angulo){
		double angulo_normalizado = angulo;
		if(angulo > Math.PI){angulo_normalizado=normalizar(angulo-2*Math.PI);}
		if(angulo < -Math.PI){angulo_normalizado=normalizar(angulo + 2*Math.PI);}
		return angulo_normalizado;
	}


	
	public void avanzaTiempo() {
		// TODO Auto-generated method stub
		//primero fijamos los valores maximos y minimos
		//segundo avanzamos... integramos
		double tiempo = this.getStateValue("dt").doubleValue();
		this.actualizaEstados(integrador.integra(this, tiempo,this.getStateValue("tactual").doubleValue()));
		//tercero actualizamos el tiempo
		this.setStateValue("tactual", this.getStateValue("tactual").doubleValue()+tiempo);

	}


	public double[] dameControlActual() {
		// TODO Auto-generated method stub
		return null;
	}


	public double[] dameDerivadas(double tiempo, double[] estados,
			double[] control) {
		// TODO Auto-generated method stub
		double h= getStateValue("ph").doubleValue();
		double nMax = 5.3820e-9*h*h-4.4291e-4*h+6.1000e+0;				//Factor n m�ximo para esa altura
		double n = 1.0/(Math.cos(getStateValue("alabeo").doubleValue()));	//Factor n actual
		double v = getStateValue("v").doubleValue();
		double w = (g*Math.tan(getStateValue("alabeo").doubleValue())/v);		//Velocidad angular
		double vmin = 7.8038e-7*h*h-8.2021e-4*h+5.9000e+1;				//Velocidad m�nima para esa altura
		double vmax = -1.0764e-7*h*h-8.2021e-4*h+3.3500e+2;				//Velocidad m�xima para esa altura
		double amax = -4.0365e-9*h*h-2.0505e-4*h+3.8000e+0;				//Aceleraci�n m�xima para esa altura
		double vmaxs = 1.7403e-11*h*h*h-4.8328e-7*h*h+3.1700e-3*h+9.3257e+1;				//Velocidad maxima de subida
		double vmaxb = 2.8064e-12*h*h*h-2.5433e-7*h*h+4.7390e-3*h-4.6005e+1;//Velocidad maxima de bajada
		

		
		double chi = getStateValue(rumbo).doubleValue()+getStateValue(dt).doubleValue()*w;
		if (chi>Math.PI) chi -= 2*Math.PI;
		else if (chi<-Math.PI) chi += 2*Math.PI;
		
		double a1=0.76666;
		double a2=0.58768;
		double vh=this.getStateValue("vh").doubleValue();
		double hck=this.getStateValue("hck").doubleValue();
		double asmax = -4.7077e-3*h+6.6078e+1;				//Aceleracion maxima de subida
		double abmax = -7.1275e-8*h*h+1.7604e-3*h-2.0142e+1;				//Aceleracion maxima de bajada		
		int num_estados=6;
		
		double[] derivadas= new double[num_estados];
		//primera derivada x
		derivadas[0]=v*Math.cos(this.getStateValue("rumbo").doubleValue());
		//segunda derivada y
		derivadas[1]=v*Math.sin(this.getStateValue("rumbo").doubleValue());
		//tercera derivada h
		derivadas[2]=vh;
		//cuarta derivada vh
		
		derivadas[3]= -a1*vh -a2*(h-hck);
		if(derivadas[3]>asmax){derivadas[3]=asmax;}
		else if(derivadas[3]<abmax){derivadas[3]=abmax;}
		
		//quinta derivada psi
		derivadas[4]=w;
		
		//sexta derivada v
		double vr=this.getStateValue("vReferencia").doubleValue();
		double kp=this.getStateValue("kp").doubleValue();
		derivadas[5]= kp*(vr-v);
		
		if((absoluto(derivadas[5]))>amax){
			derivadas[5]= signo(derivadas[5])*amax;
		}
		return derivadas;
	}
	
	public double absoluto(double valor){
		double retorno=0;
		if(valor>0){retorno=valor;}
		if(valor<0){retorno=-valor;}
		return retorno;
	}
	public double signo(double valor){
		double retorno=0;
		if(valor>0){retorno=1;}
		if(valor<0){retorno=-1;}
		return retorno;
	}
	

	public double dameComponentesV(int tipoComponente){
		double componente=0;
		double v=this.getStateValue("v").doubleValue();
		double psi=this.getStateValue("rumbo").doubleValue();
		//Devolver vx
		if(tipoComponente==1){
			componente = v*Math.cos(psi);
		}
		//Devolver vy
		if(tipoComponente==2){
			componente = v*Math.sin(psi);
		}
		//Devolver vh
		if(tipoComponente==3){
			componente = this.getStateValue("vh").doubleValue();
		}
		return componente;
	}
	
	public double dameTheta(){
		return(Math.asin(this.getStateValue("vh").doubleValue()/this.getStateValue("v").doubleValue()));
	}
	
	public double[] dameEstadoActual() {
		int num_estados=6;
		double[] estados= new double[num_estados];
		//primera estado x
		estados[0]=this.getStateValue("pe").doubleValue();
		//segunda estado y
		estados[1]=this.getStateValue("pn").doubleValue();
		//tercera estado h
		estados[2]=this.getStateValue("ph").doubleValue();
		//cuarta estado vh
		estados[3]=this.getStateValue("vh").doubleValue();
		//quinta estado psi
		estados[4]=this.getStateValue("rumbo").doubleValue();
		//sexto estado velocidad
		estados[5]=this.getStateValue("v").doubleValue();
	
		return estados;
	}


	@Override
	public void dibujar2D(GL gl) {
		_modeloAvion.dibujar2D((getStateValue("pn")).floatValue(), (getStateValue("pe")).floatValue(), gl);
	}


	@Override
	public void dibujar3D(GL gl) {
		_modeloAvion = new ModeloAvion(gl,_terreno);
		TAfin angulos = new TAfin();
//		angulos.giroAbsolutoX((getStateValue("cabeceo")).floatValue()*360/(float)(2*Math.PI), gl);
//		angulos.giroAbsolutoY((getStateValue("rumbo")).floatValue()*360/(float)(2*Math.PI), gl);
//		angulos.giroAbsolutoZ((getStateValue("alabeo")).floatValue()*360/(float)(2*Math.PI), gl);
		gl.glPushMatrix();
        gl.glMultMatrixf(angulos.getMatriz(),0);
        
        _modeloAvion.getTafin().escalar(100, 100, 100, gl);
		_modeloAvion.posicionar((getStateValue("pn")).floatValue()/100000*Dibujante.escalado,(getStateValue("ph")).floatValue()/100,(getStateValue("pe")).floatValue()/100000*Dibujante.escalado,gl);
		_modeloAvion.dibujar3D(gl);
        
        gl.glPopMatrix();
		
		
		/*_modeloAvion.roll((getStateValue("alabeo")).floatValue()*360/(float)(2*Math.PI), gl);
		_modeloAvion.pitch((getStateValue("cabeceo")).floatValue()*360/(float)(2*Math.PI), gl);
		_modeloAvion.yaw((getStateValue("rumbo")).floatValue()*360/(float)(2*Math.PI), gl);
		System.out.println("** pn: "+getStateValue("pn"));
		System.out.println("** ph: "+getStateValue("ph"));
		System.out.println("** p2: "+getStateValue("pe"));
		_modeloAvion.posicionar((getStateValue("pn")).floatValue()/100000*Dibujante.escalado,(getStateValue("ph")).floatValue()/10,(getStateValue("pe")).floatValue()/100000*Dibujante.escalado,gl);
		_modeloAvion.dibujar3D(gl);*/
	}
	
	public void inicializar(GL gl) {
		_modeloAvion = new ModeloAvion(gl,_terreno);
		_modeloAvion.getTafin().escalar(100, 100, 100, gl);
	}


	@Override
	public PV3D obtenerPosicion() {
		PV3D posicion = new PV3D((getStateValue("pn")).floatValue(),(getStateValue("ph")).floatValue(),(getStateValue("pe")).floatValue());
		return posicion;
	}


	@Override
	public float obtenerRumbo() {
		return (getStateValue("rumbo")).floatValue()*360/(float)(2*Math.PI);
	}
	
	public int devolverTipoSeguible() {
		return 1;
	}
}
