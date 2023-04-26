package xdevs.lib.projects.math;

public class RungeKutta implements IIntegrador{

	public double[] integra(IFuncion funcion, double DT,double TT) {
		// TODO Auto-generated method stub
		double[]XX =funcion.dameEstadoActual();
		double[]U = funcion.dameControlActual();
		int NX = XX.length;
		
		double T,Q;
		double[] X = new double[NX];
		double[] XA = new double[NX];
		double[] XD = new double[NX];
		XD=funcion.dameDerivadas(TT,XX,U);
		
        for (int i = 0; i < NX; i++) {
            XA[i] = XD[i] * DT;
            X[i] = XX[i] + 0.5 * XA[i];
        }
        T = TT + 0.5 * DT;
        
        XD = funcion.dameDerivadas(T,X,U);
        
        for (int i =0; i < NX; i++){
            Q = XD[i] * DT;
            X[i] = XX[i] + 0.5 * Q;
            XA[i] = XA[i] + Q + Q;
        }
        XD = funcion.dameDerivadas(T,X,U);
        for (int i =0; i < NX; i++){
            Q = XD[i] * DT;
            X[i] = XX[i] + Q;
            XA[i] = XA[i] + Q + Q;
        }
        T = TT + DT;
        XD = funcion.dameDerivadas(T,X,U);
        for (int i =0; i < NX; i++){
            X[i] = XX[i] + (XA[i] + XD[i] * DT)/6;
        }
        return X;
	}

}
