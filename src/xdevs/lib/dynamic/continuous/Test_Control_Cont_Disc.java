package xdevs.lib.dynamic.continuous;

import java.util.ArrayList;

import xdevs.core.modeling.Coupled;
import xdevs.lib.dynamic.discrete.MEALY_SSdsys;
import xdevs.lib.general.sources.PiecewiseStepFunctionGenerator;
import xdevs.lib.logic.sequential.Clock;
import xdevs.lib.numdevs.math.WeightedSum;

/**
 *  Test de control de sistema continuo G(s) = 1/s^2
 *  controlador discreto.
 *  
 * @author J.M. de la Cruz 
 * @version 1.0, 21th May 2008
 *
 */

public class Test_Control_Cont_Disc extends Coupled  {

	public Test_Control_Cont_Disc(String name, SSclsys modelo, SSdlsys control) {
		super(name);
		double[][] Steps = {{0.0,0.0},{1.0,0.20},{-1.0,5.0},{0.0,10.0}};
		PiecewiseStepFunctionGenerator pwsf = new PiecewiseStepFunctionGenerator("pwsf", Steps);
		// Initialize an arraylist with the weights:
		ArrayList<Number> pesossuma = new ArrayList<Number>();
		pesossuma.add(1.0);
		pesossuma.add(-1.0);
		WeightedSum suma = new WeightedSum(pesossuma, 0.0);
		MOORE_SScsys sys = new MOORE_SScsys("sys",modelo);
		MEALY_SSdsys ctr = new MEALY_SSdsys("ctr",control);
		Clock clk = new Clock("clock",0.1); // Mirar periodo de muestreo
                String[] portNames = {"y", "x", "u"};
		ScopeStep scope = new ScopeStep("scope", portNames); // 1 puerto de entrada = 1 serie de datos
		//scope.addInport("y", modelo.getNy());  	// Salida
		//scope.addInport("x", modelo.getNx()); 	// Estado
		//scope.addInport("u", modelo.getNu()); 				 	// en el test u es una señal de dimension 1

		// Components:
		super.addComponent(pwsf);
		super.addComponent(suma);
		super.addComponent(sys);
		super.addComponent(ctr);
		super.addComponent(clk);
		super.addComponent(scope);
		
		// Link:
		super.addCoupling(pwsf, "out", suma, "in0");
		super.addCoupling(sys,"out",suma,"in1");
		super.addCoupling(suma,"out",ctr,"in");
		super.addCoupling(clk,"out", ctr, "clock");
		super.addCoupling(ctr, "out", sys, "in");
		super.addCoupling(sys,"out",scope,"y");
		super.addCoupling(sys,"outx",scope,"x");
		super.addCoupling(ctr, "out", scope,"u");
	}
	
	public static void main(String args[]) {
		SSclsys modelo;
		Double[][] A = {{0.0, 0.0},{1.0,0.0}};
		Double[][] B = {{1.0},{0.0}};
		Double[][] C = {{0.0,1.0}};
		Double[][] D = {{0.0}};
		Double[]   x0 = {0.0, 0.0};
		double h = 0.05; // Integration period
		modelo = new SSclsys(A,B,C,D,x0,h);
		
		SSdlsys control;
		Double[][] F = {{0.2375}};
		Double[][] G = {{4.0}};
		Double[][] H = {{-4.0213}};
		Double[][] J = {{27.7}};
		Double[]   xd0 = {0.0};
		double T = 0.1; // Integration period
		control = new SSdlsys(F,G,H,J,xd0,T);
		System.out.println("Simulación: Test_Control_Cont_Disc");
				
		Test_Control_Cont_Disc ModeloM = new Test_Control_Cont_Disc("Modelo",modelo,control);
		Coordinator coordinator = new Coordinator(ModeloM);
		coordinator.simulate(0.4);
	}
}
