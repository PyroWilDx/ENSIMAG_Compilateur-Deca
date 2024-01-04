package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {
    private final Visibility visibility; // TODO jsppppp
    private final AbstractIdentifier type;
    private final AbstractIdentifier name;
    private final AbstractInitialization init;

    public DeclField(Visibility visibility, AbstractIdentifier type,
                     AbstractIdentifier name) {
        this.type = type;
        this.visibility = visibility;
        this.name = name;
        this.init = new NoInitialization();
    }

    public DeclField(Visibility visibility, AbstractIdentifier type,
                     AbstractIdentifier name, AbstractInitialization init) {
        this.visibility = visibility;
        this.type = type;
        this.name = name;
        this.init = init;
    }

    @Override
    public void codeGenDeclField(DecacCompiler compiler, int varOffset) {
        RegManager rM = compiler.getRegManager();

        init.setVarType(type.getType());
        init.codeGenInit(compiler);

        GPRegister regValue = rM.getLastRegOrImm(compiler);
        GPRegister regObjAddr = rM.getFreeReg();
        compiler.addInstruction(
                new LOAD(new RegisterOffset(-2, Register.LB), regObjAddr));
        compiler.addInstruction(
                new STORE(regValue, new RegisterOffset(varOffset, regObjAddr)));
        rM.freeReg(regObjAddr);
        rM.freeReg(regValue);
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        System.out.println(prefix + visibility);
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        init.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
    }
}
