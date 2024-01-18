package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractIdentifier;

import java.util.HashMap;
import java.util.LinkedList;

public class GameBoyManager {

    public static final int nRegs = 4;
    public static final int Addr0 = 57344;
    public static final int AddrMax = 49152;

    public static boolean doCp = false;

    public static String getImmToken() {
        return (doCp) ? "" : "#";
    }

    public static String getLabelSeparator() {
        return (doCp) ? "_" : ".";
    }

    public static int getVarAddr(int varOffset) {
        return Addr0 - varOffset;
    }

    private int printId;
    private int fieldId;
    private final HashMap<String, Integer> globalVars;
    public LinkedList<AbstractIdentifier> currClassVarStack;

    public GameBoyManager() {
        this.printId = 0;
        this.fieldId = 0;
        this.globalVars = new HashMap<>();
        this.currClassVarStack = new LinkedList<>();
    }

    public int getAndIncrPrintId()  {
        return printId++;
    }

    public void addGlobalVar(String varName) {
        globalVars.put(varName, globalVars.size());
    }

    public void addFieldVar() {
        globalVars.put(fieldId + "F", globalVars.size());
        fieldId++;
    }

    public Integer getGlobalVarAddr(String varName) {
        if (!globalVars.containsKey(varName)) return null;
        return (Addr0 - 1) - (globalVars.get(varName) * 2);
    }

    public String getCurrClassVarName() {
        return currClassVarStack.peekFirst().getName().getName();
    }

    public String getCurrClassVarClassName() {
        return currClassVarStack.peekFirst().getType().getName().getName();
    }

    public int getGlobalAddrSP() {
        return Addr0 - globalVars.size();
    }

    public int getCurrClassFieldAddr(int fieldOffset) {
        return Addr0 - (globalVars.get(getCurrClassVarName()) - fieldOffset);
    }

    public Integer extractAddrFromIdent(DecacCompiler compiler, AbstractIdentifier ident) {
        VTableManager vTM = compiler.getVTableManager();

        String identName = ident.getName().getName();

        Integer varAddr = getGlobalVarAddr(identName);
        if (varAddr == null) { // It's a Method Param or Class Field
            Integer paramOffset = vTM.getCurrParamOffsetOfMethod(identName);
            if (paramOffset != null) { // It's a Method Param
//                varAddr = getArgAddr(paramOffset);
                varAddr = 42; // TODO (GB)
            } else { // It's a Class Field
                vTM.enterClass(getCurrClassVarClassName());
                int fieldOffset = vTM.getCurrFieldOffset(identName);
                vTM.exitClass();
                varAddr = getCurrClassFieldAddr(fieldOffset);
            }
        }
        return varAddr;
    }
}
