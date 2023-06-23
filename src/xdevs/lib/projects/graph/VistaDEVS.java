package xdevs.lib.projects.graph;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.projects.graph.models.ControladorAplicacion;
import xdevs.lib.projects.graph.models.CoupledSimulacion;
import xdevs.lib.projects.graph.structs.terrain.Terreno;

public class VistaDEVS extends Coupled implements Runnable{
	private ManagerVista _managerVista;
	private ThreadAnimacion _threadAnimacion;
	
	public Port<Object> in = new Port<>("IN");
	public Port<Object> out = new Port<>("OUT");

	public VistaDEVS(ControladorAplicacion control, Terreno terreno, CoupledSimulacion simulacion, int numAviones, int numBarcos) {
		super("VistaDEVS");
		_managerVista = new ManagerVista(control, terreno, simulacion, numAviones, numBarcos);
	    _threadAnimacion = new ThreadAnimacion(30);
	    
	    addInPort(in);
	    addOutPort(out);
	    
	    addComponent(_managerVista);
	    addComponent(_threadAnimacion);
	    
	    // Conexiones desde ManagerVista
	    addCoupling(_managerVista.outAnimador, _threadAnimacion.inFPS);
	    
	    // Conexiones desde Animador
	    addCoupling(_threadAnimacion.outSeñalAnimacion, _managerVista.inAnimacion);
	}

	@Override
	public void run() {
		Coordinator coordinator = new Coordinator(this);
		coordinator.simulate(Integer.MAX_VALUE);
	}

}
