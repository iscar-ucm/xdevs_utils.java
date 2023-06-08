package xdevs.lib.projects.barcos;

import java.util.Random;

import xdevs.lib.projects.math.IFuncion;
import xdevs.lib.projects.math.IIntegrador;
import xdevs.lib.projects.math.RungeKutta;

public class Corrientes implements IFuncion{

	private double velocidad_actual;
	private FuncionCorriente funcion;
	private IIntegrador integrador;
	private double tiempo_actual=0;
	private double tiempo_integracion;
	private double mu;
	private double x;
	private double y;
	private double oleaje;
	
	public Corrientes(double mu,double tiempo_integracion){
		integrador=new RungeKutta();
		funcion=new FuncionCorriente();
		this.mu=mu;
		this.tiempo_integracion=tiempo_integracion;
	}
	
	public void actualizaEstados(double[] estadosActuales) {
		// TODO Auto-generated method stub
		if(estadosActuales[0]<0.5){
			velocidad_actual=0.5;
		}
		else
		if(estadosActuales[0]>3){
			velocidad_actual=3;
		}
		else{
			velocidad_actual=estadosActuales[0];
		}
		
	}

	public void ponOleaje(double[] ds){
		oleaje=ds[0];
	}
	
	public void avanzaTiempo(double tiempo){
		this.tiempo_integracion=tiempo;
		avanzaTiempo();
	}
	public void avanzaTiempo() {
		// TODO Auto-generated method stub	
		this.actualizaEstados(integrador.integra(this, this.tiempo_integracion,tiempo_actual));
		tiempo_actual+=tiempo_integracion;
		
	}

	public double[] dameControlActual() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public double dameUc(){
		return(
				velocidad_actual*(Math.cos(FuncionCorriente.dameCorriente(FuncionCorriente.variable,x,y)+oleaje)));
	}
	
	public double dameVc(){
		return(velocidad_actual*(Math.sin(FuncionCorriente.dameCorriente(FuncionCorriente.variable,x,y)+oleaje)));
	}

	public double[] dameDerivadas(double tiempo, double[] estados,
			double[] control) {
		// TODO Auto-generated method stub
		double[] derivadas=new double[1];
		Random randomizador=new Random();
		//derivadas[0]= randomizador.nextGaussian()-0.1*velocidad_actual;
		//System.out.println("derivada "+derivadas[0]);
		derivadas[0]= randomizador.nextGaussian()-mu*velocidad_actual;
		return derivadas;
	}

	public double[] dameEstadoActual() {
		// TODO Auto-generated method stub
		double[] estados=new double[1];
		estados[0] = velocidad_actual;
		return estados;
	}

}
