package xdevs.lib.projects.barcos;

import java.util.Iterator;
import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;
import xdevs.lib.projects.math.IOperadorBinario;
import xdevs.lib.projects.math.SumadorBinario;

/**
 * Clase que implementa un modelo xDevs, que tiene dos
 * entradas y devuelve en un tiempo dt, indicado en el 
 * constructor el resultado de la funcion binaria especificada
 * @author David
 *
 */
public class Operador extends AtomicState{
	/**
	 * Elemento que especifica el tipo de operacion que queremos implementar
	 */
	IOperadorBinario operacion;
	
	
	
	/**
	 * Constante para el puerto de entrada 1
	 */
	public Port<Double> in1 = new Port<>("IN1");
	
	/**
	 * Constante para el puerto de entrada 2
	 */
	public Port<Double> in2 = new Port<>("IN2");
	
	/**
	 * Constante para el puerto de salida 
	 */
	public Port<Vector<Double>> out = new Port<>("OUT");
	
	
	/**
	* Constante para el valor del tiempo de espera desde el 
	* calculo hasta el envio del resultado
	*/
	private static final String t = "t";
	
	/**
	* Constante para el valor del resultado de la operacion
	*/
	private static final String salida = "salida";

	/** Constructor por defecto, establece como operacion la suma */
	public Operador (String name,float dt) {
		super(name);
		addInPort(in1);	
		addInPort(in2);	
		addOutPort(out);
		this.operacion=new SumadorBinario();
		this.addState("t");
		this.addState("salida");
		this.setStateValue("t", dt);
		super.passivate();
	}
	
	/** Constructor establece como operacion , operacion introducida en el 
	 * constructor */
	public Operador (String name,float dt,IOperadorBinario operador_b) {
		super(name);
		addInPort(in1);	
		addInPort(in2);	
		addOutPort(out);
		this.operacion=operador_b;
		this.addState("t");
		this.addState("salida");
		this.setStateValue("t", dt);
		super.passivate();
	}

	@Override
	public void deltext(double e) {
		// TODO Auto-generated method stub
		Iterator<Double> iterador1 = in1.getValues().iterator();
		Iterator<Double> iterador2 = in2.getValues().iterator();
		//SIMPLEMENTE, SE ENVIA LOS DATOS A OPERAR, NADA MAS
		Double valor1 = iterador1.next();
		Double valor2 = iterador2.next();
		this.setStateValue("salida", operacion.opera(valor1, valor2));
		setSigma(this.getStateValue("t").doubleValue());
	}


	@Override
	public void deltint() {
		// TODO Auto-generated method stub
		//AQUI DEBERIA IR INFINITO??
		super.passivate();
	}


	@Override
	public void lambda() {
		// TODO Auto-generated method stub
		Vector<Double> todo = new Vector(1,0);
		todo.add(this.getStateValue("salida").doubleValue());
		out.addValue(todo);			
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.passivate();
	}
	
	
}
