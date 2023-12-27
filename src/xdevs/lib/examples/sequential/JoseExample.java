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
import xdevs.lib.logic.sequential.FRS;

/**
 *
 * @author Jose Roldan Ramirez
 */
public class JoseExample extends Coupled {

    public JoseExample() {
        super("JoseExample");
        // Primero instancio los modelos atómicos
        Clock clock = new Clock("CLK", 2.0);
        GND gnd = new GND("GND");
        VCC vcc = new VCC("VCC");
        FRS biestableRS = new FRS("RS");

        addComponent(clock);
        addComponent(vcc);
        addComponent(gnd);
        addComponent(biestableRS);

        // Ahoro conecto los modelos atómicos entre sí:
        super.addCoupling(clock.oClk, biestableRS.C);
        super.addCoupling(gnd.out, biestableRS.R);
        super.addCoupling(gnd.out, biestableRS.S);
    }

    public static void main(String[] args) {
        JoseExample example = new JoseExample();
		Coordinator coordinator = new Coordinator(example);
        coordinator.initialize();
		coordinator.simulate(40);
        coordinator.exit();
    }
}
