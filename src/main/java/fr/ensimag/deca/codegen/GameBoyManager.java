package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.HashMap;

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

    private int printId;
    private int fieldId;
    private Integer currNewFieldCount;
    private final HashMap<String, Integer> globalVars;
    private final HashMap<String, HashMap<String, Integer>> methodsVars;
    // HashMap<ClassName.MethodName, HashMap<VarName, VarOffset>
    private String currDeclaringIdentName;
    private int currMethodSpOffset; // OH LA PURGE PUTAIN

    public GameBoyManager() {
        this.printId = 0;
        this.fieldId = 0;
        this.currNewFieldCount = null;
        this.globalVars = new HashMap<>();
        this.methodsVars = new HashMap<>();
        this.currDeclaringIdentName = null;
        this.currMethodSpOffset = 0;
    }

    public int getAndIncrPrintId() {
        return printId++;
    }

    public void setCurrNewFieldCount(int value) {
        currNewFieldCount = value;
    }

    public boolean didNew() {
        return currNewFieldCount != null;
    }

    public int getAndResetNewFieldCount() {
        int fieldCount = currNewFieldCount;
        currNewFieldCount = null;
        return fieldCount;
    }

    public void addGlobalVar(String varName) {
        globalVars.put(varName, globalVars.size());
    }

    public void addGlobalFieldVar() {
        addGlobalVar(fieldId + "F");
        fieldId++;
    }

    public Integer getGlobalVarAddr(String varName) {
        if (!globalVars.containsKey(varName)) return null;
        return (Addr0 - 1) - (globalVars.get(varName) * 2);
    }

    public String getCurrMethodKey(VTableManager vTM) {
        return vTM.getCurrClassName() + "." + vTM.getCurrMethodName();
    }

    public void createCurrMethodVarsMap(VTableManager vTM) {
        methodsVars.put(getCurrMethodKey(vTM), new HashMap<>());
        addCurrMethodFieldVar(vTM); // Pour les push de BC et DE dans la méthode
        addCurrMethodFieldVar(vTM);
    }

    public void addCurrMethodVar(VTableManager vTM, String varName) {
        HashMap<String, Integer> methodVars = methodsVars.get(getCurrMethodKey(vTM));
        methodVars.put(varName, getCurrMethodVarCount(vTM));
    }

    public void addCurrMethodFieldVar(VTableManager vTM) {
        addCurrMethodVar(vTM, fieldId + "F");
        fieldId++;
    }

    public int getCurrMethodVarCount(VTableManager vTM) {
        return methodsVars.get(getCurrMethodKey(vTM)).size();
    }

    public Integer getCurrMethodVarOffset(VTableManager vTM, String varName) {
        return methodsVars.get(getCurrMethodKey(vTM)).get(varName);
    }

    public void setCurrDeclaringIdentName(String value) {
        currDeclaringIdentName = value;
    }

    public void setCurrMethodSpOffset(int value) {
        currMethodSpOffset = value;
    }

    public void incr2CurrMethodSpOffset() {
        currMethodSpOffset += 2;
    }

    public int getCurrMethodSpOffset() {
        return currMethodSpOffset;
    }

    public void loadIdentAddrIntoHL(DecacCompiler compiler, AbstractIdentifier ident) {
        VTableManager vTM = compiler.getVTableManager();
        GameBoyManager gbM = compiler.getGameBoyManager();

        String identName = ident.getName().getName();

        Integer varAddr;
        if (!vTM.isInMethod()) {
            varAddr = getGlobalVarAddr(identName);
            if (varAddr != null) {
                compiler.addInstruction(new LOAD_INT(varAddr, Register.HL));
            }
        } else {
            if (identName.equals(currDeclaringIdentName)) {
                varAddr = null;
            } else {
                varAddr = getCurrMethodVarOffset(vTM, identName);
                if (varAddr != null) {
                    compiler.addInstruction(new LOAD_SP(Register.SP, Register.HL, -varAddr * 2 - 1));
                }
            }
        }

        if (varAddr != null) return;

        Integer paramOffset = vTM.getCurrParamOffsetOfMethod(identName);
        int spOffset = gbM.getCurrMethodSpOffset();
        if (paramOffset != null) { // It's a Method Param
            compiler.addInstruction(new LOAD_SP(Register.SP, Register.HL,
                    3 + spOffset + (-paramOffset - 2) * 2));
        } else { // It's a Class Field
            compiler.addInstruction(new LOAD_SP(Register.SP, Register.HL, 3 + spOffset));
            compiler.addInstruction(new LOAD_VAL(Register.HL, Register.A));
            compiler.addInstruction(new DEC_REG(Register.HL));
            compiler.addInstruction(new LOAD_VAL(Register.HL, GPRegister.L));
            compiler.addInstruction(new LOAD_REG(Register.A, GPRegister.H));
            int fieldOffset = vTM.getCurrFieldOffset(identName);
            for (int i = 0; i < fieldOffset * 2; i++) {
                compiler.addInstruction(new DEC_REG(Register.HL));
            }
        }
    }
}
