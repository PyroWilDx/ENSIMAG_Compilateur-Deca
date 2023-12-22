package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

public class RegistersManager {

    private static final int nRegs = 16;
    private static final boolean[] stateRegs = new boolean[] {
            true, true, false, false,
            false, false, false, false,
            false, false, false, false,
            false, false, false, false
    };
    private static GPRegister lastUsedReg = null;

    private RegistersManager() {}

    public static GPRegister getUnusedReg() {
        for (int i = 0; i < nRegs; i++) {
            if (!stateRegs[i]) return Register.getR(i);
        }
        return null;
    }

    public static void setLastUsedReg(int i) {
        lastUsedReg = Register.getR(i);
    }

    public static void setLastUsedReg(GPRegister reg) {
        lastUsedReg = reg;
    }

    public static GPRegister getLastUsedReg() {
        return lastUsedReg;
    }

}
