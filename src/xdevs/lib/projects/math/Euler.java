package xdevs.lib.projects.math;

public class Euler implements IIntegrador{


	public double[] integra(IFuncion funcion, double DT,
			double TT) {
		double[]XX =funcion.dameEstadoActual();
		double[]U = funcion.dameControlActual();
		int NX = XX.length;
		
		//double T,Q;
		double[] X = new double[NX];
		double[] XD = new double[NX];
		XD=funcion.dameDerivadas(TT,XX,U);
		
        for (int i = 0; i < NX; i++) {
            X[i] = XX[i] + XD[i] * DT;
        }
		
		return X;
	}

}
