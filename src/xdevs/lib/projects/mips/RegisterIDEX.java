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
public class RegisterIDEX extends Atomic {

    public static final String inCLKName = "CLK";
    public static final String inRegWrite = "IDRegWrite";
    public static final String inMemToReg = "IDMemToReg";
    public static final String inADDRCP = "IDADDRCP";
    public static final String inRD = "IDRD";
    public static final String inRS = "IDRS";
    public static final String inRT = "IDRT";
    public static final String inbusA = "IDBusA";
    public static final String inbusB = "IDBusB";
    public static final String inMemRead = "IDMemRead";
    public static final String inMemWrite = "IDMemWrite";
    public static final String inBranch = "IDBranch";
    public static final String inRegDst = "IDRegDst";
    public static final String inALUOp = "IDALUOp";
    public static final String inALUFunct = "IDALUFunct";
    public static final String inALUSrc = "IDALUSrc";
    public static final String inExtSig = "IDExtSig";
    public static final String outADDRCP = "EXADDRCP";
    public static final String outRegWrite = "EXRegWrite";
    public static final String outMemToReg = "EXMemToReg";
    public static final String outMemRead = "EXMemRead";
    public static final String outMemWrite = "EXMemWrite";
    public static final String outBranch = "EXBranch";
    public static final String outRegDst = "EXRegDst";
    public static final String outALUOp = "EXALUOp";
    public static final String outALUFunct = "EXALUFunct";
    public static final String outALUSrc = "EXALUSrc";
    public static final String outRD = "EXRD";
    public static final String outRS = "EXRS";
    public static final String outRT = "EXRT";
    public static final String outbusA = "EXBusA";
    public static final String outbusB = "EXBusB";
    public static final String outExtSig = "EXExtSig";

    protected Port<Integer> CLK = new Port<Integer>(inCLKName);
    protected Port<Integer> IDRegWrite = new Port<Integer>(inRegWrite);
    protected Port<Integer> IDMemToReg = new Port<Integer>(inMemToReg);
    protected Port<Integer> IDADDRCP = new Port<Integer>(inADDRCP);
    protected Port<Integer> IDRD = new Port<Integer>(inRD);
    protected Port<Integer> IDRS = new Port<Integer>(inRS);
    protected Port<Integer> IDRT = new Port<Integer>(inRT);
    protected Port<Integer> IDBusA = new Port<Integer>(inbusA);
    protected Port<Integer> IDBusB = new Port<Integer>(inbusB);
    protected Port<Integer> IDMemRead = new Port<Integer>(inMemRead);
    protected Port<Integer> IDMemWrite = new Port<Integer>(inMemWrite);
    protected Port<Integer> IDBranch = new Port<Integer>(inBranch);
    protected Port<Integer> IDRegDst = new Port<Integer>(inRegDst);
    protected Port<Integer> IDALUOp = new Port<Integer>(inALUOp);
    protected Port<Integer> IDALUFunct = new Port<Integer>(inALUFunct);
    protected Port<Integer> IDALUSrc = new Port<Integer>(inALUSrc);
    protected Port<Integer> IDExtSig = new Port<Integer>(inExtSig);
    protected Port<Integer> EXADDRCP = new Port<Integer>(outADDRCP);
    protected Port<Integer> EXRegWrite = new Port<Integer>(outRegWrite);
    protected Port<Integer> EXMemToReg = new Port<Integer>(outMemToReg);
    protected Port<Integer> EXMemRead = new Port<Integer>(outMemRead);
    protected Port<Integer> EXMemWrite = new Port<Integer>(outMemWrite);
    protected Port<Integer> EXBranch = new Port<Integer>(outBranch);
    protected Port<Integer> EXRegDst = new Port<Integer>(outRegDst);
    protected Port<Integer> EXALUOp = new Port<Integer>(outALUOp);
    protected Port<Integer> EXALUFunct = new Port<Integer>(outALUFunct);
    protected Port<Integer> EXALUSrc = new Port<Integer>(outALUSrc);
    protected Port<Integer> EXRD = new Port<Integer>(outRD);
    protected Port<Integer> EXRS = new Port<Integer>(outRS);
    protected Port<Integer> EXRT = new Port<Integer>(outRT);
    protected Port<Integer> EXBusA = new Port<Integer>(outbusA);
    protected Port<Integer> EXBusB = new Port<Integer>(outbusB);
    protected Port<Integer> EXExtSig = new Port<Integer>(outExtSig);

