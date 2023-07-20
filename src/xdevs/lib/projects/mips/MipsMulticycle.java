/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ssii2009.mips.architectures;

import java.io.File;
import java.util.ArrayList;
import ssii2009.examples.general.Clock;
import ssii2009.examples.general.GND;
import ssii2009.examples.general.VCC;
import ssii2009.mips.lib.Constant;
import ssii2009.mips.lib.ControladorMulticycle;
import ssii2009.mips.lib.Mux2to1;
import ssii2009.mips.lib.Mux4to1;
import ssii2009.mips.lib.ALU;
import ssii2009.mips.lib.InsNode;
import ssii2009.mips.lib.Memory;
import ssii2009.mips.lib.Shift2;
import ssii2009.mips.lib.Registers;
import ssii2009.mips.lib.Register;
import ssii2009.mips.lib.RegisterMdr;
import ssii2009.mips.lib.SignExtender;
import ssii2009.mips.lib.Transducer;
import xdevs.kernel.simulation.CoordinatorLogger;

/**
 *
 * @author Alfonso San Miguel
 */
public class MipsMulticycle extends MipsAbstract {

        protected Clock clock;
        protected Transducer transducer;
        protected Register<Integer> pc;
        protected Mux2to1 iorD;
        protected Memory memory;
        protected Register<String> ir;
        protected RegisterMdr mdr;
        protected VCC vcc;
        protected GND gnd;
        protected InsNode insNode;
        protected Shift2 shift2j;
        protected Mux2to1 regDst;
        protected Mux4to1 memToReg;
        protected Registers registers;
        protected SignExtender signExt;
        protected Register<Integer> regA;
        protected Register<Integer> regB;
        protected Shift2 shifter;
        protected Constant constant;
        protected Mux2to1 aluSrcA;
        protected Mux4to1 aluSrcB;
        protected ALU alu;
        protected Mux4to1 pcSrc;
        protected Register<Integer> aluOut;
        protected ControladorMulticycle ctrl;

