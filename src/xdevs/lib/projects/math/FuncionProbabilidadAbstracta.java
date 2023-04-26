package xdevs.lib.projects.math;

public abstract class FuncionProbabilidadAbstracta {
	protected double media;
	protected double varianza;
	protected double num_datos;
	public void NuevoValor(boolean bueno_malo,double valor){}
	public double evalua(double x){return -1;}
	public double dameMedia(){return media;}
	public double dameVarianza(){return varianza;}
}
