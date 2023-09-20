package xdevs.lib.dynamic;
/**
 * IDynSys.java
 *
 * Interface to model Dynamic Systems 
 *
 * @author Jesús M. de la Cruz, may 16th 2008
 * @version 1.0
 *
 */
public interface IDynSys {
    
    /** gets the state value of the model without changing the state */
    public Double[] getState() ;
    /** gets the actual output of the model */
    public Double[] getOutput();
    /** gets the dimension of the state */
    public int getNx() ;
    /** gets the dimension of the input */
    public int getNu() ;
    /** gets the dimension of the output */
    public int getNy();
    /** gets the sizes of the system */
    public Sizes getSizes();
    /** gets the time of the actual state value: t */
    public double getTime() ;
    /** gets the integration time or the sampling period */
    public double getSampling();
    /** makes the system evolve to a new state: x(t+dt)
     * @param dt: time increment
     * @param xt: actual state value x(t)
     * @param ut: actual input value u(t) 
     * @return x(t+dt) */
    public Double[] update(double dt, Double[] xt, Double[] ut);
    /** calculates the derivative of the state vector 
     * @param t:    present time instant
     * @param xt:   present state value
     * @param ut:   present input signal value
     */
    public Double[] fxut(double t, Double[] xt, Double[] ut);
    /** calculates the system output y(t) 
     * @param t:  present time instant 
     * @param xt: state value x(t)
     * @param ut: input signal u(t) 
     * @return y(t) */
    public Double[] gxut(double t, Double[] xt, Double[] ut); 
    
}
