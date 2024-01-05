package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListParam params;
    // TODO peutetre creer une classe bloc pour factoriser avec Main et se rapprocher du sujet
    // TODO mais dans ce cas penser à changer le parser
    private ListDeclVar declVariables;
    private ListInst insts;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name,
                      ListParam params, ListDeclVar declVariables, ListInst insts) {
        this.type = type;
        this.name = name;
        this.params = params;
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    public void codeGenVTable(DecacCompiler compiler, AbstractIdentifier className) {
        StackManager sM = compiler.getStackManager();
        VTableManager vTM = compiler.getVTableManager();

        String mLabelStr = "code." + className.getName().getName() +
                "." + name.getName().getName();
        compiler.addInstruction(new LOAD(new LabelOperand(mLabelStr), Register.R0));

        DAddr mAddr = sM.getGbOffsetAddr();
        compiler.addInstruction(new STORE(Register.R0, mAddr));
        sM.incrVTableCpt();

        vTM.addMethodToClass(className.getName().getName(), name.getName().getName(), mAddr);
        // TODO (Merde comment on sait si c'est une méthode surchargée ou pas??)
        // Done
    }

    @Override
    public void codeGenDeclMethod(DecacCompiler compiler) {
        // TODO (decl method code)
    }

    @Override
    public SymbolTable.Symbol getName() {
        return null;
    }

    @Override
    public EnvironmentExp verifyDeclMethod(DecacCompiler compiler, ClassDefinition superClass) throws ContextualError {
        // TODO
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO
        throw new DecacInternalError("not implemented yet");

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
        throw new DecacInternalError("not implemented yet");
    }
}
