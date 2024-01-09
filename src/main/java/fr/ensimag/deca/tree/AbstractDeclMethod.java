package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclMethod extends Tree {
    // TODO

    public abstract void codeGenVTable(DecacCompiler compiler, VTable vTable);

    public abstract void codeGenDeclMethod(DecacCompiler compiler);

    public abstract SymbolTable.Symbol getName();

    public abstract EnvironmentExp verifyDeclMethodMembers(DecacCompiler compiler,
                                                           SymbolTable.Symbol superClass,
                                                           int index) throws ContextualError;
    // Done

    public abstract void verifyDeclMethodBody(DecacCompiler compiler,
                                              EnvironmentExp localEnv,
                                              ClassDefinition currentClass) throws ContextualError;
}
