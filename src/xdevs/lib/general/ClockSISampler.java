package xdevs.lib.general;


//import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *  Clocked Single Input Sampler Atomic Model
 *  The model has two inputs: signal, clock,
 *   and one output: out
 *   It keeps the signal every time it changes, 
 *   but only gives the output with the tick of the clock
 *  
 * @author Jesús M. de la Cruz
 * @date   May 5th, 2008
 *
 */

public class ClockSISampler extends Atomic {
    private static Logger logger = Logger.getLogger(ClockSISampler.class.getName());
	// Ports
	public static final String signalName = "signal";   // input 1
	public static final String clockName = "clock";		// input 2
	public static final String outName = "out";

    protected Port<Double> signal;
    protected Port<Object> clock;
    protected Port<Double> out;
	
	// Parameters
	protected Double  output; 
	
	// Debugging
	private double tiempo = 0;	// Internal time
    
	/** Clocked Single input sampler.
	 * @param name  Name of the component
	 */
	public ClockSISampler(String name){
		super(name);
        signal = new Port<Double>(signalName);
        clock = new Port<Object>(clockName);
        out = new Port<Double>(outName);
		addInPort(signal);
		addInPort(clock);
		addOutPort(out);
		super.passivate();
	}
	
	
	public void deltext(double e) {
        super.resume(e);
		// Get a value every time the signal changes	
		if(!signal.isEmpty()) {
				output = signal.getSingleValue();
		}
		//	 Output a value every tick of the clock 
		if(!clock.isEmpty()) {
			super.holdIn("transitory", 0);
		}
			
		if (logger.isLoggable(Level.INFO)) {
			tiempo = tiempo + e;
			logger.info("CSIS deltext  t: " + tiempo + " output: "+output+" "+super.getPhase());
		}
	}
		
	
	public void deltint() {
		super.passivate();
		if (logger.isLoggable(Level.INFO)){
		logger.info("CSIS deltin  t: " + tiempo + " output: "+output+" "+super.getPhase());
		}
	}
	
	public void lambda(){
		out.addValue(output);
		if (logger.isLoggable(Level.INFO)){
			logger.info("CSIS lambda t: "+tiempo + " y: "+output+" "+super.getPhase()+" s: "+ta());
		}
	}


	@Override
	public void exit() {
	}


	@Override
	public void initialize() {
		super.passivate();
	}
	
}
	
