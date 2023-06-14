package xdevs.lib.projects.uavs;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
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
	
		addCoupling(this,UAV.in,_avion,AvionState.InSolicitud1);
		
		addCoupling(this,UAV.inControlador,_controlador,ControladorRumboState.InPosRef);
		
		//addCoupling(this,UAV.OUT,_avion,AvionState.OutTodo);
		addCoupling(_avion,AvionState.outTodo,this,UAV.out);
		addCoupling(_controlador,ControladorRumboState.PeticionPunto,this,UAV.outControlador);
		
		//De reloj a peticiones
	//	addCoupling(_reloj,RelojState.OUT,_peticion,PeticionesState.In);
		
		//PETICIONES A RUTA
		addCoupling(_peticion,PeticionesState.peticionRuta,_ruta,RutaState.InRuta);
		
		//RUTA A CONTROLADOR DE RUMBO
		addCoupling(_ruta,RutaState.OutWayPoint,_controlador,ControladorRumboState.InPosRefCon);
		
		//AVION A RUTA
		addCoupling(_avion,AvionState.outTodo,_ruta,RutaState.InAvion);		
		
		//PeticionesState A AVION
		addCoupling(_peticion,PeticionesState.peticionAvion,_avion,AvionState.InSolicitud1);
		
		//AVION A RECEPTOR
		addCoupling(_avion,AvionState.OutPosicion,_receptor,ReceptorState.inPosicion);
		addCoupling(_avion,AvionState.OutVelocidad,_receptor,ReceptorState.inVelocidad);
		addCoupling(_avion,AvionState.OutAngulos,_receptor,ReceptorState.inAngulos);
		addCoupling(_avion,AvionState.OutFuel,_receptor,ReceptorState.inFuel);
		addCoupling(_avion,AvionState.OutEstado,_receptor,ReceptorState.inEstado);
		addCoupling(_avion,AvionState.outTodo,_receptor,ReceptorState.inTodo);
		
		//PeticionesState A CONTROLADOR
		addCoupling(_peticion,PeticionesState.peticionControladorRumbo,_controlador,ControladorRumboState.InPosRef);
		
		//CONTROLADOR A AVION Y DESDE AVION A CONTROLADOR
		addCoupling(_controlador,ControladorRumboState.OutPeticion,_avion,AvionState.InSolicitud2);
		addCoupling(_avion,AvionState.controlador,_controlador,ControladorRumboState.Inicializar);
		
		addCoupling(_avion,AvionState.outTodo,_controlador,ControladorRumboState.InAvion);
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
