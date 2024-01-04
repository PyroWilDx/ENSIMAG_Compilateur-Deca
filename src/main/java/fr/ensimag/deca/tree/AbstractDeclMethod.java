package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclMethod extends Tree {
    // TODO

    public abstract void codeGenVTable(DecacCompiler compiler, AbstractIdentifier className);

    public abstract void codeGenDeclMethod(DecacCompiler compiler);
    public abstract SymbolTable.Symbol getName();
    public abstract EnvironmentExp verifyDeclMethod(DecacCompiler compiler,
                                                    ClassDefinition superClass) throws ContextualError;
}
