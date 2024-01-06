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
    private OrderedHashSet<GPRegister> usedRegs;
    private DVal lastImm;

    public RegManager(int nRegs) {
        this.nRegs = nRegs;
        this.freeRegs = new LinkedList<>();
        for (int i = 2; i < nRegs; i++) {
            freeRegs.addLast(Register.getR(i));
        }
        this.usedRegs = null;
        lastImm = null;
    }

    public GPRegister getFreeReg() {
        if (!freeRegs.isEmpty()) {
            GPRegister freeReg = freeRegs.removeFirst();
            if (usedRegs != null) {
                usedRegs.addLast(freeReg);
            }
            return freeReg;
        }
        return null;
    }

    public GPRegister getLastReg() {
        return freeRegs.removeFirst();
    }

    public void freeReg(GPRegister gpReg) {
        if (gpReg != null && gpReg.getNumber() > 1) {
            freeRegs.addFirst(gpReg);
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

    public void addScratchRegs() {
        freeRegs.addFirst(Register.R1);
        freeRegs.addFirst(Register.R0);
    }

    public void removeScratchRegs() {
        freeRegs.remove(Register.R0);
        freeRegs.remove(Register.R1);
    }

    public void saveUsedRegs() {
        usedRegs = new OrderedHashSet<>();
    }

    public void doNotSaveRegs() {
        usedRegs = null;
    }

    public OrderedHashSet<GPRegister> getUsedRegs() {
        return usedRegs;
    }

}
