package fr.ensimag.deca.codegen;

public class GameBoy {

    public static final int N_OF_REGS = 7;
    public static boolean doCp = false;

    public static String getImmToken() {
        return (doCp) ? "$" : "#";
    }

}
