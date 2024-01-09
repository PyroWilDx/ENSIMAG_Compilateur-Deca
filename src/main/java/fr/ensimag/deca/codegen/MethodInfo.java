package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class MethodInfo {

    private final DAddr methodAddr;
    private final HashMap<String, Integer> methodParams;

    public MethodInfo(DAddr methodAddr) {
        this.methodAddr = methodAddr;
        this.methodParams = new HashMap<>();
    }

    public void addParam(String paramName, int paramOffset) {
        methodParams.put(paramName, paramOffset);
    }

    public DAddr getMethodAddr() {
        return methodAddr;
    }

    public int getParamCount() {
        return methodParams.size();
    }

}
