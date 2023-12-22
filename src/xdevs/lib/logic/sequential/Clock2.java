package ssii2009.examples.general;

import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;

public class Clock extends Atomic {

    public static final String outName = "out";
    protected Port<Integer> out = new Port<Integer>(outName);
    public static final String inName = "in";
    protected Port<Integer> in = new Port<Integer>(inName);
    // Estado. El único estado que tiene un reloj es el periodo del mismo,
    // así como el valor actual a lanzar (0 ó 1).
    // El periodo lo damos en unidades de tiempo, da igual si la escala es
    // pico-segundos o nano-segundos.
    protected Double period;
    protected Integer value;
    // Información adicional para contar el número de ciclos de reloj.
    protected Long count;

    /**
     * Constructor completo.
     * @param name Nombre de este modelo atómico.
     * @param period Periodo de reloj.
     * @param initialValue Valor inicial del reloj, puede ser 0 ó 1.
     */
    public Clock(String name, Double period, Integer initialValue) {
        super(name);
        super.addInport(in);
        super.addOutport(out);
        this.period = period;
        this.value = 0;
        this.count = Long.valueOf(0);
        if (initialValue != 0) {
            this.value = 1;
        }
        // Lo primero que hará este modelo atómico es lanzar value por el 
        // puerto de salida.
        super.holdIn("active", 0);
    }

    /**
     * Constructor por defecto:
     * @param name Nombre del modelo atómico
     * @param period Periodo de reloj
     */
    public Clock(String name, double period) {
        this(name, period, 0);
    }

    public void deltint() {
        // Nuevo semiperiodo:
        count++;
        // Nuevo valor:
        value = 1 - value;
        // La próxima vez que lancemos un valor será en un semiperiodo:
        super.holdIn("active", period / 2.0);
    }

    public void lambda() {
        out.setValue(value);
    }

    // La función deltext no es necesaria porque este elemento no tiene
    // puertos de entrada.
    @Override
    public void deltext(double e) {
        super.resume(e);
        if(!in.isEmpty())
            super.passivate();
    }
}

