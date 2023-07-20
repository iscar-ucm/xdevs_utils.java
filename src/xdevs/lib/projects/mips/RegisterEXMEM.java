/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.projects.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Jose
 */
public class RegisterEXMEM extends Atomic {

    public static final String inCLKName = "CLK";
    public static final String inRegWrite = "EXRegWrite";
     public static final String inMemToReg = "EXMemToReg";
    public static final String inADDRCP = "EXADDRCP";
    public static final String inRD = "EXRD";
    public static final String inAluRes = "EXAluRes";
    public static final String inAluZero = "EXAluZero";
    public static final String inMemRead = "EXMemRead";
    public static final String inMemWrite = "EXMemWrite";
    public static final String inBranch = "EXBranch";
    public static final String inBusB = "EXBusB";
    public static final String outADDRCP = "MEMADDRCP";
    public static final String outRegWrite = "MEMRegWrite";
    public static final String outMemToReg = "MEMMemToReg";
    public static final String outMemRead = "MEMMemRead";
    public static final String outMemWrite = "MEMMemWrite";
    public static final String outBranch = "MEMBranch";
    public static final String outRD = "MEMRD";
    public static final String outBusB = "MEMBusB";
    public static final String outAluRes = "MEMAluRes";
    public static final String outAluZero = "MEMAluZero";

    protected Port<Integer> CLK = new Port<Integer>(inCLKName);
    protected Port<Integer> EXRegWrite = new Port<Integer>(inRegWrite);
    protected Port<Integer> EXMemToReg = new Port<Integer>(inMemToReg);
    protected Port<Integer> EXADDRCP = new Port<Integer>(inADDRCP);
    protected Port<Integer> EXRD = new Port<Integer>(inRD);
    protected Port<Integer> EXAluRes = new Port<Integer>(inAluRes);
    protected Port<Integer> EXAluZero = new Port<Integer>(inAluZero);
    protected Port<Integer> EXMemRead = new Port<Integer>(inMemRead);
    protected Port<Integer> EXMemWrite = new Port<Integer>(inMemWrite);
    protected Port<Integer> EXBranch = new Port<Integer>(inBranch);
    protected Port<Integer> EXBusB = new Port<Integer>(inBusB);
    protected Port<Integer> MEMADDRCP = new Port<Integer>(outADDRCP);
    protected Port<Integer> MEMRegWrite = new Port<Integer>(outRegWrite);
    protected Port<Integer> MEMMemToReg = new Port<Integer>(outMemToReg);
    protected Port<Integer> MEMMemRead = new Port<Integer>(outMemRead);
    protected Port<Integer> MEMMemWrite = new Port<Integer>(outMemWrite);
    protected Port<Integer> MEMBranch = new Port<Integer>(outBranch);
    protected Port<Integer> MEMAluRes = new Port<Integer>(outAluRes);
    protected Port<Integer> MEMAluZero = new Port<Integer>(outAluZero);
    protected Port<Integer> MEMRD = new Port<Integer>(outRD);
    protected Port<Integer> MEMBusB = new Port<Integer>(outBusB);

    protected Double delayRead;
    protected Double delayWrite;
    protected Integer valueAtClk;
    protected Integer valueAtEXRegWrite;
    protected Integer valueAtEXMemToReg;
    protected Integer valueAtEXADDRCP;
    protected Integer valueAtEXRD;
    protected Integer valueAtEXBusB;
    protected Integer valueAtEXAluRes;
    protected Integer valueAtEXAluZero;
    protected Integer valueAtEXMemRead;
    protected Integer valueAtEXMemWrite;
    protected Integer valueAtEXBranch;
    protected Integer valueAtMEMADDRCP;
    protected Integer valueAtMEMRegWrite;
    protected Integer valueAtMEMMemToReg;
    protected Integer valueAtMEMMemRead;
    protected Integer valueAtMEMMemWrite;
    protected Integer valueAtMEMBranch;
    protected Integer valueAtMEMAluRes;
    protected Integer valueAtMEMAluZero;
    protected Integer valueAtMEMRD;
    protected Integer valueAtMEMBusB;

    public RegisterEXMEM(String name, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(CLK);
        super.addInPort(EXRegWrite);
        super.addInPort(EXMemToReg);
        super.addInPort(EXADDRCP);
        super.addInPort(EXRD);
        super.addInPort(EXBusB);
        super.addInPort(EXAluRes);
        super.addInPort(EXAluZero);
        super.addInPort(EXMemRead);
        super.addInPort(EXMemWrite);
        super.addInPort(EXBranch);
        super.addOutPort(MEMADDRCP);
        super.addOutPort(MEMRegWrite);
        super.addOutPort(MEMMemToReg);
        super.addOutPort(MEMMemRead);
        super.addOutPort(MEMMemWrite);
        super.addOutPort(MEMBranch);
        super.addOutPort(MEMAluRes);
        super.addOutPort(MEMRD);
        super.addOutPort(MEMBusB);
        super.addOutPort(MEMAluZero);
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;
        valueAtClk = null;
        valueAtEXRegWrite = null;
        valueAtEXMemToReg = null;
        valueAtEXADDRCP = null;
        valueAtEXRD = null;
        valueAtEXBusB = null;
        valueAtEXAluRes = null;
        valueAtEXMemRead = null;
        valueAtEXMemWrite = null;
        valueAtEXBranch = null;
        valueAtEXAluZero = null;
        valueAtMEMADDRCP = null;
        valueAtMEMRegWrite = null;
        valueAtMEMMemToReg = null;
        valueAtMEMMemRead = null;
        valueAtMEMMemWrite = null;
        valueAtMEMBranch = null;
        valueAtMEMAluRes = null;
        valueAtMEMRD = null;
        valueAtMEMBusB = null;
        valueAtMEMAluZero = null;
        super.passivate();
    }

