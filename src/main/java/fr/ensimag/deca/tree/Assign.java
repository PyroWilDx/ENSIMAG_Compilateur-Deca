package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl47
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type type = this.getLeftOperand().verifyLValue(compiler, localEnv, currentClass);
        this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, type));
        this.setType(type);
        return type;
    }

    @Override
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        // Done (Not Used)
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        StackManager sM = compiler.getStackManager();
        CondManager cM = compiler.getCondManager();
        VTableManager vTM = compiler.getVTableManager();

        boolean saveReg = false;
        DAddr iAddr;
        if (getLeftOperand() instanceof AbstractIdentifier) {
            AbstractIdentifier lIdent = (AbstractIdentifier) getLeftOperand();
            if (lIdent.getExpDefinition().getOperand() == null) {
                saveReg = true;
            }
            iAddr = CodeGenUtils.extractAddrFromIdent(compiler, lIdent);
        } else { // Should be a FieldSelection
            FieldSelection lFieldSelect = (FieldSelection) getLeftOperand();
            saveReg = true;
            iAddr = lFieldSelect.getAddrOfField(compiler);
        }

        GPRegister savedReg = null;
        boolean pushed = false;
        if (saveReg) {
            savedReg = rM.getLastReg();
            if (rM.isUsingAllRegs()) {
                if (!(getRightOperand() instanceof AbstractLiteral)) {
                    compiler.addInstruction(new PUSH(savedReg));
                    rM.freeReg(savedReg);
                    sM.incrTmpVar();
                    pushed = true;
                }
            }
        }

        getRightOperand().codeGenInst(compiler);
        GPRegister regRight = rM.getLastRegOrImm(compiler);
        if (regRight == Register.R0) {
            if (pushed || cM.isDoingCond() || vTM.isInMethod()) {
                regRight = rM.getFreeReg();
                compiler.addInstruction(new LOAD(Register.R0, regRight));
            }
        }

        if (pushed) {
            compiler.addInstruction(new LOAD(regRight, Register.R0));
            savedReg = regRight;
            regRight = Register.R0;
            compiler.addInstruction(new POP(savedReg));
            sM.decrTmpVar();
        }

        compiler.addInstruction(new STORE(regRight, iAddr));
        rM.freeReg(regRight);
        rM.freeReg(savedReg);
        // Done
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
