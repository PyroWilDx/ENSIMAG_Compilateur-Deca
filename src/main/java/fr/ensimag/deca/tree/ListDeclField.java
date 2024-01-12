package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class ListDeclField extends TreeList<AbstractDeclField> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField decl : getList()) {
            decl.decompile(s);
        }
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
                        "' already defined.", decl.getLocation());
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

    public void codeGenVTable(DecacCompiler compiler, VTable vTable) {
        int offset = 1;
        for (AbstractDeclField declField : getList()) {
            declField.codeGenVTable(compiler, vTable, offset);
            offset++;
        }
    }

    public void codeGenSetFieldsTo0(DecacCompiler compiler) {
        compiler.addInstruction(
                new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        int varOffset = 1;
        AbstractDeclField.TypeCode lastTypeCode = null;
        for (AbstractDeclField declField : getList()) {
            AbstractDeclField.TypeCode currTypeCode = declField.getInitTypeCode();
            declField.codeGenSetFieldTo0(compiler, varOffset, currTypeCode != lastTypeCode);
            varOffset++;
            lastTypeCode = currTypeCode;
        }
    }

    public void codeGenListDeclField(DecacCompiler compiler) {
        int varOffset = 1;
        AbstractDeclField.TypeCode lastTypeCode = null;
        for (AbstractDeclField declField : getList()) {
            AbstractDeclField.TypeCode currTypeCode =
                    declField.codeGenDeclField(compiler, varOffset, lastTypeCode);
            varOffset++;
            lastTypeCode = currTypeCode;
        }
    }

}
