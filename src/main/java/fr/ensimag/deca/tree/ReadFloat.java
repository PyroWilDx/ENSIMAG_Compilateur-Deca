package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;

import java.io.PrintStream;

/**
 *
 * @author gl47
 * @date 01/01/2024
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.environmentType.FLOAT);
        return compiler.environmentType.FLOAT;
    }

    @Override
    protected void codeGenOpRead(DecacCompiler compiler) {
        compiler.addInstruction(new RFLOAT());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
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
