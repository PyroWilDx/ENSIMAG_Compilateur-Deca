package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

import java.util.LinkedList;

public class RegManager {

    public final int nRegs;
    private final LinkedList<GPRegister> freeRegs;

    public RegManager(int nRegs) {
        this.nRegs = nRegs;
        this.freeRegs = new LinkedList<>();
        for (int i = 2; i < nRegs; i++) {
            freeRegs.addLast(Register.getR(i));
        }
    }

    public GPRegister getFreeReg() {
        if (!freeRegs.isEmpty()) {
            return freeRegs.removeFirst();
        }
        return null;
    }

    public GPRegister takeBackLastReg() {
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

}
