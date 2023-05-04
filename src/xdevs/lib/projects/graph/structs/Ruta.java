package xdevs.lib.projects.graph.structs;

import java.util.ArrayList;

public class Ruta {
	
	private ArrayList<Punto> ruta;
	
	private int punto_actual;
	
	private boolean bucle=false;
	
	
	public static int Exito =1;
	public static int Fracaso =-1;
	
	public Ruta(){
		punto_actual=0;
		ruta=new ArrayList<Punto>();
		bucle=false;
	};
	
	public void setBucle(boolean bucle){
		this.bucle=bucle;
	}
	
	public void añaadePuntos(ArrayList<Punto> puntos,int numero_alertar,boolean circular){
		for(int cont=0;cont<puntos.size();cont++){
			this.ruta.add(puntos.get(cont));
		}
	}
	
	public static Ruta rutaCuadrado(){
		Ruta ruta=new Ruta();
		ruta.añadePunto(new Punto(11000,12000,1000));
		ruta.añadePunto(new Punto(12000,12000,1000));
		ruta.añadePunto(new Punto(12000,11000,1000));
		ruta.añadePunto(new Punto(11000,11000,1000));
		ruta.bucle=true;
		return ruta;
	}
	
	public void ponReseteaPuntos(){
		ruta=new ArrayList<Punto>();
	}
	
	public void añadePunto(Punto punto){
		this.ruta.add(punto);
	}
	
	public Punto damePuntoActual(){
		if(this.punto_actual<this.ruta.size()){
			return this.ruta.get(this.punto_actual);
		}
		else{
			return this.ruta.get(this.punto_actual-1);	
		}
	}
	
	public int avanzaPunto(){
		int exito=Ruta.Fracaso;
		if(this.punto_actual<this.ruta.size()){
			punto_actual++;
			exito=Ruta.Exito;
		}
		else{
			if(this.bucle==true){
				punto_actual=0;
				exito=Ruta.Exito;
			}
		}
		return exito;
	}
	
	public int dameRestantes(){
		return(this.ruta.size()-this.punto_actual);
	}
	
}
