/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ssii2009.examples.flipflop;

import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;


/**
 * Primitive D flip-flop (Taken from Xilinx 11)
 * This design element is a D-type flip-flop with data input (D) and data
 * output (Q). The data on the D inputs is loaded into the flip-flop during
 * the Low-to-High clock (C) transition.
 *
 * This flip-flop is asynchronously cleared, outputs Low, when power is
 * applied.
 *
 * @author José L. Risco-Martín
 */
public class FD extends Atomic {
    // Inputs:
    public static final String inD = "D";
    protected Port<Integer> D = new Port<Integer>(inD);
    public static final String inC = "C";
    protected Port<Integer> C = new Port<Integer>(inC);

    // Outputs:
    public static final String outQ = "Q";
    protected Port<Integer> Q = new Port<Integer>(outQ);

    // State:
    protected Integer valueAtD;
    protected Integer valueAtC;
    protected Integer valueAtQ;
    // El retraso en emitir la salida cuando llega el flanco de reloj:
    protected Double delay;

    /**
     * Constructor
     * @param name Nombre de esta instancia.
     * @param delay El retardo del sistema al emitir la salida.
     */
    public FD(String name, Double delay) {
        super(name);
        super.addInport(C);
        super.addInport(D);
        super.addOutport(Q);
        this.delay = delay;
        this.valueAtC = null;
        this.valueAtD = null;
        this.valueAtQ = 0;
        // Lo primero en salir por lambda es (valueAtQ)
        super.holdIn("active", delay);
    }

    /**
     * Constructor por defecto
     * @param name Nombre de esta instancia.
     */
    public FD(String name) {
        this(name, 0.0);
    }

    @Override
    public void deltint() {
        // El estado no cambia a menos que recibamos un cambio de flanco en la
        // señal de reloj, y esto se captura con deltext.
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        super.resume(e);
        // Primero miramos si hay algo en D:
        Integer comingByD = D.getValue();
        if(comingByD!=null)
            valueAtD = comingByD;
        // Luego miramos si hay flanco de reloj
        Integer comingByC = C.getValue();
        if(comingByC!=null) {
            if(comingByC==1 && (valueAtC==null || valueAtC==0)) {
                valueAtQ = valueAtD;
                // Es tiempo de enviar la salida.
                super.holdIn("active", delay);
            }
            valueAtC = comingByC;
        }
    }

    @Override
    public void lambda() {
        Q.setValue(valueAtQ);
    }

}
