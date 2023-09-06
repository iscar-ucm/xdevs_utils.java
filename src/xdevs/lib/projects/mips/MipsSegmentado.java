/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.projects.mips;

import java.io.File;
import java.util.ArrayList;

import xdevs.core.simulation.Coordinator;

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
        super.addComponent(registers);

        signExt = new SignExtender("SignExt");
        //signExt.setLoggerActive(true);
        super.addComponent(signExt);


        muxALUSrc = new Mux2to1("MuxALUSrc");
        //muxALUSrc.setLoggerActive(true);
        super.addComponent(muxALUSrc);

        alu = new ALU("ALU", 100.0); // creation of the ALU
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
        super.addComponent(registerIFID);

        registerIDEX = new RegisterIDEX("IDEX");
        super.addComponent(registerIDEX);

        registerEXMEM = new RegisterEXMEM("EXMEM");
        super.addComponent(registerEXMEM);

        registerMEMWB = new RegisterMEMWB("MEMWB");
        super.addComponent(registerMEMWB);

        gnd = new GND("Gnd");
        super.addComponent(gnd);

        vcc = new VCC("Vcc");
        super.addComponent(vcc);

        // Couplings

        // Salidas del reloj:
        super.addCoupling(clock.out, pc.clk);
        super.addCoupling(clock.out, registers.CLK);
        super.addCoupling(clock.out, dataMemory.CLK);
        super.addCoupling(clock.out, registerIFID.CLK);
        super.addCoupling(clock.out, registerIDEX.CLK);
        super.addCoupling(clock.out, registerEXMEM.CLK);
        super.addCoupling(clock.out, registerMEMWB.CLK);

        // Salidas del PC:
        super.addCoupling(pc.out, insMem.ADDR);
        super.addCoupling(pc.out, pcAdder.opA);

        // Salidas del sumador del PC
        super.addCoupling(pcAdder.out, muxPCSrc.inPortIn0);
        super.addCoupling(pcAdder.out, registerIFID.IDADDRCP);

        // Salidas de la memoria de instrucciones
        super.addCoupling(insMem.DR, registerIFID.IDDR);
        super.addCoupling(insMem.stop, clock.in);

        // Salidas del nodo de instrucción:
        super.addCoupling(insNode.out3126, ctrl.portOp);
        super.addCoupling(insNode.out0500, ctrl.portFunct);
        super.addCoupling(insNode.out2016, registerIDEX.EXRT);
        super.addCoupling(insNode.out1511, registerIDEX.IDRD);
        super.addCoupling(insNode.out2521, registerIDEX.IDRS);
        super.addCoupling(insNode.out2521, registers.RA);
        super.addCoupling(insNode.out2016, registers.RB);
        super.addCoupling(insNode.out1500, signExt.inPortIn);
        super.addCoupling(insNode.out0500, registerIDEX.EXALUFunct);
        //super.addCoupling(insNode, InsNode.outOut2500Name, shif2j , Shift2.inPortInName);

        // Salidas del controlador
        super.addCoupling(ctrl.portPcWrite, pc.regWrite);
        super.addCoupling(ctrl.portMemRead, registerIDEX.EXMemRead);
        super.addCoupling(ctrl.portMemWrite, registerIDEX.EXMemWrite);
        super.addCoupling(ctrl.portBranch, registerIDEX.EXBranch);
        super.addCoupling(ctrl.portRegWrite, registerIDEX.EXRegWrite);
        super.addCoupling(ctrl.portMemtoReg, registerIDEX.EXMemToReg);
        super.addCoupling(ctrl.portALUSrc, registerIDEX.EXALUSrc);
        super.addCoupling(ctrl.portRegDst, registerIDEX.EXRegDst);
        super.addCoupling(ctrl.portALUOp, registerIDEX.EXALUOp);
        //super.addCoupling(ctrl, ControladorSegmentado.outJumpReg, muxjr, Mux4to1.inCtrlName);
        //super.addCoupling(ctrl, ControladorSegmentado.outRegData, muxRegData, Mux2to1.inCtrlName);

        // Salidas de los multiplexores de control / otros
        super.addCoupling(signExt.outPortOut, registerIDEX.EXExtSig);
        super.addCoupling(muxRegDst.outPortOut, registerEXMEM.MEMRD);
        super.addCoupling(muxALUSrc.outPortOut, alu.inPortOpB);
        super.addCoupling(shif2.outPortOut, branchAdder.opB);
        super.addCoupling(branchAdder.out, registerEXMEM.EXADDRCP);
        //super.addCoupling(muxPCSrc ,Mux2to1.outOutName ,muxjr, Mux4to1.inIn0Name);
        //super.addCoupling(muxjr, Mux4to1.outOutName,pc ,Register.inInName);
        //super.addCoupling(registers, Registers.outBusAName ,muxjr, Mux4to1.inIn1Name);
        //super.addCoupling(muxMemToReg, Mux2to1.outOutName, muxRegData, Mux2to1.inIn0Name);
        super.addCoupling(and2.out, muxPCSrc.inPortCtrl);
        //super.addCoupling(muxRegData, Mux2to1.outOutName, registers, Registers.inBusWName);
        //super.addCoupling(muxSlt, Mux2to1.outOutName, muxRegData, Mux2to1.inIn1Name);

        // Salidas del banco de registros
        super.addCoupling(registers.busA, registerIDEX.EXBusA);
        super.addCoupling(registers.busB, registerIDEX.EXBusB);

        // Salidas de la ALU
        super.addCoupling(alu.outPortZero, registerEXMEM.EXAluZero);
        super.addCoupling(alu.outPortOut, registerEXMEM.EXAluRes);
        //super.addCoupling(alu, ALU.outLessThanName, muxSlt, Mux2to1.inCtrlName);

        // Salidas de la memoria de datos
        super.addCoupling(dataMemory.DR, registerMEMWB.MEMDRMemDat);

        // Salidas del Registro IFID
        super.addCoupling(registerIFID.IDDR, insNode.in);
        super.addCoupling(registerIFID.IDADDRCP, registerIDEX.IDADDRCP);

        // Salidas del Registro IDEX
        super.addCoupling(registerIDEX.EXADDRCP, branchAdder.opA);
        super.addCoupling(registerIDEX.EXBusA, alu.inPortOpA);
        super.addCoupling(registerIDEX.EXBusB, muxALUSrc.inPortIn0);
        super.addCoupling(registerIDEX.EXBusB, registerEXMEM.EXBusB);
        super.addCoupling(registerIDEX.EXExtSig, shif2.inPortIn);
        super.addCoupling(registerIDEX.EXMemRead, registerEXMEM.EXMemRead);
        super.addCoupling(registerIDEX.EXMemWrite, registerEXMEM.EXMemWrite);
        super.addCoupling(registerIDEX.EXBranch, registerEXMEM.EXBranch);
        super.addCoupling(registerIDEX.EXRegDst, muxRegDst.inPortCtrl);
        super.addCoupling(registerIDEX.EXALUSrc, muxALUSrc.inPortCtrl);
        //falta el coupling del RD y para salto retardado y deteccion de operandos

        // Salidas del Registro EXMEM
        super.addCoupling(registerEXMEM.MEMADDRCP, muxPCSrc.inPortIn1);
        super.addCoupling(registerEXMEM.MEMAluRes, dataMemory.ADDR);
        super.addCoupling(registerEXMEM.MEMAluRes, registerMEMWB.MEMAluRes);
        super.addCoupling(registerEXMEM.MEMBusB, dataMemory.DW);
        super.addCoupling(registerEXMEM.MEMRegWrite, registerMEMWB.MEMRegWrite);
        super.addCoupling(registerEXMEM.MEMMemToReg, registerMEMWB.MEMMemToReg);
        super.addCoupling(registerEXMEM.MEMMemRead, dataMemory.MemRead);
        super.addCoupling(registerEXMEM.MEMMemWrite, dataMemory.MemWrite);
        super.addCoupling(registerEXMEM.MEMBranch, and2.in0);
        super.addCoupling(registerEXMEM.MEMAluZero, and2.in1);
        super.addCoupling(registerEXMEM.MEMRD, registerMEMWB.MEMRD);

        // Salidas del Registro MEMWB
        super.addCoupling(registerMEMWB.WBDRMemDat, muxMemToReg.inPortIn1);
        super.addCoupling(registerMEMWB.WBAluRes, muxMemToReg.inPortIn0);
        super.addCoupling(registerMEMWB.WBMemToReg, muxMemToReg.inPortCtrl);
        super.addCoupling(registerMEMWB.WBRD, registers.RW);
        super.addCoupling(registerMEMWB.WBRegWrite, registers.RegWrite);

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
        Coordinator coordinator = new Coordinator(mips);
        coordinator.simulate(580);
        System.out.println("Resultado: " + mips.dataMemory.getData().get(8 - 24));
    }

    public static void bench1_fibonacci() {

        /*
	Benchmark1: Función que calcula el término 32 de la serie de fibonacci
	Solución: f2 = 3524578, que está en MEM[-12]
        */
        MipsMonocycle mips = new MipsMonocycle("MIPS", "test" + File.separator + "bench1_fibonacci.dis");
        Coordinator coordinator = new Coordinator(mips);
        coordinator.simulate(580);
        System.out.println("Resultado: " + mips.dataMemory.getData().get(-12));
    }

}
