/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.logic.sequential;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Jose Roldan Ramirez
 */
public class FRS extends Atomic {

    // Inputs:
    public Port<Integer> R = new Port<Integer>("R");
    public Port<Integer> S = new Port<Integer>("S");
    public Port<Integer> C = new Port<Integer>("C");

    // Outputs:
    public Port<Integer> Q = new Port<Integer>("Q");

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
        super.addInPort(C);
        super.addInPort(S);
        super.addInPort(R);
        super.addOutPort(Q);
        this.delay = delay;
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
        Integer comingByR = R.getSingleValue();
        Integer comingByS = S.getSingleValue();
        if(comingByR!=null)
            valueAtR = comingByR;
        if(comingByS!=null)
            valueAtS = comingByS;
        Integer comingByC = C.getSingleValue();
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
		Q.addValue(valueAtQ);
    }

    @Override
    public void exit() { }

    @Override
    public void initialize() {
        this.valueAtC = null;
        this.valueAtR = null;
        this.valueAtS = null;
        this.valueAtQ = 0;
        // Lo primero en salir por lambda es (valueAtQ)
        super.holdIn("active", delay);
    }
}
