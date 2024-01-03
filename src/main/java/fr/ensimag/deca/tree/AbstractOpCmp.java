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
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        compiler.addInstruction(new CMP(valReg, saveReg));
        CondManager cM = compiler.getCondManager();
        if (cM.isInCond()) {
            if (cM.isInAnd()) {
                compiler.addInstruction(getBranchInvOpCmpInst(cM.getCurrCondFalseLabel()));
            } else if (cM.isInOr()) {
                compiler.addInstruction(getBranchOpCmpInst(cM.getCurrCondTrueLabel()));
            } else {
                compiler.addInstruction(getBranchInvOpCmpInst(cM.getCurrCondFalseLabel()));
            }
        } else {
            GPRegister reg = compiler.getRegManager().getFreeReg();
            compiler.addInstruction(getOpCmpInst(reg));
        }
    }

    protected abstract Instruction getBranchInvOpCmpInst(Label bLabel);

    protected abstract Instruction getBranchOpCmpInst(Label bLabel);

    protected abstract Instruction getOpCmpInst(GPRegister reg);
}
