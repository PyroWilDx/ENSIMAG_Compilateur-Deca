package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class VTable {

    private final String className;
    private final DAddr classAddr;
    private final HashMap<String, MethodInfo> classMethods;
    private final HashMap<String, Integer> classFields;

    public VTable(String className, DAddr classAddr) {
        this.className = className;
        this.classAddr = classAddr;
        this.classMethods = new HashMap<>();
        this.classFields = new HashMap<>();
    }

    public String getClassName() {
        return className;
    }

    public DAddr getClassAddr() {
        return classAddr;
    }

    public void addMethod(String methodName, DAddr mAddr) {
        classMethods.put(methodName, new MethodInfo(mAddr));
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
