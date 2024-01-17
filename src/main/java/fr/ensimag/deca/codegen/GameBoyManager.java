package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractIdentifier;

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

    public static int getMethodLastParamAddr(int paramCount) {
        return AddrMethod + paramCount * 16 + 16;
    }

    private final HashMap<String, Integer> globalVars;
    public String currClassVar;

    public GameBoyManager() {
        this.globalVars = new HashMap<>();
        this.currClassVar = null;
    }

    public void addGlobalVar(String varName, int varOffset) {
        globalVars.put(varName, varOffset);
    }

    public Integer getGlobalVarOffset(String varName) {
        return globalVars.get(varName);
    }

    public int getGlobalAddrSP() {
        return Addr0 - (globalVars.size() * 16);
    }

    public Integer getGlobalVarAddr(String varName) {
        if (getGlobalVarOffset(varName) == null) return null;
        return Addr0 - (getGlobalVarOffset(varName) * 16);
    }

    public int getCurrClassFieldAddr(int fieldOffset) {
        System.out.println("TEST" + currClassVar);
        return Addr0 - ((globalVars.get(currClassVar) - fieldOffset) * 16);
    }

    public int extractAddrFromIdent(DecacCompiler compiler, AbstractIdentifier ident) {
        VTableManager vTM = compiler.getVTableManager();

        String identName = ident.getName().getName();

        System.out.println(identName);
        Integer varAddr = getGlobalVarAddr(identName);
        if (varAddr == null) { // It's a Method Param or Class Field
            Integer paramOffset = vTM.getCurrParamOffsetOfMethod(identName);
            if (paramOffset != null) { // It's a Method Param
                varAddr = getArgAddr(paramOffset);
            } else { // It's a Class Field
                int fieldOffset = vTM.getCurrFieldOffset(identName);
                varAddr = getCurrClassFieldAddr(fieldOffset);
            }
        }
        return varAddr;
    }
}
