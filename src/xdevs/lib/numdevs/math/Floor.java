package xdevs.lib.numdevs.math;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 * <P>Calcula la parte entera de la entrada, redondeando hacia abajo.</P>
 * <P><B>Puertos de entrada:</B> Un �nico puerto de entrada. Est� a la escucha
 * de objetos tipo Double.</P> 
 * <P><B>Puertos de salida:</B> Un �nico puerto de salida que arroja la parte entera
 * de la entrada (redondeando hacia abajo).</P>
 * @author Jos� Luis Risco Mart�n.
 *
 */
public class Floor extends Atomic {
    /** Entrada. */
    public Port<Number> uPort = new Port<>("u");
    protected Number u;		// Input
    /** Salida. */
    public Port<Number> yPort = new Port<>("y");
    protected Number y;		// Output
    /** Constructor. */
    protected double delay;
    public Floor(double delay) {
        super();
        super.addInPort(uPort);
        super.addOutPort(yPort);
        this.delay = delay;
    }
    // ---------------------------------------------------------------
    // DEVS PROTOCOL
    // ---------------------------------------------------------------
    /** 
     * Ininicializa los atributos del modelo at�mico
     * @param parameters Parametros necesarios para configurar el �tomo.
     * En este caso no se utiliza. 
     * */
    public void initialize() {
        u = null;
        y = null;
    }
	
    public void deltint() {
        super.passivate();
    }
	
    public void deltext(double e) {
        super.resume(e);
        u = uPort.getSingleValue();
        super.holdIn("active", delay);
    }
	
    public void lambda() {
        y = Math.floor(u.doubleValue());
        yPort.addValue(y);
    }
	
    public void exit() { }
    //	 ---------------------------------------------------------------

}
