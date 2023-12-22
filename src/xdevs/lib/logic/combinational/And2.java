/*
 * And gate for the PCsource
 */
package ssii2009.examples.general;

import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;

/**
 *
 * @author Francisco Calvo
 */
public class And2 extends Atomic {

    public static final String in0Name = "in0";
    public static final String in1Name = "in1";
    public static final String outName = "out";
    protected Port<Integer> in0 = new Port<Integer>(in0Name);
    protected Port<Integer> in1 = new Port<Integer>(in1Name);
    protected Port<Integer> out = new Port<Integer>(outName);
    protected Double delay;
    protected Integer valueAtIn0;
    protected Integer valueAtIn1;
    protected Integer valueToOut;

    public And2(String name, Double delay) {
        super(name);
        super.addInport(in0);
        super.addInport(in1);
        super.addOutport(out);
        this.delay = delay;
        valueAtIn0 = null;
        valueAtIn1 = null;
        valueToOut = null;
        super.passivate();
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
        Integer tempValueAtIn0 = in0.getValue();
        if (tempValueAtIn0 != null && tempValueAtIn0 != valueAtIn0) {
            valueAtIn0 = tempValueAtIn0;
            super.holdIn("active", delay);
        }
        Integer tempValueAtIn1 = in1.getValue();
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
        out.setValue(valueToOut);
    }
}
