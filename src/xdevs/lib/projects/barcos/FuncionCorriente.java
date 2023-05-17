package ssii2007.otrosModelos;

import java.util.Random;

public class FuncionCorriente {

	public static double constante0 =0;
	public static double constante1 =1;
	public static double variable=2;
	public static double loco=4;
	public static double dameCorriente(double tipoCorriente,double x, double y) {
		// TODO Auto-generated method stub
		double devolver =0;
		if(tipoCorriente==0){
			devolver=0;
		}
		else if(tipoCorriente==1){
			devolver=1;
		}
		else if(tipoCorriente==2){
			devolver= dameCorrienteCasilla(x,y);
		}
		else if(tipoCorriente==4){
			//devolver= mpipi(Math.sin(x)+Math.cos(y)-Math.cos(x)-Math.sin(y));
			Random randomizador=new Random();
			devolver = 3;
		}
		return devolver;
	}
	
	//casillas de 100m*100m
	private static double dameCorrienteCasilla(double x,double y){
		//int numCasilla = 0;
		double corriente=0;
		int casillaX = (int)(x/10)+1;
		int casillaY = (int)(y/10)+1;
		corriente = (3*casillaX);
		return corriente;
	}
	
	private static double mpipi(double valor){
		if(valor<-Math.PI){valor=mpipi(valor+2*Math.PI);}
		if(valor>Math.PI){valor=mpipi(valor-2*Math.PI);}
		return valor;
	}
	
	public static void cambiaPuntoCambioCorriente(double minimo, double maximo){
		Random randomizador=new Random();
		FuncionCorriente.variable = randomizador.nextDouble()*maximo-minimo;
		
		
	}

}
