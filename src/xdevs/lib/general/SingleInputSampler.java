package testing.lib.atomic.general;

import java.util.logging.Level;
import java.util.logging.Logger;
import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;


/**
 *  Single Input Sampler Atomic Model
 *  
 * @author Jesús M. de la Cruz
 * @date   April 19, 2008
 *
 */
public class SingleInputSampler extends Atomic {
    private static Logger logger = Logger.getLogger(SingleInputSampler.class.getName());
	// Ports
	public static final String inName = "in";
	public static final String outName = "out";
    protected Port<Double> in = new Port<Double>(inName);
    protected Port<Double> out = new Port<Double>(outName);
	
	/** Parameters that characterize the model 
	 *  h: Sampling Period (seconds)
	 */
	protected double  h;
	
	// Parameters
	protected double  output; 
	
	// Debugging
	private double tiempo = 0;	// Internal time
    
	/** Single input sampler.
	 * 
	 * @param name  Name of the component
	 * @param h		sampling time in seconds
	 */
	public SingleInputSampler(String name, double h){
		super(name);
		this.h = h;
		//super.setMealyModel(true);
		addInport(in);
		addOutport(out);
        super.holdIn("initial", INFINITY);
	}
	
	public double ta(double t){
		return super.getSigma();
	}
	
	public void deltext(double e) {
        super.resume(e);
		output = in.getValue();
		if (super.phaseIs("initial")){
			super.holdIn("transitory", 0);
			return;
		}
		super.holdIn("new", super.getSigma());
		
		if (logger.isLoggable(Level.INFO)){
			logger.info("SAMPLER deltext t: "+tiempo + " y: "+output + " s: "+ta()+" "+super.getPhase());
		}
	}
	
	public void deltint() {
		super.holdIn("old", h);
		
		if (logger.isLoggable(Level.INFO)){
			tiempo = tiempo + h;
			logger.info("SAMPLER deltin  tnext: " + tiempo + " output: "+output+" "+super.getPhase());
		}
	}
	
	public void lambda(){
		out.setValue(output);
		if (logger.isLoggable(Level.INFO)){
			logger.info("SAMPLER lambda t: "+tiempo + " y: "+output+" "+super.getPhase()+" s: "+ta());
		}
	}
	
    @Override
	public void deltcon(double e){
		deltext(e);
		deltint();
	}
}
	
