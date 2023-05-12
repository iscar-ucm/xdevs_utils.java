package xdevs.lib.projects.barcos;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.jogamp.opengl.GL2;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.lib.projects.graph.Dibujable;
import xdevs.lib.projects.graph.Dibujante;
import xdevs.lib.projects.graph.ModeloBarco;
import xdevs.lib.projects.graph.Seguible;
import xdevs.lib.projects.graph.structs.PV3D;
import xdevs.lib.projects.graph.structs.terrain.Terreno;
import xdevs.lib.projects.math.IFuncion;
import xdevs.lib.projects.math.IIntegrador;
import xdevs.lib.projects.math.RungeKutta;
import xdevs.lib.projects.uavs.AvionState;
import xdevs.lib.util.mlved;

/**    Clase que implementa el modelo de un barco   */
public class BarcoState extends AtomicState implements IFuncion, Dibujable, Seguible{
	
		/** Constante para el puerto de entrada de las solicitudes 1 */
		public Port<Object> inSolicitud1 = new Port<>("INsolicitud1");
		
		/** Constante para el puerto de entrada de las solicitudes 2 */
		public Port<Object> inSolicitud2 = new Port<>("INsolicitud2");

		/** Constante para el puerto de salida del vector posicion	 */
		public Port<Object> outPosicion = new Port<>("OUTposicion");
		
		/** Constante para el puerto de salida del vector velocidad	 */
		public Port<Object> outVelocidad = new Port<>("OUTVelocidad");
		
		/** Constante para el puerto de salida del vector de angulos */
		public Port<Object> outAngulos = new Port<>("OUTAngulos");
		
		/** Constante para el puerto de salida del nivel de fuel */
		public Port<Object> outFuel = new Port<>("OUTFuel");
		
		/** Constante para el puerto de salida del estado	 */
		public Port<Object> outEstado = new Port<>("OUTEstado");
		
		/** Constante para el puerto de salida para el controlador	 */
		public Port<Object> outTodo = new Port<>("OUTTodo");
		
		/** Constante para el puerto de salida para los valores precisos en ese instante */
		public Port<Vector> outActualizados = new Port<>("OUTActualizados");
		
		
		/** Constante para el estado de parada	 */
		public static final int PARADA = 0;
		
		/** Constante para el estado de running	 */
		public static final int RUNNING = 1;
		
		/** Constante para el estado de destruido */
		public static final int DESTRUIDO = -1;
		//FIN DE CONSTANTES PARA ESTADO

		/** Constante para la creacion de un mariner*/
		public static final int Mariner = 1;
		
		/** Constante para la creacion de un petroleo*/
		public static final int Petrolero = 2;
		
		//OPCIONES DEL BARCO(INICIALMENTE):
		//RESET, CAMBIAR ANGULO
		//CONSTANTES PARA SOLICITUDES
		/** Constante para la solicitud de reset */
		public static final int Reset = 1;
		
		/** Constante para la solicitud de cambio de �ngulo de timon */
		public static final int CambiarAnguloTimon = 2;		
		
		/** Constante para la solicitud de cambio de velocidad */
		public static final int CambiarVelocidad = 3;
		
		/** Constante para la solicitud de repostaje	 */
		public static final int PonerFuel = 4;
		
		/** Constante para la solicitud de inicio de simulación	 */
		public static final int IniciarSimulacion = 5;
		
		/** Constante para la solicitud de fin de simulación */
		public static final int FinSimulacion = 6;
		
		/** Constante para la solicitud de destruccion del barco */
		public static final int DestruirBarco = 7;
		
		/** Constante para la solicitud de posicion	 */
		public static final int GetPosicion = 8;
		
		/** Constante para la solicitud de velocidad */
		public static final int GetVelocidad = 9;
		
		/** Constante para la solicitud de angulos */
		public static final int GetAngulos = 10;
		
		/** Constante para la solicitud de combustible */
		public static final int GetFuel = 11;
		
		/** Constante para la solicitud de estado */
		public static final int GetEstado = 12;
		
		/** Constante para la solicitud de los datos actualizados */
		public static final int GetActualizados = 13;
		
		
		//Valor de la gravedad g
		/** Constante para el valor de la gravedad	 */
		public static final double g = 9.8;
		
		
		/**
		 * Constante para el nombre del estado de la solicitud de datos actualizados
		 */
		private static final String solicitudActualizados = "solicitudActualizados";
		
