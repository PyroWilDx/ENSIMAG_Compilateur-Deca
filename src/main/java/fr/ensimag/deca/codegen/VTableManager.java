package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class VTableManager {

    private final HashMap<String, VTable> classes;

    public VTableManager() {
        this.classes = new HashMap<>();
    }

    public void addClass(String name, DAddr startAddr) {
        assert (!classes.containsKey(name));
        classes.put(name, new VTable(startAddr));
    }

    public void addMethodToClass(String className, String methodName,
                                 DAddr mAddr) {
        assert (classes.containsKey(className));
        classes.get(className).addMethod(methodName, mAddr);
    }

    public DAddr getAddrOfClass(String className) {
        return classes.get(className).getStartAddr();
    }

    public DAddr getAddrOfMethod(String className, String methodName) {
        return classes.get(className).getMethodAddr(methodName);
    }

}
