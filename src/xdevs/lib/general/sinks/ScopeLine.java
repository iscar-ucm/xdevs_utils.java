package xdevs.lib.general.sinks;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.lib.util.ScopeSeriesView;

public class ScopeLine extends Atomic {

    protected double time;
    protected ScopeSeriesView chart;
    protected String topTitle;
    protected Port<Object>[] input;

    public ScopeLine(String name, String[] portNames) {
        super(name);
        chart = new ScopeSeriesView(name, "title", "xTitle", "yTitle");
        input = new Port[portNames.length];
        for (int i = 0; i < input.length; ++i) {
            input[i] = new Port<Object>(portNames[i]);
            super.addInPort(input[i]);
            chart.addSerie(portNames[i]);
        }
        this.time = 0;
    }

    public void deltint() {
        time += super.getSigma();
        super.passivate();
    }

    public void deltext(double e) {
        super.resume(e);
        time += e;
        for (int i = 0; i < input.length; ++i) {
            Object value = input[i].getSingleValue();
            if (value != null) {
                if (value.getClass().isArray()) {
                    Double[] inputAsArray = (Double[]) value;
                    chart.add(time, inputAsArray, input[i].getName());
                } else {
                    Number y = ((Number) value);
                    chart.add(time, y.doubleValue(), input[i].getName());
                }
            }
        }
    }

    public void lambda() {
    }

    public void setTitle(String title) {
        chart.setTitle(title);
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
        super.passivate();
    }
}
