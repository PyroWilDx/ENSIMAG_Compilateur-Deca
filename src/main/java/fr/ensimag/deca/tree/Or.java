package fr.ensimag.deca.tree;


import fr.ensimag.deca.codegen.CondManager;

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
    protected int getNotLazyValue(boolean isInNot) {
        return (isInNot) ? 1 : 0;
    }

    @Override
    protected int getLazyValue(boolean isInNot) {
        return (isInNot) ? 0 : 1;
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
