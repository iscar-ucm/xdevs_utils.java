/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.projects.mips;

import java.util.ArrayList;
import java.util.HashMap;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Jose Roldan Ramirez
 * @author José L. Risco Martín
 */
public class Memory extends Atomic {
    // private static Logger logger = Logger.getLogger(Memory.class.getName());

    public static final String inClkName = "CLK";
    public static final String inAddrName = "ADDR";
    public static final String inDwName = "DW";
    public static final String inMemReadName = "MemRead";
    public static final String inMemWriteName = "MemWrite";
    public static final String outDrName = "DR";
    public static final String outStopName = "stop";

    protected Port<Integer> inClk = new Port<Integer>(inClkName);
    protected Port<Integer> inAddr = new Port<Integer>(inAddrName);
    protected Port<Integer> inDw = new Port<Integer>(inDwName);
    protected Port<Integer> inMemRead = new Port<Integer>(inMemReadName);
    protected Port<Integer> inMemWrite = new Port<Integer>(inMemWriteName);
    protected Port<Object> outDr = new Port<Object>(outDrName);
    protected Port<Integer> outStop = new Port<Integer>(outStopName);

    protected Integer valueAtClk = null;
    protected Integer valueAtAddr = null;
    protected Integer valueAtDw = null;
    protected Integer valueAtMemRead = null;
    protected Integer valueAtMemWrite = null;
    protected Integer valueToDr = null;
    protected Integer valueToStop = null;

    protected ArrayList<String> instructions;
    protected String currentInstructionInBinary;
    protected String currentInstructionInAssembler;
    protected Double delayRead;
    protected Double delayWrite;
    protected HashMap<Integer, Integer> data = new HashMap<Integer, Integer>();
    public HashMap<Integer, Integer> getData() { return data; }
    
    public Memory(String name, ArrayList<String> instructions, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(inClk);
        super.addInPort(inAddr);
        super.addInPort(inDw);
        super.addInPort(inMemWrite);
        super.addInPort(inMemRead);
        super.addOutPort(outDr);
        super.addOutPort(outStop);

        this.instructions = instructions;
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;

        super.passivate();
    }



    public Memory(String name, ArrayList<String> instructions) {
        this(name, instructions, 0.0, 0.0);
    }

    public void deltint() {
        super.passivate();
    }

    public void deltext(double e) {
        super.resume(e);

        // Primero procesamos las señales de lectura asíncrona
        if (!inMemWrite.isEmpty()) {
            valueAtMemWrite = inMemWrite.getSingleValue();
        }

        if (!inMemRead.isEmpty()) {
            valueAtMemRead = inMemRead.getSingleValue();
            if(valueAtMemRead==1)
                super.holdIn("Read", delayRead);
        }

        if (!inDw.isEmpty()) {
            valueAtDw = inDw.getSingleValue();
        }

        if (inAddr.getSingleValue() != null) {
            valueAtAddr = inAddr.getSingleValue();
            if (valueAtAddr >= 0) {
                Integer tempValueAtAddr = valueAtAddr / 4;
                if (tempValueAtAddr < instructions.size()) {
                    String currentInstruction = instructions.get(tempValueAtAddr);
                    int pos = currentInstruction.indexOf(":");
                    this.currentInstructionInBinary = currentInstruction.substring(0, pos);
                    currentInstructionInAssembler = currentInstruction.substring(pos + 1);
                } else {
                    super.holdIn(outStopName, 0.0);
                }
            }
        }
        
        // Ahora el reloj
        Integer tempValueAtCLK = inClk.getSingleValue();
        if (tempValueAtCLK != null) {
            if (valueAtMemWrite != null && valueAtMemWrite == 1 && valueAtClk != null && valueAtClk == 1 && tempValueAtCLK == 0) {
                if (valueAtAddr != null) {
                    data.put(valueAtAddr, valueAtDw);
                    System.out.println("MEM[" + valueAtAddr + "] = " + data.get(valueAtAddr));
                    super.holdIn("Write", delayWrite);
                }
            }
            valueAtClk = tempValueAtCLK;
        }
    }
    

    public void lambda() {
        if (super.phaseIs(outStopName)) {
            outStop.addValue(1);
        } else if (valueAtMemRead != null && valueAtMemRead == 1 && valueAtAddr != null) {
            // Se supone que no hay errores en la memoria.
            if (valueAtAddr >= 0) {
                outDr.addValue(currentInstructionInBinary);
                System.out.println(valueAtAddr + ": " + currentInstructionInAssembler);
            } else {
                outDr.addValue(data.get(valueAtAddr));                
            }
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
