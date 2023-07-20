/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.projects.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;


/**
 * Este es el nodo que está justo antes de la extensión de signo.
 * @author jlrisco
 */
public class InstructionNode2 extends Atomic {
    //private static Logger logger = Logger.getLogger(InstructionNode2.class.getName());

    public static final String inInName = "in";
    public static final String outOut1500Name = "out1500";
    public static final String outOut0500Name = "out0500";

    protected Port<String> in = new Port<String>(InstructionNode2.inInName);
    protected Port<Integer> out1500 =  new Port<Integer>(InstructionNode2.outOut1500Name);
    protected Port<Integer> out0500 =  new Port<Integer>(InstructionNode2.outOut0500Name);

    protected Integer valueTo1500 = null;
    protected Integer valueTo0500 = null;
    
    public InstructionNode2(String name) {
        super(name);
        super.addInPort(in);
        super.addOutPort(out1500);
        super.addOutPort(out0500);
        super.passivate();
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        super.resume(e);

        String tempValueAtIn = in.getSingleValue();
        if(tempValueAtIn!=null) {
            valueTo0500 = Integer.parseInt(tempValueAtIn.substring(10, 16), 2);
            // Hay que hacer la transformación del inmediato a complemento a 2
            int sum = 0;
            if(tempValueAtIn.charAt(0)=='1')
                sum = -((int)Math.pow(2, 16)); // Hay que restar 2 veces 2^15
            valueTo1500 = Integer.parseInt(tempValueAtIn.substring(0, 16), 2) + sum;
            super.holdIn("active", 0.0);
        }
    }

    @Override
    public void lambda() {
        out1500.addValue(valueTo1500);
        out0500.addValue(valueTo0500);
    }

    @Override
    public void exit() {
    }

    @Override
    public void initialize() {
        super.passivate();
    }

}
