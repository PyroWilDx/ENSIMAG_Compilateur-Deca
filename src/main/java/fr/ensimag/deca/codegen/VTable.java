package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class VTable {

    private final HashMap<SymbolTable.Symbol, DAddr> methods;

    public VTable() {
        this.methods = new HashMap<>();
    }

    public void addMethod(SymbolTable.Symbol name, DAddr dAddr) {
        methods.put(name, dAddr);
    }

    public DAddr getMethodAddr(SymbolTable.Symbol name) {
        return methods.get(name);
    }

}
