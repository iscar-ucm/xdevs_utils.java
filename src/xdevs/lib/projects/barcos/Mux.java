package xdevs.lib.projects.barcos;

import java.util.Vector;

import xdevs.core.modeling.AtomicState;
import xdevs.core.modeling.Port;

public class Mux extends AtomicState{
	public Port<Object>[] in ;
	public final Port<Object> out= new Port<>("OUT");
	public Vector<DatoMux> enviar;
	
	@SuppressWarnings("unchecked")
	public Mux(String name, int numEntradas){
		super(name);
		enviar= new Vector<DatoMux>();
		//A�adimos los puertos de entrada
		in= new Port[numEntradas];
		Vector[] enviar= new Vector[numEntradas];
		for(int cont=0;cont<numEntradas;cont++){
			if (this.getName().compareTo("muxcaviones")==0){
				System.out.println("cont"+cont);
			}
			in[cont] = new Port("In"+((Integer)cont).toString());
			this.addInPort(in[cont]);
			enviar[cont]=new Vector();
		}
		

		
		
		//A�adimos el puerto de salida
		this.addOutPort(out);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void deltext(double e) {
            super.resume(e);
		// TODO Auto-generated method stub
		//Iterator iterador=null;
		
		for(int cont=0;cont<in.length;cont++)
				enviar.add(new DatoMux(cont,in[cont].getSingleValue()));
				//System.out.println("siguiente "+iterador.next());
				//iterador.next();
				
			
			
		//	while(iterador.hasNext()){
		//		this.enviar[cont]=(Vector)(iterador.next());
			//	enviar=iterador.next();
		//	}*/
		
		//x.getValuesOnPort(portName)
		//Si llega algo...
		this.holdIn("active", 0);
		
	}

	@Override
	public void deltint() {
		// TODO Auto-generated method stub
		this.passivate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void lambda() {
		// TODO Auto-generated method stub
		Vector todo= new Vector();
		todo.add(this.enviar);
		enviar= new Vector<DatoMux>();
		out.addValue(todo);
	}
	
	public int dameTamaño(){
		return in.length;
	}


	@Override
	public void exit() {
	}


	@Override
	public void initialize() {
		super.passivate();
	}
	
}
