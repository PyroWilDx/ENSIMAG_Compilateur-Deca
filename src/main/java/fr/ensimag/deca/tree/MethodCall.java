package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import jdk.vm.ci.code.RegisterValue;

import java.io.PrintStream;

public class MethodCall extends AbstractMethodCall{
    private AbstractExpr expr;
    private AbstractIdentifier methodIdent;
    private RValueStar rValueStar;
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type = this.expr.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition definition = compiler.environmentType.get(type.getName());
        ClassDefinition classDefinition = definition.asClassDefinition("Method call on native type.", getLocation());

        if (!type.isClass()) {
            throw new DecacInternalError("Inconsistent type, class or not class??");
        }
        EnvironmentExp classEnv = classDefinition.getMembers();
        MethodIdentNonTerminalReturn sigAndType = this.methodIdent.verifyMethodIdent(classEnv);
        Signature sig = sigAndType.getSignature();
        Type t = sigAndType.getType();
        this.rValueStar.verifyRValueStar(compiler, localEnv, currentClass, sig);
        return t;
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
