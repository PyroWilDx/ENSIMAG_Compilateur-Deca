package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

public class DeclMethodAsm extends AbstractDeclMethod {
    private final String code;
    private final AbstractIdentifier type;
    private final AbstractIdentifier name;
    private final ListDeclParam params;

    public DeclMethodAsm(String code, AbstractIdentifier type, AbstractIdentifier name,
                         ListDeclParam params) {
        this.code = code;
        this.type = type;
        this.name = name;
        this.params = params;
    }

    @Override
    public boolean isOverride() {
        return false; // TODO
    }

    @Override
    public int getMethodIndex() {
        return 0; // TODO
    }

    public void codeGenVTable(DecacCompiler compiler, VTable vTable, int methodOffset) {
        // nothing
    }

    public void codeGenDeclMethod(DecacCompiler compiler) {
        String methodName = name.getName().getName();

        compiler.addLabel(new Label(methodName));
        compiler.add(new LineAsm(code));
    }

    public SymbolTable.Symbol getName() {
        return null;
    }

    public EnvironmentExp verifyDeclMethodMembers(DecacCompiler compiler,
                                                  SymbolTable.Symbol superClass,
                                                  int index) {
        return null;
        // TODO faut pas faire un truc là ?
        // Done
    }

    public void verifyDeclMethodBody(DecacCompiler compiler,
                                     EnvironmentExp localEnv,
                                     ClassDefinition currentClass) {
        // TODO faut pas faire un truc là ?
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
        s.print("(");
        this.params.decompile(s);
        s.println(")");
        s.println("asm(\"" + code + "\");");
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

