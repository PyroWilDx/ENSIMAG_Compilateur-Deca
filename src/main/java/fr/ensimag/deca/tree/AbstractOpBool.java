package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    private Label lazyCondLabel;
    private Label endLazyCondLabel;

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
        this.lazyCondLabel = null;
        this.endLazyCondLabel = null;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        CondManager cM = compiler.getCondManager();
        addOperation(cM);

        if (!cM.isInCond()) {
            int idCpt = compiler.getCondManager().getAndIncrIdCpt();
            lazyCondLabel = new Label("lazyCond" + idCpt);
            endLazyCondLabel = new Label("endLazyCond" + idCpt);
            addCondLabels(cM);
        }

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        if (lazyCondLabel != null && endLazyCondLabel != null) {
            GPRegister reg = compiler.getRegManager().getFreeReg();

            compiler.addInstruction(new LOAD(getNotLazyValue(cM.isInNot()), reg));
            compiler.addInstruction(new BRA(endLazyCondLabel));

            compiler.addLabel(lazyCondLabel);
            compiler.addInstruction(new LOAD(getLazyValue(cM.isInNot()), reg));

            compiler.getRegManager().freeReg(reg);

            compiler.addLabel(endLazyCondLabel);

            compiler.getCondManager().popCondLabels();
        }
        compiler.getCondManager().popOperation();
    }

    protected abstract void addOperation(CondManager cM);

    protected abstract void addCondLabels(CondManager cM);

    protected abstract int getNotLazyValue(boolean isInNot);

    protected abstract int getLazyValue(boolean isInNot);

    @Override
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        // Not Used
    }

    public Label getLazyCondLabel() {
        return lazyCondLabel;
    }

    public Label getEndLazyCondLabel() {
        return endLazyCondLabel;
    }
}
