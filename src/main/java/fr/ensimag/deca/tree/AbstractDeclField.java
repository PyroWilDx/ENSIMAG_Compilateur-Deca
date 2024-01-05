package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclField extends Tree {
    // TODO

    public abstract void codeGenDeclField(DecacCompiler compiler, int varOffset);
    public abstract EnvironmentExp verifyDeclField(DecacCompiler compiler,
                                                   SymbolTable.Symbol superClass,
                                                   SymbolTable.Symbol classDef,
                                                   int index) throws ContextualError;
    // Done
    public abstract SymbolTable.Symbol getName();
}
