package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class VTableManager {

    private final HashMap<SymbolTable.Symbol, VTable> classes;

    public VTableManager() {
        this.classes = new HashMap<>();
    }

    public void addClass(SymbolTable.Symbol name) {
        assert (!classes.containsKey(name));
        classes.put(name, new VTable());
    }

    public void addMethodToClass(SymbolTable.Symbol className,
                                 SymbolTable.Symbol methodName, DAddr dAddr) {
        assert (classes.containsKey(className));
        classes.get(className).addMethod(methodName, dAddr);
    }

}
