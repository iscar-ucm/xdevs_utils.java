package xdevs.lib.projects.barcos;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.projects.graph.structs.terrain.Terreno;

/**
 * Clase que implementa el modelo del barco completo 
 * incluyendo el barco en si asi como el controlador
 * y el modelo de referencia
 * @author David Rodilla Rodriguez
 */
public class Barco extends Coupled {
	
	/**numero unico para identificar a cada barco	 */
	private int _id;
	
	/**creador de peticiones para barco , controlador y modelo referencia	 */
	private PeticionesStateBarco _peticion;
	
	/**modelo del barco	 */
	private BarcoState _barco;
	
	/**modelo de referencia*/
	private ModeloReferencia _modelo_referencia;
	
	/**modelo del controlador*/
	private ControladorRumboBarco _controlador_rumbo;
	
	/**receptor de se�ales*/
	private ReceptorStateBarco _receptor;
	
	/**reloj*/
	//private RelojState _reloj;
	
	/**puerto de entrada*/
	public Port<Object> in = new Port<>("In");
	public Port<Object> inControladorBarco = new Port<>("INCB");
	
	/**puerto de salida*/
	public Port<Object> out = new Port<>("Out");
	public Port<Object> outControlador = new Port<>("Outcontrolador");
	
	/** Controlador del barco*/
	private ControladorBarco _controlador;
	
	
	
	/**constructor*/
	public Barco(int id,  Terreno terreno) {
		super("probador");
		_barco = new BarcoState (((Integer)id).toString(),1,terreno);
		_peticion = new PeticionesStateBarco ("Peticiones");
		_receptor = new ReceptorStateBarco (((Integer)id).toString());
		
		_modelo_referencia =new ModeloReferencia("ModeloReferencia",0.1,1,5);
		_controlador_rumbo = new ControladorRumboBarco("ControladorRumboBarco",2,0.185,107.3,50,100);
		
		_controlador = new ControladorBarco (((Integer)id).toString());
		
		//_reloj = new RelojState("Reloj",_num_barcos);
		
		addInPort(in);
		addInPort(inControladorBarco);
		addOutPort(out);
		addOutPort(outControlador);
		addComponent(_barco);
		addComponent(_peticion);
		addComponent(_receptor);
	//	addComponent(_reloj);
		addComponent(_modelo_referencia);
		addComponent(_controlador_rumbo);
		addComponent(_controlador);
		
		addCoupling(this,Barco.IN,_barco,BarcoState.InSolicitud1);
		addCoupling(_barco,BarcoState.OutTodo,this,Barco.OUT);
		
		addCoupling(this,Barco.INcontroladorBarco,_controlador,ControladorBarco.puertoInAlgoritmo);
		addCoupling(_controlador,ControladorBarco.puertoConexionExterior,this,Barco.Outcontrolador);
		//addCoupling(this,UAV.OUT,_avion,AvionState.OutTodo);
		
		//TODO HAY QUE HACER....
		//CONEXIONES DUPLICADAS AHORA MISMO, OJO:
		// ENTRADA MODELO REFERENCIA
		// SALIDAS DE PETICIONES, VAN A VARIOS, AISLAR PETICIONES... DISTINTO NUMERO
		//De controlador a Modelo de Referencia
		addCoupling(this._controlador,ControladorBarco.puertoOut,this._modelo_referencia,ModeloReferencia.puertoIn);
		//De peticiones a controlador
		addCoupling(this._peticion,PeticionesStateBarco.PeticionBarco,this._controlador,ControladorBarco.puertoIn);
		//De barco a controlador
		addCoupling(this._barco,BarcoState.OutTodo,this._controlador,ControladorBarco.puertoInBarco);
		//De peticiones a Modelo de Referencia
		addCoupling(this._peticion,PeticionesStateBarco.PeticionBarco,_modelo_referencia,ModeloReferencia.puertoIn);
		//De modelo de Referencia a Controlador
		addCoupling(this._modelo_referencia,ModeloReferencia.puertoOut,_controlador_rumbo,ControladorRumboBarco.puertoInMRef);
		//De controlador a barcoState
		addCoupling(this._controlador_rumbo,ControladorRumboBarco.puertoOut,this._barco,BarcoState.InSolicitud2);
		//De barcoState a controlador (feedback)
		addCoupling(this._barco,BarcoState.OutTodo,this._controlador_rumbo,ControladorRumboBarco.puertoInBarco);
		//De reloj a peticiones
		//addCoupling(_reloj,RelojState.OUT,_peticion,PeticionesStateBarco.In);
				
		//PeticionesState A BARCO
		addCoupling(_peticion,PeticionesStateBarco.PeticionBarco,_barco,BarcoState.InSolicitud1);
		
		//AVION A RECEPTOR
		addCoupling(_barco,BarcoState.OutPosicion,_receptor,ReceptorStateBarco.InPosicion);
		addCoupling(_barco,BarcoState.OutVelocidad,_receptor,ReceptorStateBarco.InVelocidad);
		addCoupling(_barco,BarcoState.OutAngulos,_receptor,ReceptorStateBarco.InAngulos);
		addCoupling(_barco,BarcoState.OutFuel,_receptor,ReceptorStateBarco.InFuel);
		addCoupling(_barco,BarcoState.OutEstado,_receptor,ReceptorStateBarco.InEstado);
		addCoupling(_barco,BarcoState.OutTodo,_receptor,ReceptorStateBarco.InTodo);
		


		_id = id;
	}
	
	public int getId () {
		return _id;
	}
	
	public PeticionesStateBarco getPeticion() {
		return _peticion;
	}
	
	public BarcoState getBarco() {
		return _barco;
	}
	
	public static void main(String[] args) 
	{
		Barco con = new Barco(1,null);
		Coordinator coordinator = new Coordinator(con);
		coordinator.initialize();
		coordinator.simulate(10);
		coordinator.exit();
	}

}
