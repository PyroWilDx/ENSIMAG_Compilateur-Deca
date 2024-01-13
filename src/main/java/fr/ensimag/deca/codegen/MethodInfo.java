package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class MethodInfo {

    private final String className;
    private final String methodName;
    private final DAddr methodAddr;
    private final HashMap<String, Integer> methodParams;

    public MethodInfo(String className, String methodName, DAddr methodAddr) {
        this.className = className;
        this.methodName = methodName;
        this.methodAddr = methodAddr;
        this.methodParams = new HashMap<>();
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public DAddr getMethodAddr() {
        return methodAddr;
    }

    public void addParam(String paramName, int paramOffset) {
        methodParams.put(paramName, paramOffset);
    }

    public Integer getParamOffset(String paramName) {
        return methodParams.get(paramName);
    }

    public void copyParams(MethodInfo otherMethod) {
        methodParams.putAll(otherMethod.methodParams);
    }

    public int getParamCount() {
        return methodParams.size();
    }

}
