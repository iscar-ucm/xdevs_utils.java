/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.projects.mips;

import java.util.HashMap;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;



/**
 *
 * @author José L. Risco Martín
 */
public class Transducer extends Atomic {
    
    public static final String inClkName = "clk";
    public static final String inCodOpName = "codOp";
    public static final String inStopName = "stop";

    protected Port<Integer> inClk = new Port<Integer>(Transducer.inClkName);
    protected Port<Integer> inCodOp = new Port<Integer>(Transducer.inCodOpName);
    protected Port<Integer> inStop = new Port<Integer>(Transducer.inStopName);

    protected double time = 0;
    protected int cycles = 0;
    protected HashMap<Integer, Integer> codOps = new HashMap<Integer, Integer>();
    protected boolean stopped = false;

    // Auxiliary variables
    protected Integer valueAtClk = null;

    public Transducer(String name) {
        super(name);
        super.addInPort(inClk);
        super.addInPort(inCodOp);
        super.addInPort(inStop);
        super.passivate();
    }

    @Override
    public void deltint() {
        super.passivate(); // sigma = infinito, phase = passive
    }

    @Override
    public void deltext(double e) {
        super.resume(e);

        time += e;

        Integer tempValueAtClk = inClk.getSingleValue();
        if(tempValueAtClk!=null) {
            if (valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                cycles++;
            }
            valueAtClk = tempValueAtClk;
        }

        Integer tempCodOp = inCodOp.getSingleValue();
        if(tempCodOp!=null) {
            Integer numCodOp = codOps.get(tempCodOp);
            if(numCodOp==null)
                numCodOp = 1;
            else
                numCodOp++;
            codOps.put(tempCodOp, numCodOp);
        }
        
        if(!inStop.isEmpty()) {
            stopped = true;
            super.holdIn("stop", 0.0);
        }


    }

    @Override
    public void lambda() {
        if(stopped) {
            printReport();
        }
    }

    private void printReport() {
        System.out.println("Execution time: " + time);
        System.out.println("Cycle count: " + cycles);
        Integer totalInstructions = 0;
        for(Integer codOp : codOps.keySet()) {
            Integer num = codOps.get(codOp);
            System.out.println("Instruction type: " + codOp + ", # = " + num);
            totalInstructions += num;
        }
        System.out.println("Total number of instructions: " + totalInstructions);
    }

    @Override
    public void exit() {
    }

    @Override
    public void initialize() {
        super.passivate();
    }
}
