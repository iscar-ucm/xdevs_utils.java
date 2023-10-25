package xdevs.lib.dynamic.discrete;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.general.sinks.ScopeStep;
import xdevs.lib.general.sources.Clock;
import xdevs.lib.general.sources.PiecewiseStepFunctionGenerator;

/** 
 *  Test de la función MEALYE_SSdlsys
 *  usando un sistema de 1er orden 
 *  Entrada función escalera
 *  
 * @author J.M. de la Cruz  May 18th, 2008
 *
 */

public class Test_MEALY_SSdlsys_e1 extends Coupled {
	public Test_MEALY_SSdlsys_e1(String name, SSdlsys modelo) {
		super(name);
		double[][] Steps = {{0.0,0.0},{1.0, 1.0},{-1.0,15.0},{0.0,30.0}};
		PiecewiseStepFunctionGenerator pwsf = new PiecewiseStepFunctionGenerator("pwsf", Steps);
		MEALY_SSdsys sys = new MEALY_SSdsys("sys",modelo);
		Clock clk = new Clock("clock",0.5);
                String[] portNames = {"y", "x", "u"};
		ScopeStep scope = new ScopeStep("scope", portNames); // 1 puerto de entrada = 1 serie de datos
		// Components:
		super.addComponent(pwsf);
		super.addComponent(scope);
		super.addComponent(sys);
		super.addComponent(clk);
		
	
		// Link:
		super.addCoupling(pwsf.out, sys.in);
		super.addCoupling(clk.out, sys.clock);
		super.addCoupling(sys.out,scope.getInPort("y"));
		super.addCoupling(sys.outx,scope.getInPort("x"));
		super.addCoupling(pwsf.out, scope.getInPort("u"));		
	}
	
	public static void main(String args[]) {
		SSdlsys modelo;
		Double[][] F = {{0.5}};
		Double[][] G = {{0.5}};
		Double[][] C = {{0.5}};
		Double[][] D = {{1.0}};
		Double[]   x0 = {0.0};
		double h = 0.5; 
		modelo = new SSdlsys(F,G,C,D,x0,h);
		System.out.println("Simulación: MEALY_SSdlsys");
				
		Test_MEALY_SSdlsys_e1 ModeloM = new Test_MEALY_SSdlsys_e1("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.initialize();
		coordinator.simulate(31.0);
		coordinator.exit();
	}
}
