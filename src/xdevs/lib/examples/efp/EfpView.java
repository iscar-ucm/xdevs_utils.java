package xdevs.lib.examples.efp;

import xdevs.core.examples.efp.Efp;
import xdevs.core.modeling.CoupledView;
import xdevs.core.simulation.CoordinatorView;

public class EfpView {

  public static void main(String[] args) {
    Efp efp = new Efp("efp", 1, 3, 100);
    CoupledView coupledView = new CoupledView(efp);
    coupledView.setBounds("efp", 0, 0, 500, 500);
    coupledView.setBounds("ef", 0.1, 0.1, 200, 300);
    coupledView.setBounds("generator", 0.2, 0.1, 100, 100);
    coupledView.setBounds("transducer", 0.2, 0.5, 100, 100);
    coupledView.setBounds("processor", 0.7, 0.1, 100, 100);
    CoordinatorView coordinator = new CoordinatorView(coupledView);
    coordinator.initialize();
    coordinator.setVisible(true);
  }
}
