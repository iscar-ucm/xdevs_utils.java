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
public class RegisterMEMWB extends Atomic {

    public static final String inCLKName = "CLK";
    public static final String inRegWrite = "MEMRegWrite";
    public static final String inDRMemDat = "MEMDRMemDat";
    public static final String inRD = "MEMRD";
    public static final String inAluRes = "MEMAluRes";
    public static final String inMemToReg = "MEMMemToReg";
    public static final String outDRMemDat = "WBDRMemDat";
    public static final String outRegWrite = "RegWrite";
    public static final String outMemToReg = "MemToReg";
    public static final String outRD = "WBRD";
    public static final String outAluRes = "WBAluRes";

    protected Port<Integer> CLK = new Port<Integer>(inCLKName);
    protected Port<Integer> MEMRegWrite = new Port<Integer>(inRegWrite);
    protected Port<Integer> MEMDRMemDat = new Port<Integer>(inDRMemDat);
    protected Port<Integer> MEMRD = new Port<Integer>(inRD);
    protected Port<Integer> MEMAluRes = new Port<Integer>(inAluRes);
    protected Port<Integer> MEMMemToReg = new Port<Integer>(inMemToReg);
    protected Port<Integer> WBDRMemDat = new Port<Integer>(outDRMemDat);
    protected Port<Integer> WBRegWrite = new Port<Integer>(outRegWrite);
    protected Port<Integer> WBMemToReg = new Port<Integer>(outMemToReg);
    protected Port<Integer> WBAluRes = new Port<Integer>(outAluRes);
    protected Port<Integer> WBRD = new Port<Integer>(outRD);

    protected Double delayRead;
    protected Double delayWrite;
    protected Integer valueAtClk;
    protected Integer valueAtMEMRegWrite;
    protected Integer valueAtMEMMemToReg;
    protected Integer valueAtMEMDRMemDat;
    protected Integer valueAtMEMRD;
    protected Integer valueAtMEMAluRes;
    protected Integer valueAtWBDRMemDat;
    protected Integer valueAtWBRegWrite;
    protected Integer valueAtWBMemToReg;
    protected Integer valueAtWBAluRes;
    protected Integer valueAtWBRD;

    public RegisterMEMWB(String name, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(CLK);
        super.addInPort(MEMRegWrite);
        super.addInPort(MEMMemToReg);
        super.addInPort(MEMDRMemDat);
        super.addInPort(MEMRD);
        super.addInPort(MEMAluRes);
        super.addOutPort(WBDRMemDat);
        super.addOutPort(WBRegWrite);
        super.addOutPort(WBMemToReg);
        super.addOutPort(WBAluRes);
        super.addOutPort(WBRD);
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;
        valueAtClk = null;
        valueAtMEMRegWrite = null;
        valueAtMEMMemToReg = null;
        valueAtMEMDRMemDat = null;
        valueAtMEMRD = null;
        valueAtMEMAluRes = null;
        valueAtWBDRMemDat = null;
        valueAtWBRegWrite = null;
        valueAtWBMemToReg = null;
        valueAtWBAluRes = null;
        valueAtWBRD = null;
        super.passivate();
    }

    public RegisterMEMWB(String name) {
        this(name,0.0, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate(); // sigma = infinito, phase = passive
    }

    public void deltext(double e) {
        super.resume(e);
        // Primero procesamos los valores de las entradas, lectura asíncrona.

        Integer tempValueAtMEMDRMemDat = MEMDRMemDat.getSingleValue();
        if(tempValueAtMEMDRMemDat!=null && tempValueAtMEMDRMemDat!=valueAtMEMDRMemDat) {
            valueAtMEMDRMemDat = tempValueAtMEMDRMemDat;
            super.holdIn("Read", delayRead); // phase = Read, sigma = delayRead
        }

        Integer tempValueAtMEMRD = MEMRD.getSingleValue();
        if(tempValueAtMEMRD!=null && tempValueAtMEMRD!=valueAtMEMRD) {
            valueAtMEMRD = tempValueAtMEMRD;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtMEMAluRes = MEMAluRes.getSingleValue();
        if(tempValueAtMEMAluRes!=null && tempValueAtMEMAluRes!=valueAtMEMAluRes) {
            valueAtMEMAluRes = tempValueAtMEMAluRes;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtMEMMemToReg = MEMMemToReg.getSingleValue();
        if(tempValueAtMEMMemToReg!=null && tempValueAtMEMMemToReg!=valueAtMEMMemToReg) {
            valueAtMEMMemToReg = tempValueAtMEMMemToReg;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtMEMRegWrite = MEMRegWrite.getSingleValue();
        if(tempValueAtMEMRegWrite!=null) {
            valueAtMEMRegWrite = tempValueAtMEMRegWrite;
        }

        // Ahora el reloj, que gobierna la escritura:

        Integer tempValueAtClk = CLK.getSingleValue();
        if(tempValueAtClk!=null) {
            if (valueAtMEMRegWrite != null && valueAtMEMRegWrite == 1 && valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                if (valueAtMEMDRMemDat != null && valueAtMEMRD != null && valueAtMEMAluRes != null && valueAtMEMMemToReg != null) {
                    valueAtWBDRMemDat = valueAtMEMDRMemDat;
                    valueAtWBRD = valueAtMEMRD;
                    valueAtWBAluRes = valueAtMEMAluRes;
                    valueAtWBMemToReg = valueAtMEMMemToReg;
                    valueAtWBRegWrite = valueAtMEMRegWrite;
                }
                super.holdIn("Write", delayWrite);
            }
            valueAtClk = tempValueAtClk;
        }
    }

    @Override
    public void lambda() {
        if(valueAtWBDRMemDat!=null && valueAtWBRD != null && valueAtWBAluRes != null && valueAtWBRegWrite != null && valueAtWBMemToReg != null) {
            WBDRMemDat.addValue(valueAtWBDRMemDat);
            WBRD.addValue(valueAtWBRD);
            WBAluRes.addValue(valueAtWBAluRes);
            WBMemToReg.addValue(valueAtWBMemToReg);
            WBRegWrite.addValue(valueAtWBRegWrite);
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
