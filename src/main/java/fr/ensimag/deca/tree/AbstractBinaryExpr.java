package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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
            }
            if (type1.equals(compiler.environmentType.INT)) {
                this.leftOperand = new ConvFloat(this.leftOperand);
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
        GPRegister saveReg = RegUtils.takeBackLastReg();

        boolean pushed = false;
        if (RegUtils.isUsingAllRegs()) {
            compiler.addInstruction(new PUSH(saveReg));
            RegUtils.freeReg(saveReg);
            pushed = true;
        }

        getRightOperand().codeGenInst(compiler);
        GPRegister valReg = RegUtils.takeBackLastReg();

        if (pushed) {
            compiler.addInstruction(new LOAD(valReg, Register.R0));
            saveReg = valReg;
            valReg = Register.R0;
            compiler.addInstruction(new POP(saveReg));
        }

        codeGenOp(compiler, valReg, saveReg);
        RegUtils.freeReg(valReg);
        RegUtils.freeReg(saveReg);
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
