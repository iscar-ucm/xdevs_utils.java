package xdevs.lib.projects.graph.models;

import xdevs.lib.projects.graph.structs.Punto;
import xdevs.lib.projects.graph.structs.Ruta;

public class DatosTipoPosicion {
	public static final int Avion=1;
	public static final int Barco=2;
	public static final int Naufrago=3;
	
	Ruta ruta;
	Punto punto;
	int tipoVehiculo;
	int nombre;

	public boolean tieneRuta(){
		return (ruta!=null);
	}
	/**
	 * Inclusion de ruta
	 * @param ruta ruta a incluir
	 * @param reemplazar indica si vamos a reemplazar o solo queremos inicializar
	 */
	public void ponRuta(Ruta inruta,boolean reemplazar){
		if((reemplazar)||(ruta==null)){
			this.ruta=inruta;
		}

	}
	
	public DatosTipoPosicion(int numero,int tipoVehiculo,Punto punto){
		this.nombre=numero;
		this.tipoVehiculo=tipoVehiculo;
		this.punto=punto;
		this.ruta=null;
	}

	public Ruta geRuta() {
		return ruta;
	}
	
	public Punto getPunto() {
		return punto;
	}
	public void setPunto(Punto punto) {
		this.punto = punto;
	}
	public int getTipoVehiculo() {
		return tipoVehiculo;
	}
	public void setTipoVehiculo(int tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	
	public int getNombre() {
		return nombre;
	}
	public void setNombre(int nombre) {
		this.nombre = nombre;
	}
}
