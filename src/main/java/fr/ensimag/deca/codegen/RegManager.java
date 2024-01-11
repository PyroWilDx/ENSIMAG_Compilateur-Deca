package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.Arrays;
import java.util.LinkedList;

public class RegManager {

    public final int nRegs;
    private final LinkedList<GPRegister> freeRegs;
    private final LinkedList<boolean[]> usedRegsStack;
    private DVal lastImm;

    public RegManager(int nRegs) {
        this.nRegs = nRegs;
        this.freeRegs = new LinkedList<>();
        for (int i = 2; i < nRegs; i++) {
            freeRegs.addLast(Register.getR(i));
        }
        this.usedRegsStack = new LinkedList<>();
        lastImm = null;
    }

    public GPRegister getFreeReg() {
        if (!freeRegs.isEmpty()) {
            GPRegister freeReg = freeRegs.removeFirst();
            if (!usedRegsStack.isEmpty() && freeReg.getNumber() > 1) {
                boolean[] usedRegs = usedRegsStack.getFirst();
                usedRegs[freeReg.getNumber()] = true;
            }
            return freeReg;
        }
        return null;
    }

    public GPRegister getLastReg() {
        return freeRegs.removeFirst();
    }

    public void freeReg(GPRegister gpReg) {
        if (gpReg != null) {
            assert (gpReg.getNumber() > 1);
            freeRegs.addFirst(gpReg);
        }
    }

    public void freeAllRegs() {
        freeRegs.clear();
        for (int i = 2; i < nRegs; i++) {
            freeRegs.addLast(Register.getR(i));
        }
    }

    public boolean isUsingAllRegs() {
        return freeRegs.isEmpty();
    }

    public void setLastImm(DVal dAddr) {
        lastImm = dAddr;
    }

    public DVal getLastImm() {
        DVal dVal = lastImm;
        lastImm = null;
        return dVal;
    }

    public GPRegister getLastRegOrImm(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();

        DVal lastImm = getLastImm();
        GPRegister gpReg;
        if (lastImm == null) {
            gpReg = rM.getLastReg();
        } else {
            gpReg = rM.getFreeReg();
            compiler.addInstruction(new LOAD(lastImm, gpReg));
        }
        return gpReg;
    }

    public void addScratchRegR0() {
        freeRegs.addFirst(Register.R0);
    }

    public void removeScratchRegR0() {
        freeRegs.remove(Register.R0);
    }

    public void saveUsedRegs() {
        boolean[] usedRegs = new boolean[nRegs];
        Arrays.fill(usedRegs, false);
        usedRegsStack.addFirst(usedRegs);
    }

    public boolean[] popUsedRegs() {
        return usedRegsStack.removeFirst();
    }

    public static void addSaveRegsInsts(DecacCompiler compiler, int index,
                                        boolean[] usedRegs) {
        StackManager sM = compiler.getStackManager();

        LinkedList<AbstractLine> startLines = new LinkedList<>();
        if (sM.getMaxStackSize() > 0) {
            startLines.addLast(new Line(new TSTO(sM.getMaxStackSize())));
            startLines.addLast(new Line(new BOV(ErrorUtils.stackOverflowLabel)));
        }
        for (int i = 2; i < usedRegs.length; i++) {
            if (usedRegs[i]) {
                startLines.addLast(new Line(new PUSH(Register.getR(i))));
            }
        }
        compiler.addAllLine(index, startLines);
    }

    public static void addRestoreRegsInsts(DecacCompiler compiler, boolean[] usedRegs) {
        for (int i = usedRegs.length - 1; i > 1; i--) {
            if (usedRegs[i]) {
                compiler.addInstruction(new POP(Register.getR(i)));
            }
        }
    }

}