package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VTable;
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

    public void codeGenVTable(DecacCompiler compiler, VTable vTable) {
        for (AbstractDeclMethod method : getList()) {
            method.codeGenVTable(compiler, vTable);
        }
        // Done
    }
    public EnvironmentExp verifyListDeclMethodMembers(DecacCompiler compiler,
                                                      SymbolTable.Symbol superClass) throws ContextualError {
        EnvironmentExp envReturn = new EnvironmentExp(null);
        int index = 1;
        for (AbstractDeclMethod decl : this.getList()) {
            EnvironmentExp env = decl.verifyDeclMethodMembers(compiler, superClass, index);
            index++;
            if (envReturn.disjointUnion(env) != null) {
                throw new ContextualError("Method '" + decl.getName()
                        + "' already defined.", getLocation());
            }
        }
        return envReturn;
    }
    public void verifyListDeclMethodBody(DecacCompiler compiler,
                                         EnvironmentExp localEnv,
                                         ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclMethod decl : getList()) {
            decl.verifyDeclMethodBody(compiler, localEnv, currentClass);
        }
    }
    public void codeGenListDeclMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod method : getList()) {
            method.codeGenDeclMethod(compiler);
        }
    }

}
