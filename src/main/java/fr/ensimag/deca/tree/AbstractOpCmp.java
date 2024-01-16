package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.GameBoy;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SUB;

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
        CondManager cM = compiler.getCondManager();

        cM.doOpCmp();
        super.codeGenInst(compiler);
        cM.exitOpCmp();
    }

    @Override
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        CondManager cM = compiler.getCondManager();

        if (GameBoy.doCp) {
            if (doSub()) {
                if (!(valReg instanceof GPRegister)) {
                    if (valReg instanceof ImmediateInteger) {
                        ImmediateInteger valRegImmInt = (ImmediateInteger) valReg;
                        valRegImmInt.decrValue();
                    } else if (valReg instanceof ImmediateFloat) {
                        ImmediateFloat valRegImmFloat = (ImmediateFloat) valReg;
                        valRegImmFloat.decrValue();
                    }
                } else {
                    GPRegister gpReg = (GPRegister) valReg;
                    compiler.addInstruction(new SUB(1, gpReg));
                }
            }
        }

        compiler.addInstruction(new CMP(valReg, saveReg));
        if (cM.isDoingCond()) {
            if (isNotInFalse) compiler.addInstruction(getBranchOpCmpInst(branchLabel));
            else compiler.addInstruction(getBranchInvOpCmpInst(branchLabel));
        } else {
            if (isNotInFalse) compiler.addInstruction(getOpCmpInst(saveReg));
            else compiler.addInstruction(getInvOpCmpInst(saveReg));
        }
    }

    protected boolean doSub() {
        return false;
    }

    protected abstract Instruction getBranchInvOpCmpInst(Label bLabel);

    protected abstract Instruction getBranchOpCmpInst(Label bLabel);

    protected abstract Instruction getOpCmpInst(GPRegister gpReg);

    protected abstract Instruction getInvOpCmpInst(GPRegister gpReg);
}