		//Constantes para los nombre de las variables de estado
		/** Constante para el nombre del estado phase	*/
		private static final String phase = "Phase";
		
		/** El diferencial de tiempo de integracion*/
		public static final String dt = "dt";
		
		/** Variable que contiene el tiempo actual*/
		public static final String tactual = "tactual";
		
		/** Variable x1 = angulo de gui�ada*/
		public static final String x1 = "x1"; //x1 equivale al angulo de gui�ada
		
		/** Variable x2 = velocidad de gui�ada*/
		public static final String x2 = "x2"; //x2 equivale a derivada de angulo de gui�ada
		
		/** Variable x3 = aceleracion de gui�ada*/
		public static final String x3 = "x3"; //x3 equivale a derivada 2� angulo de gui�ada
		
		/** Variable x4 = angulo de tim�n*/
		public static final String x4 = "x4";
		
		/** Variable x = posicion x*/
		public static final String x= "x";
		
		/** Variable y = posicion y*/
		public static final String y= "y";
		
		/** Velocidad x, necesaria para controlar*/
		public static final String vx= "vx";
		/** Velocidad y, necesaria para controlar*/
		public static final String vy= "vy";
		
		/** Constante T1*/
		public static final String T1= "T1";
		
		/** Constante T2*/
		public static final String T2= "T2";
		
		/** Constante T3*/
		public static final String T3= "T3";
		
		/** Constante K*/
		public static final String K = "K";
		
		/** Constante longitud del barco*/
		public static final String longitud = "longitud";
		
		/** Constante U*/
		public static final String U = "U";
		
		/** Constante u*/
		public static final String u = "u";
		
		/** Constante v*/
		public static final String v = "v";
		
		/** Variable que indica el objetivo del timon*/
		public static final String timon_obj= "timon_obj";
		
		
		
		/**
		 * Constante entera para el valor booleano True
		 */
		private static final int cierto = 1;
		
		/**
		 * Constante entera para el valor booleano False
		 */
		private static final int falso = 0;
		
		/** Integrador */
		private IIntegrador integrador;
		
		/** Ecuacion en diferencias */
		private mlved ecuacionDif;
		
		private Terreno _terreno;
		
		private ModeloBarco _modeloBarco;
		/**
		 * Constructor del avi�n.
		 * Inicializamos tanto sus puertos de entrada como sus puertos de salida.
		 * Inicializamos los vectores de posici�n, �ngulos y velocidad a 0, y las variables de solicitud a falso
		 * Inicializamos la constante kp al valor pasado como par�metro
		 * @param name
		 * @param kp
		 */
		public BarcoState (String name, double kp, Terreno terreno) {
			super(name);
			integrador=new RungeKutta();
			addInPort(inSolicitud1);			//Establecemos los puertos de entrada
			addInPort(inSolicitud2);			//Establecemos los puertos de entrada
			addOutPort(outPosicion); 			//Establecemos los puertos de salida
			addOutPort(outVelocidad);
			addOutPort(outAngulos);
			addOutPort(outFuel);
			addOutPort(outEstado);
			addOutPort(outTodo);
			addOutPort(outActualizados);
			addState(phase);						//A�adimos los estados
			addState(dt);
			addState(tactual);
			addState("x1");
			addState("x2");
			addState("x3");
			addState("x4");
			addState("T1");
			addState("T2");
			addState("T3");
			addState("x");
			addState("y");
			addState("vx");
			addState("vy");
			addState("K");
			addState("timon_obj");
			addState("longitud");
			addState("U");
			setStateValue("dt",1);
			setStateValue("solicitudActualizados",falso);
			setStateValue("x1",0.0);
			setStateValue("x2",0.0);
			setStateValue("x3",0.0);
			setStateValue("x4",0.0);
			setStateValue("x",0.0);
			setStateValue("y",0.0);
			setStateValue("vx",0.0);
			setStateValue("vy",0.0);
			setStateValue("v",0);
			setStateValue("u",7.7);
			setStateValue("timon_obj",0);
			setStateValue("tactual",0);
			ponerValores(BarcoState.Mariner);
			double T1=this.getStateValue("T1").doubleValue();
			double T2=this.getStateValue("T2").doubleValue();
			double T3=this.getStateValue("T3").doubleValue();
			double K=this.getStateValue("K").doubleValue();
			double a1 = (T1+T2)/(T1*T2);
			double a2 = (1)/(T1*T2);
			double b1 = (K*T3)/(T1*T2);
			double b2 = (K)/(T1*T2);
			double[][] A={{1.8712,-0.8722},{1,0}};
			double[][] B={{0.0625},{0}};
			double[][] C={{0.0571,-0.0541}};
			double[][] D={{0}};
			ecuacionDif=new mlved(A,B,C,D);
			this._terreno = terreno;
			setStateValue(phase,BarcoState.PARADA);		//Configuramos el avi�n como detenido
			super.passivate();			//Permanecer� para siempre en este estado hasta que no halla una transici�n externa		
		}
		
