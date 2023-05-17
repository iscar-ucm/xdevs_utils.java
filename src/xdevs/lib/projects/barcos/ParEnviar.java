package ssii2007.simulacion;

public class ParEnviar {
	private String puerto;
	private Object mensaje;
	public ParEnviar(String puerto,Object mensaje){
		this.puerto=puerto;
		this.mensaje=mensaje;
	}
	public String damePuerto(){
		return puerto;
	}
	public Object dameMensaje(){
		return mensaje;
	}
}