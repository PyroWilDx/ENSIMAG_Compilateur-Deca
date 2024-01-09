package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class This extends AbstractExpr{
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if (currentClass.getType().equals(compiler.environmentType.OBJECT)) {
            throw new ContextualError("'this' cannot refer to Object class.", getLocation());
        }
        this.setType(currentClass.getType());
        return currentClass.getType(); // TODO aucun moyen que ce soit pas un ClassType donc je comprends pas trop l'autre condition dans la regle 3.43
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
