package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        CondManager cM = compiler.getCondManager();

        cM.doCond();

        boolean firstCond = false;
        if (branchLabel == null) {
            int idCpt = cM.getAndIncrIdCpt();
            branchLabel = new Label("fastEndLabel" + idCpt);
            firstCond = true;
        }

        Label fastEndLabel = null;

        if (isNotInFalse) {
            getLeftOperand().isNotInFalse = false;
            int idCpt = cM.getAndIncrIdCpt();
            fastEndLabel = new Label("fastEndLabel" + idCpt);
            getLeftOperand().branchLabel = fastEndLabel;
            getRightOperand().isNotInFalse = true;
            getRightOperand().branchLabel = branchLabel;
        } else {
            getLeftOperand().isNotInFalse = false;
            getLeftOperand().branchLabel = branchLabel;
            getRightOperand().isNotInFalse = false;
            getRightOperand().branchLabel = branchLabel;
        }

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        if (fastEndLabel != null) compiler.addLabel(fastEndLabel);

        if (firstCond){
            int idCpt = cM.getAndIncrIdCpt();
            Label endLabel = new Label("endLabel" + idCpt);

            GPRegister gpReg = rM.getFreeReg();

            compiler.addInstruction(new LOAD(0, gpReg));
            compiler.addInstruction(new BRA(endLabel));

            compiler.addLabel(branchLabel);
            compiler.addInstruction(new LOAD(1, gpReg));

            rM.freeReg(gpReg);

            compiler.addLabel(endLabel);
        }

        cM.exitCond();
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
