package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class VTableManager {

    private final HashMap<SymbolTable.Symbol, VTable> classes;

    public VTableManager() {
        this.classes = new HashMap<>();
    }

    public void addClass(SymbolTable.Symbol name, DAddr startAddr) {
        assert (!classes.containsKey(name));
        classes.put(name, new VTable(startAddr));
    }

    public void addMethodToClass(SymbolTable.Symbol className,
                                 SymbolTable.Symbol methodName, DAddr mAddr) {
        assert (classes.containsKey(className));
        classes.get(className).addMethod(methodName, mAddr);
    }

    public DAddr getAddrOfClass(SymbolTable.Symbol className) {
        return classes.get(className).getStartAddr();
    }

}
