package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class BooleanLiteral extends Literal {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type exprType = compiler.environmentType.BOOLEAN;
        setType(exprType);
        return exprType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        CondManager cM = compiler.getCondManager();
        if (cM.isInCond()) {
            if (cM.isInAnd()) {
                if (!value && !cM.isInNot()) {
                    compiler.addInstruction(new BRA(cM.getCurrCondFalseLabel()));
                } else if (value && cM.isInNot()) {
                    compiler.addInstruction(new BRA(cM.getCurrCondTrueLabel()));
                }
            } else if (cM.isInOr()) {
                if (value && !cM.isInNot()) {
                    compiler.addInstruction(new BRA(cM.getCurrCondTrueLabel()));
                } else if (!value && cM.isInNot()) {
                    compiler.addInstruction(new BRA(cM.getCurrCondFalseLabel()));
                }
            } else {
                if (!value && !cM.isInNot()) {
                    compiler.addInstruction(new BRA(cM.getCurrCondFalseLabel()));
                } else if (value && cM.isInNot()) {
                    compiler.addInstruction(new BRA(cM.getCurrCondTrueLabel()));
                }
            }
        } else {
            compiler.getRegManager().setLastImmediate(new ImmediateInteger((value) ? 1 : 0));
        }
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

}
