package xdevs.lib.projects.graph.models;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.lib.projects.graph.structs.ListaPosicion;


public class RelojGE extends AtomicState implements Runnable{

	private long tiempo_refresco;
	private boolean funcionando=true;
	private ListaPosicion lista_posicion;
	protected Thread myThread;
	private CoupledSimulacion simulacion;

	public Port<ListaPosicion> in = new Port<>("IN");
	
	public RelojGE(String nombre,long tiempoRefresco,CoupledSimulacion simulacion){
		super(nombre);
		this.simulacion = simulacion;
		this.tiempo_refresco=tiempoRefresco;
		super.passivate();
		GoogleEarth.crearFicheroPrincipal(((Long)tiempoRefresco).intValue(), "./resources/principal.kml", "./secundario.kml");
		myThread = new Thread(this);
		lista_posicion= new ListaPosicion();
		//myThread.start();

		//run();
	}
	
	@Override
	public void deltext(double e) {
		//llega lista posicion
		lista_posicion = in.getSingleValue();
		super.passivate();
	}

	@Override
	public void deltint() {
		//no hace nada
		super.passivate();
	}

	@Override
	public void lambda() {
		// no envia nada
		return;
	}

	@Override
	public void run() {
		while(funcionando){
			try {
				//DIBUJAR AQUI
				dibuja();
				Thread.sleep(tiempo_refresco);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void dibuja() {
		//TUTORIAL PARA SERGINHO'S
		//a ver SGS q te explico...
		//la clase ListaPosicion tiene una lista con las posiciones actuales
		//de todo...
		//tienes una referencia a lo q hay en controlador, si necesitas trastear
		//avisa y hago el clone()
		//tiene lo siguiente:
		//Ruta ruta; --> te la pela un poco
		//Punto punto; --> punto actual
		//int tipoVehiculo; --> distintos tipos, determinados x las constantes
		//q estan en DatosTipoPosicion:
		//	public static int Avion=1;
		//  public static int Barco=2;
		//  public static int Naufrago=3;
		//int nombre; --> el nombre es realmente el numero de naufrago, avion o barco
		//tb supongo q te da un poco iwal
		//y lo mas importante!!!!! RECUERDAAA!!!!!!!!!!!!!!!!!!!!! cuando haces pop, ya no hay stop!
		//besitos!! 
		GoogleEarth.crearDocumentoRefresco("./resources/secundario.kml", lista_posicion, simulacion, 39, -19);
		//GoogleEarth.crearFicheroKMZ("./resources/secundario.kmz", lista_posicion, 39, -19);
	}

	public void mata(){
		funcionando=false;
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}

}
