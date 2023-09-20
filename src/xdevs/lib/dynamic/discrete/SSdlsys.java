package testing.lib.atomic.dynamic.discrete;

import testing.lib.atomic.dynamic.*;

/**
 * State Space discrete linear system
 * x(k+1) = F x(k) + G u(k)
 * y(k) = C x(k) + D u(k)
 *  
 * @author J.M. de la Cruz, May 14th, 2008
 * @version 1.0
 */
public class SSdlsys implements IDynSys{
    
    private Double[][] F ;	// State matrix
    private Double[][] G ; 	// Input matrix
    private Double[][] C ;	// Output state matrix
    private Double[][] D ;  // Output input matrix
    
    private int n;			// state dimension
    private int m;			// input dimension
    private int p;			// output dimension
		
    private Double[] x; 		// State vector
    private Double[] y;     // Output vector signal
    
    private static final int DEFAULT_H = 1;
    private double h = DEFAULT_H;   // Samplig period. 
    private int k = 0;      // Number of samplings made
    
    /** 
     * Default creator F=0, G=1, C=1, D=0, x0=0.0
     * Unit delay sistem   
     */
    public SSdlsys(){
    	F[0][0] = 0.0;	
        G[0][0] = 1.0;	
        C[0][0] = 1.0;	
        D[0][0] = 0.0;     
        n = F.length;
        m = G[0].length;
        p = C.length;				
        x[0] = 0.0;
        y[0] = 0.0;            
    }				
    
    /**
     * x(k+1) = F x(k) + G u(k)
     * y(k) = C x(k) + D u(k)
     * x0 = 0;
     * @param F    state matrix Double[][]
     * @param G    Input state matrix Double[][]
     * @param C    Output state matrix Double[][]
     * @param D    Output Input matrix Double[][]
     */
    public SSdlsys(Double[][] F, Double[][] G, Double[][] C, Double[][] D){
        this.F = F;
        this.G = G;
        this.C = C;
        this.D = D;
        n = F.length;
        m = G[0].length;
        p = C.length;
        x = new Double[n];
        y = new Double[p];
        
    }
    
    /**
     * x(k+1) = F x(k) + G u(k)
     * y(k) = C x(k) + D u(k)
     * @param x0   initial state value
     */
    public SSdlsys(Double[][] F, Double[][] G, Double[][] C, Double[][] D, Double[] x0){
        this(F,G,C,D);
        for (int i=0; i<n; i++) x[i] = x0[i];
    }
    
    /**
     * x(k+1) = F x(k) + G u(k)
     * y(k) = C x(k) + D u(k)
     * @param h   sampling period
     */
    public SSdlsys(Double[][] F, Double[][] G, Double[][] C, Double[][] D, 
            Double[] x0, double h){
        this(F,G,C,D,x0);
        this.h = (Math.abs(h)>0) ? Math.abs(h) : 1;
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
        sizes.Nxd = n;
        sizes.Nu = m;
        sizes.Ny = p;
        return sizes;
    }
    
    /** gets the sampling period */
    public double getSampling() {return h;}
    
    /** gets the time of the actual state value: tk */
    public double getTime() {return k*h;}
       
    /** makes the system evolve to a new state: x(tk+dt)
     * update the state vector and the number of samplings  
     * @param dt: time increment
     * @param xt: actual state value x(tk)
     * @param ut: actual input value u(tk), tk<= t < tk+dt */
    public Double[] update(double dt, Double[] xt, Double[] ut){
    	Double[] xx = new Double[n];
        xx = fxut(k*h,xt,ut);
        x = xx.clone();
        k = k + 1;
        return xx;
    }
    
    /** calculates the next state vector 
     * @param t:    present time instant
     * @param xt:   present state value
     * @param ut:   present input signal value
     * @return x(t+dt)  */
    public Double[] fxut(double dt, Double[] xt, Double[] ut){
    	Double[] xx = new Double[xt.length];
        for (int i=0; i < n; i++){
        	xx[i] = 0.0; 
            for (int j=0; j < n; j++)
            	xx[i] = xx[i] + F[i][j] * xt[j];
            for (int j=0; j < m; j++)
                xx[i] = xx[i] + G[i][j] * ut[j];
           }
        return xx;
    }
 
    /** calculates the system output y(tk) 
     * and update it internally
     * @param tk:  actual time instant 
     * @param xt: state value x(tk)
     * @param ut: input signal u(tk) */
    public Double[] gxut(double tk, Double[] xt, Double[] ut){
        Double[] yy = new Double[p];
        for (int i=0; i < p; i++){
            yy[i] = 0.0; 
            for (int j=0; j < n; j++)
                yy[i] = yy[i] + C[i][j] * x[j];
            for (int j=0; j < m; j++)
                yy[i] = yy[i] + D[i][j] * ut[j];
        }
        y = yy.clone();
        return yy;
    } 
    
}
