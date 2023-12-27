/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.examples.sequential;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.logic.VCC;
import xdevs.lib.logic.sequential.Clock;
import xdevs.lib.logic.sequential.FD;

/**
 *
 * @author jlrisco
 */
public class RiscoExample extends Coupled {

    public RiscoExample() {
        super("RiscoExample");
        // Primero instancio los modelos atómicos
        Clock clock = new Clock("CLK", 2.0);
        VCC vcc = new VCC("VCC");
        FD fd = new FD("D");
        
        super.addComponent(clock);
        super.addComponent(vcc);
        super.addComponent(fd);

        // Ahoro conecto los modelos atómicos entre sí:
        super.addCoupling(clock.oClk, fd.C);
        super.addCoupling(vcc.out, fd.D);
    }

    public static void main(String[] args) {
        RiscoExample example = new RiscoExample();
		Coordinator coordinator = new Coordinator(example);
        coordinator.initialize();
		coordinator.simulate(30);
        coordinator.exit();
    }
}
