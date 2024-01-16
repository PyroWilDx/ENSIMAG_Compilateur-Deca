package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;

import java.util.HashMap;
import java.util.LinkedList;

public class VTableManager {

    private final HashMap<String, VTable> vTables;
    private final LinkedList<String> currClassNameStack;
    private final LinkedList<String> currMethodNameStack;

    public VTableManager() {
        this.vTables = new HashMap<>();
        this.currClassNameStack = new LinkedList<>();
        this.currMethodNameStack = new LinkedList<>();
    }

    public void addVTable(String className, VTable vTable) {
        assert (!vTables.containsKey(className));
        vTables.put(className, vTable);
    }

    public VTable getVTable(String className) {
        return vTables.get(className);
    }

    public DAddr getClassAddr(String className) {
        return vTables.get(className).getClassAddr();
    }

    public int getFieldOffset(String className, String fieldName) {
        return vTables.get(className).getFieldOffset(fieldName);
    }

    public int getFieldCountOfClass(String className) {
        return vTables.get(className).getFieldsCount();
    }

    public int getMethodOffset(String className, String methodName) {
        return vTables.get(className).getMethodOffset(methodName);
    }

    public Label getMethodLabel(String className, String methodName) {
        return vTables.get(className).getMethodLabel(methodName);
    }

    public Integer getParamOffsetOfMethod(String className, String methodName, String paramName) {
        return vTables.get(className).getParamOffsetOfMethod(methodName, paramName);
    }

    public int getParamCountOfMethod(String className, String methodName) {
        return vTables.get(className).getParamCountOfMethod(methodName);
    }

    public void enterClass(String className) {
        currClassNameStack.addFirst(className);
    }

    public void exitClass() {
        currClassNameStack.removeFirst();
    }

    public String getCurrClassName() {
        return currClassNameStack.peekFirst();
    }

    public DAddr getCurrClassAddr() {
        return getClassAddr(getCurrClassName());
    }

    public int getCurrFieldOffset(String fieldName) {
        return getFieldOffset(getCurrClassName(), fieldName);
    }

    public int getCurrFieldCountOfClass() {
        return getFieldCountOfClass(getCurrClassName());
    }

    public void enterMethod(String methodName) {
        currMethodNameStack.addFirst(methodName);
    }

    public void exitMethod() {
        currMethodNameStack.removeFirst();
    }

    public String getCurrMethodName() {
        return currMethodNameStack.peekFirst();
    }

    public boolean isInMethod() {
        return getCurrMethodName() != null;
    }

    public int getCurrMethodOffset() {
        return getMethodOffset(getCurrClassName(), getCurrMethodName());
    }

    public Label getCurrMethodLabel() {
        return getMethodLabel(getCurrClassName(), getCurrMethodName());
    }

    public Integer getCurrParamOffsetOfMethod(String paramName) {
        if (getCurrMethodName() == null) return null;
        return getParamOffsetOfMethod(getCurrClassName(), getCurrMethodName(), paramName);
    }

    public int getCurrParamCountOfMethod() {
        return getParamCountOfMethod(getCurrClassName(), getCurrMethodName());
    }

}
