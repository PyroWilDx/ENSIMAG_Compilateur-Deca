package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

import java.io.PrintStream;
/**
 * Integer literal
 *
 * @author gl47
 * @date 13/01/2024
 */
public class NullLiteral extends AbstractLiteral{

    public NullLiteral(){

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type exprType = compiler.environmentType.INT;
        setType(exprType);
        return exprType;
        // TODO
    }

    @Override
    String prettyPrintNode() {
        return "(null)";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //TODO
    }

    @Override
    public void decompile(IndentPrintStream s) {
        //TODO
    }


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
}
