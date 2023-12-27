package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }
    private AbstractExpr operand;
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    protected abstract String getOperatorName();

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        String op = this.getOperatorName();
        Type operandType = this.operand.verifyExpr(compiler, localEnv, currentClass);
        Type type = compiler.environmentType.getTypeUnaryOp(op, operandType);
        if (type == null) {
            throw new ContextualError("Unary  operation : '" + op
                    + "' cannot have operand of type : '"
                    + operandType.getName() + "'", this.getLocation());
        }
        return type;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getOperand().codeGenInst(compiler);
        GPRegister reg = RegUtils.getCurrReg();
        codeGenOpUnary(compiler, reg);
        // TODO (Not Enough Registers) and (When Register is Freed ?)
    }

    protected abstract void codeGenOpUnary(DecacCompiler compiler, GPRegister reg);

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

}
