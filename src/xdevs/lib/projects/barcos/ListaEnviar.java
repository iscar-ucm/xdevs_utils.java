package ssii2007.simulacion;

import java.util.ArrayList;



public class ListaEnviar{
	

	ArrayList<ParEnviar> listaEnviar;
	
	public ListaEnviar(){
		listaEnviar=new ArrayList<ParEnviar>();
	}
	
	public void añadePar(ParEnviar par){
		listaEnviar.add(par);
	}
	
	public ArrayList<ParEnviar> dameLista(){
		return listaEnviar;
	}
	
	

}
