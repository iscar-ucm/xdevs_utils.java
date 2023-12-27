/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.examples.sequential;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.logic.GND;
import xdevs.lib.logic.VCC;
import xdevs.lib.logic.sequential.Clock;
import xdevs.lib.logic.sequential.FJK;

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
        FJK fjk = new FJK("D");

        super.addComponent(clock);
        super.addComponent(gnd);
        super.addComponent(fjk);

        // Ahoro conecto los modelos atómicos entre sí:
        super.addCoupling(clock.oClk, fjk.C);
        super.addCoupling(gnd.out, fjk.J);
        super.addCoupling(gnd.out, fjk.K);
    }

    public static void main(String[] args) {
        PacoExample example = new PacoExample();
		Coordinator coordinator = new Coordinator(example);
        coordinator.initialize();
		coordinator.simulate(30);
        coordinator.exit();
    }
}