        public MipsMulticycle(String name, String filePath) {
        super(name);
        ArrayList<String> instructions = new ArrayList<String>();
        try {
            instructions = super.loadDisassembledFile(filePath);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        // Components:
        clock = new Clock("Clock", 101.0);
        //clock.setLoggerOutputActive(true);
        super.addComponent(clock); // multicycle's PC

        transducer = new Transducer("Transducer");
        super.addComponent(transducer);

        pc = new Register<Integer>("PC", 0);
        //pc.setLoggerOutputActive(true);
        super.addComponent(pc);

        iorD = new Mux2to1("IorD");
        super.addComponent(iorD);

        memory = new Memory("Memory", instructions, 100.0, 100.0);
        //memory.setLoggerOutputActive(true);
        //memory.setLoggerActive(true);
        super.addComponent(memory);

        ir = new Register<String>("IR", null);
        //ir.setLoggerActive(true);
        super.addComponent(ir);

        mdr = new RegisterMdr("MDR");
        super.addComponent(mdr);

        gnd = new GND("Gnd");
        super.addComponent(gnd);

        vcc = new VCC("Vcc");
        super.addComponent(vcc);


        insNode = new InsNode("Node");
        //insNode.setLoggerActive(true);
        super.addComponent(insNode);

        shift2j = new Shift2("Shift2j");
        super.addComponent(shift2j);

        regDst = new Mux2to1("RegDst"); // mux of the Registers Bank
        super.addComponent(regDst);

        memToReg = new Mux4to1("MemToReg");
        super.addComponent(memToReg);

        registers = new Registers ("Registers", Registers.WRITE_MODE.PERIOD, 50.0, 50.0);
        super.addComponent(registers);
        // Añadimos la salida del programa artificialmente, que cargue en el registro ra(31)
        // el número de instrucciones. De esta forma, al llegar a jr, sólo ejecutará la instrucción siguiente
        // al jr y acabará el programa.
        Integer destinyAsInt = 4*instructions.size();
        registers.setRegisterValue(31, destinyAsInt);

        signExt = new SignExtender("SignExt");
        super.addComponent(signExt);

        regA = new Register<Integer>("A",0);
        super.addComponent(regA);

        regB = new Register<Integer>("B",0);
        super.addComponent(regB);

        shifter = new Shift2("Shifter2");
        super.addComponent(shifter);

        constant = new Constant("4", 4);
        super.addComponent(constant);

        aluSrcA = new Mux2to1("MuxALUA");
        super.addComponent(aluSrcA);

        aluSrcB = new Mux4to1("MuxALUB");
        super.addComponent(aluSrcB);

        pcSrc = new Mux4to1("PCSrc");
        super.addComponent(pcSrc);

        alu = new ALU("ALU", 100.0); // creation of the ALU
        //alu.setLoggerOutputActive(true);
        alu.setLoggerActive(true);
        super.addComponent(alu);

        aluOut = new Register<Integer>("ALUOut",0);
        super.addComponent(aluOut);

        ctrl = new ControladorMulticycle("Ctrl");
        super.addComponent(ctrl);

        // COUPLINGS
        // Salidas del PC
        super.addCoupling(pc, Register.outOutName, iorD, Mux2to1.inIn0Name);
        super.addCoupling(pc, Register.outOutName, aluSrcA, Mux2to1.inIn0Name);
        // Salidas de IorD
        super.addCoupling(iorD, Mux2to1.outOutName, memory, Memory.inAddrName);
        // Salidas de la memoria
        super.addCoupling(memory, Memory.outDrName, ir, Register.inInName);
        super.addCoupling(memory, Memory.outDrName, mdr, Register.inInName);
        super.addCoupling(memory, Memory.outStopName, clock, Clock.inName);
        super.addCoupling(memory, Memory.outStopName, transducer, Transducer.inStopName);
        // Salidas del IR
        super.addCoupling(ir, Register.outOutName, insNode, InsNode.inInName);
        // Salidas del MDR
        super.addCoupling(mdr, Register.outOutName, memToReg, Mux4to1.inIn1Name);
        // Salidas del GND
        super.addCoupling(gnd, GND.outName, memToReg, Mux4to1.inIn2Name);
        // Salidas del VCC
        super.addCoupling(vcc, VCC.outName, memToReg, Mux4to1.inIn3Name);
        // Salidas del InsNode
        super.addCoupling(insNode, InsNode.outOut3126Name, transducer, Transducer.inCodOpName);
        super.addCoupling(insNode, InsNode.outOut2500Name, shift2j, Shift2.inPortInName);
        super.addCoupling(insNode, InsNode.outOut2521Name, registers, Registers.inRAName);
        super.addCoupling(insNode, InsNode.outOut2016Name, registers, Registers.inRBName);
        super.addCoupling(insNode, InsNode.outOut2016Name, regDst, Mux2to1.inIn0Name);
        super.addCoupling(insNode, InsNode.outOut1511Name, regDst, Mux2to1.inIn1Name);
        super.addCoupling(insNode, InsNode.outOut1500Name, signExt, SignExtender.inPortInName);
        super.addCoupling(insNode, InsNode.outOut3126Name, ctrl, ControladorMulticycle.inOpName);
        super.addCoupling(insNode, InsNode.outOut0500Name, ctrl, ControladorMulticycle.inFunctName);
        super.addCoupling(insNode, InsNode.outOut1006Name, alu, ALU.inShamtName);
        // Salidas del registro de desplazamiento para j
        super.addCoupling(shift2j, Shift2.outPortOutName, pcSrc, Mux4to1.inIn2Name);
        // Salidas de RegDst
        super.addCoupling(regDst, Mux2to1.outOutName, registers, Registers.inRWName);
        // Salidas de MemToReg
        super.addCoupling(memToReg, Mux4to1.outOutName, registers, Registers.inBusWName);
        // Salidas del banco de registros
        super.addCoupling(registers, Registers.outBusAName, regA, Register.inInName);
        super.addCoupling(registers, Registers.outBusBName, regB, Register.inInName);
        // Salidas del extensor de signo
        super.addCoupling(signExt, SignExtender.outPortOutName, aluSrcB, Mux4to1.inIn2Name);
        super.addCoupling(signExt, SignExtender.outPortOutName, shifter, Shift2.inPortInName);
        // Salidas del registro A
        super.addCoupling(regA, Register.outOutName, aluSrcA, Mux2to1.inIn1Name);
        super.addCoupling(regA, Register.outOutName, pcSrc, Mux4to1.inIn1Name);
        // Salidas del registro B
        super.addCoupling(regB, Register.outOutName, memory, Memory.inDwName);
        super.addCoupling(regB, Register.outOutName, aluSrcB, Mux4to1.inIn0Name);
        // Salidas de la constante 4:
        super.addCoupling(constant, Constant.outOutName, aluSrcB, Mux4to1.inIn1Name);
        // Salidas del registro de desplazamiento 1
        super.addCoupling(shifter, Shift2.outPortOutName, aluSrcB, Mux4to1.inIn3Name);
        // Salidas de ALUSrcA
        super.addCoupling(aluSrcA, Mux2to1.outOutName, alu, ALU.inOpAName);
        // Salidas de ALUSrcB
        super.addCoupling(aluSrcB, Mux4to1.outOutName, alu, ALU.inOpBName);
        // Salidas del PCSrc
        super.addCoupling(pcSrc, Mux4to1.outOutName, pc, Register.inInName);
        // Salidas de la ALU
        super.addCoupling(alu, ALU.outAluOutName, pcSrc, Mux4to1.inIn0Name);
        super.addCoupling(alu, ALU.outAluOutName, aluOut, Register.inInName);
        super.addCoupling(alu, ALU.outZeroName, ctrl, ControladorMulticycle.inZeroName);
        super.addCoupling(alu, ALU.outLessThanName, ctrl, ControladorMulticycle.inLessThanName);
        // Salidas de ALUOut
        super.addCoupling(aluOut, Register.outOutName, iorD, Mux2to1.inIn1Name);
        super.addCoupling(aluOut, Register.outOutName, memToReg, Mux4to1.inIn0Name);
        // Salidas del reloj
        super.addCoupling(clock, Clock.outName, transducer, Transducer.inClkName);
        super.addCoupling(clock, Clock.outName, pc, Register.inClkName);
        super.addCoupling(clock, Clock.outName, memory, Memory.inClkName);
        super.addCoupling(clock, Clock.outName, ir, Register.inClkName);
        super.addCoupling(clock, Clock.outName, mdr, Register.inClkName);
        super.addCoupling(clock, Clock.outName, registers, Registers.inCLKName);
        super.addCoupling(clock, Clock.outName, regA, Register.inClkName);
        super.addCoupling(clock, Clock.outName, regB, Register.inClkName);
        super.addCoupling(clock, Clock.outName, aluOut, Register.inClkName);
        super.addCoupling(clock, Clock.outName, ctrl, ControladorMulticycle.inClkName);
        // Salidas del controlador:
        super.addCoupling(ctrl, ControladorMulticycle.outPCWriteName, pc, Register.inRegWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outIorDName, iorD, Mux2to1.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outMemWriteName, memory, Memory.inMemWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outMemReadName, memory, Memory.inMemReadName);
        super.addCoupling(ctrl, ControladorMulticycle.outIRWriteName, ir, Register.inRegWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outMDRWriteName, mdr, Register.inRegWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outRegDstName, regDst, Mux2to1.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outMemtoRegName, memToReg, Mux4to1.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outRegWriteName, registers, Registers.inRegWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outAWriteName, regA, Register.inRegWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outBWriteName, regB, Register.inRegWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outALUSrcAName, aluSrcA, Mux2to1.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outALUSrcBName, aluSrcB, Mux2to1.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outPcSrcName, pcSrc, Mux4to1.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outAluCtrlName, alu, ALU.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outOutWriteName, aluOut, Register.inRegWriteName);
    }

    public static void main(String[] args) {
        //test1();
        bench1_fibonacci();
        //bench2_prodesc();
        //bench3_mcd();
    }

    public static void test1() {
        // Primer test. c = a + b, con a = 3, b = 4
        // Examinando el ensamblado, el resultado lo almacena en MEM[8-24]
        MipsMulticycle mips = new MipsMulticycle("MIPS", "test" + File.separator + "foo.dis");
        CoordinatorLogger coordinator = new CoordinatorLogger(mips);
        coordinator.simulate(1800);
        System.out.println("Resultado: " + mips.memory.getData().get(8 - 24));
    }

    public static void bench1_fibonacci() {

        /*
        Benchmark1: Función que calcula el término 32 de la serie de fibonacci
        Solución: f2 = 3524578, que está en MEM[-20]
         */
        MipsMulticycle mips = new MipsMulticycle("MIPS", "test" + File.separator + "bench1_fibonacci.dis");
        CoordinatorLogger coordinator = new CoordinatorLogger(mips);
        coordinator.simulate(180000);
        System.out.println("Resultado: " + mips.memory.getData().get(-20));
    }

        public static void bench2_prodesc() {

        /*
	Benchmark2: Función que calcula el producto escalar de dos vectores
	Solución: result = 120
*/
        MipsMulticycle mips = new MipsMulticycle("MIPS", "test" + File.separator + "bench2_prodesc.dis");
        CoordinatorLogger coordinator = new CoordinatorLogger(mips);
        coordinator.simulate(180000);
        System.out.println("Resultado: " + mips.memory.getData().get(-24));
    }

    public static void bench3_mcd() {
/*
	Benchmark3: Función que calcula el máximo común divisor de dos números
	Solución: result = 256
*/
        MipsMulticycle mips = new MipsMulticycle("MIPS", "test" + File.separator + "bench3_mcd.dis");
        CoordinatorLogger coordinator = new CoordinatorLogger(mips);
        coordinator.simulate(180000);
        System.out.println("Resultado: " + mips.memory.getData().get(-24));
    }

}
