package testing.lib.atomic.sinks;

import xdevs.kernel.modeling.Atomic;

import java.util.HashMap;
import java.util.Iterator;
import testing.nondevs.views.ScopeViewXY;
import xdevs.kernel.modeling.Port;

/**
 * 
 * @author Jos� L. Risco-Mart�n
 *
 */
public class ScopeXY extends Atomic {

    protected HashMap<String, Port<Double>> xPorts = new HashMap<String, Port<Double>>();
    protected HashMap<String, Port<Double>> yPorts = new HashMap<String, Port<Double>>();
    protected ScopeViewXY chart;

    public ScopeXY(String name, ScopeViewXY.MODE mode) {
        super(name);
        chart = new ScopeViewXY(name, mode);
        super.holdIn("configure", 0.0);
    }

    public void addSerie(String serieName) {
        Port<Double> xPort = new Port<Double>(serieName + "X");
        Port<Double> yPort = new Port<Double>(serieName + "Y");
        xPorts.put(serieName, xPort);
        yPorts.put(serieName, yPort);
        super.addInport(xPort);
        super.addInport(yPort);
        chart.addSerie(serieName);
    }

    public void deltint() {
        chart.configure();
        super.passivate();
    }

    public void deltext(double e) {
        super.resume(e);
        Iterator<String> itr = xPorts.keySet().iterator();
        while (itr.hasNext()) {
            String serieName = itr.next();
            if (!xPorts.get(serieName).isEmpty() && !yPorts.get(serieName).isEmpty()) {
                chart.add(serieName, xPorts.get(serieName).getValue(), yPorts.get(serieName).getValue());
            }
        }
    }

    public void lambda() {
    }

    public void setTitle(String title) {
        chart.setChartTitle(title);
    }

    public void setXTitle(String title) {
        chart.setXTitle(title);
    }

    public void setYTitle(String title) {
        chart.setYTitle(title);
    }
}
