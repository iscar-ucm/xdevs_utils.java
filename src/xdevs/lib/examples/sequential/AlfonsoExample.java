package xdevs.lib.examples.sequential;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.logic.GND;
import xdevs.lib.logic.VCC;
import xdevs.lib.logic.sequential.Clock;
import xdevs.lib.logic.sequential.FT;

/**
 *
 * @author asanmiguel
 */
public class AlfonsoExample extends Coupled {

    public AlfonsoExample() {
        super("AlfonsoExample");
        // Primero instancio los modelos atómicos
        Clock clock = new Clock("CLK", 2.0);
        VCC vcc = new VCC("VCC");
        GND gnd = new GND("GND");
        FT biestableT = new FT("T");

        super.addComponent(clock);
        super.addComponent(vcc);
        super.addComponent(gnd);
        super.addComponent(biestableT);

        // Ahoro conecto los modelos atómicos entre sí:
        super.addCoupling(clock.oClk, biestableT.C);
        super.addCoupling(gnd.out, biestableT.T);
    }

    public static void main(String[] args) {
        AlfonsoExample example = new AlfonsoExample();
		Coordinator coordinator = new Coordinator(example);
        coordinator.initialize();
		coordinator.simulate(4);
        coordinator.exit();
    }
}