    public RegisterEXMEM(String name) {
        this(name,0.0, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate(); // sigma = infinito, phase = passive
    }

    public void deltext(double e) {
        super.resume(e);
        // Primero procesamos los valores de las entradas, lectura asíncrona.

        Integer tempValueAtEXADDRCP = EXADDRCP.getSingleValue();
        if(tempValueAtEXADDRCP!=null && tempValueAtEXADDRCP!=valueAtEXADDRCP) {
            valueAtEXADDRCP = tempValueAtEXADDRCP;
            super.holdIn("Read", delayRead); // phase = Read, sigma = delayRead
        }

        Integer tempValueAtEXRD = EXRD.getSingleValue();
        if(tempValueAtEXRD!=null && tempValueAtEXRD!=valueAtEXRD) {
            valueAtEXRD = tempValueAtEXRD;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtEXBusB = EXBusB.getSingleValue();
        if(tempValueAtEXBusB!=null && tempValueAtEXBusB!=valueAtEXBusB) {
            valueAtEXBusB = tempValueAtEXBusB;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtEXAluRes = EXAluRes.getSingleValue();
        if(tempValueAtEXAluRes!=null && tempValueAtEXAluRes!=valueAtEXAluRes) {
            valueAtEXAluRes = tempValueAtEXAluRes;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtEXMemToReg = EXMemToReg.getSingleValue();
        if(tempValueAtEXMemToReg!=null && tempValueAtEXMemToReg!=valueAtEXMemToReg) {
            valueAtEXMemToReg = tempValueAtEXMemToReg;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtEXMemRead = EXMemRead.getSingleValue();
        if(tempValueAtEXMemRead!=null && tempValueAtEXMemRead!=valueAtEXMemRead) {
            valueAtEXMemRead = tempValueAtEXMemRead;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtEXMemWrite = EXMemWrite.getSingleValue();
        if(tempValueAtEXMemWrite!=null && tempValueAtEXMemWrite!=valueAtEXMemWrite) {
            valueAtEXMemWrite = tempValueAtEXMemWrite;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtEXBranch = EXBranch.getSingleValue();
        if(tempValueAtEXBranch!=null && tempValueAtEXBranch!=valueAtEXBranch) {
            valueAtEXBranch = tempValueAtEXBranch;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtEXAluZero = EXAluZero.getSingleValue();
        if(tempValueAtEXAluZero!=null && tempValueAtEXAluZero!=valueAtEXAluZero) {
            valueAtEXAluZero = tempValueAtEXAluZero;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtEXRegWrite = EXRegWrite.getSingleValue();
        if(tempValueAtEXRegWrite!=null) {
            valueAtEXRegWrite = tempValueAtEXRegWrite;
        }

        // Ahora el reloj, que gobierna la escritura:

        Integer tempValueAtClk = CLK.getSingleValue();
        if(tempValueAtClk!=null) {
            if (valueAtEXRegWrite != null && valueAtEXRegWrite == 1 && valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                if (valueAtEXADDRCP != null && valueAtEXRD != null && valueAtEXBusB != null && valueAtEXAluRes != null && valueAtEXMemToReg != null && valueAtEXMemRead != null && valueAtEXMemWrite != null && valueAtEXBranch != null && valueAtEXAluZero != null) {
                    valueAtMEMADDRCP = valueAtEXADDRCP;
                    valueAtMEMRD = valueAtEXRD;
                    valueAtMEMBusB = valueAtEXBusB;
                    valueAtMEMAluRes = valueAtEXAluRes;
                    valueAtMEMRegWrite = valueAtEXRegWrite;
                    valueAtMEMMemToReg = valueAtEXMemToReg;
                    valueAtMEMMemRead = valueAtEXMemRead;
                    valueAtMEMMemWrite = valueAtEXMemWrite;
                    valueAtMEMBranch = valueAtEXBranch;
                    valueAtMEMAluZero = valueAtEXAluZero;
                }
                super.holdIn("Write", delayWrite);
            }
            valueAtClk = tempValueAtClk;
        }
    }

    @Override
    public void lambda() {
        if(valueAtMEMADDRCP!=null && valueAtMEMRD != null && valueAtMEMBusB != null && valueAtMEMAluRes != null && valueAtMEMRegWrite != null && valueAtMEMMemToReg != null && valueAtMEMMemRead != null && valueAtMEMMemWrite != null && valueAtMEMBranch != null && valueAtMEMAluZero != null) {
            MEMADDRCP.addValue(valueAtMEMADDRCP);
            MEMRD.addValue(valueAtMEMRD);
            MEMBusB.addValue(valueAtMEMBusB);
            MEMAluRes.addValue(valueAtMEMAluRes);
            MEMRegWrite.addValue(valueAtMEMRegWrite);
            MEMMemToReg.addValue(valueAtMEMMemToReg);
            MEMMemRead.addValue(valueAtMEMMemRead);
            MEMMemWrite.addValue(valueAtMEMMemWrite);
            MEMBranch.addValue(valueAtMEMBranch);
            MEMAluZero.addValue(valueAtMEMAluZero);
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
