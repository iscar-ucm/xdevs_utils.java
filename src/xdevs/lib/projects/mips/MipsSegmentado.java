/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ssii2009.mips.architectures;

import java.io.File;
import java.util.ArrayList;
import ssii2009.examples.general.And2;
import ssii2009.examples.general.Clock;
import ssii2009.mips.lib.Adder;
import ssii2009.mips.lib.InstructionsMemory;
import ssii2009.mips.lib.Mux2to1;
import ssii2009.mips.lib.ALU;
import ssii2009.mips.lib.ConstantAdder;
import ssii2009.mips.lib.Shift2;
import ssii2009.mips.lib.DataMemory;
import ssii2009.mips.lib.InsNode;
import ssii2009.mips.lib.Registers;
import ssii2009.mips.lib.Register;
import ssii2009.mips.lib.SignExtender;
import ssii2009.examples.general.GND;
import ssii2009.examples.general.VCC;
import ssii2009.mips.lib.ControladorSegmentado;
import ssii2009.mips.lib.RegisterEXMEM;
import ssii2009.mips.lib.RegisterIDEX;
import ssii2009.mips.lib.RegisterIFID;
import ssii2009.mips.lib.RegisterMEMWB;
import xdevs.kernel.simulation.CoordinatorLogger;

/**
 *
 * @author Jose Roldan
 */
public class MipsSegmentado extends MipsAbstract {

    protected Clock clock; // reloj
    protected Register pc; // contador de progama
    protected ConstantAdder pcAdder; // sumador contador de programa
    protected InstructionsMemory insMem; // memoria de instrucciones
    protected InsNode insNode;
    /**
    * Este es el nodo que está justo después de la memoria de instrucciones.
    * Separa la instrucción originalmente en binario en varias partes, traduciento
    * la información a tipo entero
    */
    protected ControladorSegmentado ctrl; // controlador segmentado
    protected Mux2to1 muxRegDst; // mux banco de registros
    protected Registers registers;// banco de registros
    protected SignExtender signExt; // extensor de signo
    protected Mux2to1 muxALUSrc; // mux de la ALU
    protected ALU alu; // ALU
    protected Shift2 shif2; // desplazador para el salto
    protected Adder branchAdder; // sumador de saltos
    protected And2 and2;
    protected Mux2to1 muxPCSrc; // mux de salto o siguiente instruccion
    protected DataMemory dataMemory; // memoria de datos
    protected Mux2to1 muxMemToReg; // mux despues memoria de datos
    //otected Mux4to1 muxjr;
    //protected Shift2 shif2j;
    //protected NodeJ nodej;
    //protected Mux2to1 muxRegData;
    //protected Mux2to1 muxSlt;
    protected RegisterIFID registerIFID;
    protected RegisterIDEX registerIDEX;
    protected RegisterEXMEM registerEXMEM;
    protected RegisterMEMWB registerMEMWB;
    protected GND gnd;
    protected VCC vcc;
    

