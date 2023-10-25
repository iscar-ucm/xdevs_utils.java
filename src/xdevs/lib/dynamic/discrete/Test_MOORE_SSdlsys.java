package xdevs.lib.dynamic.discrete;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.general.sinks.ScopeStep;
import xdevs.lib.general.sources.PiecewiseStepFunctionGenerator;

/** 
 *  Test de la función MOORE_SSdlsys
 *  usando un sistema de 2º orden
 *  Entrada función escalera
 *  
 * @author J.M. de la Cruz  May 14th, 2008
 *
 */
public class Test_MOORE_SSdlsys extends Coupled {
	public Test_MOORE_SSdlsys(String name, SSdlsys modelo) {
		super(name);
		double[][] Steps = {{0.0,0.0},{1.0, 1.0},{-1.0,15.0},{0.0,30.0}};
		PiecewiseStepFunctionGenerator pwsf = new PiecewiseStepFunctionGenerator("pwsf", Steps);
		MOORE_SSdsys sys = new MOORE_SSdsys("sys",modelo);
                String[] portNames = {"y", "x", "u"};
		ScopeStep scope = new ScopeStep("scope", portNames); // 1 puerto de entrada = 1 serie de datos
		// Components:
		super.addComponent(pwsf);
		super.addComponent(scope);
		super.addComponent(sys);
		
	
		// Link:
		super.addCoupling(pwsf.out, sys.in);
		super.addCoupling(sys.out,scope.getInPort("y"));
		super.addCoupling(sys.outx,scope.getInPort("x"));
		super.addCoupling(pwsf.out, scope.getInPort("u"));
	}
	
	public static void main(String args[]) {
		SSdlsys modelo;
		Double[][] F = {{1.414, -0.6065},{1.0,0.0}};
		Double[][] G = {{0.5},{0.0}};
		Double[][] C = {{0.2088,0.1766}};
		Double[][] D = {{0.0}};
		Double[]   x0 = {3.0, 3.0};
		double h = 0.5; 
		modelo = new SSdlsys(F,G,C,D,x0,h);
		System.out.println("Simulación: MOORE_SSdlsys");
				
		Test_MOORE_SSdlsys ModeloM = new Test_MOORE_SSdlsys("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.initialize();
		coordinator.simulate(31.0);
		coordinator.exit();
	}
}
