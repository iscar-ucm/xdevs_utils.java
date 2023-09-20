package testing.lib.atomic.dynamic.discrete;

/** 
 *  Test de la función MOORE_SSdsys
 *  usando la ecuación de Van der Pol
 *  en SSvdpol
 *  
 * @author J.M. de la Cruz  May 14th, 2008
 *
 */
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.Coordinator;
import testing.lib.atomic.sinks.ScopeStep;
import testing.lib.atomic.sources.Constant;
import testing.lib.atomic.dynamic.*;

public class Test_MOORE_SSdsys_vdpol extends Coupled {

	public Test_MOORE_SSdsys_vdpol(String name, IDynSys modelo) {
		super(name);
		Constant cts = new Constant("cts", 0.0, 0.0);
		MOORE_SSdsys sys = new MOORE_SSdsys("sys",modelo);
                String[] portNames = {"y", "x"};
		ScopeStep scope = new ScopeStep("scope", portNames); // 1 puerto de entrada = 1 serie de datos
	
		// Components:
		super.addComponent(scope);
		super.addComponent(sys);
		super.addComponent(cts);
	
		// Link:
		super.addCoupling(cts, "out", sys, "in");
		super.addCoupling(sys,"out",scope,"y");
		super.addCoupling(sys,"outx",scope,"x");
		
	}
	
	public static void main(String args[]) {
		SSdvdpol modelo;
		Double[] x0 = {0.2,0.2};
		double h = 0.1; 
		modelo = new SSdvdpol(x0,h);
		System.out.println("Simulación: MOORE_SSvdpol");
				
		Test_MOORE_SSdsys_vdpol ModeloM = new Test_MOORE_SSdsys_vdpol("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.simulate(20.0);
	}
}
