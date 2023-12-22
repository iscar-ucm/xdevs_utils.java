package ssii2009.examples.general;

import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;

public class GND extends Atomic {
    // Nombres de los puertos de entrada/salida. En este caso, al tratarse de
    // un GND, sólo tenemos un puerto de salida.

    public static final String outName = "out";
    protected Port<Integer> out = new Port<Integer>(outName);

    public GND(String name, double delay) {
        super(name);
        super.addOutport(out);
        super.holdIn("active", delay);
    }

    public GND(String name) {
        this(name, 0.0);
    }

    public void deltint() {
        super.passivate();
    }

    public void lambda() {
        out.setValue(0);
    }

    // La función deltext no es necesaria porque este elemento no tiene
    // puertos de entrada.
    @Override
    public void deltext(double e) {
        super.resume(e);
        super.passivate();
    }
}

