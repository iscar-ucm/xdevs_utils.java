package ssii2009.examples.models;

import ssii2009.examples.flipflop.FT;
import ssii2009.examples.general.Clock;
import ssii2009.examples.general.VCC;
import ssii2009.examples.general.GND;
import xdevs.kernel.modeling.Coupled;
import xdevs.kernel.simulation.CoordinatorLogger;

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
        biestableT.setLoggerActive(true);

        super.addComponent(clock);
        super.addComponent(vcc);
        super.addComponent(gnd);
        super.addComponent(biestableT);

        // Ahoro conecto los modelos atómicos entre sí:
        super.addCoupling(clock, Clock.outName, biestableT, FT.inC);
        super.addCoupling(gnd, GND.outName, biestableT, FT.inT);
    }

    public static void main(String[] args) {
        AlfonsoExample example = new AlfonsoExample();
		CoordinatorLogger coordinator = new CoordinatorLogger(example);
		coordinator.simulate(4);
        
    }
}
