package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;

public class GameBoy {

    public static final int N_OF_REGS = 7;
//    public static final int N_OF_REGS = 3;

    public static boolean doCp = false;

    public static String getImmToken() {
        return (doCp) ? "" : "#";
    }

}
