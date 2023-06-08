package xdevs.lib.projects.barcos;

import java.util.Random;

import xdevs.lib.projects.math.IFuncion;
import xdevs.lib.projects.math.IIntegrador;
import xdevs.lib.projects.math.RungeKutta;

public class Mar implements IFuncion{
	private double gan_espectro_olas;
	private double frec_pico_espectro;
	private double amortig_relativo;
	private double tiempo_integracion;
	private double tiempo_actual;
	private double dw;
	private IIntegrador integrador;
	private Random randomizador;
	private double y;
	private double dy;
	private double w;
	
	
	
	public Mar(){
		randomizador=new Random();
		gan_espectro_olas=	0.5;
		frec_pico_espectro=	1.2;
		amortig_relativo=	0.1;
		tiempo_integracion=	1;
		tiempo_actual = 0;
		integrador=new RungeKutta();
		y=0;
		dy=0;
		dw=0;
	}
	
	public double[] dameDerivadas(double tiempo,double[] estados,double[] control){
		double[] derivadas = new double[3];
		derivadas[0]=estados[1];
		derivadas[1]=3*2*amortig_relativo*frec_pico_espectro*gan_espectro_olas*w -
		2*amortig_relativo*frec_pico_espectro*estados[1] - 
		frec_pico_espectro*frec_pico_espectro*estados[0]+dw;
		derivadas[2]=randomizador.nextGaussian();
		return derivadas;
	}
	public double[] dameEstadoActual(){
		double[] estado = new double[3];
		estado[0]=y;
		estado[1]=dy;
		estado[2]=dw;
		return estado;
	}
	
	public double[] dameSalidaActual(){
		double[] salida = new double[1];
		salida[0] = Math.PI*y/180;
		return salida;
	}
	
	public double[] dameControlActual(){
		return null;
	}
	
	public void avanzaTiempo(){
		
		w=randomizador.nextGaussian();
		this.actualizaEstados(integrador.integra(this, this.tiempo_integracion,tiempo_actual));
		tiempo_actual+=tiempo_integracion;
	}
	
	public void actualizaEstados(double[] estados) {
		this.y=estados[0];
		this.dy=estados[1];
		this.dw=estados[2];
	}
	
	public void imprimeEstados(){
		System.out.println(" y "+ y );
		System.out.println(" dy "+ dy );
		System.out.println(" dw "+ dw );
	}
	
	public static void main(String args[]){
		Mar mar= new Mar();
		for(int cont=0;cont<100;cont++){
			mar.imprimeEstados();
			mar.avanzaTiempo();
		}
		
		
		
	}
}
