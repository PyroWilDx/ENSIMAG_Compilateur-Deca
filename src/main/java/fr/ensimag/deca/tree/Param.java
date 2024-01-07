package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;

public class Param extends AbstractParam {
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
        type.prettyPrint(s,prefix,false);
        name.prettyPrint(s,prefix,true);
        //throw new DecacInternalError("not implemented yet");

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
        throw new DecacInternalError("not implemented yet");

    }

    @Override
    public SymbolTable.Symbol getName() {
        return this.name.getName();
    }

    @Override
    public Type verifyDeclParamMembers(DecacCompiler compiler) throws ContextualError {
        Type type = this.type.verifyType(compiler);
        if (type.equals(compiler.environmentType.VOID)) {
            throw new ContextualError("Parameter type cannot be void.", getLocation());
        }
        return type;
        // Done
    }

    @Override
    public EnvironmentExp verifyDeclParamBody(DecacCompiler compiler) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        EnvironmentExp env = new EnvironmentExp(null);
        ExpDefinition def = new ParamDefinition(t, getLocation());
        env.declare(this.name.getName(), def);
        return env;
        // Done
    }
}
