package testing.lib.atomic.dynamic.discrete;

import testing.lib.atomic.dynamic.*;

/**
 * Template of State Space Disctete Non Linear Model
 * 
 * @author J.M. de la Cruz, May 14 th, 2008
 * @version 1.0
 *
 */
public abstract class SSdnlsys implements IDynSys{
	/**
	 * TO DO
	 * Here Parameters of the model
	 */ 
	    
	private int n;			// state dimension
	private int m;			// input dimension
	private int p;			// output dimension
			
	private Double[] x; 		// State vector
	private Double[] y;     // Output vector signal

	private double h = 1;   // Samplig period. Default = 1
	private int k = 0;      // Number of samplings made
	    
	/** 
	 * Default creator x(k+1) = u(k), x0 = 0, 
	 * y(k) = x(k)
	 *    
	 */
	    public SSdnlsys(){     
	        n = 1;
	        m = 1;			
	        p = 1;				
	        x[0] = 0.0;
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
	     * do no update the number of samplings nor the state vector
	     * @param dt: time increment
	     * @param xt: actual state value x(tk)
	     * @param ut: actual input value u(tk), tk<= t < tk+dt */
	    public Double[] update(double dt, Double[] xt, Double[] ut){
	    	Double[] xx = new Double[n];
	        xx = fxut(getTime(),xt,ut);
	        return xx;
	    }
	    
	    /** calculates the next state vector 
	     * @param t:    present time instant
	     * @param xt:   present state value
	     * @param ut:   present input signal value
	     * @return x(t+dt)  */
	    public Double[] fxut(double dt, Double[] xt, Double[] ut){
	    	Double[] xx = new Double[xt.length];
	        xx = ut;
	        return xx;
	    }
	    
	    /** calculates the system output y(tk) 
	     * and update it internally
	     * @param tk:  actual time instant 
	     * @param xt: state value x(tk)
	     * @param ut: input signal u(tk) */
	    public Double[] gxut(double tk, Double[] xt, Double[] ut){
	        Double[] yy = new Double[xt.length];
	    	yy = xt;
	    	y = yy;
	        return yy;
	    }

}
