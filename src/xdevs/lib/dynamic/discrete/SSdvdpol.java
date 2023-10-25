package xdevs.lib.dynamic.discrete;

import xdevs.lib.dynamic.IDynSys;
import xdevs.lib.dynamic.Sizes;

/**
 * Ecuacion de Van der Pol discreta
 * 
 * Ejemplo de modelo no lineal discreto
 * 
 * @author J.M. de la Cruz, May 14th, 2008
 * @version 1.0
 *
 */
public class SSdvdpol implements IDynSys{
	// Parameters
	double mm = 1.0;
	double cc = 0.5;
	double kk = 1.0;
	
	private int n;			// state dimension
	private int m;			// input dimension
	private int p;			// output dimension
			
	private Double[] x; 		// State vector
	private Double[] y;     // Output vector signal

	private double h = 1;   // Samplig period. Default = 1
	private int k = 0;      // Number of samplings made
	    
	
	    public SSdvdpol(Double[] x0, double h){     
	        n = 2;
	        m = 1;			
	        p = 1;				
	        x = new Double[n];
	        y = new Double[p];
	        this.h = h;
	        x = x0;
	        y[0] = 0.0;            
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
	     * updates the number of samplings nor the state vector
	     * @param dt: time increment
	     * @param xt: actual state value x(tk)
	     * @param ut: actual input value u(tk), tk<= t < tk+dt */
	    public Double[] update(double dt, Double[] xt, Double[] ut){
	    	Double[] xx = new Double[xt.length];
	    	xx = fxut(dt,xt,ut);
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
	    	Double u = xt[1];
	        Double v = (1/mm) * (-kk*xt[0] + 2*cc*(1-xt[0]*xt[0])*xt[1]);
	        xx[0] = xt[0] + dt * u;
	        xx[1] = xt[1] + dt * v;
	        return xx;
	    }
	    
	    /** calculates the system output y(tk) 
	     * but do not update it internally
	     * @param tk:  actual time instant 
	     * @param xt: state value x(tk)
	     * @param ut: input signal u(tk) */
	    public Double[] gxut(double tk, Double[] xt, Double[] ut){
	    	Double[] yy = new Double[1];
	    	yy[0] = xt[0];
	        y = yy;
	        return yy;
	    }

}