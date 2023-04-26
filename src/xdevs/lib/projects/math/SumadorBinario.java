package xdevs.lib.projects.math;

/**
 * Clase que implementa la clase abstracta, OperadorBinarioAbs
 * y que define el comportamiento de la operacion como la suma de 
 * los dos elementos
 * @author David
 */
public class SumadorBinario implements IOperadorBinario{

	public SumadorBinario(){
	}
	
	public Double opera(Double a, Double b) {
		return a+b;
	}

}
