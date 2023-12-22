/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ssii2009.examples.models;

import ssii2009.examples.flipflop.FD;
import ssii2009.examples.general.Clock;
import ssii2009.examples.general.VCC;
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.Coordinator;
import xdevs.kernel.util.AtomicLogger;

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
        AtomicLogger biestableD = new AtomicLogger(new FD("D"));
        
        super.addComponent(clock);
        super.addComponent(vcc);
        super.addComponent(biestableD);

        // Ahoro conecto los modelos atómicos entre sí:
        super.addCoupling(clock, Clock.outName, biestableD, FD.inC);
        super.addCoupling(vcc, VCC.outName, biestableD, FD.inD);
    }

    public static void main(String[] args) {
        RiscoExample example = new RiscoExample();
		Coordinator coordinator = new Coordinator(example);
		coordinator.simulate(30);
    }
}
