package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public abstract class AbstractDot extends AbstractExpr{
    private AbstractExpr classMere;
    private AbstractExpr champ;
    public AbstractDot(AbstractExpr classMere, AbstractExpr champ){
        Validate.notNull(classMere, "classMere cannot be null");
        Validate.notNull(champ, "champ cannot be null");
        this.classMere = classMere;
        this.champ = champ;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        //TODO
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //TODO
    }

    protected abstract void codeGenDot(DecacCompiler compiler,
                                      DVal valReg, GPRegister saveReg);

    @Override
    public void decompile(IndentPrintStream s) {
        //TODO
    }

    public AbstractExpr getClassMere(){
        return this.classMere;
    }
    public AbstractExpr getChamp(){
        return this.champ;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classMere.iter(f);
        champ.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classMere.prettyPrint(s, prefix, false);
        champ.prettyPrint(s, prefix, true);
    }
}
