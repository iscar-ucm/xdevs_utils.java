package xdevs.lib.projects.graph.models;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.projects.barcos.Barco;
import xdevs.lib.projects.barcos.Controlador;
import xdevs.lib.projects.barcos.Mux;
import xdevs.lib.projects.barcos.Naufrago;
import xdevs.lib.projects.graph.Dibujable;
import xdevs.lib.projects.graph.structs.terrain.Terreno;
import xdevs.lib.projects.uavs.UAV;

/**
 * Clase que implementa la clase simulaci�n, que es el modelo acoplado que
 * contiene toda la simulaci�n
 */
public class CoupledSimulacion extends Coupled implements Dibujable, Runnable {

	/**
	 * Lista que contiene los aviones
	 */
	ArrayList<UAV> _aviones;
	
	/**
	 * Lista que contiene los barcos
	 */
	ArrayList<Barco> _barcos;
	
	/**
	 * Lista que contiene los naufragos
	 */
	ArrayList<Naufrago> _naufragos;
	
	/**
	 * Controlador de todos los elementos de la aplicaci�n
	 */
	Controlador _controlador;
	
	/**
	 * Terreno sobre el que se realiza la simulaci�n
	 */
	Terreno _terreno;
	
	/**
	 * Reloj de la aplicacion
	 */
	RelojState _reloj;
	
	/**
	 * Reloj encargado del dibujo en google earth
	 */
	RelojGE _relojge;
	
	/**
	 * Mux para los aviones
	 */
	Mux _muxaviones;
	/**
	 * Mux para los barcos
	 */
	Mux _muxbarcos;
	
	/**
	 * Mux para el controlador de los aviones
	 */
	Mux _muxcaviones;
	
	/**
	 * Mux para el controlador de los barcos
	 */
	Mux _muxcbarcos;
	
	/**
	 * Mux para los naufragos
	 */
	Mux _muxnaufragos;
	
	/**
	 * Constructora
	 * @param name nombre del modelo
	 * @param numaviones numero de aviones
	 * @param numbarcos numero de barcos
	 * @param num_naufragos numero de naufragos
	 * @param tiempo tiempo en el que queremos que comience la busqueda
	 * @param terreno terreno sobre el que queremos que comience la busqueda
	 * @param guardarXML booleano==true si, queremos que en cualquier caso sobreescriba el contenido 
	 * del fichero xml, ==false si queremos que solo escriba en caso de que este vac�o
	 * @param nombreSimulacion nombre que vamos a dar al fichero de simulaci�n
	 */
	public CoupledSimulacion(String name, int numaviones,int numbarcos,int num_naufragos,int tiempo,Terreno terreno,
			boolean guardarArchivo,String nombreSimulacion,boolean XmlOSer,long t_gearth,Double velocidad_avion,
			Double posicionnavion,Double posicioneavion,Double distanciaavion,
			Double posicionnbarco,Double posicionebarco,Double distanciabarco,boolean rescatanaviones) {
		super(name);
		_aviones=new ArrayList<UAV>();
		_barcos=new ArrayList<Barco>();
		_naufragos=new ArrayList<Naufrago>();

		_muxaviones=new Mux("muxaviones",numaviones);
		_muxbarcos=new Mux("muxbarcos",numbarcos);
		_muxcbarcos=new Mux("muxcbarcos",numbarcos);
		_muxcaviones=new Mux("muxcaviones",numaviones);
		_muxnaufragos=new Mux("muxnaufragos",num_naufragos);
		
		_controlador= new Controlador("ayd",tiempo,numaviones,numbarcos,num_naufragos,guardarArchivo,nombreSimulacion,XmlOSer,velocidad_avion,
				posicionnavion,posicioneavion,distanciaavion,posicionnbarco,posicionebarco,distanciabarco,rescatanaviones);

		_reloj = new RelojState("reloj",10);
		
		_relojge = new RelojGE("reloj g.earth",t_gearth,this);
		new Thread(_relojge).start();
		_terreno = terreno;
		this.addComponent(_muxaviones);
		this.addComponent(_muxbarcos);
		this.addComponent(_muxcaviones);
		this.addComponent(_muxcbarcos);
		this.addComponent(_muxnaufragos);
		this.addComponent(_controlador);
		this.addComponent(_reloj);
		this.addComponent(_relojge);
		this.addCoupling(_reloj.out, _controlador.inReloj);
		this.addCoupling(_controlador.salidaGE, _relojge.in);
		for(int i=0;i<numaviones;i++){
			UAV uav=new UAV(i,_terreno);
			_aviones.add(uav);
			this.addComponent(uav);
			this.addCoupling(uav.out, _muxaviones.in[i]);
			this.addCoupling(_muxaviones.out, _controlador.inAviones);
			this.addCoupling(uav.outControlador, _muxcaviones.in[i]);
			this.addCoupling(_muxcaviones.out, _controlador.inControladorAviones);
			
			this.addCoupling(_controlador.outAviones, uav.in);
			this.addCoupling(_controlador.outControladorAviones, uav.inControlador);
		}
		for(int i=0;i<numbarcos;i++){
			
			Barco barco = new Barco(numaviones+i,_terreno);
			_barcos.add(barco);
			this.addComponent(barco);
			this.addCoupling(barco.out, _muxbarcos.in[i]);
			this.addCoupling(_muxbarcos.out, _controlador.inBarcos);
			this.addCoupling(_controlador.outBarcos, barco.in);
			
			this.addCoupling(barco.outControlador, _muxcbarcos.in[i]);
			this.addCoupling(_muxcbarcos.out, _controlador.inControladorBarcos);
			this.addCoupling(_controlador.outControladorBarcos, barco.inControladorBarco);
		}
		
		//System.out.println("creado");
		//VAMOS A CREAR UNA SIMULACION CON 50 NAUFRAGOS.
		for(int i=0;i<num_naufragos;i++){
			Naufrago naufrago=new Naufrago(((Integer)i).toString(),tiempo,terreno);
			_naufragos.add(naufrago);
			this.addComponent(naufrago);
			//this.addCoupling(naufrago, Naufrago.Out,_algoritmo, AlgoritmoYDetecciones.InNaufragos);
			this.addCoupling(_controlador.outNaufragos, naufrago.inSolicitud);
			
			this.addCoupling(naufrago.out, _muxnaufragos.in[i]);
			this.addCoupling(_muxnaufragos.out, _controlador.inNaufragos);
		}
	}

