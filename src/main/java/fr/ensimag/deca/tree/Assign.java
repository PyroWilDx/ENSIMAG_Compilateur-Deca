package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl47
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type type = this.getLeftOperand().verifyLValue(compiler, localEnv, currentClass);
        this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, type);
        this.setType(type);
        return type;
    }

    @Override
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        // Done (Not Used)
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        AbstractIdentifier lIdent = (AbstractIdentifier) getLeftOperand();
        getRightOperand().codeGenInst(compiler);
        GPRegister reg = compiler.getRegManager().loadImmediateIntoFreeReg(compiler);
        compiler.addInstruction(new STORE(reg, lIdent.getExpDefinition().getOperand()));
        compiler.getRegManager().freeReg(reg);
        // Done
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
