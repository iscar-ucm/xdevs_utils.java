package xdevs.lib.dynamic.continuous;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.dynamic.IDynSys;
import xdevs.lib.general.sinks.ScopeLine;

/** 
 *  Test de la función MOORE_SScsys
 *  usando la ecuación de Van der Pol
 *  en SScvdpol. No se usa entrada.
 *  
 * @author J.M. de la Cruz,  May 20th 2008
 *
 */
public class Test_MOORE_SScsys_vdpol_autonomous extends Coupled {

	public Test_MOORE_SScsys_vdpol_autonomous(String name, IDynSys modelo) {
		super(name);
		MOORE_SScsys sys = new MOORE_SScsys("sys",modelo);
                String[] portNames = {"y", "x"};
		ScopeLine scope = new ScopeLine("scope", portNames); // 1 puerto de entrada = 1 serie de datos
	
		// Components:
		super.addComponent(scope);
		super.addComponent(sys);
	
		// Link:
		super.addCoupling(sys.out,scope.getInPort("y"));
		super.addCoupling(sys.outx,scope.getInPort("x"));
	}
	
	public static void main(String args[]) {
		SScvdpol modelo;
		Double[] x0 = {0.2,0.2};
		double h = 0.1; 
		modelo = new SScvdpol(x0,h);
		System.out.println("Simulación: MOORE_SScvdpol_autonomous");
				
		Test_MOORE_SScsys_vdpol_autonomous ModeloM = new Test_MOORE_SScsys_vdpol_autonomous("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.initialize();
		coordinator.simulate(20.0);
		coordinator.exit();
	}
}