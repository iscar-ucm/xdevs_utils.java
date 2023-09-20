package xdevs.lib.dynamic.continuous;

import java.util.logging.Level;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;
import xdevs.lib.dynamic.IDynSys;

/**
 *  MM_SScsys: 
 *  	Atomic Moore & Mealy State Space Continuous 
 *      Linear - NonLinear System Model      
 *       
 *  ¡This model can not be used with self feeback through   
 *  othe Mealy type models, since it reacts instantaneously 
 *  at input events. So, if there is a feedback throug Mealy
 *  model it falls into an infinity loop!
 *  
 *  It has the advantege over Mealy models that 
 *  do not need an external clock to work
 *             
 *  Uses a IDynSys model
 *  
 *  dx(t) = f(t,x(t),u(t))
 *  y(t) = g(t,x(t))
 *  
 *  @author J.M. de la Cruz 
 *  @version 1.0, May 19th 2008 Integrates till either new entry or dint
 */
public class MM_SScsys extends Atomic {

    private static Logger logger = Logger.getLogger(MEALY_SScsys.class.getName());
//	 Ports
    public static final String outName = "out";		// Output of the system
    public static final String outxName = "outx"; 	// Output of the state. May not be used
    public static final String inName = "in";
    protected Port<Object> in;
    protected Port<Double[]> out;
    protected Port<Double[]> outx;
    /** Parameters that characterizes the model */
    private IDynSys mymodel;	// Discrete dynamic model
    private Double[] x;					// State vector
    private Double[] u;					// Input vector
    private Double[] uold;				// Previous input vector
    private Double[] y;					// Output vector
    private double dt;				// present integration time
    private String utype;		    // {"array","Double"," "} Type of the input signal
    //		 Debugging
    private double tiempo = 0;	// Internal time

    /** Creates a causal continuos system in state space.
     *  @param name of the model
     *  @param model object of type IDynSys
     */
    public MM_SScsys(String name, IDynSys model) {
        super(name);
        in = new Port<Object>(inName);
        out = new Port<Double[]>(outName);
        outx = new Port<Double[]>(outxName);
        addInPort(in);
        addOutPort(out);
        addOutPort(outx);
        mymodel = model;
        x = new Double[mymodel.getNx()];
        x = mymodel.getState();
    }

    @Override
    public void initialize() {
        if (mymodel.getNu() == 0) {		// autonomous system
            super.holdIn("initial", 0);
            u = new Double[1];
            uold = new Double[1];
            u[0] = 0.0;
            uold[0] = 0.0;
        } else {
            super.holdIn("initial", Constants.INFINITY);  // Waiting for the first input
            u = new Double[mymodel.getNu()];
            uold = new Double[mymodel.getNu()];
        }
    }

    public void deltext(double e) {
        super.resume(e);
        Object uu;
        uu = in.getSingleValue();
        if (super.phaseIs("initial")) {
            utype = getTypeUu(uu); // If Phase == initial we see the kind of input
            getSignalU(utype, uu);
            uold = u.clone();
            super.holdIn("initial", 0);
        } else {
            uold = u.clone();
            getSignalU(utype, uu);
            super.holdIn("new", 0);
        }
        dt = e;
        if (logger.isLoggable(Level.INFO)) {
            logger.info("MOORE_SScsys deltext t: " + (tiempo + e) + ", " + super.getPhase());
            for (int i = 0; i < u.length; i++) {
                logger.info("u(" + i + "): " + u[i]);
                logger.info("u0(" + i + "): " + uold[i]);
            }
        }

    }

    public void deltint() {
        super.holdIn("old", mymodel.getSampling());
        tiempo = tiempo + dt;
        dt = mymodel.getSampling();
        tiempo = tiempo + dt;

        if (logger.isLoggable(Level.INFO)) {
            logger.info("MOORE_SScsys deltin  tnext: " + tiempo);
            for (int i = 0; i < x.length; i++) {
                logger.info(" x(" + (i + 1) + "): " + x[i]);
            }
        }
    }

    public void lambda() {
        if (super.phaseIs("initial")) {
            y = mymodel.gxut(dt, x, uold);		// calculates output
            super.holdIn("new", super.getSigma());
        } else {
            double h;
            if (super.phaseIs("new")) // output due to new input
            {
                h = dt;
            } else {
                h = mymodel.getSampling();
            }
            x = mymodel.update(h, x, uold);		// update state
            y = mymodel.gxut(dt, x, uold);		// calculates output
            uold = u.clone();
        }
        out.addValue(y);
        outx.addValue(x);
        if (logger.isLoggable(Level.INFO)) {
            //System.out.println("MOORE_SScsys lambda t: "+tiempo + ", "+super.getPhase()+" s: "+ta());
            for (int i = 0; i < y.length; i++) {
                logger.info("y(" + i + "): " + y[i]);
            }
        }
    }

    @Override
    public void deltcon(double e) {
        deltext(e);
        deltint();
    }

    /* Determines if the input is an array or an scalar
     * @param uu Initial input
     * @return {"array", "Double", " "}*/
    private String getTypeUu(Object uu) {
        String uutype = uu.getClass().getCanonicalName();
        if (uutype.equals("java.lang.Double[]")) {
            uutype = "array";
        } else if (uutype.equals("java.lang.Double")) {
            uutype = "Double";
        } else {
            uutype = " ";
            System.out.println(super.getName() + ": MOORE_SScsys, ¡error tipo de dato de entrada!");
            for (int i = 0; i < mymodel.getNu(); i++) {
                u[i] = 0.0;
            }
        }
        return uutype;
    }

    /* Get the input vector and assign it to u in due form
     * depending on the type of signal: Double or Double[]
     */
    private void getSignalU(String utype, Object uu) {
        if (utype.equals("array")) {
            System.arraycopy(uu, 0, u, 0, u.length);
        } else if (utype.equals("Double")) {
            u[0] = (Double) uu;
        }
    }

    @Override
    public void exit() {
    }

}
