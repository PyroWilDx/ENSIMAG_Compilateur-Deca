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

    public static int getVarAddr(int varOffset) {
        return Addr0 - varOffset;
    }

    private int printId;
    private int fieldId;
    private boolean isDeclaring;
    private Integer currNewFieldCount;
    private final HashMap<String, Integer> globalVars;

    public GameBoyManager() {
        this.printId = 0;
        this.fieldId = 0;
        this.currNewFieldCount = null;
        this.globalVars = new HashMap<>();
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

    public void addFieldVar() {
        globalVars.put(fieldId + "F", globalVars.size());
        fieldId++;
    }

    public Integer getGlobalVarAddr(String varName) {
        if (!globalVars.containsKey(varName)) return null;
        return (Addr0 - 1) - (globalVars.get(varName) * 2);
    }

    public void loadIdentAddrIntoHL(DecacCompiler compiler, AbstractIdentifier ident) {
        VTableManager vTM = compiler.getVTableManager();

        String identName = ident.getName().getName();

        Integer varAddr = null;
        if (!vTM.isInMethod()) {

            varAddr = getGlobalVarAddr(identName);
        }
        if (varAddr != null) {
            compiler.addInstruction(new LOAD_INT(varAddr, Register.HL));
        } else {
            Integer paramOffset = vTM.getCurrParamOffsetOfMethod(identName);
            if (paramOffset != null) { // It's a Method Param
                compiler.addInstruction(new LOAD_SP(Register.SP, Register.HL, 3 + (-paramOffset - 2) * 2));
            } else { // It's a Class Field
                System.out.println("CLASS FIELD");
                compiler.addInstruction(new LOAD_SP(Register.SP, Register.HL, +3));
                compiler.addInstruction(new LOAD_VAL(Register.HL, Register.A));
                compiler.addInstruction(new LOAD_SP(Register.SP, Register.HL, +2));
                compiler.addInstruction(new LOAD_VAL(Register.HL, GPRegister.L));
                compiler.addInstruction(new LOAD_REG(Register.A, GPRegister.H));
                int fieldOffset = vTM.getCurrFieldOffset(identName);
                for (int i = 0; i < fieldOffset * 2; i++) {
                    compiler.addInstruction(new DEC_REG(Register.HL));
                }
            }
        }
    }
}
