package xdevs.lib.logic;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class GND extends Atomic {
    // Nombres de los puertos de entrada/salida. En este caso, al tratarse de
    // un GND, sólo tenemos un puerto de salida.

    public Port<Integer> out = new Port<Integer>("out");
    protected Double delay;

    public GND(String name, double delay) {
        super(name);
        super.addOutPort(out);
        this.delay = delay;
    }

    public GND(String name) {
        this(name, 0.0);
    }

    public void deltint() {
        super.passivate();
    }

    public void lambda() {
        out.addValue(0);
    }

    // La función deltext no es necesaria porque este elemento no tiene
    // puertos de entrada.
    @Override
    public void deltext(double e) {
        super.resume(e);
        super.passivate();
    }

    @Override
    public void exit() { }

    @Override
    public void initialize() {
        super.holdIn("active", delay);
    }
}

