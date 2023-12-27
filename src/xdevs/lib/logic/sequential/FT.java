/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.logic.sequential;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 * FT is a synchronous, resettable toggle flip-flop
 *.The Q output toggles, or changes state, when the toggle enable (T)
 * input is High  during the Low-to-High clock transition.
 *
 * @author asanmiguel
 */
public class FT extends Atomic {

     // Inputs:
    public Port<Integer> T = new Port<Integer>("T");
    public Port<Integer> C = new Port<Integer>("C");

    // Outputs:
    public Port<Integer> Q = new Port<Integer>("Q");

    // State:
    protected Integer valueAtT;
    protected Integer valueAtC;
    protected Integer valueAtQ;
    // El retraso en emitir la salida cuando llega el flanco de reloj:
    protected Double delay;

    /**
     * Constructor
     * @param name Nombre de esta instancia.
     * @param delay El retardo del sistema al emitir la salida.
     */
    public FT(String name, Double delay) {
        super(name);
        super.addInPort(C);
        super.addInPort(T);
        super.addOutPort(Q);
        this.delay = delay;
    }

    /**
     * Constructor por defecto
     * @param name Nombre de esta instancia.
     */
    public FT(String name) {
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
        // Primero miramos si hay algo en T:
        Integer comingByT = T.getSingleValue();

        if(comingByT!=null)
            valueAtT = comingByT;
        // Luego miramos si hay flanco de reloj
        Integer comingByC = C.getSingleValue();
        if(comingByC!=null) {
            if(comingByC==1 && (valueAtC==null || valueAtC==0)) {
                valueAtQ = (( 1-valueAtT)*valueAtQ + valueAtT*(1-valueAtQ) )> 0 ? 1 : 0;
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
        this.valueAtT = null;
        this.valueAtQ = 0;
        // Lo primero en salir por lambda es (valueAtQ)
        super.holdIn("active", delay);
   }
}