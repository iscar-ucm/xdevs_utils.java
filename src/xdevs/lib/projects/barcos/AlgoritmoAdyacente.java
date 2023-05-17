package ssii2007.simulacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;

import ssii2007.incidencias.ProbabilidadNaufrago;
import ssii2007.incidencias.ProbabilidadNaufragoCasillas;
import ssii2007.matematico.FuncionProbabilidadAbstracta;

public class AlgoritmoAdyacente implements Algoritmo{
	public static final int numPuntos = 20;
	public static final int distancia = 2000;
	public static final int ka = 500;
	public static final int kb = 5000;
	public static ArrayList<Punto> recorridos = new ArrayList<Punto>();
	public static final double peso = 100;

	
	public Ruta dameRuta(int tipoVehiculo,Punto posicion_actual,Punto punto_inicial,int tiempo,	FuncionProbabilidadAbstracta probabilidadAngulo,
	FuncionProbabilidadAbstracta probabilidadVelocidad,ProbabilidadNaufragoCasillas Cprobabilidad){
		Ruta ruta = new Ruta();
		int k=0;
		if (tipoVehiculo==1){k=ka;}
		else{k=kb;}
		//recorridos=new ArrayList<Punto>();
		Random randomizador =new Random();
		Punto posicion_actualizada = posicion_actual;
		recorridos.add(new Punto(0,0,0));
		for(int cont=0;cont<numPuntos;cont++){
			posicion_actualizada =dameMovimiento(randomizador.nextDouble(),posicion_actualizada,punto_inicial,tiempo+k*cont,
					probabilidadAngulo,probabilidadVelocidad);
			ruta.añadePunto(posicion_actualizada);
			
		}
		return ruta;
	}
	
