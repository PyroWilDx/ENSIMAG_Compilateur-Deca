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
    protected void addOperation(CondManager cM) {
        cM.addOrOperation();
    }

    @Override
    protected void addCondLabels(CondManager cM) {
        cM.addCondLabels(getLazyCondLabel(), getEndLazyCondLabel());
    }

    @Override
    protected int getNotLazyValue() {
        return (inNot) ? 1 : 0;
    }

    @Override
    protected int getLazyValue() {
        return (inNot) ? 0 : 1;
    }

    @Override
    protected Label getLastCondLabel(CondManager cM) {
        return cM.getLastCondFalseLabel();
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
