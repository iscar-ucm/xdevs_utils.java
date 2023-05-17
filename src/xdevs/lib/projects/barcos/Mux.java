package ssii2007.Mux;

import java.util.Vector;

import testing.kernel.modeling.AtomicState;
import testing.kernel.modeling.DevsDessMessage;
import xdevs.kernel.modeling.Port;


public class Mux extends AtomicState{
	public Port[] In ;
	public final Port Out= new Port("OUT");
	public DevsDessMessage msj;
	public Vector<DatoMux> enviar;
	
	@SuppressWarnings("unchecked")
	public Mux(String name, int numEntradas){
		super(name);
		enviar= new Vector<DatoMux>();
		//A�adimos los puertos de entrada
		In= new Port[numEntradas];
		Vector[] enviar= new Vector[numEntradas];
		for(int cont=0;cont<numEntradas;cont++){
			if (this.getName().compareTo("muxcaviones")==0){
				System.out.println("cont"+cont);
			}
			In[cont] = new Port("In"+((Integer)cont).toString());
			this.addInport(In[cont]);
			enviar[cont]=new Vector();
		}
		

		
		
		//A�adimos el puerto de salida
		this.addOutport(Out);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void deltext(double e) {
            super.resume(e);
		// TODO Auto-generated method stub
		//Iterator iterador=null;
		
		for(int cont=0;cont<In.length;cont++)
				enviar.add(new DatoMux(cont,In[cont].getValue()));
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
		Out.setValue(todo);
	}
	
	public int dameTamaño(){
		return In.length;
	}
	
}
