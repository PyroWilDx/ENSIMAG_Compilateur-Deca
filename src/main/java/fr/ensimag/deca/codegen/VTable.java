package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;

import java.util.HashMap;

public class VTable {

    private final DAddr startAddr;
    private final HashMap<SymbolTable.Symbol, DAddr> methods;

    public VTable(DAddr startAddr) {
        this.startAddr = startAddr;
        this.methods = new HashMap<>();
    }

    public void addMethod(SymbolTable.Symbol name, DAddr mAddr) {
        methods.put(name, mAddr);
    }

    public DAddr getMethodAddr(SymbolTable.Symbol name) {
        return methods.get(name);
    }

    public DAddr getStartAddr() {
        return startAddr;
    }

}
