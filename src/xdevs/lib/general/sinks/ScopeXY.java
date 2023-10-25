package xdevs.lib.general.sinks;

import java.util.HashMap;
import java.util.Iterator;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.lib.util.ScopeSeriesViewXY;

/**
 * 
 * @author Jos� L. Risco-Mart�n
 *
 */
public class ScopeXY extends Atomic {

    protected HashMap<String, Port<Double>> xPorts = new HashMap<String, Port<Double>>();
    protected HashMap<String, Port<Double>> yPorts = new HashMap<String, Port<Double>>();
    protected ScopeSeriesViewXY chart;

    public ScopeXY(String name, ScopeSeriesViewXY.MODE mode) {
        super(name);
        chart = new ScopeSeriesViewXY(name, mode);
        super.holdIn("configure", 0.0);
    }

    public void addSerie(String serieName) {
        Port<Double> xPort = new Port<Double>(serieName + "X");
        Port<Double> yPort = new Port<Double>(serieName + "Y");
        xPorts.put(serieName, xPort);
        yPorts.put(serieName, yPort);
        super.addInPort(xPort);
        super.addInPort(yPort);
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
                chart.add(serieName, xPorts.get(serieName).getSingleValue(), yPorts.get(serieName).getSingleValue());
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

    @Override
    public void exit() {
    }

    @Override
    public void initialize() {
        super.holdIn("configure", 0.0);
    }
}
