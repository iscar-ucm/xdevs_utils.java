package xdevs.lib.examples.qss;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.general.sinks.TimeSeriesScope;
import xdevs.lib.general.sources.Step;
import xdevs.lib.numdevs.integrator.IntegratorQSS;

public class StepIntegrationQSS extends Coupled {
	public StepIntegrationQSS(String name) {
		super(name);
		// Escal�n (nombre, valor inicial, tiempo de cambio, valor final)
		Step step = new Step("step", 0.0, 0.0, 1.0);
		// Integrador
		IntegratorQSS integratorQSS = new IntegratorQSS(0.01, 0.0);
		// Time Series
		TimeSeriesScope scope = new TimeSeriesScope("scope", "title window", "title figure", "time", "value", "in");
		
		// Components:
		super.addComponent(step);
		super.addComponent(integratorQSS);
		super.addComponent(scope);
		
		// Link:
		super.addCoupling(step.out, integratorQSS.in);
		super.addCoupling(integratorQSS.out, scope.in[0]);
	}
	
	public static void main(String args[]) {
		StepIntegrationQSS model = new StepIntegrationQSS("model");
		Coordinator coordinator = new Coordinator(model);
		coordinator.initialize();
		coordinator.simulate(80);
		coordinator.exit();
	}
}
