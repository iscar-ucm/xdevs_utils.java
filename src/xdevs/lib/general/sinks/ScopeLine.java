package testing.lib.atomic.sinks;

import testing.nondevs.views.ScopeView;
import xdevs.kernel.modeling.Atomic;

import xdevs.kernel.modeling.Port;

public class ScopeLine extends Atomic {

    protected double time;
    protected ScopeView chart;
    protected String topTitle;
    protected Port<Object>[] input;

    public ScopeLine(String name, String[] portNames) {
        super(name);
        chart = new ScopeView(name, "title", "xTitle", "yTitle");
        input = new Port[portNames.length];
        for (int i = 0; i < input.length; ++i) {
            input[i] = new Port<Object>(portNames[i]);
            super.addInport(input[i]);
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
            Object value = input[i].getValue();
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
}
