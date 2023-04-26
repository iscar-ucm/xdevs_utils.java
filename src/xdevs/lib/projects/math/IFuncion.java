package xdevs.lib.projects.math;

public interface IFuncion {
	public double[] dameDerivadas(double tiempo,double[] estados,double[] control);
	public double[] dameEstadoActual();
	public double[] dameControlActual();
	public void actualizaEstados(double[] estadosActuales);
	public void avanzaTiempo();
}
