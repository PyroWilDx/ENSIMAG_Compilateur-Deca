package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class BooleanLiteral extends AbstractLiteral {

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
        RegManager rM = compiler.getRegManager();
        CondManager cM = compiler.getCondManager();

        if (cM.isInCond()) {
            Instruction bInst = null;
            Label tLabel = cM.getCurrCondTrueLabel();
            Label fLabel = cM.getCurrCondFalseLabel();
            if (cM.isInAnd()) {
                if (!value && !inNot) bInst = new BRA(fLabel);
                else if (value && inNot) bInst = new BRA(fLabel);
            } else if (cM.isInOr()) {
                if (value && !inNot) new BRA(tLabel);
                else if (!value && inNot) new BRA(tLabel);
            } else {
                if (!value && !inNot) bInst = new BRA(fLabel);
                else if (value && inNot) bInst = new BRA(fLabel);
            }
            if (bInst != null) compiler.addInstruction(bInst);
        } else {
            rM.setLastImm(new ImmediateInteger((value) ? 1 : 0));
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
