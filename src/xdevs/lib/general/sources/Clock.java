package xdevs.lib.general.sources;

import java.util.logging.Level;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;


/**
 *  Tick Clock Generator
 *  
 * @author Jesús M. de la Cruz
 * @date   May 5, 2008
 *
 */
public class Clock extends Atomic {
    private static Logger logger = Logger.getLogger(Clock.class.getName());
	// Ports
    public Port<Double> out = new Port<Double>("out");
	
	/** Parameters that characterizes the model */
	private double dt;	// Tick clock in seconds
	
	// Internal Parameters
	protected double tiempo = 0.0;   // Output of the function
	
	
	/** Generates a Tick Clock every dt seconds
	 *  @param dt delta of time in seconds
	 */
	public Clock(String name, double dt){
		super(name);
		this.dt = dt;
		addOutPort(out);
		super.holdIn("initial", 0);
	}
		
	public void deltext(double e) {
        super.resume(e);
    }
	
	public void deltint() {
		super.holdIn("active",dt);
		tiempo = tiempo + super.getSigma();
	}
	
	public void lambda(){
		out.addValue(tiempo);
		if (logger.isLoggable(Level.INFO)){
			logger.info("t: " + tiempo + " fase: "+ super.getPhase());
		}
	}

	@Override
	public void exit() {
	}

	@Override
	public void initialize() {
		super.holdIn("initial", 0);
	}
}

	