    protected Double delayRead;
    protected Double delayWrite;
    protected Integer valueAtClk;
    protected Integer valueAtIDRegWrite;
    protected Integer valueAtIDMemToReg;
    protected Integer valueAtIDADDRCP;
    protected Integer valueAtIDRD;
    protected Integer valueAtIDRS;
    protected Integer valueAtIDRT;
    protected Integer valueAtIDBusA;
    protected Integer valueAtIDBusB;
    protected Integer valueAtIDMemRead;
    protected Integer valueAtIDMemWrite;
    protected Integer valueAtIDBranch;
    protected Integer valueAtIDRegDst;
    protected Integer valueAtIDALUOp;
    protected Integer valueAtIDALUFunct;
    protected Integer valueAtIDALUSrc;
    protected Integer valueAtIDExtSig;
    protected Integer valueAtEXADDRCP;
    protected Integer valueAtEXRegWrite;
    protected Integer valueAtEXMemToReg;
    protected Integer valueAtEXMemRead;
    protected Integer valueAtEXMemWrite;
    protected Integer valueAtEXBranch;
    protected Integer valueAtEXRegDst;
    protected Integer valueAtEXALUOp;
    protected Integer valueAtEXALUFunct;
    protected Integer valueAtEXALUSrc;
    protected Integer valueAtEXRD;
    protected Integer valueAtEXRS;
    protected Integer valueAtEXRT;
    protected Integer valueAtEXBusA;
    protected Integer valueAtEXBusB;
    protected Integer valueAtEXExtSig;

    public RegisterIDEX(String name, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(CLK);
        super.addInPort(IDRegWrite);
        super.addInPort(IDMemToReg);
        super.addInPort(IDADDRCP);
        super.addInPort(IDRD);
        super.addInPort(IDRS);
        super.addInPort(IDRT);
        super.addInPort(IDBusA);
        super.addInPort(IDBusB);
        super.addInPort(IDMemRead);
        super.addInPort(IDMemWrite);
        super.addInPort(IDBranch);
        super.addInPort(IDRegDst);
        super.addInPort(IDALUOp);
        super.addInPort(IDALUFunct);
        super.addInPort(IDALUSrc);
        super.addInPort(IDExtSig);
        super.addOutPort(EXADDRCP);
        super.addOutPort(EXRegWrite);
        super.addOutPort(EXMemToReg);
        super.addOutPort(EXMemRead);
        super.addOutPort(EXMemWrite);
        super.addOutPort(EXBranch);
        super.addOutPort(EXRegDst);
        super.addOutPort(EXALUOp);
        super.addOutPort(EXALUFunct);
        super.addOutPort(EXALUSrc);
        super.addOutPort(EXRD);
        super.addOutPort(EXRS);
        super.addOutPort(EXRT);
        super.addOutPort(EXBusA);
        super.addOutPort(EXBusB);
        super.addOutPort(EXExtSig);
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;
        valueAtClk = null;
        valueAtIDRegWrite = null;
        valueAtIDMemToReg = null;
        valueAtIDADDRCP = null;
        valueAtIDRD = null;
        valueAtIDRS = null;
        valueAtIDRT = null;
        valueAtIDBusA = null;
        valueAtIDBusB = null;
        valueAtIDMemRead = null;
        valueAtIDMemWrite = null;
        valueAtIDBranch = null;
        valueAtIDRegDst = null;
        valueAtIDALUOp = null;
        valueAtIDALUFunct = null;
        valueAtIDALUSrc = null;
        valueAtIDExtSig = null;
        valueAtEXADDRCP = null;
        valueAtEXRegWrite = null;
        valueAtEXMemToReg = null;
        valueAtEXMemRead = null;
        valueAtEXMemWrite = null;
        valueAtEXBranch = null;
        valueAtEXRegDst = null;
        valueAtEXALUOp = null;
        valueAtEXALUFunct = null;
        valueAtEXALUSrc = null;
        valueAtEXRD = null;
        valueAtEXRS = null;
        valueAtEXRT = null;
        valueAtEXBusA = null;
        valueAtEXBusB = null;
        valueAtEXExtSig = null;
        super.passivate();
    }

