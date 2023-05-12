package xdevs.lib.projects.graph.models;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import xdevs.core.modeling.Coupled;
import xdevs.lib.projects.graph.Dibujable;
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
		// TODO Auto-generated constructor stub
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
		this.addCoupling(_reloj, RelojState.OUT, _controlador, Controlador.InReloj);
		this.addCoupling(_controlador, Controlador.salidaGE, _relojge, RelojGE.IN);
		for(int i=0;i<numaviones;i++){
			UAV uav=new UAV(i,_terreno);
			_aviones.add(uav);
			this.addComponent(uav);
			this.addCoupling(uav, UAV.OUT, _muxaviones, _muxaviones.In[i]);
			this.addCoupling(_muxaviones, _muxaviones.Out, _controlador, Controlador.InAviones);
			this.addCoupling(uav, UAV.OUTcontrolador, _muxcaviones, _muxcaviones.In[i]);
			this.addCoupling(_muxcaviones, _muxcaviones.Out, _controlador, Controlador.InControladorAviones);
			
			this.addCoupling(_controlador, Controlador.OutAviones,
					uav, UAV.IN);
			this.addCoupling(_controlador, Controlador.OutControladorAviones,
					uav, UAV.INcontrolador);
		}
		for(int i=0;i<numbarcos;i++){
			
			Barco barco = new Barco(numaviones+i,_terreno);
			_barcos.add(barco);
			this.addComponent(barco);
			this.addCoupling(barco, Barco.OUT, _muxbarcos, _muxbarcos.In[i]);
			this.addCoupling(_muxbarcos,_muxbarcos.Out, _controlador, Controlador.InBarcos);
			this.addCoupling(_controlador, Controlador.OutBarcos,
					barco, Barco.IN);
			
			this.addCoupling(barco, Barco.Outcontrolador, _muxcbarcos, _muxcbarcos.In[i]);
			this.addCoupling(_muxcbarcos, _muxcbarcos.Out, _controlador, Controlador.InControladorBarcos);
			this.addCoupling(_controlador, Controlador.OutControladorBarcos,
					barco, Barco.INcontroladorBarco);
		}
		
		//System.out.println("creado");
		//VAMOS A CREAR UNA SIMULACION CON 50 NAUFRAGOS.
		for(int i=0;i<num_naufragos;i++){
			Naufrago naufrago=new Naufrago(((Integer)i).toString(),tiempo,terreno);
			_naufragos.add(naufrago);
			this.addComponent(naufrago);
			//this.addCoupling(naufrago, Naufrago.Out,_algoritmo, AlgoritmoYDetecciones.InNaufragos);
			this.addCoupling(_controlador, Controlador.OutNaufragos,naufrago, Naufrago.InSolicitud);
			
			this.addCoupling(naufrago, Naufrago.Out, _muxnaufragos, _muxnaufragos.In[i]);
			this.addCoupling(_muxnaufragos, _muxnaufragos.Out, _controlador, Controlador.InNaufragos);
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
	public void dibujar3D(GL gl) {
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
	public void inicializar(GL gl) {
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
