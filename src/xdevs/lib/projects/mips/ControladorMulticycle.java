package xdevs.lib.projects.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Francisco Calvo
 */
public class ControladorMulticycle extends Atomic {

    public static final String inOpName = "op";
    public static final String inFunctName = "funct";
    public static final String inZeroName = "zero";
    public static final String inLessThanName = "lessThan";

    public static final String outMemReadName = "memRead";
    public static final String outMemWriteName = "memWrite";
    public static final String outMDRWriteName = "MDRWrite";
    public static final String outIRWriteName = "IRWrite";
    public static final String outRegDstName = "regDst";
    public static final String outRegWriteName = "regWrite";
    public static final String outAWriteName = "AWrite";
    public static final String outBWriteName = "BWrite";
    public static final String outPCWriteName = "PCWrite";
    public static final String outAluCtrlName = "ALUCtrl";
    public static final String outPcSrcName = "PCSrc";
    public static final String outOutWriteName = "OUTWrite";
    public static final String outIorDName = "IorD";
    public static final String outMemtoRegName = "memtoReg";
    public static final String outALUSrcAName = "ALUSrcA";
    public static final String outALUSrcBName = "ALUSrcB";

    public static final String inClkName = "clk";
    public Port<Integer> clk = new Port<Integer>(ControladorMulticycle.inClkName);
    protected Integer valueAtClk = null;
    
    public Port<Integer> portOp;
    public Port<Integer> portFunct;
    public Port<Integer> portZero;
    public Port<Integer> inLessThan = new Port<Integer>(ControladorMulticycle.inLessThanName);

    public Port<Integer> portMemRead;
    public Port<Integer> portMemWrite;
    public Port<Integer> portMDRWrite;
    public Port<Integer> portIRWrite;
    public Port<Integer> portRegDst;
    public Port<Integer> portRegWrite;
    public Port<Integer> portAWrite;
    public Port<Integer> portBWrite;
    public Port<Integer> portPCWrite;
    public Port<Integer> outAluCtrl;
    public Port<Integer> outPcSrc = new Port<Integer>(ControladorMulticycle.outPcSrcName);
    public Port<Integer> portOutWrite;
    public Port<Integer> portIorD;
    public Port<Integer> portMemtoReg;
    public Port<Integer> portALUSrcA;
    public Port<Integer> portALUSrcB;

    protected Integer currentState = 0;
    protected Double delay;
    protected Integer valueAtOp;
    protected Integer valueAtFunct;
    protected Integer valueAtZero;
    protected Integer valueAtLessThan = null;

    public ControladorMulticycle(String name, Double delay) {
        super(name);
        portOp = new Port<Integer>(ControladorMulticycle.inOpName);
        portFunct = new Port<Integer>(ControladorMulticycle.inFunctName);
        portZero = new Port<Integer>(ControladorMulticycle.inZeroName);

        portMemRead = new Port<Integer>(ControladorMulticycle.outMemReadName);
        portMemWrite = new Port<Integer>(ControladorMulticycle.outMemWriteName);
        portMDRWrite = new Port<Integer>(ControladorMulticycle.outMDRWriteName);
        portIRWrite = new Port<Integer>(ControladorMulticycle.outIRWriteName);
        portRegDst = new Port<Integer>(ControladorMulticycle.outRegDstName);
        portRegWrite = new Port<Integer>(ControladorMulticycle.outRegWriteName);
        portAWrite = new Port<Integer>(ControladorMulticycle.outAWriteName);
        portBWrite = new Port<Integer>(ControladorMulticycle.outBWriteName);
        portPCWrite = new Port<Integer>(ControladorMulticycle.outPCWriteName);
        outAluCtrl = new Port<Integer>(ControladorMulticycle.outAluCtrlName);
        portOutWrite = new Port<Integer>(ControladorMulticycle.outOutWriteName);
        portIorD = new Port<Integer>(ControladorMulticycle.outIorDName);
        portMemtoReg = new Port<Integer>(ControladorMulticycle.outMemtoRegName);
        portALUSrcA = new Port<Integer>(ControladorMulticycle.outALUSrcAName);
        portALUSrcB = new Port<Integer>(ControladorMulticycle.outALUSrcBName);


        super.addInPort(clk);
        super.addInPort(portOp);
        super.addInPort(portFunct);
        super.addInPort(portZero);
        super.addInPort(inLessThan);

        super.addOutPort(portMemRead);
        super.addOutPort(portMemWrite);
        super.addOutPort(portMDRWrite);
        super.addOutPort(portIRWrite);
        super.addOutPort(portRegDst);
        super.addOutPort(portRegWrite);
        super.addOutPort(portAWrite);
        super.addOutPort(portBWrite);
        super.addOutPort(portPCWrite);
        super.addOutPort(outAluCtrl);
        super.addOutPort(outPcSrc);
        super.addOutPort(portOutWrite);
        super.addOutPort(portIorD);
        super.addOutPort(portMemtoReg);
        super.addOutPort(portALUSrcA);
        super.addOutPort(portALUSrcB);

        this.delay = delay;
        valueAtOp = null;
        valueAtFunct = null;
        valueAtZero = null;

        super.holdIn("active", delay);
    }

