package xdevs.lib.examples.efp;

import java.util.logging.Level;

import xdevs.core.examples.efp.Gpt;
import xdevs.core.modeling.CoupledView;
import xdevs.core.simulation.CoordinatorView;

/**
 *
 * @author jlrisco
 */
public class GptView {

  public static void main(String[] args) {
    Gpt gpt = new Gpt("Gpt", 1, 10);
    CoupledView coupledView = new CoupledView(gpt);
    coupledView.setBounds("Gpt", 0, 0, 500, 500);
    coupledView.setBounds("generator", 0.1, 0.1, 100, 100);
    coupledView.setBounds("transducer", 0.1, 0.5, 100, 100);
    coupledView.setBounds("processor", 0.7, 0.1, 100, 100);
    CoordinatorView coordinator = new CoordinatorView(coupledView);
    coordinator.initialize();
    coordinator.setVisible(true);
  }
}
