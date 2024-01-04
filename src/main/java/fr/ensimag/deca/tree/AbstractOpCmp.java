package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.getCondManager().doOpCmp();

        super.codeGenInst(compiler);

        compiler.getCondManager().exitOpCmp();
    }

    @Override
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        CondManager cM = compiler.getCondManager();
        compiler.addInstruction(new CMP(valReg, saveReg));
        Instruction opInst;
        if (cM.isInCond()) {
            Label tLabel = cM.getCurrCondTrueLabel();
            Label fLabel = cM.getCurrCondFalseLabel();
            if (cM.isInAnd()) {
                if (inNot) opInst = getBranchOpCmpInst(fLabel);
                else opInst = getBranchInvOpCmpInst(fLabel);
            } else if (cM.isInOr()) {
                if (inNot) opInst = getBranchInvOpCmpInst(tLabel);
                else opInst = getBranchOpCmpInst(tLabel);
            } else {
                if (inNot) opInst = getBranchOpCmpInst(fLabel);
                else opInst = getBranchInvOpCmpInst(fLabel);
            }
        } else {
            GPRegister reg = compiler.getRegManager().getFreeReg();
            if (inNot) opInst = getInvOpCmpInst(reg);
            else opInst = getOpCmpInst(reg);
            compiler.getRegManager().freeReg(reg);
        }
        compiler.addInstruction(opInst);
    }

    protected abstract Instruction getBranchInvOpCmpInst(Label bLabel);

    protected abstract Instruction getBranchOpCmpInst(Label bLabel);

    protected abstract Instruction getOpCmpInst(GPRegister reg);

    protected abstract Instruction getInvOpCmpInst(GPRegister reg);
}
