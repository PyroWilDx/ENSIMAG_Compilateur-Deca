package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl47
 * @date 01/01/2024
 */
public class IfThenElse extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;
    private final ListInst elseBranch;

    private static int ifThenElseCpt = 0;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        // Done
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label startElseLabel = new Label("startElse" + ifThenElseCpt);
        Label endIfThenElseLaBel = new Label("endIfThenElse" + ifThenElseCpt);
        ifThenElseCpt++;

        // noinspection Duplicates
        condition.codeGenInst(compiler);
        GPRegister reg = compiler.getRegManager().takeBackLastReg();
        compiler.addInstruction(new CMP(0, reg));
        compiler.getRegManager().freeReg(reg);
        compiler.addInstruction(new BEQ(startElseLabel));
        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(endIfThenElseLaBel));
        compiler.addLabel(startElseLabel);
        elseBranch.codeGenListInst(compiler);
        compiler.addLabel(endIfThenElseLaBel);
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
