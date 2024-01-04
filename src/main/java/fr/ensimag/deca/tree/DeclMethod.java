package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod{
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
    public void codeGenVTable(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("TODO")), Register.R0));
//        compiler.addInstruction(new STORE(Register.R0, TODO OFFSET));
        // TODO (généraliser la classe declvarmanager)
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
        throw new DecacInternalError("not implemented yet");
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
