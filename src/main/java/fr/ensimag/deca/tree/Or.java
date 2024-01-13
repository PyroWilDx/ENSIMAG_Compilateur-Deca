package fr.ensimag.deca.tree;


import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.ima.pseudocode.Label;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Label setOperandCondVals(CondManager cM) {
        Label fastEndLabel = null;

        if (isNotInFalse) {
            getLeftOperand().isNotInFalse = true;
            getLeftOperand().branchLabel = branchLabel;
            getRightOperand().isNotInFalse = true;
            getRightOperand().branchLabel = branchLabel;
        } else {
            getLeftOperand().isNotInFalse = true;
            fastEndLabel = cM.getUniqueLabel();
            getLeftOperand().branchLabel = fastEndLabel;
            getRightOperand().isNotInFalse = false;
            getRightOperand().branchLabel = branchLabel;
        }

        return fastEndLabel;
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
