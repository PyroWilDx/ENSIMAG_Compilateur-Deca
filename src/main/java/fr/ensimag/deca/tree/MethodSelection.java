package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class MethodSelection  extends AbstractMethodCall {
    private AbstractExpr expr;
    private AbstractIdentifier methodIdent;
    private ListExpr listExpr;

    public MethodSelection(AbstractExpr expr,AbstractIdentifier methodIdent ,ListExpr listExpr){
        this.expr = expr;
        this.methodIdent = methodIdent;
        this.listExpr = listExpr;
    }

    public Type verifyMethodCall(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return this.verifyMethodCall(compiler, localEnv, currentClass);
    }

    @Override
    public void codeGenMethodCall(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);

        methodIdent.prettyPrint(s, prefix, false);
        listExpr.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        methodIdent.iter(f);
        listExpr.iter(f);
    }
}

