package xdevs.lib.dynamic.continuous;

import java.util.logging.Level;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;
import xdevs.lib.dynamic.IDynSys;

/**
 *  MEALYE_SSdsys: Atomic Mealy State Space Continuous 
 *                Linear - NonLinear System Model
 *                
 *  Uses a IDynSys model
 *  
 *  dx(t) = f(t,x(t),u(t))
 *  y(t) = g(t,x(t))
 *  
 *  @author J.M. de la Cruz, May 19th, 2008
 *  @version 1.0
 */



public class MEALY_SScsys extends Atomic {
	
    private static Logger logger = Logger.getLogger(MEALY_SScsys.class.getName());
//	 Ports
	public static final String outName = "out";		// Output of the system
	public static final String outxName = "outx"; 	// Output of the state. May not be used
	public static final String inName = "in";		// Input signal
	public static final String clockName = "clock";	// Clock signal input

    protected Port<Object> in;
    protected Port<Object> clock;
    protected Port<Double[]> out;
    protected Port<Double[]> outx;
	
	/** Parameters that characterizes the model */
	private IDynSys mymodel;		// Discrete dynamic model
	private Double[] x;				// State vector
	private Double[] u;				// Input vector
	private Double[] uold;          // Previous input vector
	private Double[] y;				// Output vector
	
	private double dt;				// present integration time
	private String utype;		    // {"array","Double"," "}
	
	private double tiempo = 0;	// Internal time
	
	/** Creates a causal discrete system in state space.
	 *  @param name of the model
	 *  @param model object of type IModeloDinamicoD
	 */
	public MEALY_SScsys(String name, IDynSys model){
		super(name);
        in = new Port<Object>(inName);
        clock = new Port<Object>(clockName);
        out = new Port<Double[]>(outName);
        outx = new Port<Double[]>(outxName);
		addInPort(clock);
		addInPort(in);
		addOutPort(out);
		addOutPort(outx);
		mymodel = model;
		u = new Double[mymodel.getNu()];
		uold = new Double[mymodel.getNu()];
		x = new Double[mymodel.getNx()];
		x = mymodel.getState();
	}
	
	@Override
	public void initialize() {
		super.passivate();
	}

	public double ta(double t){
		return super.getSigma();
	}
	
	public void deltext(double e) {
        super.resume(e);
		Object uu = in.getSingleValue();
		if (uu != null){ 
			if (super.phaseIs("initial")) {
				utype = getTypeUu(uu);
				getSignalU(utype,uu);
				uold = u.clone();
                super.holdIn("initial",0);
			}
			else{
				uold = u.clone();
				getSignalU(utype,uu);
				super.holdIn("new",0);
			}
			dt = e;
		}
		
		Object clk = clock.getSingleValue();
		if (clk != null){
			super.holdIn("transitory", 0);
		}
		if (logger.isLoggable(Level.INFO)){
			logger.info("MEALY_SScsys deltext t: "+(tiempo+e)+", "+super.getPhase());
			for (int i=0; i< u.length; i++) 
				logger.info("u("+i+"): "+u[i]);
		}
	}
	
	public void deltint() {
		super.holdIn("old", Constants.INFINITY);
		tiempo = tiempo + dt;
		dt = mymodel.getSampling();
		
		if (logger.isLoggable(Level.INFO)){
			logger.info("MEALY_SScsys deltin  tnext: " + tiempo);
			for (int i = 0; i < x.length; i++)
				logger.info(" x("+(i+1)+"): "+x[i]);
		}		
	}
	
	public void lambda(){
		if (super.phaseIs("initial")){
			y = mymodel.gxut(dt,x,u);
			super.holdIn("new", super.getSigma());
		}
		else{
			double h;
			if (super.phaseIs("new"))  // output due to new input
				h = dt;
			else
				h = mymodel.getSampling();
			x = mymodel.update(h, x, uold);		// update state
			y = mymodel.gxut(dt,x,u);		// calculates output
			uold = u.clone();
		}
		out.addValue(y);
		outx.addValue(x);
		if (logger.isLoggable(Level.INFO)){
			logger.info("MEALY_SScsys lambda t: "+tiempo + ", "+super.getPhase()+" s: "+ta());
			for (int i=0; i< y.length; i++) 
				logger.info("y("+i+"): "+y[i]);
		}
	}
	
    @Override
	public void deltcon(double e){
		deltext(e);
		deltint();
	}
	
	/* Determines if the input is an array or an scalar 
	 * @param uu Initial input
	 * @return {"array", "Double", " "}*/
	private String getTypeUu(Object uu){
		String uutype = uu.getClass().getCanonicalName();
		if (uutype.equals("java.lang.Double[]")){
			uutype = "array";
		}
		else if(uutype.equals("java.lang.Double")) {
			uutype = "Double";
		}
		else{
			uutype = " ";
			System.out.println(super.getName()+": Error tipo de dato de entrada.");
			u[0] = 0.0;
		}
		return uutype;
	}
	
	/* Get the input vector and assign it to u in due form
	 * depending on the type of signal: Double or Double[]
	 */
	private void getSignalU(String utype, Object uu){
		if (utype.equals("array"))
			System.arraycopy(uu, 0, u, 0, u.length);
		else if (utype.equals("Double"))
			u[0] = (Double) uu;		
	}

	@Override
	public void exit() {
	}


}