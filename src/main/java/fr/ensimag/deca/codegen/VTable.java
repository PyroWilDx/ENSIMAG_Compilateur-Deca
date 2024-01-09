package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;
import java.util.LinkedList;

public class VTable {

    private final SymbolTable.Symbol superClassSymbol;
    private final SymbolTable.Symbol classSymbol;
    private final String className;
    private final DAddr classAddr;
    private final HashMap<String, MethodInfo> classMethods;
    private final LinkedList<MethodInfo> classMethodsOrderered;
    private final HashMap<String, Integer> classFields;

    public VTable(SymbolTable.Symbol superClassSymbol, SymbolTable.Symbol classSymbol,
                  DAddr classAddr) {
        this.superClassSymbol = superClassSymbol;
        this.classSymbol = classSymbol;
        this.className = classSymbol.getName();
        this.classAddr = classAddr;
        this.classMethods = new HashMap<>();
        this.classMethodsOrderered = new LinkedList<>();
        this.classFields = new HashMap<>();
    }

    public SymbolTable.Symbol getSuperClassSymbol() {
        return superClassSymbol;
    }

    public VTable getVTableOfSuperClass(VTableManager vTM) {
        return vTM.getVTable(superClassSymbol.getName());
    }

    public SymbolTable.Symbol getClassSymbol() {
        return classSymbol;
    }

    public String getClassName() {
        return className;
    }

    public DAddr getClassAddr() {
        return classAddr;
    }

    public void addMethod(String methodName, DAddr mAddr) {
        MethodInfo methodInfo = new MethodInfo(className, methodName, mAddr);
        classMethods.put(methodName, methodInfo);
        classMethodsOrderered.addLast(methodInfo);
    }

    public LinkedList<MethodInfo> getClassMethodsOrderered() {
        return classMethodsOrderered;
    }

    public void addParamToMethod(String methodName, String paramName,
                                 int paramOffset) {
        classMethods.get(methodName).addParam(paramName, paramOffset);
    }

    public DAddr getMethodAddr(String methodName) {
        return classMethods.get(methodName).getMethodAddr();
    }

    public int getParamCountOfMethod(String methodName) {
        return classMethods.get(methodName).getParamCount();
    }

    public void addField(String fieldName, int fieldOffset) {
        classFields.put(fieldName, fieldOffset);
    }

    public int getFieldOffset(String fieldName) {
        return classFields.get(fieldName);
    }

    public int getFieldsCount() {
        return classFields.size();
    }

}
