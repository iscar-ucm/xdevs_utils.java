/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ssii2009.examples.models;

import ssii2009.examples.flipflop.FJK;
import ssii2009.examples.general.Clock;
import ssii2009.examples.general.VCC;
import ssii2009.examples.general.GND;
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.Coordinator;

/**
 *
 * @author jlrisco
 */
public class PacoExample extends Coupled {

    public PacoExample() {
        super("PacoExample");
        // Primero instancio los modelos atómicos
        Clock clock = new Clock("CLK", 2.0);
        GND gnd = new GND("GND");
        VCC vcc = new VCC("VCC");
        FJK biestableJK = new FJK("D");

        super.addComponent(clock);
        super.addComponent(gnd);
        super.addComponent(biestableJK);

        // Ahoro conecto los modelos atómicos entre sí:
        super.addCoupling(clock, Clock.outName, biestableJK, FJK.inC);
        super.addCoupling(gnd, GND.outName, biestableJK, FJK.inJ);
        super.addCoupling(gnd, GND.outName, biestableJK, FJK.inK);
    }

    public static void main(String[] args) {
        PacoExample example = new PacoExample();
		Coordinator coordinator = new Coordinator(example);
		coordinator.simulate(30);
    }
}
