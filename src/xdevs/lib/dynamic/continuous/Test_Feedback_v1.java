package testing.lib.atomic.dynamic.continuous;
/**
 * Test of a system with feedback
 * The system is a double integrator
 * @author J.M. de la Cruz
 * @version 1.0, 16th May 2008
 *
 */
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.Coordinator;
import testing.lib.atomic.sinks.ScopeStep;
import testing.lib.atomic.sources.PiecewiseStepFunctionGenerator;
import testing.lib.atomic.math.WeightedSum;

public class Test_Feedback_v1 extends Coupled  {

	public Test_Feedback_v1(String name, SSclsys modelo) {
		super(name);
		double[][] Steps = {{0.0,0.0},{1.0,1.0},{-1.0,15.0},{0.0,30.0}};
		PiecewiseStepFunctionGenerator pwsf = new PiecewiseStepFunctionGenerator("pwsf", Steps);
		Double[] pesossuma = {1.0, -1.0};
		WeightedSum suma = new WeightedSum("suma",pesossuma);
		MOORE_SScsys sys = new MOORE_SScsys("sys",modelo);
                String[] portNames = {"y", "x", "u"};
		ScopeStep scope = new ScopeStep("scope", portNames); // 1 puerto de entrada = 1 serie de datos

		// Components:
		super.addComponent(pwsf);
		super.addComponent(suma);
		super.addComponent(sys);
		super.addComponent(scope);
		
		// Link:
		super.addCoupling(pwsf, "out", suma, "in0");
		super.addCoupling(sys,"out",suma,"in1");
		super.addCoupling(suma,"out",sys,"in");
		super.addCoupling(sys,"out",scope,"y");
		super.addCoupling(sys,"outx",scope,"x");
		super.addCoupling(pwsf, "out", scope,"u");
		
	}
	
	public static void main(String args[]) {
		SSclsys modelo;
		Double[][] A = {{0.0, 0.0},{1.0,0.0}};
		Double[][] B = {{1.0},{0.0}};
		Double[][] C = {{0.0,1.0}};
		Double[][] D = {{0.0}};
		Double[]   x0 = {0.0, 0.0};
		double h = 0.025; // Integration period
		modelo = new SSclsys(A,B,C,D,x0,h);
		System.out.println("Simulación: Test_Feedback_v1");
				
		Test_Feedback_v1 ModeloM = new Test_Feedback_v1("Modelo",modelo);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.simulate(30.0);
	}
}
