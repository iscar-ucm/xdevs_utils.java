package ssii2007.otrosModelos;

import java.util.Iterator;
import java.util.Vector;

import testing.kernel.modeling.AtomicState;
import testing.kernel.modeling.DevsDessMessage;

import ssii2007.aviones.AvionState;
import ssii2007.matematico.IOperadorBinario;
import ssii2007.matematico.RungeKutta;
import ssii2007.matematico.SumadorBinario;

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
	public static final String In1 = "IN1";
	
	/**
	 * Constante para el puerto de entrada 2
	 */
	public static final String In2 = "IN2";
	
	/**
	 * Constante para el puerto de salida 
	 */
	public static final String Out = "OUT";
	
	
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
		addInport(In1);	
		addInport(In2);	
		addOutport(Out);
		this.operacion=new SumadorBinario();
		this.addState("t");
		this.addState("salida");
		this.setStateValue("t", dt);
		setSigma(INFINITY);
	}
	
	/** Constructor establece como operacion , operacion introducida en el 
	 * constructor */
	public Operador (String name,float dt,IOperadorBinario operador_b) {
		super(name);
		addInport(In1);	
		addInport(In2);	
		addOutport(Out);
		this.operacion=operador_b;
		this.addState("t");
		this.addState("salida");
		this.setStateValue("t", dt);
		setSigma(INFINITY);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deltext(double e, DevsDessMessage x) {
		// TODO Auto-generated method stub
		Iterator iterador1 = x.getValuesOnPort(Operador.In1).iterator();
		Iterator iterador2 = x.getValuesOnPort(Operador.In2).iterator();
		//SIMPLEMENTE, SE ENVIA LOS DATOS A OPERAR, NADA MAS
		Double valor1 = ((Double)(iterador1.next()));
		Double valor2 = ((Double)(iterador2.next()));
		this.setStateValue("salida", operacion.opera(valor1, valor2));
		setSigma(this.getStateValue("t").doubleValue());
	}


	@Override
	public void deltint() {
		// TODO Auto-generated method stub
		//AQUI DEBERIA IR INFINITO??
		setSigma(INFINITY);
	}


	@SuppressWarnings("unchecked")
	@Override
	public DevsDessMessage lambda() {
		// TODO Auto-generated method stub
		DevsDessMessage msg = new DevsDessMessage();
		Vector todo = new Vector(1,0);
		todo.add(this.getStateValue("salida").doubleValue());
		msg.add(Operador.Out,todo);			
		return msg;
	}
	
	
}