    public RegisterIDEX(String name) {
        this(name,0.0, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate(); // sigma = infinito, phase = passive
    }

    public void deltext(double e) {
        super.resume(e);
        // Primero procesamos los valores de las entradas, lectura asíncrona.

        Integer tempValueAtIDADDRCP = IDADDRCP.getSingleValue();
        if(tempValueAtIDADDRCP!=null && tempValueAtIDADDRCP!=valueAtIDADDRCP) {
            valueAtIDADDRCP = tempValueAtIDADDRCP;
            super.holdIn("Read", delayRead); // phase = Read, sigma = delayRead
        }

        Integer tempValueAtIDRD = IDRD.getSingleValue();
        if(tempValueAtIDRD!=null && tempValueAtIDRD!=valueAtIDRD) {
            valueAtIDRD = tempValueAtIDRD;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDRS = IDRS.getSingleValue();
        if(tempValueAtIDRS!=null && tempValueAtIDRS!=valueAtIDRS) {
            valueAtIDRS = tempValueAtIDRS;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDRT = IDRT.getSingleValue();
        if(tempValueAtIDRT!=null && tempValueAtIDRT!=valueAtIDRT) {
            valueAtIDRT = tempValueAtIDRT;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDBusA = IDBusA.getSingleValue();
        if(tempValueAtIDBusA!=null && tempValueAtIDBusA!=valueAtIDBusA) {
            valueAtIDBusA = tempValueAtIDBusA;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDBusB = IDBusB.getSingleValue();
        if(tempValueAtIDBusB!=null && tempValueAtIDBusB!=valueAtIDBusB) {
            valueAtIDBusB = tempValueAtIDBusB;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDMemToReg = IDMemToReg.getSingleValue();
        if(tempValueAtIDMemToReg!=null && tempValueAtIDMemToReg!=valueAtIDMemToReg) {
            valueAtIDMemToReg = tempValueAtIDMemToReg;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDMemRead = IDMemRead.getSingleValue();
        if(tempValueAtIDMemRead!=null && tempValueAtIDMemRead!=valueAtIDMemRead) {
            valueAtIDMemRead = tempValueAtIDMemRead;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDMemWrite = IDMemWrite.getSingleValue();
        if(tempValueAtIDMemWrite!=null && tempValueAtIDMemWrite!=valueAtIDMemWrite) {
            valueAtIDMemWrite = tempValueAtIDMemWrite;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDBranch = IDBranch.getSingleValue();
        if(tempValueAtIDBranch!=null && tempValueAtIDBranch!=valueAtIDBranch) {
            valueAtIDBranch = tempValueAtIDBranch;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDRegDst = IDRegDst.getSingleValue();
        if(tempValueAtIDRegDst!=null && tempValueAtIDRegDst!=valueAtIDRegDst) {
            valueAtIDRegDst = tempValueAtIDRegDst;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDALUOp = IDALUOp.getSingleValue();
        if(tempValueAtIDALUOp!=null && tempValueAtIDALUOp!=valueAtIDALUOp) {
            valueAtIDALUOp = tempValueAtIDALUOp;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDALUFunct = IDALUFunct.getSingleValue();
        if(tempValueAtIDALUFunct!=null && tempValueAtIDALUFunct!=valueAtIDALUFunct) {
            valueAtIDALUFunct = tempValueAtIDALUFunct;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDALUSrc = IDALUSrc.getSingleValue();
        if(tempValueAtIDALUSrc!=null && tempValueAtIDALUSrc!=valueAtIDALUSrc) {
            valueAtIDALUSrc = tempValueAtIDALUSrc;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDExtSig = IDExtSig.getSingleValue();
        if(tempValueAtIDExtSig!=null && tempValueAtIDExtSig!=valueAtIDExtSig) {
            valueAtIDExtSig = tempValueAtIDExtSig;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtIDRegWrite = IDRegWrite.getSingleValue();
        if(tempValueAtIDRegWrite!=null) {
            valueAtIDRegWrite = tempValueAtIDRegWrite;
        }

        // Ahora el reloj, que gobierna la escritura:

        Integer tempValueAtClk = CLK.getSingleValue();
        if(tempValueAtClk!=null) {
            if (valueAtIDRegWrite != null && valueAtIDRegWrite == 1 && valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                if (valueAtIDADDRCP != null && valueAtIDRD != null && valueAtIDRS != null && valueAtIDRT != null && valueAtIDBusA != null && valueAtIDBusB != null && valueAtIDMemToReg != null && valueAtIDMemRead != null && valueAtIDMemWrite != null && valueAtIDBranch != null && valueAtIDRegDst != null && valueAtIDALUOp != null && valueAtIDALUFunct != null && valueAtIDALUSrc != null && valueAtIDExtSig != null) {
                    valueAtEXADDRCP = valueAtIDADDRCP;
                    valueAtEXRD = valueAtIDRD;
                    valueAtEXRS = valueAtIDRS;
                    valueAtEXRT = valueAtIDRT;
                    valueAtEXBusA = valueAtIDBusA;
                    valueAtEXBusB = valueAtIDBusB;
                    valueAtEXRegWrite = valueAtIDRegWrite;
                    valueAtEXMemToReg = valueAtIDMemToReg;
                    valueAtEXMemRead = valueAtIDMemRead;
                    valueAtEXMemWrite = valueAtIDMemWrite;
                    valueAtEXBranch = valueAtIDBranch;
                    valueAtEXRegDst = valueAtIDRegDst;
                    valueAtEXALUOp = valueAtIDALUOp;
                    valueAtEXALUFunct = valueAtIDALUFunct;
                    valueAtEXALUSrc = valueAtIDALUSrc;
                    valueAtEXExtSig = valueAtIDExtSig;
                }
                super.holdIn("Write", delayWrite);
            }
            valueAtClk = tempValueAtClk;
        }
    }

    @Override
    public void lambda() {
        if(valueAtEXADDRCP!=null && valueAtEXRD != null && valueAtEXRS != null && valueAtEXRT != null && valueAtEXBusA != null && valueAtEXBusB != null && valueAtEXRegWrite != null && valueAtEXMemToReg != null && valueAtEXMemRead != null && valueAtEXMemWrite != null && valueAtEXBranch != null && valueAtEXRegDst != null && valueAtEXALUOp != null && valueAtEXALUFunct != null && valueAtEXALUSrc != null && valueAtEXExtSig != null) {
            EXADDRCP.addValue(valueAtEXADDRCP);
            EXRD.addValue(valueAtEXRD);
            EXRD.addValue(valueAtEXRS);
            EXRD.addValue(valueAtEXRT);
            EXBusA.addValue(valueAtEXBusA);
            EXBusB.addValue(valueAtEXBusB);
            EXRegWrite.addValue(valueAtEXRegWrite);
            EXMemToReg.addValue(valueAtEXMemToReg);
            EXMemRead.addValue(valueAtEXMemRead);
            EXMemWrite.addValue(valueAtEXMemWrite);
            EXBranch.addValue(valueAtEXBranch);
            EXRegDst.addValue(valueAtEXRegDst);
            EXALUOp.addValue(valueAtEXALUOp);
            EXALUFunct.addValue(valueAtEXALUFunct);
            EXALUSrc.addValue(valueAtEXALUSrc);
            EXExtSig.addValue(valueAtEXExtSig);
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