    public MipsSegmentado(String name, String filePath) {
        super(name);
        ArrayList<String> instructions = new ArrayList<String>();
        try {
            instructions = super.loadDisassembledFile(filePath);
        }
        catch(Exception ee) {
            ee.printStackTrace();
        }
        // Components:
        clock = new Clock("Clock",600.0);
        //clock.setLoggerActive(true);
        super.addComponent(clock);

        pc = new Register<Integer>("PC", 0);
        pc.setLoggerActive(true);
        super.addComponent(pc);

        pcAdder = new ConstantAdder("PCAdder", 4, 100.0);
        //pcAdder.setLoggerActive(true);
        super.addComponent(pcAdder);

        // Añadimos una instrucción artificialmente, que cargue en el registro ra(31)
        // el número de instrucciones. De esta forma, al llegar a jr, sólo ejecutará la instrucción siguiente
        // al jr y acabará el programa. Recuerda que el código generado es para
        // saltos retardados.
        Integer destinyAsInt = 4*instructions.size();
        String destinyAsStr = Integer.toBinaryString(destinyAsInt);
        while(destinyAsStr.length()!=16)
            destinyAsStr = "0" + destinyAsStr;
        instructions.add(0, "0010010000011111" + destinyAsStr + ":addiu ra,zero," + destinyAsInt); // addiu ra, zero, instructions.size*4
        insMem = new InstructionsMemory("InsMem", instructions, 200.0);
        //insMem.setLoggerActive(true);
        super.addComponent(insMem);

        insNode = new InsNode("Node");
        //insNode.setLoggerActive(true);
        super.addComponent(insNode);

        ctrl = new ControladorSegmentado("Ctrl");
        //ctrl.setLoggerActive(true);
        super.addComponent(ctrl);

        muxRegDst = new Mux2to1("MuxRegDst"); // mux of the Registers Bank
        //muxRegDst.setLoggerActive(true);
        super.addComponent(muxRegDst);

        registers = new Registers ("Registers", Registers.WRITE_MODE.PERIOD, 50.0, 50.0);// Register Bank creation
        registers.setLoggerActive(true);
        super.addComponent(registers);

        signExt = new SignExtender("SignExt");
        //signExt.setLoggerActive(true);
        super.addComponent(signExt);


        muxALUSrc = new Mux2to1("MuxALUSrc");
        //muxALUSrc.setLoggerActive(true);
        super.addComponent(muxALUSrc);

        alu = new ALU("ALU", 100.0); // creation of the ALU
        alu.setLoggerActive(true);
        super.addComponent(alu);

        shif2 = new Shift2("ShiftLeft2");
        //shif2.setLoggerActive(true);
        super.addComponent(shif2);

        branchAdder = new Adder("BranchAdder", 100.0);
        //branchAdder.setLoggerActive(true);
        super.addComponent(branchAdder);

        and2 = new And2("And");
        //and2.setLoggerActive(true);
        super.addComponent(and2);

        muxPCSrc = new Mux2to1("MuxPCSrc");
        //muxPCSrc.setLoggerActive(true);
        super.addComponent(muxPCSrc);

        dataMemory = new DataMemory("DataMem", 200.0, 200.0);
        dataMemory.setLoggerActive(true);
        super.addComponent(dataMemory);

        muxMemToReg = new Mux2to1("MuxMemToReg");
        //muxMemToReg.setLoggerActive(true);
        super.addComponent(muxMemToReg);

        /*muxjr = new Mux4to1("MuxJR");
        //muxjr.setLoggerActive(true);
        super.addComponent(muxjr);

        shif2j = new Shift2("ShiftLeft2j");
        //shif2.setLoggerActive(true);
        super.addComponent(shif2j);

        nodej = new NodeJ ("NodeJ");
        super.addComponent(nodej);

        muxRegData = new Mux2to1("muxRegData");
        //muxRegData.setLoggerActive(true);
        super.addComponent(muxRegData);

        muxSlt = new Mux2to1("muxSlt");
        //muxSlt.setLoggerActive(true);
        super.addComponent(muxSlt);*/

        registerIFID = new RegisterIFID("IFID");
        registerIFID.setLoggerActive(true);
        super.addComponent(registerIFID);

        registerIDEX = new RegisterIDEX("IDEX");
        registerIDEX.setLoggerActive(true);
        super.addComponent(registerIDEX);

        registerEXMEM = new RegisterEXMEM("EXMEM");
        registerEXMEM.setLoggerActive(true);
        super.addComponent(registerEXMEM);

        registerMEMWB = new RegisterMEMWB("MEMWB");
        registerMEMWB.setLoggerActive(true);
        super.addComponent(registerMEMWB);

        gnd = new GND("Gnd");
        super.addComponent(gnd);

        vcc = new VCC("Vcc");
        super.addComponent(vcc);

        // Couplings

        // Salidas del reloj:
        super.addCoupling(clock, Clock.outName, pc, Register.inClkName);
        super.addCoupling(clock, Clock.outName, registers, Registers.inCLKName);
        super.addCoupling(clock, Clock.outName, dataMemory, DataMemory.CLKName);
        super.addCoupling(clock, Clock.outName, registerIFID, RegisterIFID.inCLKName);
        super.addCoupling(clock, Clock.outName, registerIDEX, RegisterIDEX.inCLKName);
        super.addCoupling(clock, Clock.outName, registerEXMEM, RegisterEXMEM.inCLKName);
        super.addCoupling(clock, Clock.outName, registerMEMWB, RegisterMEMWB.inCLKName);

        // Salidas del PC:
        super.addCoupling(pc, Register.outOutName, insMem, InstructionsMemory.inADDRName);
        super.addCoupling(pc, Register.outOutName, pcAdder, ConstantAdder.inOpAName);

        // Salidas del sumador del PC
        super.addCoupling(pcAdder, ConstantAdder.outOutName, muxPCSrc, Mux2to1.inIn0Name);
        super.addCoupling(pcAdder, ConstantAdder.outOutName, registerIFID, RegisterIFID.inADDRCP);

        // Salidas de la memoria de instrucciones
        super.addCoupling(insMem, InstructionsMemory.outDRName, registerIFID, RegisterIFID.inDR);
        super.addCoupling(insMem, InstructionsMemory.outStopName, clock, Clock.inName);

        // Salidas del nodo de instrucción:
        super.addCoupling(insNode, InsNode.outOut3126Name, ctrl, ControladorSegmentado.inOpName);
        super.addCoupling(insNode, InsNode.outOut0500Name, ctrl, ControladorSegmentado.inFunctName);
        super.addCoupling(insNode, InsNode.outOut2016Name, registerIDEX , RegisterIDEX.inRT);
        super.addCoupling(insNode, InsNode.outOut1511Name, registerIDEX , RegisterIDEX.inRD);
        super.addCoupling(insNode, InsNode.outOut2521Name, registerIDEX , RegisterIDEX.inRS);
        super.addCoupling(insNode, InsNode.outOut2521Name, registers , Registers.inRAName);
        super.addCoupling(insNode, InsNode.outOut2016Name, registers , Registers.inRBName);
        super.addCoupling(insNode, InsNode.outOut1500Name, signExt , SignExtender.inPortInName);
        super.addCoupling(insNode, InsNode.outOut0500Name, registerIDEX, RegisterIDEX.inALUFunct);
        //super.addCoupling(insNode, InsNode.outOut2500Name, shif2j , Shift2.inPortInName);

        // Salidas del controlador
        super.addCoupling(ctrl, ControladorSegmentado.outPCWriteName, pc, Register.inRegWriteName);
        super.addCoupling(ctrl, ControladorSegmentado.outMemReadName, registerIDEX, RegisterIDEX.inMemRead);
        super.addCoupling(ctrl, ControladorSegmentado.outMemWriteName, registerIDEX, RegisterIDEX.inMemWrite);
        super.addCoupling(ctrl, ControladorSegmentado.outBranchName, registerIDEX, RegisterIDEX.inBranch);
        super.addCoupling(ctrl, ControladorSegmentado.outRegWriteName, registerIDEX, RegisterIDEX.inRegWrite);
        super.addCoupling(ctrl, ControladorSegmentado.outMemtoRegName, registerIDEX, RegisterIDEX.inMemToReg);
        super.addCoupling(ctrl, ControladorSegmentado.outALUSrcName, registerIDEX, RegisterIDEX.inALUSrc);
        super.addCoupling(ctrl, ControladorSegmentado.outRegDstName, registerIDEX, RegisterIDEX.inRegDst);
        super.addCoupling(ctrl, ControladorSegmentado.outALUOpName, registerIDEX, RegisterIDEX.inALUOp);
        //super.addCoupling(ctrl, ControladorSegmentado.outJumpReg, muxjr, Mux4to1.inCtrlName);
        //super.addCoupling(ctrl, ControladorSegmentado.outRegData, muxRegData, Mux2to1.inCtrlName);

        // Salidas de los multiplexores de control / otros
        super.addCoupling(signExt , SignExtender.outPortOutName, registerIDEX, RegisterIDEX.inExtSig);
        super.addCoupling(muxRegDst, Mux2to1.outOutName, registerEXMEM , RegisterEXMEM.inRD);
        super.addCoupling(muxALUSrc,Mux2to1.outOutName, alu, ALU.inOpBName);
        super.addCoupling(shif2, Shift2.outPortOutName, branchAdder, Adder.inOpBName);
        super.addCoupling(branchAdder, Adder.outOutName, registerEXMEM, RegisterEXMEM.inADDRCP);
        //super.addCoupling(muxPCSrc ,Mux2to1.outOutName ,muxjr, Mux4to1.inIn0Name);
        //super.addCoupling(muxjr, Mux4to1.outOutName,pc ,Register.inInName);
        //super.addCoupling(registers, Registers.outBusAName ,muxjr, Mux4to1.inIn1Name);
        //super.addCoupling(muxMemToReg, Mux2to1.outOutName, muxRegData, Mux2to1.inIn0Name);
        super.addCoupling(and2, And2.outName, muxPCSrc, Mux2to1.inCtrlName);
        //super.addCoupling(muxRegData, Mux2to1.outOutName, registers, Registers.inBusWName);
        //super.addCoupling(muxSlt, Mux2to1.outOutName, muxRegData, Mux2to1.inIn1Name);

        // Salidas del banco de registros
        super.addCoupling(registers, Registers.outBusAName, registerIDEX, RegisterIDEX.inbusA);
        super.addCoupling(registers, Registers.outBusBName, registerIDEX , RegisterIDEX.inbusB);

        // Salidas de la ALU
        super.addCoupling(alu, ALU.outZeroName, registerEXMEM, RegisterEXMEM.inAluZero);
        super.addCoupling(alu, ALU.outAluOutName, registerEXMEM, RegisterEXMEM.inAluRes);
        //super.addCoupling(alu, ALU.outLessThanName, muxSlt, Mux2to1.inCtrlName);

        // Salidas de la memoria de datos
        super.addCoupling(dataMemory, DataMemory.DRName, registerMEMWB, RegisterMEMWB.inDRMemDat);

        // Salidas del Registro IFID
        super.addCoupling(registerIFID, RegisterIFID.outDR, insNode, InsNode.inInName);
        super.addCoupling(registerIFID, RegisterIFID.outADDRCP, registerIDEX, RegisterIDEX.inADDRCP);

        // Salidas del Registro IDEX
        super.addCoupling(registerIDEX, RegisterIDEX.outADDRCP, branchAdder, Adder.inOpAName);
        super.addCoupling(registerIDEX, RegisterIDEX.outbusA, alu, ALU.inOpAName);
        super.addCoupling(registerIDEX, RegisterIDEX.outbusB, muxALUSrc, Mux2to1.inIn0Name);
        super.addCoupling(registerIDEX, RegisterIDEX.outbusB, registerEXMEM, RegisterEXMEM.inBusB);
        super.addCoupling(registerIDEX, RegisterIDEX.outExtSig, shif2, Shift2.inPortInName);
        super.addCoupling(registerIDEX, RegisterIDEX.outMemRead, registerEXMEM, RegisterEXMEM.inMemRead);
        super.addCoupling(registerIDEX, RegisterIDEX.outMemWrite, registerEXMEM, RegisterEXMEM.inMemWrite);
        super.addCoupling(registerIDEX, RegisterIDEX.outBranch, registerEXMEM, RegisterEXMEM.inBranch);
        super.addCoupling(registerIDEX, RegisterIDEX.outRegDst, muxRegDst, Mux2to1.inCtrlName);
        super.addCoupling(registerIDEX, RegisterIDEX.outALUSrc, muxALUSrc, Mux2to1.inCtrlName);
        //falta el coupling del RD y para salto retardado y deteccion de operandos

        // Salidas del Registro EXMEM
        super.addCoupling(registerEXMEM, RegisterEXMEM.outADDRCP, muxPCSrc, Mux2to1.inIn1Name);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outAluRes, dataMemory, DataMemory.ADDRName);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outAluRes, registerMEMWB, RegisterMEMWB.inAluRes);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outBusB, dataMemory, DataMemory.DWName);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outRegWrite, registerMEMWB, RegisterMEMWB.inRegWrite);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outMemToReg, registerMEMWB, RegisterMEMWB.inMemToReg);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outMemRead, dataMemory, DataMemory.MemReadName);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outMemWrite, dataMemory, DataMemory.MemWriteName);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outBranch, and2, And2.in0Name);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outAluZero, and2, And2.in1Name);
        super.addCoupling(registerEXMEM, RegisterEXMEM.outRD, registerMEMWB, RegisterMEMWB.inRD);

        // Salidas del Registro MEMWB
        super.addCoupling(registerMEMWB, RegisterMEMWB.outDRMemDat, muxMemToReg, Mux2to1.inIn1Name);
        super.addCoupling(registerMEMWB, RegisterMEMWB.outAluRes, muxMemToReg, Mux2to1.inIn0Name);
        super.addCoupling(registerMEMWB, RegisterMEMWB.outMemToReg, muxMemToReg, Mux2to1.inCtrlName);
        super.addCoupling(registerMEMWB, RegisterMEMWB.outRD, registers, Registers.inRWName);
        super.addCoupling(registerMEMWB, RegisterMEMWB.outRegWrite, registers, Registers.inRegWriteName );

        // NodeJ
        //super.addCoupling(shif2j, Shift2.outPortOutName, nodej, NodeJ.inPortIn1Name);
        //super.addCoupling(pcAdder, ConstantAdder.outOutName, nodej, NodeJ.inPortIn2Name);
        //super.addCoupling(nodej, NodeJ.outPortOutName, muxjr, Mux4to1.inIn2Name);

        // Entradas del multiplexor para el slt
        //super.addCoupling(gnd, GND.outName, muxSlt, Mux2to1.inIn0Name);
        //super.addCoupling(vcc, VCC.outName, muxSlt, Mux2to1.inIn1Name);

    }

    public static void main(String[] args) {
        bench1_fibonacci();
    }

    public static void test1() {
        // Primer test. c = a + b, con a = 3, b = 4
        // Examinando el ensamblado, el resultado lo almacena en MEM[8-24]
        MipsMonocycle mips = new MipsMonocycle("MIPS", "test" + File.separator + "foo.dis");
        CoordinatorLogger coordinator = new CoordinatorLogger(mips);
        coordinator.simulate(580);
        System.out.println("Resultado: " + mips.dataMemory.getData().get(8 - 24));
    }

    public static void bench1_fibonacci() {

        /*
	Benchmark1: Función que calcula el término 32 de la serie de fibonacci
	Solución: f2 = 3524578, que está en MEM[-12]
        */
        MipsMonocycle mips = new MipsMonocycle("MIPS", "test" + File.separator + "bench1_fibonacci.dis");
        CoordinatorLogger coordinator = new CoordinatorLogger(mips);
        coordinator.simulate(580);
        System.out.println("Resultado: " + mips.dataMemory.getData().get(-12));
    }

}
