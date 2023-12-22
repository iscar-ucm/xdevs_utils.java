/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ssii2009.examples.flipflop;

import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;

/**
 *
 * @author Jose Roldan Ramirez
 */
public class FRS extends Atomic {

    // Inputs:
    public static final String inR = "R";
    protected Port<Integer> R = new Port<Integer>(inR);
    public static final String inS = "S";
    protected Port<Integer> S = new Port<Integer>(inS);
    public static final String inC = "C";
    protected Port<Integer> C = new Port<Integer>(inC);

    // Outputs:
    public static final String outQ = "Q";
    protected Port<Integer> Q = new Port<Integer>(outQ);

    // State:
    protected Integer valueAtR;
    protected Integer valueAtS;
    protected Integer valueAtC;
    protected Integer valueAtQ;
    // El retraso en emitir la salida cuando llega el flanco de reloj:
    protected Double delay;

    /**
     * Constructor
     * @param name Nombre de esta instancia.
     * @param delay El retardo del sistema al emitir la salida.
     */
    public FRS(String name, Double delay) {
        super(name);
        super.addInport(C);
        super.addInport(S);
        super.addInport(R);
        super.addOutport(Q);
        this.delay = delay;
        this.valueAtC = null;
        this.valueAtR = null;
        this.valueAtS = null;
        this.valueAtQ = 0;
        // Lo primero en salir por lambda es (valueAtQ)
        super.holdIn("active", delay);
    }

    /**
     * Constructor por defecto
     * @param name Nombre de esta instancia.
     */
    public FRS(String name) {
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
        Integer comingByR = R.getValue();
        Integer comingByS = S.getValue();
        if(comingByR!=null)
            valueAtR = comingByR;
        if(comingByS!=null)
            valueAtS = comingByS;
        Integer comingByC = C.getValue();
        if(comingByC!=null) {
            if(comingByC==1 && (valueAtC==null || valueAtC==0)) {
                valueAtQ = ((1-valueAtC)*valueAtQ) + valueAtC*(1-valueAtR)*((1-valueAtS)*valueAtQ + valueAtS) > 0 ? 1 : 0;
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
