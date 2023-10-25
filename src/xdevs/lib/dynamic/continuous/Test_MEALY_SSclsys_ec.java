package xdevs.lib.dynamic.continuous;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.general.sinks.ScopeStep;
import xdevs.lib.general.sources.Clock;
import xdevs.lib.general.sources.PiecewiseStepFunctionGenerator;

/** 
 *  Test de la función MEALYE_SSclsys
 *  usando un sistema de 1er orden continuo
 *  Entrada función escalera
 *  
 * @author J.M. de la Cruz  May 18th, 2008
 *
 */
public class Test_MEALY_SSclsys_ec extends Coupled {
	public Test_MEALY_SSclsys_ec(String name, SSclsys modelo) {
		super(name);
		double[][] Steps = {{0.0,0.0},{1.0, 1.05},{-1.0,15.0},{0.0,30.0}};
		PiecewiseStepFunctionGenerator pwsf = new PiecewiseStepFunctionGenerator("pwsf", Steps);
		MEALY_SScsys sys = new MEALY_SScsys("sys",modelo);
		Clock clk = new Clock("clock",0.1);
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
		SSclsys modelo;
		Double[][] A = {{-2.0}};
		Double[][] B = {{1.0}};
		Double[][] C = {{-1.0}};
		Double[][] D = {{1.0}};
		Double[]   x0 = {0.0};
		double h = 0.1; 
		modelo = new SSclsys(A,B,C,D,x0,h);
		System.out.println("Simulación: MEALY_SSdlsys");
				
		Test_MEALY_SSclsys_ec ModeloM = new Test_MEALY_SSclsys_ec("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.initialize();
		coordinator.simulate(31.0);
		coordinator.exit();
	}
}