		@Override
		public void exit() {
		}

		@Override
		public void initialize() {
			super.passivate();
		}

		/**metodo que pone los valores a las constantes
		 * en funcion del tipo de barco */
		public void ponerValores(int tipoBarco){
			if (tipoBarco==BarcoState.Mariner){
				this.setStateValue("longitud", 161);
				this.setStateValue("U",7.7);
				this.setStateValue("K", 0.185);
				this.setStateValue("T1", 118.0);
				this.setStateValue("T2", 7.8);
				this.setStateValue("T3", 18.5);
			}
			if (tipoBarco==BarcoState.Petrolero){
				this.setStateValue("longitud", 350);
				this.setStateValue("U",8.1);
				this.setStateValue("u",8.1);
				this.setStateValue("K", -0.019);
				this.setStateValue("T1", -124.1);
				this.setStateValue("T2", 16.4);
				this.setStateValue("T3", 46.0);
			}
		}
		
		/** Funcion que dado un angulo, lo convierte a 
		 * un angulo entre [-Pi,Pi]*/
		public double normalizar(double angulo){
			double angulo_normalizado = angulo;
			if(angulo > Math.PI){angulo_normalizado=normalizar(angulo-2*Math.PI);}
			if(angulo < -Math.PI){angulo_normalizado=normalizar(angulo + 2*Math.PI);}
			return angulo_normalizado;
		}
		
		@SuppressWarnings("unchecked")
		/**
		 * Funci�n de transici�n externa para el modelo at�mico del avi�n
		 * @param arg0
		 * @param arg1
		 */
		public void deltext(double e) {
			super.resume(e);
			if (getStateValue(phase).intValue() != BarcoState.DESTRUIDO) {
				Iterator iteradorSolicitud1 = this.inSolicitud1.getValues().iterator();
				Iterator iteradorSolicitud2 = this.inSolicitud2.getValues().iterator();
				while (iteradorSolicitud1.hasNext()) {
					//En primer lugar obtenemos el tipo de solicitud
					Vector solicitud = ((Vector)iteradorSolicitud1.next());
					//En funci�n del tipo de solicitud, obtenemos los datos y realizamos la operaci�n requerida
					switch ((Integer)solicitud.get(0)) {
					case BarcoState.Reset: {
						// 
						if((Integer)solicitud.get(5)==(Integer.parseInt(this.getName()))){
						 Vector pos=new Vector();
						 Vector vel=new Vector();
						 pos.add(solicitud.get(1));
						 pos.add(solicitud.get(2));
						 vel.add(solicitud.get(3));
						 vel.add(solicitud.get(4));
						 reset(pos,vel);
						}
						 //VELOCIDADES POR LA MAREA
					}; break;
					case BarcoState.CambiarVelocidad: {
						 cambiarVelocidad((Double)solicitud.get(1));
					}; break;
					case BarcoState.CambiarAnguloTimon: {
						cambiarAnguloTimon((Double)solicitud.get(1));
					}; break;
					case BarcoState.PonerFuel: {
						ponerFuel((Double)solicitud.get(1));
					}; break;
					case BarcoState.IniciarSimulacion: {
						iniciarSimulacion((Double)solicitud.get(1));
					}; break;
					case BarcoState.FinSimulacion: {
						finSimulacion();
					}; break;
					case BarcoState.DestruirBarco: {
						destruirBarco();
					}; break;
					case BarcoState.GetPosicion: getPosicion(); break;
					case BarcoState.GetVelocidad: getVelocidad(); break;
					case BarcoState.GetAngulos: getAngulos(); break;
					case BarcoState.GetFuel: getFuel(); break;
					case BarcoState.GetEstado: getEstado(); break;
					case BarcoState.GetActualizados: getActualizados(); break;
					}
				}
				
				while (iteradorSolicitud2.hasNext()) {
					//En primer lugar obtenemos el tipo de solicitud
					Vector solicitud = ((Vector)iteradorSolicitud2.next());
					//En funci�n del tipo de solicitud, obtenemos los datos y realizamos la operaci�n requerida
					switch ((Integer)solicitud.get(0)) {
					case BarcoState.Reset: {
						 Vector pos=new Vector();
						 pos.add(solicitud.get(1));
						 pos.add(solicitud.get(2));
						 //pos.add(solicitud.get(3));
						 Vector vel=new Vector();
						 vel.add(solicitud.get(3));
						 vel.add(solicitud.get(4));
						 vel.add(solicitud.get(5));
						 vel.add(solicitud.get(6));
						 reset(pos,vel);
					}; break;
					case BarcoState.CambiarVelocidad: {
						 cambiarVelocidad((Double)solicitud.get(1));
					}; break;
					case BarcoState.CambiarAnguloTimon: {
						cambiarAnguloTimon((Double)solicitud.get(1));
					}; break;
					case BarcoState.PonerFuel: {
						ponerFuel((Double)solicitud.get(1));
					}; break;
					case BarcoState.IniciarSimulacion: {
						iniciarSimulacion((Double)solicitud.get(1));
					}; break;
					case BarcoState.FinSimulacion: {
						finSimulacion();
					}; break;
					case BarcoState.DestruirBarco: {
						destruirBarco();
					}; break;
					case BarcoState.GetPosicion: getPosicion(); break;
					case BarcoState.GetVelocidad: getVelocidad(); break;
					case BarcoState.GetAngulos: getAngulos(); break;
					case BarcoState.GetFuel: getFuel(); break;
					case BarcoState.GetEstado: getEstado(); break;
					}
				}
			}
		}


