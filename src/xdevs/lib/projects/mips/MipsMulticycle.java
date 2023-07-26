/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.projects.mips;

import java.io.File;
import java.util.ArrayList;

import xdevs.core.simulation.Coordinator;
import xdevs.lib.general.sources.Constant;

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
        super.addComponent(alu);

        aluOut = new Register<Integer>("ALUOut",0);
        super.addComponent(aluOut);

        ctrl = new ControladorMulticycle("Ctrl");
        super.addComponent(ctrl);

        // COUPLINGS
        // Salidas del PC
        super.addCoupling(pc.out, iorD.inPortIn0);
        super.addCoupling(pc.out, aluSrcA.inPortIn0);
        // Salidas de IorD
        super.addCoupling(iorD.outPortOut, memory.inAddr);
        // Salidas de la memoria
        super.addCoupling(memory.outDr, ir.in);
        super.addCoupling(memory.outDr, mdr.in);
        super.addCoupling(memory.outStop, clock.in);
        super.addCoupling(memory.outStop, transducer.inStop);
        // Salidas del IR
        super.addCoupling(ir.out, insNode.in);
        // Salidas del MDR
        super.addCoupling(mdr.out, memToReg.inPortIn1);
        // Salidas del GND
        super.addCoupling(gnd.out, memToReg.inPortIn2);
        // Salidas del VCC
        super.addCoupling(vcc.out, memToReg.inPortIn3);
        // Salidas del InsNode
        super.addCoupling(insNode.out3126, transducer.inCodOp);
        super.addCoupling(insNode.out2500, shift2j.inPortIn);
        super.addCoupling(insNode.out2521, registers.RA);
        super.addCoupling(insNode.out2016, registers.RB);
        super.addCoupling(insNode.out2016, regDst.inPortIn0);
        super.addCoupling(insNode.out1511, regDst.inPortIn1);
        super.addCoupling(insNode.out1500, signExt.inPortIn);
        super.addCoupling(insNode.out3126, ctrl.portOp);
        super.addCoupling(insNode.out0500, ctrl.portFunct);
        // TODO: Error de conexión en el diagrama de bloques:
        // super.addCoupling(insNode.out1006, InsNode.outOut1006Name, alu, ALU.inShamtName);
        // Salidas del registro de desplazamiento para j
        super.addCoupling(shift2j.outPortOut, pcSrc.inPortIn2);
        // Salidas de RegDst
        super.addCoupling(regDst.outPortOut, registers.RW);
        // Salidas de MemToReg
        super.addCoupling(memToReg.outPortOut, registers.busW);
        // Salidas del banco de registros
        super.addCoupling(registers.busA, regA.in);
        super.addCoupling(registers.busB, regB.in);
        // Salidas del extensor de signo
        super.addCoupling(signExt.outPortOut, aluSrcB.inPortIn2);
        super.addCoupling(signExt.outPortOut, shifter.inPortIn);
        // Salidas del registro A
        super.addCoupling(regA.out, aluSrcA.inPortIn1);
        super.addCoupling(regA.out, pcSrc.inPortIn1);
        // Salidas del registro B
        super.addCoupling(regB.out, memory.inDw);
        super.addCoupling(regB.out, aluSrcB.inPortIn0);
        // Salidas de la constante 4:
        super.addCoupling(constant.oOut, aluSrcB.inPortIn1);
        // Salidas del registro de desplazamiento 1
        super.addCoupling(shifter.outPortOut, aluSrcB.inPortIn3);
        // Salidas de ALUSrcA
        super.addCoupling(aluSrcA.outPortOut, alu.inPortOpA);
        // Salidas de ALUSrcB
        super.addCoupling(aluSrcB.outPortOut, alu.inPortOpB);
        // Salidas del PCSrc
        super.addCoupling(pcSrc.outPortOut, pc.in);
        // Salidas de la ALU
        super.addCoupling(alu.outPortOut, pcSrc.inPortIn0);
        super.addCoupling(alu.outPortOut, aluOut.in);
        super.addCoupling(alu.outPortZero, ctrl.portZero);
        super.addCoupling(alu.outLessThan, ctrl.inLessThan);
        // Salidas de ALUOut
        super.addCoupling(aluOut.out, iorD.inPortIn1);
        super.addCoupling(aluOut.out, memToReg.inPortIn0);
        // Salidas del reloj
        super.addCoupling(clock.out, transducer.inClk);
        super.addCoupling(clock.out, pc.clk);
        super.addCoupling(clock.out, memory.inClk);
        super.addCoupling(clock.out, ir.clk);
        super.addCoupling(clock.out, mdr.clk);
        super.addCoupling(clock.out, registers.CLK);
        super.addCoupling(clock.out, regA.clk);
        super.addCoupling(clock.out, regB.clk);
        super.addCoupling(clock.out, aluOut.clk);
        super.addCoupling(clock.out, ctrl.clk);
        // Salidas del controlador:
        super.addCoupling(ctrl.portPCWrite, pc.regWrite);
        super.addCoupling(ctrl.portIorD, iorD.inPortCtrl);
        super.addCoupling(ctrl.portMemWrite, memory.inMemWrite);
        super.addCoupling(ctrl.portMemRead, memory.inMemRead);
        super.addCoupling(ctrl.portIRWrite, ir.regWrite);
        super.addCoupling(ctrl.portMDRWrite, mdr.regWrite);
        super.addCoupling(ctrl.portRegDst, regDst.inPortCtrl);
        super.addCoupling(ctrl.portMemtoReg, memToReg.inPortCtrl);
        super.addCoupling(ctrl.portRegWrite, registers.RegWrite);
        super.addCoupling(ctrl.portAWrite, regA.regWrite);
        super.addCoupling(ctrl.portBWrite, regB.regWrite);
        super.addCoupling(ctrl.portALUSrcA, aluSrcA.inPortCtrl);
        super.addCoupling(ctrl.portALUSrcB, aluSrcB.inPortCtrl);
        super.addCoupling(ctrl.outPcSrc, pcSrc.inPortCtrl);
        super.addCoupling(ctrl.outAluCtrl, alu.inPortCtrl);
        super.addCoupling(ctrl.portOutWrite, aluOut.regWrite);
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
        Coordinator coordinator = new Coordinator(mips);
        coordinator.simulate(1800);
        System.out.println("Resultado: " + mips.memory.getData().get(8 - 24));
    }

    public static void bench1_fibonacci() {

        /*
        Benchmark1: Función que calcula el término 32 de la serie de fibonacci
        Solución: f2 = 3524578, que está en MEM[-20]
         */
        MipsMulticycle mips = new MipsMulticycle("MIPS", "test" + File.separator + "bench1_fibonacci.dis");
        Coordinator coordinator = new Coordinator(mips);
        coordinator.simulate(180000);
        System.out.println("Resultado: " + mips.memory.getData().get(-20));
    }

        public static void bench2_prodesc() {

        /*
	Benchmark2: Función que calcula el producto escalar de dos vectores
	Solución: result = 120
*/
        MipsMulticycle mips = new MipsMulticycle("MIPS", "test" + File.separator + "bench2_prodesc.dis");
        Coordinator coordinator = new Coordinator(mips);
        coordinator.simulate(180000);
        System.out.println("Resultado: " + mips.memory.getData().get(-24));
    }

    public static void bench3_mcd() {
/*
	Benchmark3: Función que calcula el máximo común divisor de dos números
	Solución: result = 256
*/
        MipsMulticycle mips = new MipsMulticycle("MIPS", "test" + File.separator + "bench3_mcd.dis");
        Coordinator coordinator = new Coordinator(mips);
        coordinator.simulate(180000);
        System.out.println("Resultado: " + mips.memory.getData().get(-24));
    }

}
