/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ssii2009.examples.models;

import ssii2009.examples.flipflop.FRS;
import ssii2009.examples.general.Clock;
import ssii2009.examples.general.VCC;
import ssii2009.examples.general.GND;
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.Coordinator;

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
        super.addCoupling(clock, Clock.outName, biestableRS, FRS.inC);
        super.addCoupling(gnd, GND.outName, biestableRS, FRS.inR);
        super.addCoupling(gnd, GND.outName, biestableRS, FRS.inS);
    }

    public static void main(String[] args) {
        JoseExample example = new JoseExample();
		Coordinator coordinator = new Coordinator(example);
		coordinator.simulate(40);
    }
}
