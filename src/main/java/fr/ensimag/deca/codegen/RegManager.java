package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.util.LinkedList;

public class RegManager {

    public final int nRegs;
    private final LinkedList<GPRegister> freeRegs;
    private DVal lastImmediate;

    public RegManager(int nRegs) {
        this.nRegs = nRegs;
        this.freeRegs = new LinkedList<>();
        for (int i = 2; i < nRegs; i++) {
            freeRegs.addLast(Register.getR(i));
        }
        lastImmediate = null;
    }

    public GPRegister getFreeReg() {
        if (!freeRegs.isEmpty()) {
            return freeRegs.removeFirst();
        }
        return null;
    }

    public GPRegister getLastReg() {
        return freeRegs.removeFirst();
    }

    public void freeReg(GPRegister reg) {
        if (reg != null && reg.getNumber() > 1) {
            freeRegs.addFirst(reg);
        }
    }

    public boolean isUsingAllRegs() {
        return freeRegs.isEmpty();
    }

    public void setLastImmediate(DVal dAddr) {
        lastImmediate = dAddr;
    }

    public DVal getLastImmediate() {
        DVal dVal = lastImmediate;
        lastImmediate = null;
        return dVal;
    }

    public GPRegister getLastRegOrImm(DecacCompiler compiler) {
        DVal lastImmediate = getLastImmediate();
        GPRegister reg;
        if (lastImmediate == null) {
            reg = compiler.getRegManager().getLastReg();
        } else {
            reg = compiler.getRegManager().getFreeReg();
            compiler.addInstruction(new LOAD(lastImmediate, reg));
        }
        return reg;
    }

}
