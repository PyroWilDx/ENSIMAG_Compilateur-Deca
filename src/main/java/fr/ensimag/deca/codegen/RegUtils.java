package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

import java.util.LinkedList;

public class RegUtils {

    public static final int nRegs = 16;
    private static final LinkedList<GPRegister> freeRegs = initFreeRegs();

    private RegUtils() {

    }

    private static LinkedList<GPRegister> initFreeRegs() {
        LinkedList<GPRegister> freeRegs = new LinkedList<>();
        for (int i = 2; i < nRegs; i++) {
            freeRegs.addLast(Register.getR(i));
        }
        return freeRegs;
    }

    public static GPRegister getFreeReg() {
        if (!freeRegs.isEmpty()) {
            return freeRegs.removeFirst();
        }
        return null;
    }

    public static GPRegister takeBackLastReg() {
        return freeRegs.removeFirst();
    }

    public static void freeReg(GPRegister reg) {
        if (reg != null && reg.getNumber() > 1) {
            freeRegs.addFirst(reg);
        }
    }

    public static boolean isUsingAllRegs() {
        return freeRegs.isEmpty();
    }

}
