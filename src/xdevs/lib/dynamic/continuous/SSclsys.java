package xdevs.lib.dynamic.continuous;

import xdevs.lib.dynamic.IDynSys;
import xdevs.lib.dynamic.IntegratorRK4;
import xdevs.lib.dynamic.Sizes;

/**
 * State Space discrete linear system
 * dx(t) = F x(t) + G u(t)
 * y(t) = C x(t) + D u(t)
 *  
 * @author J.M. de la Cruz
 * @version 1.0,	May 14th 2008
 */
public class SSclsys implements IDynSys {
    
    private Double[][] A ;	// State matrix
    private Double[][] B ; 	// Input matrix
    private Double[][] C ;	// Output state matrix
    private Double[][] D ;  // Output input matrix
    
    private int n;			// state dimension
    private int m;			// input dimension
    private int p;			// output dimension
		
    private Double[] x; 	// State vector
    private Double[] y;     // Output vector signal
    final double DEFAULT_H = 0.01;
    private double h = DEFAULT_H; // Integration period. 
    private int k = 0;      // Number of integrations made
    
    /** 
     * Default creator A=0, B=1, C=1, D=0, x0=0.0
     * Integrator   
     */
    public SSclsys(){
    	A[0][0] = 0.0;	
        B[0][0] = 1.0;	
        C[0][0] = 1.0;	
        D[0][0] = 0.0;     
        n = A.length;
        m = B[0].length;
        p = C.length;				
        x[0] = 0.0;
        y[0] = 0.0;            
    }				
    
    /**
     * x0 = 0;
     * @param A    state matrix Double[][]
     * @param B    Input state matrix Double[][]
     * @param C    Output state matrix Double[][]
     * @param D    Output Input matrix Double[][]
     */
    public SSclsys(Double[][] A, Double[][] B, Double[][] C, Double[][] D){
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        n = A.length;
        m = B[0].length;
        p = C.length;
        x = new Double[n];
        y = new Double[p];
        
    }
    
    /**
     * @param x0   initial state value
     */
    public SSclsys(Double[][] A, Double[][] B, Double[][] C, Double[][] D, Double[] x0){
        this(A,B,C,D);
        for (int i=0; i<n; i++) x[i] = x0[i];
    }
    
    /**
     * @param h   integrating period
     */
    public SSclsys(Double[][] A, Double[][] B, Double[][] C, Double[][] D, 
            Double[] x0, double h){
        this(A,B,C,D,x0);
        this.h = (Math.abs(h)>0) ? Math.abs(h) : 0.1;
    }
    
    /** gets the state value of the model without changing the state */
    public Double[] getState() {return x;}
    
    /** gets the actual output of the model */
    public Double[] getOutput() {return y;}
    
    /** gets the dimension of the state */
    public int getNx() {return n;}
    
    /** gets the dimension of the input */
    public int getNu() {return m;}
    
    /** gets the dimension of the output */
    public int getNy(){return p;}
    
    /** gets the sizes of the system */
    public Sizes getSizes(){
        Sizes sizes = new Sizes();
        sizes.Nxc = n;
        sizes.Nu = m;
        sizes.Ny = p;
        return sizes;
    }
    
    /** gets the integrating period */
    public double getSampling() {return h;}
    
    /** gets the time of the actual state value: t */
    public double getTime() {return k*h;}
  
    /** makes the system evolve to a new state: x(tk+dt)
     * updates the state and the number of integrating steps
     * @param dt: time increment
     * @param xt: actual state value x(tk)
     * @param ut: actual input value u(tk), tk<= t < tk+dt */
    public Double[] update(double dt, Double[] xt, Double[] ut){
        Double[] xx = new Double[xt.length];
        xx =  IntegratorRK4.rk4(this, k*h, h, xt, ut, xt.length);
        x = xx.clone();
        k = k + 1;
        return xx;
    }
    
    /** calculates the derivative of the state vector 
     * @param t:    present time instant
     * @param xt:   present state value
     * @param ut:   present input signal value
     * @return dxt: derivative of the state signal  */
    public Double[] fxut(double dt, Double[] xt, Double[] ut){
    	Double[] dx = new Double[xt.length];
        for (int i=0; i < n; i++){
        	dx[i] = 0.0; 
            for (int j=0; j < n; j++)
            	dx[i] = dx[i] + A[i][j] * xt[j];
            for (int j=0; j < m; j++)
                dx[i] = dx[i] + B[i][j] * ut[j];
           }
        return dx;
    }
    
    /** calculates the system output y(tk) 
     * and updates it internally
     * @param tk:  actual time instant 
     * @param xt: state value x(tk)
     * @param ut: input signal u(tk) */
    public Double[] gxut(double tk, Double[] xt, Double[] ut){
        Double[] yy = new Double[p];
        for (int i=0; i < p; i++){
            yy[i] = 0.0; 
            for (int j=0; j < n; j++)
                yy[i] = yy[i] + C[i][j] * xt[j];
            for (int j=0; j < m; j++)
                yy[i] = yy[i] + D[i][j] * ut[j];
        }
        y = yy.clone();
        return yy;
    } 
    
}
