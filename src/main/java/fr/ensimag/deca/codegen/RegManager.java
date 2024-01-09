package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.Arrays;
import java.util.LinkedList;

public class RegManager {

    public enum RegStatus {
        NOT_USED,
        WAS_USED_BUT_NOT_REUSED,
        WAS_USED_AND_IS_REUSED
    }

    public final int nRegs;
    private final LinkedList<GPRegister> freeRegs;
    private final LinkedList<RegStatus[]> usedRegsStack;
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
                RegStatus[] usedRegs = usedRegsStack.getFirst();
                if (usedRegs[freeReg.getNumber()] == RegStatus.WAS_USED_BUT_NOT_REUSED) {
                    usedRegs[freeReg.getNumber()] = RegStatus.WAS_USED_AND_IS_REUSED;
                }
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
        RegStatus[] usedRegs = new RegStatus[nRegs];
        Arrays.fill(usedRegs, RegStatus.WAS_USED_BUT_NOT_REUSED);

        usedRegs[0] = RegStatus.NOT_USED;
        usedRegs[1] = RegStatus.NOT_USED;
        for (GPRegister gpReg : freeRegs) {
            usedRegs[gpReg.getNumber()] = RegStatus.NOT_USED;
        }

        usedRegsStack.addFirst(usedRegs);
    }

    public RegStatus[] popUsedRegs() {
        return usedRegsStack.removeFirst();
    }

    public static void addSaveRegsInsts(DecacCompiler compiler, int index,
                                        RegStatus[] usedRegs) {
        StackManager sM = compiler.getStackManager();

        LinkedList<AbstractLine> startLines = new LinkedList<>();
        startLines.addLast(new Line(new TSTO(sM.getMaxStackSize())));
        startLines.addLast(new Line(new BOV(ErrorUtils.stackOverflowLabel)));
        for (int i = 2; i < usedRegs.length; i++) {
            if (usedRegs[i] == RegStatus.WAS_USED_AND_IS_REUSED) {
                startLines.addFirst(new Line(new PUSH(Register.getR(i))));
            }
        }
        compiler.addAllLine(index, startLines);
    }

    public static void addRestoreRegsInsts(DecacCompiler compiler, RegStatus[] usedRegs) {
        for (int i = 2; i < usedRegs.length; i++) {
            if (usedRegs[i] == RegStatus.WAS_USED_AND_IS_REUSED) {
                compiler.addInstruction(new POP(Register.getR(i)));
            }
        }
    }

}
