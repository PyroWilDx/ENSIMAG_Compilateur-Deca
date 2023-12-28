package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class RegUtils {

    public static final int nRegs = 16;
    private static final boolean[] stateRegs = new boolean[]{
            true, true, false, false,
            false, false, false, false,
            false, false, false, false,
            false, false, false, false
    };
    private static GPRegister currReg = null;

    private RegUtils() {
    }

    public static GPRegister getUnusedReg() {
        for (int i = 0; i < nRegs; i++) {
            if (!stateRegs[i]) {
                stateRegs[i] = true;
                GPRegister reg = Register.getR(i);
                setCurrReg(reg);
                return reg;
            }
        }
        return null;
    }

    public static void freeReg(int i) {
        stateRegs[i] = false;
    }

    public static void freeReg(GPRegister reg) {
        freeReg(reg.getNumber());
    }

    public static void setCurrReg(GPRegister reg) {
        currReg = reg;
    }

    public static GPRegister getCurrReg() {
        return currReg;
    }

}
