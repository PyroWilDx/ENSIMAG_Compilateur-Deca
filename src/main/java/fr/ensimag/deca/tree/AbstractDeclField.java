package fr.ensimag.deca.tree;

import com.sun.tools.javac.comp.Env;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclField extends Tree {
    // TODO

    public abstract void codeGenDeclField(DecacCompiler compiler, int varOffset);
    public abstract EnvironmentExp verifyDeclFieldMembers(DecacCompiler compiler,
                                                          SymbolTable.Symbol superClass,
                                                          SymbolTable.Symbol classDef,
                                                          int index) throws ContextualError;
    // Done

    public abstract void verifyDeclFieldBody(DecacCompiler compiler,
                                             EnvironmentExp localEnv,
                                             ClassDefinition classDef) throws ContextualError;
    // Done

    public abstract SymbolTable.Symbol getName();
}
