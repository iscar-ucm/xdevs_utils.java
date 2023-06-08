package xdevs.lib.projects.barcos;

public class Rescate {
	private int barco_rescatador;
	private int tiempo;
	
	public Rescate(int barco_rescatador,int tiempo){
		this.barco_rescatador=barco_rescatador;
		this.tiempo=tiempo;
	}
	
	public int dameBarco(){
		return barco_rescatador;
	}
	public int dameTiempo(){
		return tiempo;
	}
}
