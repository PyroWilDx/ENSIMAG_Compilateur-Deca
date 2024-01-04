package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclMethod extends Tree {
    // TODO

    public abstract void codeGenVTable(DecacCompiler compiler, AbstractIdentifier className);

}
