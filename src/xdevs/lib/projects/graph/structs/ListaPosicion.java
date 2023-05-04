package xdevs.lib.projects.graph.structs;

import java.util.ArrayList;

import xdevs.lib.projects.graph.models.DatosTipoPosicion;

/**
 * Lista de objetos DatosTipoPosicion,con la que podemos hacer algunas operaciones
 */
public class ListaPosicion {
	
	ArrayList<DatosTipoPosicion> lista;
	
	public ListaPosicion(){
		lista=new ArrayList<DatosTipoPosicion>();
	}
	
	public void añadePosicion(DatosTipoPosicion nueva){
		int posicion=buscaPosicion(nueva);
		if(posicion!=-1){
			DatosTipoPosicion antigua=lista.get(posicion);
			nueva.ponRuta(antigua.ruta, false);
			lista.set(posicion, nueva);
			
		}
		else{
			lista.add(nueva);
		}
	}
	
	

	public void eliminaPosicion(int nombre,int tipoVehiculo){
		for (int cont=0;cont<lista.size();cont++){
			if((lista.get(cont).nombre==nombre)&&(lista.get(cont).tipoVehiculo==tipoVehiculo)){
				lista.remove(cont);
			}
		}
		
	}
	

	public int buscaPosicion(DatosTipoPosicion posicion){
		int num_pos=-1;
		DatosTipoPosicion actual=null;
		boolean encontrado=false;
		int cont=0;
		while((!encontrado)&&(cont<lista.size())){
			actual=lista.get(cont);
			if((posicion.getNombre()==actual.getNombre())
				&&
				(posicion.getTipoVehiculo()==actual.getTipoVehiculo())
				){
				num_pos=cont;
				encontrado=true;
			}
			cont++;
		}
		return num_pos;
		
	}
	
	public ArrayList<DatosTipoPosicion> dameLista(){
		return lista;
	}
	
	
	public ArrayList<DatosTipoPosicion> filtraTipo(int tipo){
		ArrayList<DatosTipoPosicion> filtrado=new ArrayList<DatosTipoPosicion>();
		for(int cont=0;cont<lista.size();cont++){
			if(lista.get(cont).getTipoVehiculo()==tipo){
				filtrado.add(lista.get(cont));
			}
		}
		return filtrado;
	}
}
