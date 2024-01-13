package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        CondManager cM = compiler.getCondManager();

        if ((getLeftOperand() instanceof BooleanLiteral) &&
                getRightOperand() instanceof BooleanLiteral) {
            BooleanLiteral lOperand = (BooleanLiteral) getLeftOperand();
            BooleanLiteral rOperand = (BooleanLiteral) getRightOperand();
            boolean caseTrue1 = (doEq() && lOperand.getValue() == rOperand.getValue() && isNotInFalse) ||
                    (!doEq() && lOperand.getValue() != rOperand.getValue() && isNotInFalse);
            boolean caseTrue2 = (doEq() && lOperand.getValue() != rOperand.getValue() && !isNotInFalse) ||
                    (!doEq() && lOperand.getValue() == rOperand.getValue() && !isNotInFalse);
            if (caseTrue1 || caseTrue2) {
                if (cM.isDoingCond()) {
                    compiler.addInstruction(new BRA(branchLabel));
                } else {
                    rM.setLastImm(new ImmediateInteger(1));
                }
            } else { // False
                if (!cM.isDoingCond()) {
                    rM.setLastImm(new ImmediateInteger(0));
                }
            }
        } else if (getLeftOperand() instanceof BooleanLiteral) {
            BooleanLiteral lOperand = (BooleanLiteral) getLeftOperand();
            if ((doEq() && !lOperand.getValue()) ||
                    (!doEq() && lOperand.getValue())) {
                getRightOperand().isNotInFalse = !getRightOperand().isNotInFalse;
            }
            getRightOperand().branchLabel = branchLabel;
            getRightOperand().codeGenInst(compiler);
        } else if (getRightOperand() instanceof BooleanLiteral) {
            BooleanLiteral rOperand = (BooleanLiteral) getRightOperand();
            if ((doEq() && !rOperand.getValue()) ||
                    (!doEq() && rOperand.getValue())) {
                getLeftOperand().isNotInFalse = !getLeftOperand().isNotInFalse;
            }
            getLeftOperand().branchLabel = branchLabel;
            getLeftOperand().codeGenInst(compiler);
        } else {
            super.codeGenInst(compiler);
        }
    }

    public abstract boolean doEq();

}