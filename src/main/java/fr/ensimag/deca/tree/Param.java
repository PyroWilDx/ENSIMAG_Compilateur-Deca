package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class Param extends AbstractParam{
    private AbstractIdentifier type;
    private AbstractIdentifier name;

    public Param(AbstractIdentifier type, AbstractIdentifier name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
        throw new DecacInternalError("not implemented yet");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO
        throw new DecacInternalError("not implemented yet");

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
        throw new DecacInternalError("not implemented yet");

    }

    @Override
    public Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
        Type type = this.type.verifyType(compiler);
        if (type.equals(compiler.environmentType.VOID)) {
            throw new ContextualError("Parameter type cannot be void.", getLocation());
        }
        return type;
        // Done
    }
}
