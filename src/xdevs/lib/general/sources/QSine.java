package testing.lib.atomic.sources;

import java.util.logging.Logger;
import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;


/**
 *  Quantified Sine Generator Atomic Model
 *  
 * @author Jesús M. de la Cruz
 * @date   April 19, 2008
 *
 */
public class QSine extends Atomic {
    private static Logger logger = Logger.getLogger(QSine.class.getName());
	// Ports
	public static final String outName = "out";
    protected Port<Double> out = new Port<Double>(outName);
	
	/** Parameters that characterizes the model */
	private double w;		// Frequency: rad/seg
	private double fi = 0;	// Initial phase: rad
	private double A = 1.0; // Amplitude of the signal
	private double of = 0;  // Offset of the signal
	
	// Internal Parameters
	protected double q = 1.e-2; // Quantum value
	
	protected double t = 0;		//  time
	
	protected double dt;		//  next sigma
	
	protected double output;   // Next output of the function
		
	/** Generates a Quantified Sine Signal.
	 *  A*sin(w*t + fi) + of
	 *  fi default value: 0.0
	 *  A  default value: 1.0
	 *  of default value: 0.0
	 *  quantum default value: 1.0e-2
	 *  @param name Name of the component
	 *  @param w  frequency rad/seg
	 */
	public QSine(String name, double w){
		super(name);
		this.w = w;
		addOutport(out);
		super.holdIn("initial",0);
		output = fun(super.getPhase());
	}
	
	/** Generates a Quantified Sine Signal.
	 *  A*sin(w*t + fi) + of
	 *  A  default value: 1.0
	 *  of default value: 0.0
	 *  quantum default value: 1.0e-2
	 *  @param name Name of the component
	 *  @param w  frequency rad/seg
	 *  @param fi initial phase
	 */
	public QSine(String name, double w, double fi){
		this(name, w);
		this.fi = fi;
	}
	
	/** Generates a Quantified Sine Signal.
	 *  A*sin(w*t + fi) + of
	 *  A  default value: 1.0
	 *  of default value: 0.0
	 *  @param name Name of the component
	 *  @param w  frequency rad/seg
	 *  @param fi initial phase
	 *  @param q  quantum value
	 */
	public QSine(String name, double w, double fi, double q){
		this(name, w, fi);
		this.q = q;
	}
	
	/** Generates a Quantified Sine Signal.
	 *  A*sin(w*t + fi) + of
	 *  quantum default value: 1.0e-2
	 *  @param name Name of the component
	 *  @param w  frequency rad/seg
	 *  @param fi initial phase
	 *  @param A  amplitude of the signal
	 *  @param of offset of the signal
	 */
	public QSine(String name, double w, double fi, double A, double of){
		this(name, w, fi);
		this.A = A;
		this.of = of;
	}
	
	/** Generates a Quantified Sine Signal.
	 *  A*sin(w*t + fi) + of
	 *  @param name Name of the component
	 *  @param w  frequency rad/seg
	 *  @param fi initial phase
	 *  @param A  amplitude of the signal
	 *  @param of offset of the signal
	 *  @param quantum value
	 */
	public QSine(String name, double w, double fi, double A, double of, double q){
		this(name, w, fi, A, of);
		this.q = q / A;
	}
	
	public void deltext(double e) {
        super.resume(e);
    }
	
	public void deltint() {
		output = fun(super.getPhase());
		super.holdIn("active",dt);
	}
	
	public void lambda(){
		double value = A * output + of;
		out.setValue(value);
	}
	
	protected double fun(String phase){
        /*
    }
		double y = Math.sin(w*t+fi);
		double yc = Math.cos(w*t + fi);
		double yn = 0;
		
		if (phase == "initial"){
			super.setPhase("active");
			if (debug == 1)
				System.out.println("t: " + t + "; y: "+ y +"; yc: "+yc+" "+super.getPhase());
			return y;
		}
		else if ( w== 0){ 
			dt = Double.MAX_VALUE;
			super.setPhase("passive");
			return y;
		}
		
		int se = 0; 			// En principio no hay sobreelongaci�n
		if (yc > 0)	
			if ((y  + q) <= 1)
				yn = y + q;
			else{
				yn = 2.0 - y - q;
				se = 1; 		// Sobreelongaci�n de +1
			}
		else if (yc < 0)
			if ((y - q) >= -1)
				yn = y - q;
			else{
				yn = -2.0 - y + q;
				se = -1;		// Sobreelongaci�
         */
         return 0;
         }
}