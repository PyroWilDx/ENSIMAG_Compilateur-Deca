package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
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
//        try {
//            ExpDefinition def = new VariableDefinition(varType, this.getLocation());
//            this.varName.setDefinition(def);
//            declEnv.declare(varName.getName(), def);
//            // CONDITION type != void
//        } catch (EnvironmentExp.DoubleDefException e) { // pas possible d'avoir cette erreur mais idea pas content
//            throw new UnknownError();
//        }
        // j't'ai mis ca en comm, psq sinon ca compile pas
        ExpDefinition def = new VariableDefinition(varType, this.getLocation());
        this.varName.setDefinition(def);
        try {
            declEnv.declare(varName.getName(), def);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new DecacInternalError("Symbol cannot have been declared twice.");
        }
        if (localEnv.disjointUnion(declEnv) != null) {
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
        RegManager rM = compiler.getRegManager();
        StackManager sM = compiler.getStackManager();

//        initialization.setVarType(type.getType()); // No Init
        initialization.codeGenInit(compiler);

        GPRegister gpReg = rM.getLastRegOrImm(compiler);
        DAddr varAddr = sM.getGbOffsetAddr();
        varName.getExpDefinition().setOperand(varAddr);
        compiler.addInstruction(new STORE(gpReg, varAddr));
        rM.freeReg(gpReg);
        sM.incrGbVarCpt();
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
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
