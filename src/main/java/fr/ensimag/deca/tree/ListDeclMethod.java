package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod>{
    @Override
    public void decompile(IndentPrintStream s) {
        // TODO ?
    }

    public void codeGenVTable(DecacCompiler compiler) {
        for (AbstractDeclMethod method : getList()) {
            method.codeGenVTable(compiler);
        }
    }

}
