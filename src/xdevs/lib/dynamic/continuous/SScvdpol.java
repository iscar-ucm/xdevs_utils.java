package xdevs.lib.dynamic.continuous;

import xdevs.lib.dynamic.IDynSys;
import xdevs.lib.dynamic.IntegratorRK4;
import xdevs.lib.dynamic.Sizes;

/**
 *  Example of continuous dynamic model
 *  
 *  Atomic Continuous Model of Van der Pol Equation
 *  
 * @author J.M. de la Cruz
 * @version 1.0,	May 16th 2008
 */

public class SScvdpol implements IDynSys{
	// Parameters
	double mm = 1.0;
	double cc = 0.5;
	double kk = 1.0;

	private int n;			// state dimension
	private int m;			// input dimension
	private int p;			// output dimension
			
	private Double[] x; 		// State vector
	private Double[] y;     // Output vector signal

	final double DEFAULT_H = 0.01;
    private double h = DEFAULT_H; // Integration period. 
    private int k = 0;      // Number of integrations made
	    
	
	    public SScvdpol(Double[] x0, double h){     
	        n = 2;
	        m = 0;			
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
	        sizes.Nxc = n;
	        sizes.Nu = m;
	        sizes.Ny = p;
	        return sizes;
	    }
	    
	    /** gets the integration or sampling period */
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
	    	Double u = xt[1];
	        Double v = (1/mm) * (-kk*x[0] + 2*cc*(1-x[0]*x[0])*x[1]);
	        dx[0] = u;
	        dx[1] =  v;
	        return dx;
	    }
	    
	    /** calculates the system output y(tk) 
	     * and updates it internally
	     * @param tk:  actual time instant 
	     * @param xt: state value x(tk)
	     * @param ut: input signal u(tk) */
	    public Double[] gxut(double tk, Double[] xt, Double[] ut){
	        Double[] yy = new Double[p];
	        yy[0] = xt[0];
	        y = yy.clone();
	        return yy;
	    } 

}


