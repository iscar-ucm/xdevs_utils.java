package xdevs.lib.projects.uavs;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.projects.graph.structs.terrain.Terreno;

//import xdevs.kernel.modeling.Coupled;

//import xdevs.kernel.simulation.Coordinator;
//import ssii2007.grafico.estructura.terreno.Terreno;

public class UAV extends Coupled {
	
	private int _id;
	
	private AvionState _avion;
	
	private PeticionesState _peticion;
	
	private ReceptorState _receptor;
	
	private ControladorRumboState _controlador;
	
	//private RelojState _reloj;
	
	private RutaState _ruta;

	public Port<Object> in = new Port<>("In");
	public Port<Object> inControlador = new Port<>("Incontrolador");
	
	public Port<Object> out = new Port<>("Out");
	public Port<Object> outControlador = new Port<>("Outcontrolador");
	
	
	public UAV(int id,  Terreno terreno) {
		super("probador");
		_avion = new AvionState (((Integer)id).toString(),1,terreno);
		_peticion = new PeticionesState ("Peticiones");
		_receptor = new ReceptorState (((Integer)id).toString());
		_controlador = new ControladorRumboState(((Integer)id).toString());
		//_reloj = new RelojState("Reloj",_num_aviones);
		_ruta= new RutaState("Ruta");
		addInPort(in);
		addInPort(inControlador);
		addOutPort(out);
		addOutPort(outControlador);
		
		addComponent(_avion);
		addComponent(_peticion);
		addComponent(_receptor);
		addComponent(_controlador);
	//	addComponent(_reloj);
		addComponent(_ruta);
	
		addCoupling(this.in,_avion.inSolicitud1);
		
		addCoupling(this.inControlador,_controlador.inPosRef);
		
		//addCoupling(this,UAV.OUT,_avion,AvionState.OutTodo);
		addCoupling(_avion.outTodo,this.out);
		addCoupling(_controlador.peticionPunto,this.outControlador);
		
		//De reloj a peticiones
	//	addCoupling(_reloj,RelojState.OUT,_peticion,PeticionesState.In);
		
		//PETICIONES A RUTA
		addCoupling(_peticion.peticionRuta,_ruta.InRuta);
		
		//RUTA A CONTROLADOR DE RUMBO
		addCoupling(_ruta.OutWayPoint,_controlador.inPosRefCon);
		
		//AVION A RUTA
		addCoupling(_avion.outTodo,_ruta.InAvion);		
		
		//PeticionesState A AVION
		addCoupling(_peticion.peticionAvion,_avion.inSolicitud1);
		
		//AVION A RECEPTOR
		addCoupling(_avion.outPosicion,_receptor.inPosicion);
		addCoupling(_avion.outVelocidad,_receptor.inVelocidad);
		addCoupling(_avion.outAngulos,_receptor.inAngulos);
		addCoupling(_avion.outFuel,_receptor.inFuel);
		addCoupling(_avion.outEstado,_receptor.inEstado);
		addCoupling(_avion.outTodo,_receptor.inTodo);
		
		//PeticionesState A CONTROLADOR
		addCoupling(_peticion.peticionControladorRumbo,_controlador.inPosRef);
		
		//CONTROLADOR A AVION Y DESDE AVION A CONTROLADOR
		addCoupling(_controlador.outPeticion,_avion.inSolicitud2);
		addCoupling(_avion.controlador,_controlador.inicializar);
		
		addCoupling(_avion.outTodo,_controlador.inAvion);
		this.
		_id = id;
	}
	
	public int getId () {
		return _id;
	}
	
	public PeticionesState getPeticion() {
		return _peticion;
	}
	
	public AvionState getAvion() {
		return _avion;
	}
	
	public static void main(String[] args) 
	{
		UAV con = new UAV(1,null);
		Coordinator coordinator = new Coordinator(con);
		coordinator.simulate(10);
	}
	

	
}
