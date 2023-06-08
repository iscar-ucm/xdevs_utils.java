package xdevs.lib.projects.barcos;

public class DatoMux {
	private int Entrada;
	private Object Dato;
	
	public DatoMux(int in,Object data){
		this.Entrada=in;
		this.Dato=data;
	}
	
	public int damePuerto(){
		return Entrada;
	}
	
	public Object dameDato(){
		return Dato;
	}
}
