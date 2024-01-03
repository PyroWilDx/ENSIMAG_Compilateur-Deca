package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
                              AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        String op = this.getOperatorName();
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type = compiler.environmentType.getTypeBinaryOp(op, type1, type2);
        if (type == null) {
            throw new ContextualError("Binary operation : '" + op +
                    "' cannot have operands of types : '" + type1.getName() + "', '"
                    + type2.getName() + "'.", this.getLocation());
        }
        if (type.equals(compiler.environmentType.FLOAT)) {
            if (type2.equals(compiler.environmentType.INT)) {
                this.rightOperand = new ConvFloat(this.rightOperand);
                this.rightOperand.verifyExpr(compiler, localEnv, currentClass); // TODO tout pourri
            }
            if (type1.equals(compiler.environmentType.INT)) {
                this.leftOperand = new ConvFloat(this.leftOperand);
                this.leftOperand.verifyExpr(compiler, localEnv, currentClass); //TODO de même
            }
        }
        this.leftOperand.setType(type); // TODO pas forcement la meilleure idée
        this.rightOperand.setType(type);
        setType(type);
        return type;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getLeftOperand().codeGenInst(compiler);
        GPRegister regLeft = compiler.getRegManager().getLastRegOrImm(compiler);

        boolean pushed = false;
        if (compiler.getRegManager().isUsingAllRegs()) {
            if (!(getRightOperand() instanceof Literal)) {
                compiler.addInstruction(new PUSH(regLeft));
                compiler.getRegManager().freeReg(regLeft);
                compiler.getStackManager().incrStackSize();
                pushed = true;
            } // Else, don't need to PUSH because Literal don't need Registers.
        }

        getRightOperand().codeGenInst(compiler);
        DVal lastImmediateRight = compiler.getRegManager().getLastImmediate();
        GPRegister regRight = null;
        if (lastImmediateRight == null) {
            regRight = compiler.getRegManager().getLastReg();
        }

        if (pushed) {
            if (lastImmediateRight == null) {
                compiler.addInstruction(new LOAD(regRight, Register.R0));
                regLeft = regRight;
                regRight = Register.R0;
            } else {
                regLeft = compiler.getRegManager().getFreeReg();
            }
            compiler.addInstruction(new POP(regLeft));
            compiler.getStackManager().decrStackSize();
        }

        DVal dVal = (lastImmediateRight == null) ? regRight : lastImmediateRight;
        codeGenOp(compiler, dVal, regLeft);

        compiler.getRegManager().freeReg(regRight);
        compiler.getRegManager().freeReg(regLeft);
        // Done
    }

    protected abstract void codeGenOp(DecacCompiler compiler,
                                      DVal valReg, GPRegister saveReg);

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

}
