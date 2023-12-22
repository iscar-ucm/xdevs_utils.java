/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.logic.sequential;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 * 2-Bit Cascadable Binary Counter with Clock Enable and Asynchronous Clear
 * (Taken from Xilinx 11)
 * This design element is an asynchronously clearable, cascadable binary
 * counter. The asynchronous clear (CLR) input, when High, overrides all
 * other inputs and forces the Q outputs, terminal count (TC), and clock
 * enable out (CEO) to logic level zero, independent of clock transitions.
 * The Q outputs increment when the clock enable input (CE) is High during
 * the Low-to-High clock (C) transition. The counter ignores clock transitions
 * when CE is Low. The TC output is High when all Q outputs are High.
 *
 * Create larger counters by connecting the CEO output of each stage to the
 * CE input of the next stage and connecting the C and CLR inputs in parallel.
 * CEO is active (High) when TC and CE are High. The maximum length of the
 * counter is determined by the accumulated CE-to-TC propagation delays
 * versus the clock period. The clock period must be greater than n (tCE-TC),
 * where n is the number of stages and the time tCE-TC is the CE-to-TC
 * propagation delay of each stage. When cascading counters, use the CEO
 * output if the counter uses the CE input or use the TC output if it does not.
 *
 * This counter is asynchronously cleared, outputs Low, when power is applied.
 * This design element is a D-type flip-flop with data input (D) and data
 * output (Q). The data on the D inputs is loaded into the flip-flop during
 * the Low-to-High clock (C) transition.
 *
 * @author José L. Risco-Martín
 */
public class CB2CE extends Atomic {

    // Inputs:
    protected Port<Integer> CE;
    protected Port<Integer> C;
    protected Port<Integer> CLR;

    // Outputs:
    protected Port<Integer> Q1;
    protected Port<Integer> Q0;
    protected Port<Integer> CEO;
    protected Port<Integer> TC;

    // State:
    protected Integer valueAtCE;
    protected Integer valueAtC;
    protected Integer valueAtCLR;
    protected Integer valueAtQ1;
    protected Integer valueAtQ0;
    protected Integer valueAtTC;
    protected Integer valueAtCEO;
    // El retraso en emitir la salida cuando llega el flanco de reloj:
    protected Double delay;
    // El contador, no valos a pararnos a incrmentar Q1 y Q0 a base de
    // sentencias "if".
    protected Integer count;

    /**
     * Constructor
     * @param name Nombre de esta instancia.
     * @param delay El retardo del sistema al emitir la salida.
     */
    public CB2CE(String name, Double delay) {
        super(name);
        CE = new Port<Integer>("CE");
        C = new Port<Integer>("C");
        CLR = new Port<Integer>("CLR");
        Q1 = new Port<Integer>("Q1");
        Q0 = new Port<Integer>("Q0");
        CEO = new Port<Integer>("CEO");
        TC = new Port<Integer>("TC");
        this.delay = delay;
    }

    /**
     * Constructor por defecto
     * @param name Nombre de esta instancia.
     */
    public CB2CE(String name) {
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
        // Primero miramos si hay algo en las entradas:

        // Primero las señales asíncronas:

        // Si CLR es 1 desactivamos todo y mandamos salida:
        valueAtCLR = CLR.getSingleValue();
        if(valueAtCLR!=null && valueAtCLR==1) {
            count = 0;
            valueAtQ1 = 0;
            valueAtQ0 = 0;
            valueAtTC = 0;
            valueAtCEO = 0;
            super.holdIn("active", delay);
            return;
        }
        
        // Si CE es 0, el sistema no cambia y CEO es 0
        valueAtCE = CE.getSingleValue();
        if(valueAtCE!=null && valueAtCE==0) {
            valueAtQ1 = null; // Para no enviar la salida.
            valueAtQ0 = null; // Para no enviar la salida.
            valueAtTC = null; // Para no enviar la salida.
            valueAtCEO = 0;
            super.holdIn("active", delay);
            return;
        }

        // Luego miramos si hay flanco de reloj
        Integer comingByC = C.getSingleValue();
        if(comingByC!=null) {
            if(comingByC==1 && valueAtC==0) {
                count = (count + 1) % 4;
                valueAtQ0 = count % 2;
                valueAtQ1 = (count / 2) % 2;
                valueAtTC = valueAtQ1 * valueAtQ0;
                // En ppio no cabe la posibilidad de que valueAtCE sea null,
                // pero por si acaso
                if(valueAtCE!=null)
                    valueAtCEO = valueAtTC * valueAtCE;
                else
                    valueAtCEO = null;
                // Es tiempo de enviar la salida.
                super.holdIn("active", delay);
            }
            valueAtC = comingByC;
        }
    }

    @Override
    public void lambda() {
		if(valueAtQ1!=null) Q1.addValue(valueAtQ1);
        if(valueAtQ0!=null) Q0.addValue(valueAtQ0);
        if(valueAtTC!=null) TC.addValue(valueAtTC);
        if(valueAtCEO!=null) CEO.addValue(valueAtCEO);
    }

    @Override
    public void exit() { }

    @Override
    public void initialize() {
        count = 0;
        valueAtCE = null;
        valueAtC = null;
        valueAtCLR = null;
        valueAtQ1 = 0;
        valueAtQ0 = 0;
        valueAtTC = 0;
        valueAtCEO = null;
        // Lo primero en salir por lambda es (valueAtQ)
        super.holdIn("active", delay);
    }

}
