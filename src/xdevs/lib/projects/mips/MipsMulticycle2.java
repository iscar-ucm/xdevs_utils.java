
package ssii2009.mips.architectures;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import ssii2009.examples.general.And2;
import ssii2009.examples.general.Clock;
import ssii2009.mips.lib.Adder;
import ssii2009.mips.lib.Constant;
import ssii2009.mips.lib.InstructionNode1;
import ssii2009.mips.lib.InstructionNode2;
import ssii2009.mips.lib.Mux2to1;
import ssii2009.mips.lib.Mux4to1;
import ssii2009.mips.lib.ALU;
import ssii2009.mips.lib.Shift2;
import ssii2009.mips.lib.DataMemory;
import ssii2009.mips.lib.Registers;
import ssii2009.mips.lib.Register;
import ssii2009.mips.lib.SignExtender;
import ssii2009.mips.lib.ControladorMulticycle;
import xdevs.kernel.simulation.Coordinator;

/**
 *
 * @author Jose Roldan
 */
public class MipsMulticycle2 extends MipsAbstract {

    public MipsMulticycle2(String name, String filePath){
        super(name);
        ArrayList<String> instructions = new ArrayList<String>();
        try {
            instructions = super.loadDisassembledFile(filePath);
        }
        catch(Exception ee) {
            ee.printStackTrace();
        }
        //Componentes
        Clock clock = new Clock("Clock",4);
        super.addComponent(clock);

        Constant constantPCAdder = new Constant("C4", 4);
        super.addComponent(constantPCAdder);

        // Multicycle PC
        Register pc = new Register<Integer>("PC", 0);
        super.addComponent(pc);

        DataMemory dataMem = new DataMemory("DataMem");
        super.addComponent(dataMem);

        InstructionNode1 node1 = new InstructionNode1("Node1");
        super.addComponent(node1);

        //Multiplexor Entrada RW Banco de Registros
        Mux2to1 muxregisters = new Mux2to1("MuxBR");
        super.addComponent(muxregisters);

        //Multiplexor MDR
        Mux2to1 muxmdr = new Mux2to1("MuxMDR");
        super.addComponent(muxmdr);

        //Banco Registros
        Registers registers = new Registers ("Registers");
        super.addComponent(registers);

        InstructionNode2 node2 = new InstructionNode2("Node2");
        super.addComponent(node2);

        //Extensor de signo
        SignExtender signext = new SignExtender("SignExt");
        super.addComponent(signext);

        //Multiplexor Salida B Banco de registros
        Mux4to1 muxalub = new Mux4to1("MuxALUB");
        super.addComponent(muxalub);

        //Multiplexor Salida A Banco de registros
        Mux2to1 muxalua = new Mux2to1("MuxALUA");
        super.addComponent(muxalua);

        //Desplazador
        Shift2 shifter = new Shift2("Shifter2");
        super.addComponent(shifter);

        Adder branchAdder = new Adder("BranchAdder");
        super.addComponent(branchAdder);

        //ALU
        ALU alu = new ALU("ALU");
        super.addComponent(alu);

        And2 and = new And2("And");
        super.addComponent(and);

        Mux2to1 muxpc = new Mux2to1("MuxPC");
        super.addComponent(muxpc);

        //Registro IR
        Register<String> IRreg= new Register<String>("IR", null);
        super.addComponent(IRreg);

        //Registro Target
        Register<Integer> Target= new Register<Integer>("RegTarget",0);
        super.addComponent(Target);
        
        Mux2to1 PcScr = new Mux2to1("MuxPcScr");
        super.addComponent(PcScr);

        ControladorMulticycle ctrl = new ControladorMulticycle("Ctrl");
        super.addComponent(ctrl);

       

        //COUPLINGS

        super.addCoupling(clock,Clock.outName,pc,Register.inClkName);
        super.addCoupling(PcScr,Mux2to1.outOutName,pc,Register.inInName);
        super.addCoupling(pc,Register.outOutName, muxpc,Mux2to1.inIn0Name);
        super.addCoupling(alu,ALU.outAluOutName,muxpc,Mux2to1.inIn1Name);
        super.addCoupling(pc,Register.outOutName,muxalua,Mux2to1.inIn0Name);

        super.addCoupling(ctrl, ControladorMulticycle.outPCWriteName, pc, Register.inRegWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outIorDName, muxpc, Mux2to1.inCtrlName);

        super.addCoupling(clock,Clock.outName,dataMem,DataMemory.CLKName);
        super.addCoupling(muxpc,Mux2to1.outOutName,dataMem,DataMemory.ADDRName);
        super.addCoupling(registers,Registers.inRBName,dataMem,DataMemory.DWName);
        super.addCoupling(dataMem,DataMemory.DRName,IRreg,Register.inInName);
        super.addCoupling(dataMem,DataMemory.DRName,muxmdr,Mux2to1.inIn1Name);

        super.addCoupling(ctrl, ControladorMulticycle.outMemReadName, dataMem, DataMemory.MemReadName);
        super.addCoupling(ctrl, ControladorMulticycle.outMemWriteName, dataMem, DataMemory.MemWriteName);

        super.addCoupling(clock,Clock.outName,IRreg,Register.inClkName);
        super.addCoupling(IRreg,Register.outOutName,node1,InstructionNode1.inInName);
        super.addCoupling(node1,InstructionNode1.outOut2521Name,registers,Registers.inRAName);
        super.addCoupling(node1,InstructionNode1.outOut2016Name,registers,Registers.inRBName);
        super.addCoupling(muxregisters,Mux2to1.outOutName,registers,Registers.inRWName);
        super.addCoupling(node1,InstructionNode1.outOut2016Name,muxregisters,Mux2to1.inIn0Name);
        super.addCoupling(node1,InstructionNode1.outOut1511Name,muxregisters,Mux2to1.inIn1Name);
        super.addCoupling(muxmdr,Mux2to1.outOutName,registers,Registers.inBusWName);
        super.addCoupling(node1,InstructionNode1.outOut1500Name,node2,InstructionNode2.inInName);
        super.addCoupling(node2,InstructionNode2.outOut1500Name,signext,SignExtender.inPortInName);
        super.addCoupling(signext,SignExtender.outPortOutName,shifter,Shift2.inPortInName);

        super.addCoupling(ctrl, ControladorMulticycle.outIRWriteName, IRreg, Register.inRegWriteName);
        super.addCoupling(ctrl, ControladorMulticycle.outRegDstName, muxregisters, Mux2to1.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outMemtoRegName, muxmdr, Mux2to1.inCtrlName);

        super.addCoupling(clock,Clock.outName,registers,Registers.inCLKName);
        super.addCoupling(registers,Registers.outBusAName,muxalua,Mux2to1.inIn1Name);
        super.addCoupling(registers,Registers.outBusBName,muxalub,Mux4to1.inIn1Name);
        super.addCoupling(constantPCAdder,Constant.outOutName,muxalub,Mux4to1.inIn2Name);
        super.addCoupling(signext,SignExtender.outPortOutName,muxalub,Mux4to1.inIn3Name);
        super.addCoupling(shifter,Shift2.outPortOutName,muxalub,Mux4to1.inIn3Name); // Revisar

        super.addCoupling(ctrl, ControladorMulticycle.outRegWriteName, registers, Registers.inRegWriteName);

        super.addCoupling(muxalua,Mux2to1.outOutName,alu,ALU.inOpAName);
        super.addCoupling(muxalub,Mux4to1.outOutName,alu,ALU.inOpBName);
        super.addCoupling(alu,ALU.outAluOutName,muxmdr,Mux2to1.inIn0Name);
        super.addCoupling(alu,ALU.outAluOutName,Target,Register.inInName);
        super.addCoupling(alu,ALU.outAluOutName,PcScr,Mux2to1.inIn0Name);

        super.addCoupling(ctrl, ControladorMulticycle.outALUSrcAName, muxalua, Mux2to1.inCtrlName);
        super.addCoupling(ctrl, ControladorMulticycle.outALUSrcBName, muxalub, Mux4to1.inCtrlName);

        super.addCoupling(clock,Clock.outName,Target,Registers.inCLKName);
        super.addCoupling(Target,Register.outOutName,PcScr,Mux2to1.inIn1Name);
        

    }

    public static void main(String[] args) {
        MipsMulticycle2 mips = new MipsMulticycle2("MIPS", "test" + File.separator + "foo.dis");
        Coordinator coordinator = new Coordinator(mips);
        coordinator.simulate(40);
    }

}