		/**
		 * Inicializa la posici�n y la velocidad del avi�n a los valores dados
		 * @param posicion
		 * @param velocidad
		 */
		private void reset (Vector<Double> posicion, Vector<Double> velocidad) {
			this.setStateValue("x", posicion.get(0));
			this.setStateValue("y", posicion.get(1));
			setStateValue(dt,1);
			setStateValue(phase,BarcoState.RUNNING);
			this.setSigma(this.getStateValue(dt).doubleValue());
		}
		
		private void cambiarVelocidad (double vReferenciaValue) {
			
		}
		
		private void cambiarAnguloTimon (double timon) {
			this.setStateValue("timon_obj",limitar(-20,20,timon));
	
		}
		
		private void ponerFuel (double fuelValue) {
			
		}
		
		private void iniciarSimulacion (double dtValue) {
			setStateValue(dt,dtValue);
			setStateValue(phase,BarcoState.RUNNING);
			this.setSigma(dtValue);
		}
		
		private void finSimulacion () {
			setStateValue(phase,BarcoState.PARADA);
			super.passivate();
		}
		
		private void destruirBarco() {
			setStateValue(phase,BarcoState.DESTRUIDO);
			super.passivate();
		}
		
		private void getPosicion() {

		}
		
		private void getVelocidad() {

		}
		
		private void getAngulos() {
		}
		
		private void getFuel() {

		}
		
		private void getEstado() {

		}
		
		private void getActualizados() {
	
		}
		
		public static double modulo(Vector<Double> datos) {
			double suma = 0;
			for (int i = 0; i<datos.size(); i++) suma+=datos.get(i).doubleValue()*datos.get(i).doubleValue();
			return (Math.sqrt(suma));
		}

		public void deltint() {
			
			this.avanzaTiempo();
			//imprimeEstado();
			this.setSigma(getStateValue("dt").doubleValue());
		}
		
		public void imprimeEstado(){

			System.out.println(
			"BARCOSTATE "+ this.getName()+
			"x1 "+
			this.getStateValue("x1").doubleValue()+
			"x2 "+
			this.getStateValue("x2").doubleValue()+
			"x3 "+
			this.getStateValue("x3").doubleValue()+
			"x4 "+
			this.getStateValue("x4").doubleValue()+
			"timon_obj "+
			this.getStateValue("timon_obj").doubleValue()+
			"x "+
			this.getStateValue("x").doubleValue()+
			"y "+
			this.getStateValue("y").doubleValue()+
			"vx "+
			this.getStateValue("vx").doubleValue()+
			"vy "+
			this.getStateValue("vy").doubleValue()
			);
		}


