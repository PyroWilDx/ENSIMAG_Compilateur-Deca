package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.LineGb;

import java.util.HashMap;

public class GameBoyManager {

    public static final int nRegs = 4;
    public static final int Addr_0 = 65535;

    public static boolean doCp = false;

    public static String getImmToken() {
        return (doCp) ? "" : "#";
    }

    public static void loadAddr0(DecacCompiler compiler, String regName) {
        compiler.add(new LineGb("ld " + regName + ", " + Addr_0));
    }

    public static void loadAddrFromOffset(DecacCompiler compiler, String regName, int offset) {
        compiler.add(new LineGb("ld " + regName + ", " + (Addr_0 - offset * 16)));
    }

    private final HashMap<String, GlobalVarInfo> globalVars;

    public GameBoyManager() {
        this.globalVars = new HashMap<>();
    }

    public void addGlobalVar(String varName, int varOffset, boolean isAddr) {
        globalVars.put(varName, new GlobalVarInfo(varOffset, isAddr));
    }

    public int getGlobalVarOffset(String varName) {
        return globalVars.get(varName).varOffset;
    }

    public boolean isGlobalVarAddr(String varName) {
        return globalVars.get(varName).isAddr;
    }
}

class GlobalVarInfo {
    int varOffset;
    boolean isAddr;

    public GlobalVarInfo(int varOffset, boolean isAddr) {
        this.varOffset = varOffset;
        this.isAddr = isAddr;
    }
}