	/**
	 * M�todo que devuelve la lista de aviones
	 * @return lista de aviones
	 */
	public ArrayList<UAV> dameAviones(){
		return _aviones;
	}
	
	/**
	 * M�todo que devuelve la lista de barcos
	 * @return lista de barcos
	 */
	public ArrayList<Barco> dameBarcos(){
		return _barcos;
	}

	@Override
	public void dibujar2D(GL2 gl) {
		for(int i=0;i<_aviones.size();i++) {
			_aviones.get(i).getAvion().dibujar2D(gl);
		}
		for(int i=0;i<_barcos.size();i++) {
			_barcos.get(i).getBarco().dibujar2D(gl);
		}
		for(int i=0;i<_naufragos.size();i++) {
			_naufragos.get(i).dibujar2D(gl);
		}
	}

	@Override
	public void dibujar3D(GL2 gl) {
		for(int i=0;i<_aviones.size();i++) {
			_aviones.get(i).getAvion().dibujar3D(gl);
		}
		for(int i=0;i<_barcos.size();i++) {
			_barcos.get(i).getBarco().dibujar3D(gl);
		}
		for(int i=0;i<_naufragos.size();i++) {
			_naufragos.get(i).dibujar3D(gl);
		}
	}

	@Override
	public void inicializar(GL2 gl) {
		for(int i=0;i<_aviones.size();i++) {
			_aviones.get(i).getAvion().inicializar(gl);
		}
		for(int i=0;i<_barcos.size();i++) {
			_barcos.get(i).getBarco().inicializar(gl);
		}
		for(int i=0;i<_naufragos.size();i++) {
			_naufragos.get(i).inicializar(gl);
		}
	}

	/**
	 * Metodo run
	 */
	@Override
	public void run() {
		Coordinator coordinator = new Coordinator(this);
		coordinator.simulate(Integer.MAX_VALUE);
	}
	
	/**
	 * Metodo que dice cuantas veces quiere que vaya mas rapida la simulaci�n
	 * que el tiempo que representa
	 * @param numVeces numero de veces que ir�a mas rapido la simulacion
	 */	
	public void ponVelocidadVista(double numVecesMasRapido){
		_reloj.ponVelocidad(numVecesMasRapido);
	}

	/**
	 * Metodo que dice cuantas veces quiere que vaya mas rapida la simulaci�n
	 * que el tiempo que representa
	 * @param numVeces numero de veces que ir�a mas rapido la simulacion
	 */
	public void ponVelocidad(int numVeces){
		_reloj.ponVelocidad(numVeces);
	}
}
