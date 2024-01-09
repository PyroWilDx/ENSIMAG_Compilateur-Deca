package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class VTableManager {

    private final HashMap<String, VTable> vTables;
    private String currClassName;
    private String currMethodName;

    public VTableManager() {
        this.vTables = new HashMap<>();
        this.currClassName = null;
        this.currMethodName = null;
    }

    public void addVTable(String className, VTable vTable) {
        assert (!vTables.containsKey(className));
        vTables.put(className, vTable);
    }

    public DAddr getAddrOfClass(String className) {
        return vTables.get(className).getClassAddr();
    }

    public DAddr getAddrOfMethod(String className, String methodName) {
        return vTables.get(className).getMethodAddr(methodName);
    }

    public int getParamCountOfMethod(String className, String methodName) {
        return vTables.get(className).getParamCountOfMethod(methodName);
    }

    public int getOffsetOfField(String className, String fieldName) {
        return vTables.get(className).getFieldOffset(fieldName);
    }

    public int getFieldCountOfClass(String className) {
        return vTables.get(className).getFieldsCount();
    }

    public void setCurrClassName(String value) {
        currClassName = value;
    }

    public String getCurrClassName() {
        return currClassName;
    }

    public DAddr getCurrAddrOfClass() {
        return getAddrOfClass(currClassName);
    }

    public DAddr getCurrAddrOfMethod(String methodName) {
        return getAddrOfMethod(currClassName, methodName);
    }

    public int getCurrParamCountOfMethod(String methodName) {
        return getParamCountOfMethod(currClassName, methodName);
    }

    public int getCurrOffsetOfField(String fieldName) {
        return getOffsetOfField(currClassName, fieldName);
    }

    public int getCurrFieldCountOfClass() {
        return getFieldCountOfClass(currClassName);
    }

    public void setCurrMethodName(String value) {
        currMethodName = value;
    }

    public String getCurrMethodName() {
        return currMethodName;
    }

}
