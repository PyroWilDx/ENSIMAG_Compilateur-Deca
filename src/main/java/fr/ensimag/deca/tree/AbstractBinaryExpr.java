package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorManager;
import fr.ensimag.deca.codegen.GameBoyManager;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
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
        if (localEnv == null) localEnv = new EnvironmentExp(null);
        if (currentClass == null) currentClass = compiler.environmentType.OBJECT.getDefinition();
        String op = this.getOperatorName();
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type = compiler.environmentType.getTypeBinaryOp(op, type1, type2);
        if (type == null) {
            throw new ContextualError("Binary operation '" + op +
                    "' cannot have operands of types : '" + type1.getName() + "', '"
                    + type2.getName() + "'.", this.getLocation());
        }
        if (type.isFloat()) {
            if (type2.isInt()) {
                this.rightOperand = new ConvFloat(this.rightOperand);
                this.rightOperand.verifyExpr(compiler, localEnv, currentClass); // TODO tout pourri
            }
            if (type1.isInt()) {
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
        RegManager rM = compiler.getRegManager();
        ErrorManager eM = compiler.getErrorManager();
        StackManager sM = compiler.getStackManager();

        if (!GameBoyManager.doCp) {
            if ((getLeftOperand() instanceof FloatLiteral) && // Juste pour test rapide haha
                    getRightOperand() instanceof FloatLiteral) {
                if (this instanceof Divide) {
                    FloatLiteral fLL = (FloatLiteral) getLeftOperand();
                    FloatLiteral fLR = (FloatLiteral) getRightOperand();
                    if (fLR.getValue() != 0.f) {
                        GPRegister gpReg = rM.getFreeReg();
                        compiler.addInstruction(new LOAD(fLL.getValue() / fLR.getValue(), gpReg));
                        rM.freeReg(gpReg);
                        return;
                    }
                }
            }
        }

        getLeftOperand().codeGenInst(compiler);
        GPRegister regLeft = rM.getLastRegOrImm(compiler);
        if (GameBoyManager.doCp) {
            if (regLeft == Register.HL) {
                regLeft = rM.getFreeReg();
                compiler.addInstruction(new LOAD_REG(Register.HL.getLowReg(), regLeft.getLowReg()));
            }
        } else {
            if (regLeft == Register.R0) { // On vient de faire un return
                // L'opérande de droite peut aussi utiliser R0
                // Donc on fait un LOAD, nécessaire pour sauvegarder la valeur
                regLeft = rM.getFreeReg();
                compiler.addInstruction(new LOAD(Register.R0, regLeft));
            }
        }

        boolean pushed = false;
        if (rM.isUsingAllRegs()) {
            if (!(getRightOperand() instanceof AbstractLiteral)) {
                compiler.addInstruction(new PUSH(regLeft));
                rM.freeReg(regLeft);
                sM.incrTmpVar();
                pushed = true;
            } // Else, don't need to PUSH because Literal don't need Registers.
        }

        getRightOperand().codeGenInst(compiler);
        DVal lastImmRight = rM.getLastImm();
        GPRegister regRight = null;
        if (lastImmRight == null) {
            regRight = rM.getLastReg();
            if (GameBoyManager.doCp) {
                if (regRight == Register.HL) {
                    if (pushed) {
                        regRight = rM.getFreeReg();
                        compiler.addInstruction(new LOAD_REG(Register.HL.getLowReg(), regRight.getLowReg()));
                    }
                }
            } else {
                if (regRight == Register.R0) { // On vient de faire un return
                    if (pushed) {
                        // Si on avait PUSH, alors on va utiliser R0 juste après
                        // Donc on doit sauvegarder la valeur
                        regRight = rM.getFreeReg();
                        compiler.addInstruction(new LOAD(Register.R0, regLeft));
                    }
                }
            }
        }

        if (pushed) {
            if (GameBoyManager.doCp) {
                // Non ce n'est pas null IDEA
                compiler.addInstruction(new LOAD_REG(regRight.getLowReg(), Register.HL.getLowReg()));
            } else {
                compiler.addInstruction(new LOAD(regRight, Register.R0));
            }
            regLeft = regRight;
            if (GameBoyManager.doCp) {
                regRight = Register.HL;
            } else {
                regRight = Register.R0;
            }
            compiler.addInstruction(new POP(regLeft));
            sM.decrTmpVar();
        }

        if (GameBoyManager.doCp) {
            DVal dVal = (lastImmRight == null) ? regRight.getLowReg() : lastImmRight;
            codeGenOp(compiler, dVal, regLeft.getLowReg());
        } else {
            DVal dVal = (lastImmRight == null) ? regRight : lastImmRight;
            codeGenOp(compiler, dVal, regLeft);
        }

        if (!GameBoyManager.doCp) {
            if (getType().isFloat()) {
                if (compiler.getCompilerOptions().doCheck()) {
                    compiler.addInstruction(new BOV(eM.getFloatOverflowLabel()));
                }
            }
        }

        rM.freeReg(regRight);
        rM.freeReg(regLeft);
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