    public ControladorMulticycle(String name) {
        this(name, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        super.resume(e);
        // Primero almacenamos los valores que puedan llegar por OP, FUNCT y ZERO
        if(!portOp.isEmpty()) {
            valueAtOp = portOp.getSingleValue();
        }
        if(!portFunct.isEmpty()) {
            valueAtFunct = portFunct.getSingleValue();
        }
        if(!portZero.isEmpty()) {
            valueAtZero = portZero.getSingleValue();
        }
        if(!inLessThan.isEmpty()) {
            valueAtLessThan = inLessThan.getSingleValue();
        }

        // Realizamos transición de estado si llega señal de CLK
        Integer tempValueAtClk = clk.getSingleValue();
        if(tempValueAtClk!=null) {
            if (valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                Integer nextState = 0;
                if(currentState==0) {
                    nextState = 1;
                }
                else if(currentState==1) {
                    if ((valueAtOp == 0) && (valueAtFunct == 8)) { // jr
                        nextState = 13;
                    }
                    else if(valueAtOp==0) // tipo R
                        nextState = 7;
                    else if (valueAtOp == 2) // j
                        nextState = 17;
                    else if(valueAtOp==4) // beq
                        nextState = 9;
                    else if(valueAtOp==9) // addiu
                        nextState = 11;
                    else if (valueAtOp == 10) // slti
                        nextState = 14;
                    else if(valueAtOp==35) // lw
                        nextState = 2;
                    else if(valueAtOp==43) // sw
                        nextState = 5;
                    else
                        System.err.println("(op) not implemented yet: (" + valueAtOp + ")");


                }
                else if(currentState==2)
                    nextState = 3;
                else if(currentState==3)
                    nextState = 4;
                else if(currentState==4)
                    nextState = 0;
                else if(currentState==5)
                    nextState = 6;
                else if(currentState==6)
                    nextState = 0;
                else if(currentState==7)
                    nextState = 8;
                else if(currentState==8)
                    nextState = 0;
                else if(currentState==9) {
                    if(valueAtZero==1)
                        nextState = 10;
                    else
                        nextState = 0;
                }
                else if(currentState==10)
                    nextState = 0;
                else if(currentState==11)
                    nextState = 12;
                else if(currentState==12)
                    nextState = 0;
                else if(currentState==13)
                    nextState = 0;
                else if(currentState==14) {
                    if(valueAtLessThan==1)
                        nextState = 15;
                    else
                        nextState = 16;
                }
                else if(currentState==15)
                    nextState = 0;
                else if(currentState==16)
                    nextState = 0;
                else if(currentState==17)
                    nextState = 0;
                currentState = nextState;
                super.holdIn("active", delay);
            }
            valueAtClk = tempValueAtClk;
        }

}

    @Override
    public void lambda() {
        Integer PCWrite = 0;
        Integer IorD = 0;
        Integer MemWrite = 0;
        Integer MemRead = 0;
        Integer IRWrite = 0;
        Integer MDRWrite = 0;
        Integer RegDst = 0;
        Integer MemToReg = 0;
        Integer RegWrite = 0;
        Integer AWrite = 0;
        Integer BWrite = 0;
        Integer ALUSrcA = 0;
        Integer ALUSrcB = 0;
        Integer ALUCtrl = ALU.ALU_ADD;
        Integer OutWrite = 0;
        System.out.println("Controlador = " + currentState);
        switch (currentState){
            case 0: // IR <- MEM[PC]; PC <- PC + 4
                IRWrite = 1;
                MemRead = 1;
                PCWrite = 1;
                ALUSrcA = 0;
                ALUSrcB = 1;
                ALUCtrl = ALU.ALU_ADD;
                outPcSrc.addValue(0);
                break;
            case 1: // A <- BR[rs], B <- BR[rt]                
                AWrite = 1;
                BWrite = 1;
                break;
            case 2: // ALUOut <- A + SignExt(inmed)
                ALUSrcA = 1;
                ALUSrcB = 2;
                ALUCtrl = ALU.ALU_ADD;
                OutWrite = 1;
                break;
            case 3: // MDR <- MEM[ALUout]
                MDRWrite = 1;
                MemRead = 1;
                IorD = 1;
                break;
            case 4: // BR[rt] <- MDR
                RegWrite = 1;
                RegDst = 0;
                MemToReg = 1;
                break;
            case 5: // ALUOut <- A + SignExt(inmed)
                ALUSrcA = 1;
                ALUSrcB = 2;
                ALUCtrl = ALU.ALU_ADD;
                OutWrite = 1;
                break;
            case 6: // MEM[ALUOut] <- B
                MemWrite = 1;
                IorD = 1;
                break;
            case 7: // ALUOut <- A op B
                ALUSrcA = 1;
                ALUSrcB = 0;
                if (valueAtFunct == 0) // sll, nop
                {
                    ALUCtrl = ALU.ALU_SLL;
                } else if (valueAtFunct == 32) // add
                {
                    ALUCtrl = ALU.ALU_ADD;
                } else if (valueAtFunct == 33) // addu
                {
                    ALUCtrl = ALU.ALU_ADD;
                } else if (valueAtFunct == 34) // sub
                {
                    ALUCtrl = ALU.ALU_SUB;
                } else if (valueAtFunct == 35) // subu
                {
                    ALUCtrl = ALU.ALU_SUB;
                } else if (valueAtFunct == 36) // and
                {
                    ALUCtrl = ALU.ALU_AND;
                } else if (valueAtFunct == 37) // or
                {
                    ALUCtrl = ALU.ALU_OR;
                } else if (valueAtFunct == 42) // slt
                {
                    ALUCtrl = ALU.ALU_SLT;
                } else {
                    System.err.println("(op, funct) not implemented yet: (" + valueAtOp + ", " + valueAtFunct + ")");
                }
                OutWrite = 1;
                break;
            case 8: // BR[rd] <- ALUOut
                RegDst = 1;
                MemToReg = 0;
                RegWrite = 1;
                break;
            case 9: // A - B
                ALUSrcA = 1;
                ALUSrcB = 0;
                ALUCtrl = ALU.ALU_SUB;
                break;
            case 10: // PC <- PC + 4*SignExt(inmed)
                PCWrite = 1;
                ALUSrcA = 0;
                ALUSrcB = 3;
                ALUCtrl = ALU.ALU_ADD;
                outPcSrc.addValue(0);
                break;
            case 11: // addiu: ALUOut <- A + SignExt(inmed)
                ALUSrcA = 1;
                ALUSrcB = 2;
                ALUCtrl = ALU.ALU_ADD;
                OutWrite = 1;
                break;
            case 12: // addiu: BR[rt] <- ALUOut
                RegDst = 0;
                MemToReg = 0;
                RegWrite = 1;
                break;
            case 13: // jr: PC <- A
                PCWrite = 1;
                outPcSrc.addValue(1);
                break;
            case 14: // slti: A - SignExt(inmed)
                ALUSrcA = 1;
                ALUSrcB = 2;
                ALUCtrl = ALU.ALU_SUB;
                break;
            case 15: // slti: BR[rt] <- 1
                RegDst = 0;
                MemToReg = 3;
                RegWrite = 1;
                break;
            case 16: // slti: BR[rt] <- 0
                RegDst = 0;
                MemToReg = 2;
                RegWrite = 1;
                break;
            case 17: // j: PC <- 4 * label
                PCWrite = 1;
                outPcSrc.addValue(2);
                break;
        }

        this.portPCWrite.addValue(PCWrite);
        this.portIorD.addValue(IorD);
        this.portMemWrite.addValue(MemWrite);
        this.portMemRead.addValue(MemRead);
        this.portIRWrite.addValue(IRWrite);
        this.portMDRWrite.addValue(MDRWrite);
        this.portRegDst.addValue(RegDst);
        this.portMemtoReg.addValue(MemToReg);
        this.portRegWrite.addValue(RegWrite);
        this.portAWrite.addValue(AWrite);
        this.portBWrite.addValue(BWrite);
        this.portALUSrcA.addValue(ALUSrcA);
        this.portALUSrcB.addValue(ALUSrcB);
        this.outAluCtrl.addValue(ALUCtrl);
        this.portOutWrite.addValue(OutWrite);
    }

    @Override
    public void exit() {
    }

    @Override
    public void initialize() {
        valueAtOp = null;
        valueAtFunct = null;
        valueAtZero = null;
        super.holdIn("active", delay);
    }

}
