package testing.lib.atomic.dynamic.continuous;

/** 
 *  Test de la función MOORE_SSclsys
 *  usando un sistema de 2º orden
 *  Entrada función escalera
 *  
 * @author J.M. de la Cruz 
 * @version 1.0,  May 16th 2008 
 */
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.Coordinator;
import testing.lib.atomic.sinks.ScopeStep;
import testing.lib.atomic.sources.PiecewiseStepFunctionGenerator;

public class Test_MOORE_SSclsys extends Coupled {
	public Test_MOORE_SSclsys(String name, SSclsys modelo) {
		super(name);
		double[][] Steps = {{0.0,0.0},{1.0, 1.0},{-1.0,15.05},{0.0,30.0}};
		PiecewiseStepFunctionGenerator pwsf = new PiecewiseStepFunctionGenerator("pwsf", Steps);
		MOORE_SScsys sys = new MOORE_SScsys("sys",modelo);
                String[] portNames = {"y", "x", "u"};
		ScopeStep scope = new ScopeStep("scope", portNames); // 1 puerto de entrada = 1 serie de datos
		// Components:
		super.addComponent(pwsf);
		super.addComponent(scope);
		super.addComponent(sys);
		
	
		// Link:
		super.addCoupling(pwsf, "out", sys, "in");
		super.addCoupling(sys,"out",scope,"y");
		super.addCoupling(sys,"outx",scope,"x");
		super.addCoupling(pwsf, "out", scope,"u");
		
	}
	
	public static void main(String args[]) {
		SSclsys modelo;
		Double[][] A = {{-1.0, -1.0},{1.0,0.0}};
		Double[][] B = {{0.0},{1.0}};
		Double[][] C = {{1.0,0.0}};
		Double[][] D = {{0.0}};
		Double[]   x0 = {0.0, 0.0};
		double h = 0.1; // Integration period
		modelo = new SSclsys(A,B,C,D,x0,h);
		System.out.println("Simulación: MOORE_SSclsys");
				
		Test_MOORE_SSclsys ModeloM = new Test_MOORE_SSclsys("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.simulate(31.0);
	}
}