		/** metodo que actualiza los estados del barco*/
		public void actualizaEstados(double[] estadosActuales) {
			this.setStateValue("x1",normalizar(estadosActuales[0]));
		// de momento simulacion, 1 segundo... modificar?
		//	this.setStateValue("x2", estadosActuales[1]);
			this.setStateValue("x3",estadosActuales[2]);
			this.setStateValue("x4",estadosActuales[3]);
			this.setStateValue("x" ,estadosActuales[4]);
			this.setStateValue("y" ,estadosActuales[5]);
			this.setStateValue("vx",(this.getStateValue("u").doubleValue()*Math.cos(this.getStateValue("x1").doubleValue())
					-this.getStateValue("v").doubleValue()*Math.sin(this.getStateValue("x1").doubleValue())));
			this.setStateValue("vy",this.getStateValue("u").doubleValue()*Math.sin(this.getStateValue("x1").doubleValue())
					+this.getStateValue("v").doubleValue()*Math.cos(this.getStateValue("x1").doubleValue()));
		}

		public double[] dameControlActual() {
			// TODO Auto-generated method stub
			return null;
		}

		/** metodo que convierte de angulos a radianes*/
		public double angARad(double valorGrados){
			return((valorGrados/(360))*2*Math.PI);		
		}
		
		/** metodo que convierte de radianes a angulos*/
		public double radAAng(double valorRadianes){
			return((valorRadianes/(2*Math.PI))*360);			
		}
		
		/** funcion que dado un valor y unos valores minimo y maximo en grados, 
		 * convierte valor al valor limitado, en radianes
		 * */
		public double limitar(double valorMinimo,double valorMaximo,double valor){
			double valorMinimoRadianes = angARad(valorMinimo);
			double valorMaximoRadianes = angARad(valorMaximo);
			double valorLimitado = valor;
			if(valorLimitado > valorMaximoRadianes){valorLimitado=valorMaximoRadianes;}
			if(valorLimitado < valorMinimoRadianes){valorLimitado=valorMinimoRadianes;}
			return valorLimitado;
		}

		/** funcion que da las derivadas */
		public double[] dameDerivadas(double tiempo, double[] estados,
				double[] control) {
			// TODO Auto-generated method stub
			double[] derivadas= new double[6];

			//derivadas[0]=this.ecuacionDif.update(this.getStateValue("x4").doubleValue())[0][0];
			derivadas[0]=this.getStateValue("x2").doubleValue();
			//
			derivadas[1]=this.getStateValue("x3").doubleValue();
		
			derivadas[3]=limitar(-5,5,
					(		this.getStateValue("timon_obj").doubleValue()
							-this.getStateValue("x4").doubleValue())
					);
			
			derivadas[2]=((
					(-
					(this.getStateValue("T1").doubleValue()+this.getStateValue("T2").doubleValue())/
					(this.getStateValue("T1").doubleValue()*(this.getStateValue("T2").doubleValue()))
							)*
							(this.getStateValue("x3").doubleValue()))
							-
					(
							((1)/(this.getStateValue("T1").doubleValue()*this.getStateValue("T2").doubleValue()))
							*
							(this.getStateValue("x2").doubleValue())
					)
							+
					(this.getStateValue("K").doubleValue()*(
							this.getStateValue("T3").doubleValue()*derivadas[3]+
							this.getStateValue("x4").doubleValue()))
							);
			
			derivadas[4]=(this.getStateValue("u").doubleValue()*Math.cos(this.getStateValue("x1").doubleValue())
					-this.getStateValue("v").doubleValue()*Math.sin(this.getStateValue("x1").doubleValue()));
			
			derivadas[5]=(this.getStateValue("u").doubleValue()*Math.sin(this.getStateValue("x1").doubleValue())
					+this.getStateValue("v").doubleValue()*Math.cos(this.getStateValue("x1").doubleValue()));
			return derivadas;
		}
		
		/** funcion que dado un vector y su titulo, lo muestra por pantalla*/
		public void imprimeVector(String nombre,double[] vector){
			System.out.println("COMIENZO "+nombre);
			for(int cont=0;cont<vector.length;cont++){
				//System.out.println(String.valueOf(cont)+" "+vector[cont]);
			}
			System.out.println("FIN" + nombre);
		}


