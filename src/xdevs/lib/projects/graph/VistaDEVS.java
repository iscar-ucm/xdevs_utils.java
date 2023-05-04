package xdevs.lib.projects.graph;

import xdevs.core.modeling.Coupled;
import xdevs.lib.projects.graph.models.ControladorAplicacion;
import xdevs.lib.projects.graph.structs.terrain.Terreno;

public class VistaDEVS extends Coupled implements Runnable{
	private ManagerVista _managerVista;
	private ThreadAnimacion _threadAnimacion;
	
	public static final String In = "IN";
	public static final String Out = "OUT";

	public VistaDEVS(ControladorAplicacion control, Terreno terreno, CoupledSimulacion simulacion, int numAviones, int numBarcos) {
		super("VistaDEVS");
		_managerVista = new ManagerVista(control, terreno, simulacion, numAviones, numBarcos);
	    _threadAnimacion = new ThreadAnimacion(30);
	    
	    addInport(In);
	    addOutport(Out);
	    
	    addComponent(_managerVista);
	    addComponent(_threadAnimacion);
	    
	    // Conexiones desde ManagerVista
	    addCoupling(_managerVista,ManagerVista.OutAnimador,_threadAnimacion,ThreadAnimacion.InFPS);
	    
	    // Conexiones desde Animador
	    addCoupling(_threadAnimacion,ThreadAnimacion.OutSeñalAnimacion,_managerVista,ManagerVista.InAnimacion);
	}

	@Override
	public void run() {
		Coordinator coordinator = new Coordinator(this);
		coordinator.simulate(Integer.MAX_VALUE);
	}

}
