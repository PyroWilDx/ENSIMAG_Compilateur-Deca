package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;
import java.util.LinkedList;

public class VTable {

    private final SymbolTable.Symbol superClassSymbol;
    private final String className;
    private final DAddr classAddr;
    private final HashMap<String, VMethodInfo> classMethods;
    private final LinkedList<VMethodInfo> classMethodsOrderered;
    private final HashMap<String, Integer> classFields;

    public VTable(SymbolTable.Symbol superClassSymbol, SymbolTable.Symbol classSymbol,
                  DAddr classAddr) {
        this.superClassSymbol = superClassSymbol;
        this.className = classSymbol.getName();
        this.classAddr = classAddr;
        this.classMethods = new HashMap<>();
        this.classMethodsOrderered = new LinkedList<>();
        this.classFields = new HashMap<>();
    }

    public VTable getVTableOfSuperClass(VTableManager vTM) {
        return vTM.getVTable(superClassSymbol.getName());
    }

    public String getClassName() {
        return className;
    }

    public DAddr getClassAddr() {
        return classAddr;
    }

    public void addMethod(String methodName, DAddr mAddr) {
        VMethodInfo methodInfo = new VMethodInfo(className, methodName, mAddr);
        classMethods.put(methodName, methodInfo);
        classMethodsOrderered.addLast(methodInfo);
    }

    public void addSuperMethod(String superClassName, String methodName, DAddr mAddr) {
        VMethodInfo methodInfo = new VMethodInfo(superClassName, methodName, mAddr);
        classMethods.put(methodName, methodInfo);
        classMethodsOrderered.addLast(methodInfo);
    }

    public void copyMethodParams(VTable otherVTable, String methodName) {
        classMethods.get(methodName).copyParams(otherVTable.classMethods.get(methodName));
    }

    public LinkedList<VMethodInfo> getClassMethodsOrderered() {
        return classMethodsOrderered;
    }

    public DAddr getMethodAddr(String methodName) {
        return classMethods.get(methodName).getMethodAddr();
    }

    public void addParamToMethod(String methodName, String paramName,
                                 int paramOffset) {
        classMethods.get(methodName).addParam(paramName, paramOffset);
    }

    public Integer getParamOffsetOfMethod(String methodName, String paramName) {
        return classMethods.get(methodName).getParamOffset(paramName);
    }

    public int getParamCountOfMethod(String methodName) {
        return classMethods.get(methodName).getParamCount();
    }

    public void addField(String fieldName, int fieldOffset) {
        classFields.put(fieldName, fieldOffset);
    }

    public void addAllFields(VTable otherVTable) {
        classFields.putAll(otherVTable.classFields);
    }

    public int getFieldOffset(String fieldName) {
        return classFields.get(fieldName);
    }

    public int getFieldsCount() {
        return classFields.size();
    }

}
