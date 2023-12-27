/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.logic.sequential;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 * FJKC is a single J-K-type flip-flop with J, K, and asynchronous clear (CLR)
 * inputs and data output (Q). The asynchronous clear (CLR) input, when High,
 * overrides all other inputs and resets the (Q) output Low. When (CLR) is Low,
 * the output responds to the state of the J and K inputs, as shown in the
 * following truth table, during the Low-to-High clock (C) transition.
   The flip-flop is asynchronously cleared, output Low, when power is applied.
 *
 * @author Francisco Calvo
 */
public class FJK extends Atomic {

     // Inputs:
    public Port<Integer> J = new Port<Integer>("J");
    public Port<Integer> K = new Port<Integer>("K");
    public Port<Integer> C = new Port<Integer>("C");

    // Outputs:
    public Port<Integer> Q = new Port<Integer>("Q");

    // State:
    protected Integer valueAtJ;
    protected Integer valueAtK;
    protected Integer valueAtC;
    protected Integer valueAtQ;
    // El retraso en emitir la salida cuando llega el flanco de reloj:
    protected Double delay;

    /**
     * Constructor
     * @param name Nombre de esta instancia.
     * @param delay El retardo del sistema al emitir la salida.
     */
    public FJK(String name, Double delay) {
        super(name);
        super.addInPort(K);
        super.addInPort(C);
        super.addInPort(J);
        super.addOutPort(Q);
        this.delay = delay;
    }

    /**
     * Constructor por defecto
     * @param name Nombre de esta instancia.
     */
    public FJK(String name) {
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
        // Primero miramos si hay algo en J y K:
        Integer comingByJ = J.getSingleValue();
        Integer comingByK = K.getSingleValue();
        if(comingByJ!=null)
            valueAtJ = comingByJ;
        if(comingByK!=null)
            valueAtK = comingByK;
        // Luego miramos si hay flanco de reloj
        Integer comingByC = C.getSingleValue();
        if(comingByC!=null) {
            if(comingByC==1 && (valueAtC==null || valueAtC==0)) {
                valueAtQ  = (valueAtJ * (1-valueAtQ) + (1-valueAtK) * valueAtQ) > 0 ? 1 : 0;
                // Es tiempo de enviar la salida.
                super.holdIn("active", delay);
            }
            valueAtC = comingByC;
        }
    }

    @Override
    public void lambda() {
		Q.addValue(valueAtQ);
    }

    @Override
    public void exit() { }

    @Override
    public void initialize() {
        this.valueAtC = null;
        this.valueAtJ = null;
        this.valueAtK = null;
        this.valueAtQ = 0;
        // Lo primero en salir por lambda es (valueAtQ)
        super.holdIn("active", delay);
    }
}
