package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
        // TODO ?
    }

    public void codeGenVTable(DecacCompiler compiler, AbstractIdentifier className) {
        for (AbstractDeclMethod method : getList()) {
            method.codeGenVTable(compiler, className);
        }
        // Done
    }
    public EnvironmentExp verifyListDeclMethod(DecacCompiler compiler,
                                               SymbolTable.Symbol superClass) throws ContextualError {
        EnvironmentExp envReturn = new EnvironmentExp(null);
        int index = 1;
        for (AbstractDeclMethod decl : this.getList()) {
            EnvironmentExp env = decl.verifyDeclMethod(compiler, superClass, index);
            index++;
            if (!envReturn.disjointUnion(env)) {
                throw new ContextualError("Method '" + decl.getName()
                        + "' already defined.", getLocation());
            }
        }
        return envReturn;
    }
    public void codeGenListDeclMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod method : getList()) {
            method.codeGenDeclMethod(compiler);
        }
    }

}
