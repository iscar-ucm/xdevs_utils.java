package xdevs.lib.dynamic.discrete;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.dynamic.IDynSys;
import xdevs.lib.general.sinks.ScopeStep;
import xdevs.lib.general.sources.Constant;

/** 
 *  Test de la función MOORE_SSdsys
 *  usando la ecuación de Van der Pol
 *  en SSvdpol
 *  
 * @author J.M. de la Cruz  May 14th, 2008
 *
 */
public class Test_MOORE_SSdsys_vdpol extends Coupled {

	public Test_MOORE_SSdsys_vdpol(String name, IDynSys modelo) {
		super(name);
		Constant<Double> cts = new Constant<>("cts", 0.0, 0.0);
		MOORE_SSdsys sys = new MOORE_SSdsys("sys",modelo);
                String[] portNames = {"y", "x"};
		ScopeStep scope = new ScopeStep("scope", portNames); // 1 puerto de entrada = 1 serie de datos
	
		// Components:
		super.addComponent(scope);
		super.addComponent(sys);
		super.addComponent(cts);
	
		// Link:
		super.addCoupling(cts.oOut, sys.in);
		super.addCoupling(sys.out,scope.getInPort("y"));
		super.addCoupling(sys.outx,scope.getInPort("x"));
	}
	
	public static void main(String args[]) {
		SSdvdpol modelo;
		Double[] x0 = {0.2,0.2};
		double h = 0.1; 
		modelo = new SSdvdpol(x0,h);
		System.out.println("Simulación: MOORE_SSvdpol");
				
		Test_MOORE_SSdsys_vdpol ModeloM = new Test_MOORE_SSdsys_vdpol("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.initialize();
		coordinator.simulate(20.0);
		coordinator.exit();
	}
}
