package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

public class ListDeclField extends TreeList<AbstractDeclField>{
    @Override
    public void decompile(IndentPrintStream s) {
        // TODO ?
    }
    public EnvironmentExp verifyListDeclField(DecacCompiler compiler, SymbolTable.Symbol superClass,
                                              SymbolTable.Symbol name) throws ContextualError {
        EnvironmentExp envReturn = new EnvironmentExp(null);
        for (AbstractDeclField decl : this.getList()) {
            EnvironmentExp env = decl.verifyDeclField(compiler, superClass, name);
            if (!envReturn.disjointUnion(env)) {
                throw new ContextualError("Field '" + decl.getName() +
                        "' already defined.", getLocation());
            }
        }
        return envReturn;
    }
    public void codeGenListDeclField(DecacCompiler compiler) {
        int varOffset = 1;
        for (AbstractDeclField declField : getList()) {
            declField.codeGenDeclField(compiler, varOffset);
            varOffset++;
        }
    }

}
