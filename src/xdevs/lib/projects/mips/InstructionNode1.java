/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.projects.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;


/**
 * Este es el nodo que está justo después de la memoria de instrucciones.
 * Separa la instrucción originalmente en binario en varias partes, traduciento
 * la información a tipo entero
 * @author jlrisco
 */
public class InstructionNode1 extends Atomic {

    public static final String inInName = "in";
    public static final String outOut3126Name = "out3126";
    public static final String outOut2521Name = "out2511";
    public static final String outOut2016Name = "out2016";
    public static final String outOut1511Name = "out1511";
    public static final String outOut1500Name = "out1500";

    protected Port<String> in;
    protected Port<Integer> out3126;
    protected Port<Integer> out2521;
    protected Port<Integer> out2016;
    protected Port<Integer> out1511;
    protected Port<String> out1500;

    protected String valueAtIn = null;
    
    public InstructionNode1(String name) {
        super(name);
        in = new Port<String>(InstructionNode1.inInName);
        out3126 = new Port<Integer>(InstructionNode1.outOut3126Name);
        out2521 = new Port<Integer>(InstructionNode1.outOut2521Name);
        out2016 = new Port<Integer>(InstructionNode1.outOut2016Name);
        out1511 = new Port<Integer>(InstructionNode1.outOut1511Name);
        out1500 = new Port<String>(InstructionNode1.outOut1500Name);
        super.addInPort(in);
        super.addOutPort(out3126);
        super.addOutPort(out2521);
        super.addOutPort(out2016);
        super.addOutPort(out1511);
        super.addOutPort(out1500);
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
            valueAtIn = tempValueAtIn;
            super.holdIn("active", 0.0);
        }
    }

    @Override
    public void lambda() {
        out3126.addValue(Integer.parseInt(valueAtIn.substring(0, 6), 2)); //salida 1 de node1
        out2521.addValue(Integer.parseInt(valueAtIn.substring(6, 11), 2)); // salida2 de node1
        out2016.addValue(Integer.parseInt(valueAtIn.substring(11, 16), 2));// salida 3 de node1
        out1511.addValue(Integer.parseInt(valueAtIn.substring(16, 21), 2)); // salida a ALU de node2
        out1500.addValue(valueAtIn.substring(16, 32)); //  salida 4 del node
    }

    @Override
    public void exit() {
    }

    @Override
    public void initialize() {
        super.passivate();
    }

}
