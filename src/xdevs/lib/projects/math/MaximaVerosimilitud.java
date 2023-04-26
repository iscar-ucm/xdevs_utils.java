package xdevs.lib.projects.math;

import java.util.Random;

public class MaximaVerosimilitud extends FuncionProbabilidadAbstracta{

	
	public MaximaVerosimilitud(double media,double varianza,double num_datos){
		this.media=media;
		this.varianza=varianza;
		this.num_datos=num_datos;
	}
	
	public void NuevoValor(boolean bueno_malo,double valor){
		if(num_datos==0){
			media=0;
			varianza=0;
		}
		if(bueno_malo){
			num_datos++;
			varianza= varianza +(1/this.num_datos)*((valor-media)*(valor-media)-varianza);
			media=media + (1/this.num_datos)*(valor-media);
			
		}
	}
	
	public double evalua(double x){
		double devuelve=-1;
		//System.out.println("media "+media);
		//System.out.println("varianza "+varianza);
		devuelve=(1/(Math.sqrt(2*Math.PI*varianza)))*
		Math.pow(Math.E ,(-((x-media)*(x-media))/(2*varianza)) );
		return devuelve;
	}
	
	public static void main(String[] args){
		FuncionProbabilidadAbstracta funcion=new MaximaVerosimilitud(0,2,0);
		funcion.evalua(0);
		Random randomizador=new Random();
		double randomizado=-1;
		for(int cont=0;cont<100;cont++){
			randomizado=randomizador.nextGaussian();
			System.out.println("numero randomizado "+randomizado);
			funcion.NuevoValor(true, randomizado);
			System.out.println("valor probabilidad de 1 " + funcion.evalua(0));
			System.out.println("valor probabilidad de 3 " + funcion.evalua(3));
			System.out.println("valor probabilidad de 6 " + funcion.evalua(6));
			System.out.println("valor probabilidad de 9 " + funcion.evalua(9));
			System.out.println("valor probabilidad de 5 " + funcion.evalua(5));
		}
	}
}
