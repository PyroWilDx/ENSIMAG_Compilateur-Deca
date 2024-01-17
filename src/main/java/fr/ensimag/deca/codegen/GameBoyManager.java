package fr.ensimag.deca.codegen;

import java.util.HashMap;

public class GameBoyManager {

    public static final int nRegs = 4;
    public static final int Addr0 = 57344;
    public static final int AddrMax = 49152;
    public static final int AddrMethod = AddrMax + 32 * 16;

    public static boolean doCp = false;

    public static String getImmToken() {
        return (doCp) ? "" : "#";
    }

    public static int getVarAddr(int varOffset) {
        return Addr0 - varOffset * 16;
    }

    public static int getArgAddr(int argOffset) {
        return AddrMethod + argOffset * 16;
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
