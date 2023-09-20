package xdevs.lib.dynamic;
/**
 * Dynamic Systems Integrator
 * @author Jesús M. de la Cruz, May 16th 2008
 * @version 1.0
 *
 */

public class IntegratorRK4 {
    
    /** Runge Kuta order 4th integrator
     * @param Fun: object implementing the interface IDynSys needed
     *             to calculate the system state derivative dx=f(t,x,u) 
     *             
     * @param TT: actual time
     * @param DT: integration interval [TT, TT+DT]
     * @param XX: actual
     * @param  U: control en el intervalo DT
     * @param NX: no de variables de estado
     * @return X(TT+DT)
     */
    public static Double[] rk4(IDynSys Fun, double TT, double DT, Double[] XX, Double[] U, int NX) {  
        double T, Q;
        Double[] X = new Double[NX];
        Double[] XA = new Double[NX];
        Double[] XD = new Double[NX];
        
        XD = Fun.fxut(TT, XX, U);
        
        for (int i = 0; i < NX; i++) {
            XA[i] = XD[i] * DT;
            X[i] = XX[i] + 0.5 * XA[i];
        }
        T = TT + 0.5 * DT;
        XD = Fun.fxut(T,X,U);
        
        for (int i =0; i < NX; i++){
            Q = XD[i] * DT;
            X[i] = XX[i] + 0.5 * Q;
            XA[i] = XA[i] + Q + Q;
        }
        XD = Fun.fxut(T,X,U);
        for (int i =0; i < NX; i++){
            Q = XD[i] * DT;
            X[i] = XX[i] + Q;
            XA[i] = XA[i] + Q + Q;
        }
        T = TT + DT;
        XD = Fun.fxut(T,X,U);
        for (int i =0; i < NX; i++){
            X[i] = XX[i] + (XA[i] + XD[i] * DT)/6;
        }
        return X;
    }
    
}
