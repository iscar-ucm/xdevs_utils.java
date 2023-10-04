package xdevs.lib.dynamic.discrete;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;
import xdevs.lib.dynamic.IDynSys;

/**
 *  MEALYE_SSdsys: Atomic Moore State Space Discrete 
 *                Linear - NonLinear System Model
 *                
 *  Uses a IDynSys model
 *  
 *  x(k+1) = f(k,x(k),u(k))
 *  y(k) = g(k,x(k),u(k))
 *  
 *  @author J.M. de la Cruz, May 18th, 2008
 *  @version 1.0
 */
public class MEALY_SSdsys extends Atomic {
//	 Ports

    public static final String outName = "out";		// Output of the system
    public Port<Double[]> out = new Port<Double[]>(outName);
    public static final String outxName = "outx"; 	// Output of the state. May not be used
    protected Port<Double[]> outx = new Port<Double[]>(outxName);
    public static final String inName = "in";		// Input signal
    public Port<Object> in = new Port<Object>(inName);
    public static final String clockName = "clock";	// Clock signal input
    public Port<Object> clock = new Port<Object>(clockName);
    /** Parameters that characterizes the model */
    private IDynSys mymodel;			// Discrete dynamic model
    private Double[] x;					// State vector
    private Double[] u;					// Input vector
    private Double[] y;					// Output vector
    private String utype;		    // {"array","Double"," "}
    //		 Debugging
    static final int YES = 1;
    static final int NO = 0;
    private int debug = YES;  	// 1 debug, 0 no debug
    private double tiempo = 0;	// Internal time

    /** Creates a causal discrete system in state space.
     *  @param name of the model
     *  @param model object of type IModeloDinamicoD
     */
    public MEALY_SSdsys(String name, IDynSys model) {
        super(name);
        addInPort(in);
        addInPort(clock);
        addOutPort(out);
        addOutPort(outx);
        mymodel = model;
        u = new Double[mymodel.getNu()];
        for (int i = 0; i < mymodel.getNu(); i++) {
            u[i] = 0.0;
        }
        x = new Double[mymodel.getNx()];
        //y = new Double[mymodel.getNy()];
        x = mymodel.getState();
    }

    @Override
    public void initialize() {
        super.holdIn("initial", Constants.INFINITY);  // Waiting for the first input
    }

    public double ta(double t) {
        return super.getSigma();
    }

    public void deltext(double e) {
        super.resume(e);
        Object uu = in.getSingleValue();
        if (uu != null) {
            if (super.phaseIs("initial")) {
                utype = getTypeUu(uu);
                super.holdIn("new", 0);
            }
            getSignalU(utype, uu);
        }

        Object clk = clock.getSingleValue();
        if (clk != null) {
            if (!super.phaseIs("initial")) {
                super.holdIn("transitory", 0);
            } else {
                return;
            }
        }
        if (debug == YES) {
            //System.out.println("MEALY_SSdsys deltext t: "+(tiempo+e)+", "+super.getPhase());
            for (int i = 0; i < u.length; i++) {
                System.out.println("u(" + i + "): " + u[i]);
            }
        }
    }

    public void deltint() {
        double h = mymodel.getSampling();
        x = mymodel.update(h, x, u);
        super.holdIn("old", Constants.INFINITY);

        if (debug == YES) {
            tiempo = tiempo + h;
            System.out.println("MEALY_SSdsys deltin  tnext: " + tiempo);
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
            //System.out.println("MEALY_SSdsys lambda t: "+tiempo + ", "+super.getPhase()+" s: "+ta());
            for (int i = 0; i < y.length; i++) {
                System.out.println("y(" + i + "): " + y[i]);
            }
        }
    }

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
            System.out.println(super.getName() + ": Error tipo de dato de entrada.");
            u[0] = 0.0;
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
