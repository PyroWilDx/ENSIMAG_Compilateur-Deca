package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.DeclVarUtils;
import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp envExpSup,
                                 EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError { //regle 3.17
        EnvironmentExp declEnv = new EnvironmentExp(null);
        //TypeDefinition typeDef = compiler.environmentType.defOfType((this.type.getName()));
        Type varType = this.type.verifyType(compiler);
        try {
            ExpDefinition def = new VariableDefinition(varType, this.getLocation());
            this.varName.setDefinition(def);
            declEnv.declare(varName.getName(), def);
            // CONDITION type != void
        } catch (EnvironmentExp.DoubleDefException e) { // pas possible d'avoir cette erreur mais idea pas content
            throw new UnknownError();
        }
        if (!localEnv.disjointUnion(declEnv)) {
            throw new ContextualError("Variable '" + varName.getName().toString() + "' already declared.", this.getLocation());
        }
        if (varType == compiler.environmentType.VOID) {
            throw new ContextualError("Variable type cannot be void", this.getLocation());
        }
        EnvironmentExp localEnvInit = EnvironmentExp.empile(localEnv, envExpSup);
        this.initialization.verifyInitialization(compiler, varType, localEnvInit, currentClass);
        // Done
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        DeclVarUtils.currDeclVarType = type.getType();
        initialization.codeGenInit(compiler);
        GPRegister reg = RegUtils.takeBackLastReg();
//        DAddr dAddr = varName.getExpDefinition().getOperand();
        DAddr dAddr = new RegisterOffset(DeclVarUtils.getGbOffset(), Register.GB);
        varName.getExpDefinition().setOperand(dAddr); // TODO (Remove and Replace)
        compiler.addInstruction(new STORE(reg, dAddr));
        RegUtils.freeReg(reg);
        DeclVarUtils.incrGbVarCount();
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
