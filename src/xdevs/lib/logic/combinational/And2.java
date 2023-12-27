/*
 * And gate for the PCsource
 */
package xdevs.lib.logic.combinational;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Francisco Calvo
 */
public class And2 extends Atomic {

    public Port<Integer> in0 = new Port<Integer>("in0");
    public Port<Integer> in1 = new Port<Integer>("in1");
    public Port<Integer> out = new Port<Integer>("out");
    protected Double delay;
    protected Integer valueAtIn0;
    protected Integer valueAtIn1;
    protected Integer valueToOut;

    public And2(String name, Double delay) {
        super(name);
        super.addInPort(in0);
        super.addInPort(in1);
        super.addOutPort(out);
        this.delay = delay;
    }

    public And2(String name) {
        this(name, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        super.resume(e);
        // Primero procesamos los valores de las entradas.
        Integer tempValueAtIn0 = in0.getSingleValue();
        if (tempValueAtIn0 != null && tempValueAtIn0 != valueAtIn0) {
            valueAtIn0 = tempValueAtIn0;
            super.holdIn("active", delay);
        }
        Integer tempValueAtIn1 = in1.getSingleValue();
        if (tempValueAtIn1 != null && tempValueAtIn1 != valueAtIn1) {
            valueAtIn1 = tempValueAtIn1;
            super.holdIn("active", delay);
        }

        if (super.phaseIs("active") && valueAtIn0 != null && valueAtIn1 != null) {
            valueToOut = valueAtIn0 * valueAtIn1;
        }

    }

    @Override
    public void lambda() {
        out.addValue(valueToOut);
    }

    @Override
    public void exit() { }

    @Override
    public void initialize() {
        valueAtIn0 = null;
        valueAtIn1 = null;
        valueToOut = null;
        super.passivate();
    }
}
