package testing.lib.atomic.dynamic.continuous;

/** 
 *  Test de la función MOORE_SScsys
 *  usando la ecuación de Van der Pol
 *  en SScvdpol
 *  
 * @author J.M. de la Cruz,  May 16th 2008
 *
 */
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.Coordinator;
import testing.lib.atomic.dynamic.*;
import testing.lib.atomic.sinks.ScopeLine;
import testing.lib.atomic.sources.Constant;

public class Test_MOORE_SScsys_vdpol extends Coupled {

	public Test_MOORE_SScsys_vdpol(String name, IDynSys modelo) {
		super(name);
		Constant cts = new Constant("cts", 0.0, 0.0);
		MOORE_SScsys sys = new MOORE_SScsys("sys",modelo);
                String[] portNames = {"y", "x"};
		ScopeLine scope = new ScopeLine("scope", portNames); // 1 puerto de entrada = 1 serie de datos
	
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
		SScvdpol modelo;
		Double[] x0 = {0.2,0.2};
		double h = 0.1; 
		modelo = new SScvdpol(x0,h);
		System.out.println("Simulación: MOORE_SScvdpol");
				
		Test_MOORE_SScsys_vdpol ModeloM = new Test_MOORE_SScsys_vdpol("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.simulate(20.0);
	}
}