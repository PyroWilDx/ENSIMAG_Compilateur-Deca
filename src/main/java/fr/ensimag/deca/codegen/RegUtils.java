package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class RegUtils {

    public static final int nRegs = 16;
    public static int usedRegCpt = 2;
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
        for (int i = 2; i < nRegs; i++) {
            if (!stateRegs[i]) {
                GPRegister reg = Register.getR(i);
                setCurrReg(reg);
                useReg(i);
                return reg;
            }
        }
        return null;
    }

    public static boolean isUsingAllRegs() {
        return (usedRegCpt == nRegs);
    }

    public static void useReg(int i) {
        if (i < 2) return;

        if (!stateRegs[i]) usedRegCpt++;
        stateRegs[i] = true;
    }

    public static void useReg(GPRegister reg) {
        if (reg != null) useReg(reg.getNumber());
    }

    public static void freeReg(int i) {
        if (i < 2) return;

        if (stateRegs[i]) usedRegCpt--;
        stateRegs[i] = false;
    }

    public static void freeReg(GPRegister reg) {
        if (reg != null) freeReg(reg.getNumber());
    }

    public static void setCurrReg(GPRegister reg) {
        if (reg != null) currReg = reg;
    }

    public static GPRegister getCurrReg() {
        return currReg;
    }

    public static GPRegister getAndUseCurrReg() {
        useReg(currReg);
        return getCurrReg();
    }

}
