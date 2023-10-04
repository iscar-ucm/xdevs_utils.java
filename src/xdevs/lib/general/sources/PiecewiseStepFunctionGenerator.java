package xdevs.lib.general.sources;

import java.util.logging.Level;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *  Piecewise Step Function Generator Atomic Model
 *  
 * @author Jesús M. de la Cruz
 * @date   April 19, 2008
 *
 */
public class PiecewiseStepFunctionGenerator extends Atomic {
    private static Logger logger = Logger.getLogger(PiecewiseStepFunctionGenerator.class.getName());
	// Ports
	public static final String inName = "in";
	public static final String outName = "out";
    public Port<Double> in = new Port<Double>(inName);
    public Port<Double> out = new Port<Double>(outName);
	/** Parameters that characterizes the model */
	/** Array with pairs <value, time> 
	 *  with first element: <initial value, *>
	 * */
	protected double[][] Steps = { {0,-1}, {1,1}}; 
    /* Example
     * 
     *{
     *   { 1, -1 }, // {Initial value, -1}
     *   { -1, 1 },  // {Value, time}
     *   { 0, 2 },  // {Value, time}
     *   { 2, 3}   // {Value, time}
     *};
     */
	// Internal parameters
	private double output;   // Next output of the function
	
	private double actualTime; // Actual instant for a step
	
	private double previousTime; //Previous instant for a step
	
	private int index = 0;   // Index to the array Steps
	
	// Debugging
	private double tiempo = 0;
	
	/**
	 * Generates Piecewise Steps at given time instants
	 * The <value,time> pairs are given in an array
	 * The first row is <initial value, any value(not used)>
	 * The following rows are <step value, time instant>
	 * @param name Name of the component
	 * @param Steps Array with element <step value, time instant>
	 * 
	 */
	public PiecewiseStepFunctionGenerator(String name, double[][] Steps){
		super(name);
		addInPort(in);
		addOutPort(out);
		this.Steps = Steps;
		output = Steps[index][0];	// Initial value
		actualTime = 0;				// Initial time
		index ++;					// Next step
	}
	
	@Override
	public void initialize() {
		super.holdIn("initial", 0);
	}

	public void deltext(double e) {
        super.resume(e);
    }
	
	public void deltint() {
		if (index  < Steps.length) {      // There remains steps
			previousTime = actualTime;
			actualTime = Steps[index][1];
			output = Steps[index][0];
            super.holdIn("active", actualTime - previousTime);
			index ++;
		}
		else{								// No more steps
            super.passivate();
		}
	}
	
	public void lambda(){
		out.addValue(output);
//		 Debugging
		if (logger.isLoggable(Level.INFO)){
			tiempo = tiempo + super.getSigma();
			logger.info("t: " + tiempo + "; y : "+output);
		}
		//
	}

	@Override
	public void exit() {
	}

}

