package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.LinkedList;

public class DeclMethodAsm extends AbstractDeclMethod {
    private final String code;
    private final AbstractIdentifier type;
    private final AbstractIdentifier name;
    private final ListParam params;

    public DeclMethodAsm(String code, AbstractIdentifier type, AbstractIdentifier name,
                         ListParam params) {
        this.code = code;
        this.type = type;
        this.name = name;
        this.params = params;
    }

    public void codeGenVTable(DecacCompiler compiler, VTable vTable) {
        // nothing
    }

    public void codeGenDeclMethod(DecacCompiler compiler) {
        compiler.addLabel(new Label(name.getName().getName()));
        compiler.add(new LineAsm(code));
    }

    public SymbolTable.Symbol getName() {
        return null;
    }

    public EnvironmentExp verifyDeclMethodMembers(DecacCompiler compiler,
                                                  SymbolTable.Symbol superClass,
                                                  int index) {
        return null;
        // Done
    }

    public void verifyDeclMethodBody(DecacCompiler compiler,
                                     EnvironmentExp localEnv,
                                     ClassDefinition currentClass) {
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO

        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        System.out.println(prefix + "AsmCode (" + code + ")");

    }

}

