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
    public Label setOperandCondVals(CondManager cM) {
        Label fastEndLabel = null;

        if (isNotInFalse) {
            getLeftOperand().isNotInFalse = false;
            fastEndLabel = cM.getUniqueLabel();
            getLeftOperand().branchLabel = fastEndLabel;
            getRightOperand().isNotInFalse = true;
            getRightOperand().branchLabel = branchLabel;
        } else {
            getLeftOperand().isNotInFalse = false;
            getLeftOperand().branchLabel = branchLabel;
            getRightOperand().isNotInFalse = false;
            getRightOperand().branchLabel = branchLabel;
        }

        return fastEndLabel;
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
