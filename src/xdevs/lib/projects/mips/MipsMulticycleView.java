/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.projects.mips;

import java.io.File;

import xdevs.core.modeling.CoupledView;
import xdevs.core.simulation.CoordinatorView;

/**
 * 
 * @author José L. Risco-Martín
 */
public class MipsMulticycleView extends MipsMulticycle {

    public MipsMulticycleView(String name, String filePath) {
        super(name, filePath);
    }

    public static void main(String[] args) {
        MipsMulticycleView mips = null;
        CoupledView mipsView = null;
        try {
            mips = new MipsMulticycleView("MIPS", "test" + File.separator + "bench1_fibonacci.dis");
            mipsView = new CoupledView(mips);
            mipsView.setBounds("MIPS", 0, 0, 1800, 900);
            mipsView.setBounds(mips.clock.getName(), 0.4, 0.1, 40, 40);
            mipsView.setBounds(mips.transducer.getName(), 0.5, 0.1, 100, 80);
            mipsView.setBounds(mips.ctrl.getName(), 0.05, 0.00, 110, 250);
            mipsView.setBounds(mips.shift2j.getName(), 0.02, 0.40, 80, 80);
            mipsView.setBounds(mips.pcSrc.getName(), 0.02, 0.50, 80, 80);
            mipsView.setBounds(mips.pc.getName(), 0.02, 0.60, 80, 80);
            mipsView.setBounds(mips.iorD.getName(), 0.02, 0.70, 80, 80);
            mipsView.setBounds(mips.memory.getName(), 0.10, 0.4, 200, 200);
            mipsView.setBounds(mips.ir.getName(), 0.17, 0.3, 80, 80);
            mipsView.setBounds(mips.mdr.getName(), 0.17, 0.65, 80, 80);
            mipsView.setBounds(mips.insNode.getName(), 0.25, 0.40, 120, 120);
            mipsView.setBounds(mips.regDst.getName(), 0.36, 0.40, 80, 80);
            mipsView.setBounds(mips.memToReg.getName(), 0.36, 0.50, 80, 80);
            mipsView.setBounds(mips.gnd.getName(), 0.34, 0.60, 20, 20);
            mipsView.setBounds(mips.vcc.getName(), 0.34, 0.65, 20, 20);
            mipsView.setBounds(mips.registers.getName(), 0.45, 0.25, 180, 300);
            mipsView.setBounds(mips.signExt.getName(), 0.45, 0.60, 80, 80);
            mipsView.setBounds(mips.shifter.getName(), 0.51, 0.60, 80, 80);
            mipsView.setBounds(mips.regA.getName(), 0.57, 0.25, 80, 80);
            mipsView.setBounds(mips.regB.getName(), 0.57, 0.45, 80, 80);
            mipsView.setBounds(mips.aluSrcA.getName(), 0.57, 0.35, 80, 80);
            mipsView.setBounds(mips.aluSrcB.getName(), 0.57, 0.55, 80, 80);
            mipsView.setBounds(mips.constant.getName(), 0.57, 0.65, 45, 45);
            mipsView.setBounds(mips.alu.getName(), 0.65, 0.35, 100, 90);
            mipsView.setBounds(mips.aluOut.getName(), 0.65, 0.25, 80, 80);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        if (mipsView == null) {
            return;
        }
        CoordinatorView coordinator = new CoordinatorView(mipsView);
        coordinator.setVisible(true);
        //Coordinator coordinator = new Coordinator(mips);
        //coordinator.simulate(Long.MAX_VALUE);
    }
}
