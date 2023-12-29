package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

import java.util.LinkedList;

public class RegUtils {

    public static int nRegs = 16;
    private static LinkedList<GPRegister> freeRegs = initFreeRegs();

    private RegUtils() {

    }

    public static void setNRegs(int nRegs) {
        assert (nRegs >= 4 && nRegs <= 16 &&
                freeRegs.size() == RegUtils.nRegs - 2);

        RegUtils.nRegs = nRegs;
        RegUtils.freeRegs = initFreeRegs();
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
