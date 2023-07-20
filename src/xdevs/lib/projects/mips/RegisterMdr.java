/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.projects.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;



/**
 *
 * @author Jose Roldan Ramirez
 * @author José L. Risco Martín
 */
public class RegisterMdr extends Atomic {
    
    public static final String inClkName = "clk";
    public static final String inRegWriteName = "RegWrite";
    public static final String inInName = "in";
    public static final String outOutName = "out";

    protected Port<Integer> clk = new Port<Integer>(RegisterMdr.inClkName);
    protected Port<Integer> regWrite = new Port<Integer>(RegisterMdr.inRegWriteName);
    protected Port<Object> in = new Port<Object>(RegisterMdr.inInName);
    protected Port<Integer> out = new Port<Integer>(RegisterMdr.outOutName);

    protected Double delayRead;
    protected Double delayWrite;
    protected Integer valueAtClk;
    protected Integer valueAtRegWrite;
    protected Object valueAtIn;
    protected Integer valueAtOut;

    public RegisterMdr(String name, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(clk);
        super.addInPort(regWrite);
        super.addInPort(in);
        super.addOutPort(out);
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;
        valueAtClk = null;
        valueAtIn = null;
        valueAtOut = 0;
        super.holdIn("Read",delayRead);
    }

    public RegisterMdr(String name) {
        this(name, 0.0, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate(); // sigma = infinito, phase = passive
    }

    @Override
    public void deltext(double e) {
        super.resume(e);
        // Primero procesamos los valores de la entrada

        if(!in.isEmpty()) {
            valueAtIn = in.getSingleValue();
        }

        Integer tempValueAtRegWrite = regWrite.getSingleValue();
        if(tempValueAtRegWrite!=null) {
            valueAtRegWrite = tempValueAtRegWrite;
        }

        // Ahora el reloj, que gobierna la escritura:

        Integer tempValueAtClk = clk.getSingleValue();
        if(tempValueAtClk!=null) {
            if (valueAtRegWrite != null && valueAtRegWrite == 1 && valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                if (valueAtIn != null) {
                    valueAtOut = (Integer)valueAtIn;
                }
                super.holdIn("Write", delayWrite);
            }
            valueAtClk = tempValueAtClk;
        }
    }

    @Override
    public void lambda() {
        if(valueAtOut!=null) {
            out.addValue(valueAtOut);
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public void initialize() {
        super.passivate();
    }
}