		/** funcion que da el estado actual del barco*/
		public double[] dameEstadoActual() {
			// TODO Auto-generated method stub
			double[] estado=new double[6];
			estado[0]=this.getStateValue("x1").doubleValue();
			estado[1]=this.getStateValue("x2").doubleValue();
			estado[2]=this.getStateValue("x3").doubleValue();
			estado[3]=this.getStateValue("x4").doubleValue();
			estado[4]=this.getStateValue("x").doubleValue();
			estado[5]=this.getStateValue("y").doubleValue();
			return estado;
		}


		@Override
		public void lambda() {
			Vector<Number> posicion = new Vector<Number>(3,0);
			posicion.add(getStateValue(x).doubleValue());
			posicion.add(getStateValue(y).doubleValue());
			Vector<Number> velocidad = new Vector<Number>(3,0);
			Vector<Number> angulos = new Vector<Number>(3,0);
			Vector<Number> estados = new Vector<Number>(6,0);

			if (getStateValue(solicitudActualizados).intValue()==cierto) {
				Vector todoActualizado = new Vector(5,0);
				Vector<Double> posicionActualizada = new Vector<Double>(3,0);
				Vector<Double> velocidadActualizada = new Vector<Double>(3,0);
				Vector<Double> angulosActualizados = new Vector<Double>(3,0);
				posicionActualizada.add(this.getStateValue("y").doubleValue());
				posicionActualizada.add(this.getStateValue("x").doubleValue());
				
				
				//TODO PROVISIONAL HAY QUE CALCULAR COSAS?
				
				todoActualizado.add(posicionActualizada);
				outActualizados.addValue(todoActualizado);
			}
			Vector todo = new Vector(9,0);
			//todo.add(posicion);
			estados.add((Double)(getStateValue("x").doubleValue()));
			estados.add((Double)(getStateValue("y").doubleValue()));
			estados.add((Double)(getStateValue("x1").doubleValue()));
			estados.add((Double)(getStateValue("x2").doubleValue()));
			estados.add((Double)(getStateValue("x3").doubleValue()));
			estados.add((Double)(getStateValue("x4").doubleValue()));
			estados.add((Double)(getStateValue("vx").doubleValue()));
			estados.add((Double)(getStateValue("vy").doubleValue()));
			estados.add((Double)(getStateValue("U").doubleValue()));
			estados.add((Integer)(Integer.parseInt(this.getName())));
			todo.add(estados);
			outTodo.addValue(todo);
		}


		public void avanzaTiempo() {
			// TODO Auto-generated method stub
			double tiempo = 1;
			this.setStateValue("x2",ecuacionDif.update(this.getStateValue("x4").doubleValue())[0][0] *tiempo);
			this.actualizaEstados(integrador.integra(this, tiempo,this.getStateValue("tactual").doubleValue()));
			this.setStateValue("tactual", this.getStateValue("tactual").doubleValue()+tiempo);
		//	imprimeEstado();
			
		}

		
		public void deltcon(double e) {
			//En el caso de que se produzca una transici�n externa de manera simultanea a una transici�n interna,
			//realizaremos en primer lugar la funci�n de transici�n externa, puesto que tras la funci�n de transici�n
			//interna tendremos calculados los valores de los datos del avi�n para el instante k+1
			deltext(e);	
			deltint();
		}

		@Override
		public void dibujar2D(GL2 gl) {
			_modeloBarco.dibujar2D((getStateValue("x")).floatValue(), (getStateValue("y")).floatValue(), gl);
		}


		@Override
		public void dibujar3D(GL2 gl) {
			_modeloBarco = new ModeloBarco(gl,_terreno);
			_modeloBarco.posicionar((getStateValue("x")).floatValue()/1000*Dibujante.escalado,-0.2f,(getStateValue("y")).floatValue()/1000*Dibujante.escalado,gl);
			_modeloBarco.dibujar3D(gl);
		}
		
		public void inicializar(GL2 gl) {
			_modeloBarco = new ModeloBarco(gl,_terreno);
		}

		@Override
		public PV3D obtenerPosicion() {
			PV3D posicion = new PV3D((getStateValue("x")).floatValue(),0,(getStateValue("y")).floatValue());
			return posicion;
		}

		@Override
		public float obtenerRumbo() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		public int devolverTipoSeguible() {
			return 0;
		}

}
