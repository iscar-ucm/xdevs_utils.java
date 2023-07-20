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
public class RegisterIFID extends Atomic {

    public static final String inCLKName = "CLK";
    public static final String inRegWrite = "RegWrite";
    public static final String inDR = "IFDR";
    public static final String inADDRCP = "IFAddrCP";
    public static final String outDR = "IDDR";
    public static final String outADDRCP = "IDADDRCP";

    protected Port<Integer> CLK = new Port<Integer>(inCLKName);
    protected Port<Integer> RegWrite = new Port<Integer>(inRegWrite);
    protected Port<Integer> IFDR = new Port<Integer>(inDR);
    protected Port<Integer> IFADDRCP = new Port<Integer>(inADDRCP);
    protected Port<Integer> IDDR = new Port<Integer>(outDR);
    protected Port<Integer> IDADDRCP = new Port<Integer>(outADDRCP);

    protected Double delayRead;
    protected Double delayWrite;
    protected Integer valueAtClk;
    protected Integer valueAtRegWrite;
    protected Integer valueAtIFDR;
    protected Integer valueAtIFADDRCP;
    protected Integer valueAtIDDR;
    protected Integer valueAtIDADDRCP;

    public RegisterIFID(String name, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(CLK);
        super.addInPort(RegWrite);
        super.addInPort(IFDR);
        super.addInPort(IFADDRCP);
        super.addOutPort(IDDR);
        super.addOutPort(IDADDRCP);
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;
        valueAtClk = null;
        valueAtRegWrite = null;
        valueAtIFDR = null;
        valueAtIFADDRCP = null;
        valueAtIDDR = null;
        valueAtIDADDRCP = null;
        super.passivate();
    }

    public RegisterIFID(String name) {
        this(name,0.0, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate(); // sigma = infinito, phase = passive
    }

    public void deltext(double e) {
        super.resume(e);
        // Primero procesamos los valores de las entradas, lectura asíncrona.

        Integer tempValueAtIFDR = IFDR.getSingleValue();
        if(tempValueAtIFDR!=null && tempValueAtIFDR!=valueAtIFDR) {
            valueAtIFDR = tempValueAtIFDR;
            super.holdIn("Read", delayRead); // phase = Read, sigma = delayRead
        }

        Integer tempValueAtIFADDRCP = IFADDRCP.getSingleValue();
        if(tempValueAtIFADDRCP!=null && tempValueAtIFADDRCP!=valueAtIFADDRCP) {
            valueAtIFADDRCP = tempValueAtIFADDRCP;
            super.holdIn("Read", delayRead);
        }

        Integer tempValueAtRegWrite = RegWrite.getSingleValue();
        if(tempValueAtRegWrite!=null) {
            valueAtRegWrite = tempValueAtRegWrite;
        }

        // Ahora el reloj, que gobierna la escritura:

        Integer tempValueAtClk = CLK.getSingleValue();
        if(tempValueAtClk!=null) {
            if (valueAtRegWrite != null && valueAtRegWrite == 1 && valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                if (valueAtIFDR != null && valueAtIFADDRCP != null) {
                    valueAtIDDR = valueAtIFDR;
                    valueAtIDADDRCP = valueAtIFADDRCP;
                }
                super.holdIn("Write", delayWrite);
            }
            valueAtClk = tempValueAtClk;
        }
    }

    @Override
    public void lambda() {
        if(valueAtIDDR!=null && valueAtIDADDRCP != null) {
            IDDR.addValue(valueAtIDDR);
            IDADDRCP.addValue(valueAtIDADDRCP);
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