	public static Punto dameMovimiento(double randomizado,Punto posicion_actual,Punto punto_inicial,int tiempo,FuncionProbabilidadAbstracta probabilidadAngulo,
			FuncionProbabilidadAbstracta probabilidadVelocidad){
		Random randomizador=new Random();
		int distancia_local = randomizador.nextInt(10)*1000+distancia;
		
		Punto[] movimientos=new Punto[4];
		double[] num_repeticiones=new double[4];
		Arrays.fill(num_repeticiones, 1);
		for(int cont=0;cont<recorridos.size();cont++){
			if((recorridos.get(cont).getE()>=posicion_actual.getE())&&
				((recorridos.get(cont).getE()<posicion_actual.getE()+distancia_local))&&
				((recorridos.get(cont).getN()>=posicion_actual.getN()+distancia_local))&&
				((recorridos.get(cont).getN()<posicion_actual.getN()+2*distancia_local))){
				System.out.println("entra 0");
				num_repeticiones[0]*=peso;}
			
			if((recorridos.get(cont).getE()>=posicion_actual.getE()+distancia_local)&&
					((recorridos.get(cont).getE()<posicion_actual.getE()+2*distancia_local))&&
					((recorridos.get(cont).getN()>=posicion_actual.getN()))&&
					((recorridos.get(cont).getN()<posicion_actual.getN()+distancia_local))){System.out.println("entra 1");num_repeticiones[1]*=peso;}
			
			if((recorridos.get(cont).getE()>=posicion_actual.getE()+distancia_local)&&
					((recorridos.get(cont).getE()<posicion_actual.getE()))&&
					((recorridos.get(cont).getN()<=posicion_actual.getN()-distancia_local))&&
					((recorridos.get(cont).getN()>posicion_actual.getN()-2*distancia_local))){System.out.println("entra 2");num_repeticiones[2]*=peso;}
			
			if((recorridos.get(cont).getE()<=posicion_actual.getE()-distancia_local)&&
					((recorridos.get(cont).getE()>posicion_actual.getE()-2*distancia_local))&&
					((recorridos.get(cont).getN()>=posicion_actual.getN()))&&
					((recorridos.get(cont).getN()<posicion_actual.getN()+distancia_local))){System.out.println("entra 3");num_repeticiones[3]*=peso;}
		}
		movimientos[0]=new Punto(posicion_actual.getN()+distancia_local, posicion_actual.getE(),0); //inc n
		movimientos[1]=new Punto(posicion_actual.getN(), posicion_actual.getE()+distancia_local,0); //inc e
	//	movimientos[2]=new Punto(posicion_actual.getN()+Math.sqrt(distancia), posicion_actual.getE()+Math.sqrt(distancia),0); //inc ne
		movimientos[2]=new Punto(posicion_actual.getN()-distancia_local, posicion_actual.getE(),0);
		movimientos[3]=new Punto(posicion_actual.getN(), posicion_actual.getE()-distancia_local,0);
		
	//	movimientos[5]=new Punto(posicion_actual.getN()-Math.sqrt(distancia), posicion_actual.getE()-Math.sqrt(distancia),0);
	//	movimientos[6]=new Punto(posicion_actual.getN()-Math.sqrt(distancia), posicion_actual.getE()+Math.sqrt(distancia),0);
	//	movimientos[7]=new Punto(posicion_actual.getN()+Math.sqrt(distancia), posicion_actual.getE()-Math.sqrt(distancia),0);
		double[]probabilidad=new double[4];
		Vector<Double> vector= new Vector<Double>();
		for(int cont=0;cont<4;cont++){
			probabilidad[cont]=valoracion(movimientos[cont],punto_inicial,probabilidadAngulo,probabilidadVelocidad,tiempo)/num_repeticiones[cont];
			vector.add(probabilidad[cont]);
		}
		double[] copiap = Arrays.copyOfRange(probabilidad, 0, probabilidad.length);
		Arrays.sort(copiap);
		//System.out.println("array"+Arrays.toString(copiap));
		
		double suma=0;
		double factor =0.3;
		int cont=-1;
		while((suma<randomizado)&&(cont<3)){
			cont++;
			suma=factor + suma;
			factor = factor/1.5;
		}
		
		double valor = copiap[(copiap.length-1)-cont];
		
		
		int devolver=0;
		int cont3=0;
		//System.out.println("valor"+valor);
		while(valor!=probabilidad[cont3]){
			cont3++;
		}

		if(valor==probabilidad[cont3]){devolver = cont3;}
		//en cont tenemos el numero q queremos...
		
		Punto puntodevolver = movimientos[devolver];
		if(devolver==0){recorridos.add(new Punto(posicion_actual.getN()+distancia,posicion_actual.getE(),0));System.out.println(num_repeticiones[0]);}
		if(devolver==1){recorridos.add(new Punto(posicion_actual.getN(),posicion_actual.getE()+distancia,0));System.out.println(num_repeticiones[1]);}
		if(devolver==2){recorridos.add(new Punto(posicion_actual.getN()-distancia,posicion_actual.getE(),0));System.out.println(num_repeticiones[2]);}
		if(devolver==3){recorridos.add(new Punto(posicion_actual.getN(),posicion_actual.getE()-distancia,0));System.out.println(num_repeticiones[3]);}

		if((copiap[copiap.length-1])<0.000001){
			double ang_per = probabilidadAngulo.dameMedia();
			double vel_per = probabilidadVelocidad.dameMedia();
			puntodevolver=new Punto(punto_inicial.getN()-Math.sin(ang_per)*vel_per*tiempo,
					punto_inicial.getE()-Math.cos(ang_per)*vel_per*tiempo,
					0);
			
		}
		return puntodevolver;
	}
	
	public static double distancia(Punto p1,Punto p2){
		double de=Math.pow(p1.getE()-p2.getE(),2);
		double dn=Math.pow(p1.getN()-p2.getN(),2);
		return Math.sqrt(dn+de);
	}
	
	public static double valoracion(Punto punto,Punto puntoInicial,FuncionProbabilidadAbstracta probabilidadAngulo,
			FuncionProbabilidadAbstracta probabilidadVelocidad,int tiempo){
		double angulo = Math.atan2(puntoInicial.getN()-punto.getN(),puntoInicial.getE()-punto.getE());
		double distancia = distancia(puntoInicial,punto);
		double probabilidadA=probabilidadAngulo.evalua(angulo);
		double probabilidadV=probabilidadVelocidad.evalua(distancia/tiempo);
		return probabilidadA*probabilidadV;
	}
}
