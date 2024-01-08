package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public abstract class AbstractDot extends AbstractExpr {
    private AbstractExpr classExpr;
    private AbstractExpr champ;

    public AbstractDot(AbstractExpr classExpr, AbstractExpr champ) {
        Validate.notNull(classExpr, "classExpr cannot be null");
        Validate.notNull(champ, "champ cannot be null");
        this.classExpr = classExpr;
        this.champ = champ;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();

        GPRegister gpReg = rM.getFreeReg();
//        compiler.addInstruction(new LOAD(addrInstanceObj, gpReg));
        compiler.addInstruction(new CMP(new NullOperand(), gpReg));
        compiler.addInstruction(new BEQ(ErrorUtils.nullPointerLabel));
        rM.freeReg(gpReg);
        // TODO (a voir comment mettre l'addr du champ comme un identifier ?)
        // TODO (ou ca se trouve ya meme pas besoin)
    }

    @Override
    public void decompile(IndentPrintStream s) {
        //TODO
    }

    public AbstractExpr getClassExpr() {
        return this.classExpr;
    }

    public AbstractExpr getChamp() {
        return this.champ;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classExpr.iter(f);
        champ.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classExpr.prettyPrint(s, prefix, false);
        champ.prettyPrint(s, prefix, true);
    }
}
