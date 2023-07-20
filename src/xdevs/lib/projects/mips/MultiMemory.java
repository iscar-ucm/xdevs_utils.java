/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.projects.mips;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class MultiMemory extends Coupled {

    public static final String inClkName = "CLK";
    public static final String inAddrName = "ADDR";
    public static final String inDwName = "DW";
    public static final String inMemReadName = "MemRead";
    public static final String inMemWriteName = "MemWrite";
    public static final String outDrName = "DR";

    protected Port<Integer> inClk = new Port<Integer>(inClkName);
    protected Port<Integer> inAddr = new Port<Integer>(inAddrName);
    protected Port<Integer> inDw = new Port<Integer>(inDwName);
    protected Port<Integer> inMemRead = new Port<Integer>(inMemReadName);
    protected Port<Integer> inMemWrite = new Port<Integer>(inMemWriteName);
    protected Port<Integer> outDr = new Port<Integer>(outDrName);

    protected InstructionsMemory insMemory;
    protected DataMemory dataMemory;
    public DataMemory getDataMemory() { return dataMemory; }

    public MultiMemory(String name, ArrayList<String> instructions, double delayRead, double delayWrite, double delayInst) {
        super(name);

        super.addInPort(inClk);
        super.addInPort(inAddr);
        super.addInPort(inDw);
        super.addInPort(inMemWrite);
        super.addInPort(inMemRead);
        super.addOutPort(outDr);

        insMemory = new InstructionsMemory("InsMemory", instructions, delayInst);
        dataMemory = new DataMemory("DataMemory", null, delayRead, delayWrite);
        addComponent(insMemory);
        addComponent(dataMemory);
        addCoupling(inClk, dataMemory.CLK);
        addCoupling(inAddr, insMemory.ADDR);
        addCoupling(inAddr, dataMemory.ADDR);
        addCoupling(inDw, dataMemory.DW);
        addCoupling(inMemWrite, dataMemory.MemWrite);
        addCoupling(inMemRead, dataMemory.MemRead);
        addCoupling(insMemory.DR, outDr);
        addCoupling(dataMemory.DR, outDr);
    }

}
