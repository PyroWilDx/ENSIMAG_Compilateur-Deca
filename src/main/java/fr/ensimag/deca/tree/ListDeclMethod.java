package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

public class ListDeclMethod extends TreeList<AbstractDeclMethod>{
    @Override
    public void decompile(IndentPrintStream s) {
        // TODO ?
    }

    public void codeGenVTable(DecacCompiler compiler, AbstractIdentifier className) {
        for (AbstractDeclMethod method : getList()) {
            method.codeGenVTable(compiler, className);
        }
        // TODO (Faut voir si les méthodes sont dans l'ordre ou pas ?)
    }

}
