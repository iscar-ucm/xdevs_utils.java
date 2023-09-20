package xdevs.lib.dynamic.continuous;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;
import xdevs.lib.dynamic.IDynSys;

/**
 *  MOORE_SScsys: 
 *  	Atomic Moore State Space Continuous 
 *      Linear - NonLinear System Model      
 *                
 *  Uses a IDynSys model
 *  
 *  dx(t) = f(t,x(t),u(t))
 *  y(t) = g(t,x(t))
 *  
 *  @author J.M. de la Cruz 
 *  @version 1.0, May 19th 2008 Integrates till either new entry or dint
 */
public class MOORE_SScsys extends Atomic {
//	 Ports

    public static final String outName = "out";		// Output of the system
    protected Port<Double[]> out = new Port<Double[]>(outName);
    public static final String outxName = "outx"; 	// Output of the state. May not be used
    protected Port<Double[]> outx = new Port<Double[]>(outxName);
    public static final String inName = "in";
    protected Port<Object> in = new Port<Object>(inName);
    /** Parameters that characterizes the model */
    private IDynSys mymodel;	// Discrete dynamic model
    private Double[] x;					// State vector
    private Double[] u;					// Input vector
    private Double[] y;					// Output vector
    private String utype;		    // {"array","Double"," "} Type of the input signal
    //		 Debugging
    private final int YES = 1;
    private int debug = YES;  	// 1 debug, 0 no debug
    private double tiempo = 0;	// Internal time

    /** Creates a causal continuos system in state space.
     *  @param name of the model
     *  @param model object of type IDynSys
     */
    public MOORE_SScsys(String name, IDynSys model) {
        super(name);
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
            u[0] = 0.0;
        } else {
            super.holdIn("initial", Constants.INFINITY);
            u = new Double[mymodel.getNu()];
        }
    }

    public double ta(double t) {
        return super.getSigma();
    }

    public void deltext(double e) {
        super.resume(e);
        Object uu;
        uu = in.getSingleValue();
        if (super.phaseIs("initial")) {
            super.holdIn("initial", 0);
            utype = getTypeUu(uu); 			// we see the kind of input
            getSignalU(utype, uu);
        } else {
            getSignalU(utype, uu);
            super.holdIn("new", super.getSigma());
        }

        if (debug == YES) {
            //System.out.println("MOORE_SScsys deltext t: "+(tiempo+e)+", "+super.getPhase());
            for (int i = 0; i < u.length; i++) {
                System.out.println("u(" + i + "): " + u[i]);
            }
        }

    }

    public void deltint() {
        double h = mymodel.getSampling();
        x = mymodel.update(h, x, u);		// update next state
        super.holdIn("old", h);

        if (debug == YES) {
            tiempo = tiempo + h;
            System.out.println("MOORE_SScsys deltin  tnext: " + tiempo);
            for (int i = 0; i < x.length; i++) {
                System.out.println(" x(" + (i + 1) + "): " + x[i]);
            }
        }
    }

    public void lambda() {
        double tk = mymodel.getTime();
        y = mymodel.gxut(tk, x, u);
        out.addValue(y);
        outx.addValue(x);

        if (debug == YES) {
            //System.out.println("MOORE_SScsys lambda t: "+tiempo + ", "+super.getPhase()+" s: "+ta());
            for (int i = 0; i < y.length; i++) {
                System.out.println("y(" + i + "): " + y[i]);
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
        if (utype == "array") {
            System.arraycopy(uu, 0, u, 0, u.length);
        } else if (utype == "Double") {
            u[0] = (Double) uu;
        }
    }

    @Override
    public void exit() {
    }

}
