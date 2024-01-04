package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField>{
    @Override
    public void decompile(IndentPrintStream s) {
        // TODO ?
    }

    public void codeGenListDeclField(DecacCompiler compiler) {
        int varOffset = 1;
        for (AbstractDeclField declField : getList()) {
            declField.codeGenDeclField(compiler, varOffset);
            varOffset++;
        }
    }

}
