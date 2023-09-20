package testing.lib.atomic.dynamic.discrete;

/** 
 *  Test de la función MEALYE_SSdlsys
 *  usando un sistema de 2º orden tipo MOORE
 *  Entrada función escalera
 *  
 * @author J.M. de la Cruz  May 18th, 2008
 *
 */
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.Coordinator;
import testing.lib.atomic.sinks.ScopeStep;
import testing.lib.atomic.sources.PiecewiseStepFunctionGenerator;
import testing.lib.atomic.sources.Clock;

public class Test_MEALY_SSdlsys extends Coupled {
	public Test_MEALY_SSdlsys(String name, SSdlsys modelo) {
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
		super.addCoupling(pwsf, "out", sys, "in");
		super.addCoupling(clk,"out", sys, "clock");
		super.addCoupling(sys,"out",scope,"y");
		super.addCoupling(sys,"outx",scope,"x");
		super.addCoupling(pwsf, "out", scope,"u");
		
	}
	
	public static void main(String args[]) {
		SSdlsys modelo;
		Double[][] F = {{1.414, -0.6065},{1.0,0.0}};
		Double[][] G = {{0.5},{0.0}};
		Double[][] C = {{0.2088,0.1766}};
		Double[][] D = {{0.0}};
		Double[]   x0 = {0.0, 0.0};
		double h = 0.5; 
		modelo = new SSdlsys(F,G,C,D,x0,h);
		System.out.println("Simulación: MEALY_SSdlsys");
				
		Test_MEALY_SSdlsys ModeloM = new Test_MEALY_SSdlsys("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.simulate(31.0);
	}
}
