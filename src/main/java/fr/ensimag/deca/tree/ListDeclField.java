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
    public EnvironmentExp verifyListDeclFieldMembers(DecacCompiler compiler, SymbolTable.Symbol superClass,
                                                     SymbolTable.Symbol name) throws ContextualError {
        EnvironmentExp envReturn = new EnvironmentExp(null);
        int index = 1;
        for (AbstractDeclField decl : this.getList()) {
            EnvironmentExp env = decl.verifyDeclFieldMembers(compiler, superClass, name, index);
            index++;
            if (envReturn.disjointUnion(env) != null) {
                throw new ContextualError("Field '" + decl.getName() +
                        "' already defined.", getLocation());
            }
        }
        return envReturn;
        // Done
    }
    public void verifyListDeclFieldBody(DecacCompiler compiler,
                                        EnvironmentExp localEnv,
                                        ClassDefinition classDef) throws ContextualError {
        for (AbstractDeclField decl : getList()) {
            decl.verifyDeclFieldBody(compiler, localEnv, classDef);
        }
    }
    public void codeGenListDeclField(DecacCompiler compiler) {
        int varOffset = 1;
        for (AbstractDeclField declField : getList()) {
            declField.codeGenDeclField(compiler, varOffset);
            varOffset++;
        }
    }

}
