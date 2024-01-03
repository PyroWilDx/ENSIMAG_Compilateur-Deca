package fr.ensimag.deca.tree;


import fr.ensimag.deca.codegen.CondManager;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void addOperation(CondManager cM) {
        cM.addAndOperation();
    }

    @Override
    protected void addCondLabels(CondManager cM) {
        cM.addCondLabels(getEndLazyCondLabel(), getLazyCondLabel());
    }

    @Override
    protected int getNotLazyValue() {
        return (inNot) ? 0 : 1;
    }

    @Override
    protected int getLazyValue() {
        return (inNot) ? 1 : 0;
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
