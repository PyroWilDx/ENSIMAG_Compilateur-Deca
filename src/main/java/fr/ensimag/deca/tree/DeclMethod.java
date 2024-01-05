package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.LinkedList;

public class DeclMethod extends AbstractDeclMethod {
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListParam params;
    // TODO peutetre creer une classe bloc pour factoriser avec Main et se rapprocher du sujet
    // TODO mais dans ce cas penser à changer le parser
    private ListDeclVar declVariables;
    private ListInst insts;
    private Label mStartLabel;
    private Label mEndLabel;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name,
                      ListParam params, ListDeclVar declVariables, ListInst insts) {
        this.type = type;
        this.name = name;
        this.params = params;
        this.declVariables = declVariables;
        this.insts = insts;
        this.mStartLabel = null;
    }

    @Override
    public void codeGenVTable(DecacCompiler compiler, AbstractIdentifier className) {
        StackManager sM = compiler.getStackManager();
        VTableManager vTM = compiler.getVTableManager();

        mStartLabel = new Label("code." + className.getName().getName() +
                "." + name.getName().getName());
        mEndLabel = new Label("end." + className.getName().getName() +
                "." + name.getName().getName());
        compiler.addInstruction(new LOAD(new LabelOperand(mStartLabel), Register.R0));

        DAddr mAddr = sM.getGbOffsetAddr();
        compiler.addInstruction(new STORE(Register.R0, mAddr));
        sM.incrVTableCpt();

        vTM.addMethodToClass(className.getName().getName(), name.getName().getName(), mAddr);
        // TODO (Merde comment on sait si c'est une méthode surchargée ou pas??)
        // Done
    }

    @Override
    public void codeGenDeclMethod(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        StackManager sM = new StackManager();
        compiler.setStackManager(sM);

        compiler.addLabel(mStartLabel);
        int iTSTO = compiler.getProgramLineCount();

        rM.saveUsedRegs();
        insts.codeGenListInst(compiler); // TODO (A voir s'il faut créer un nouveau codegen)

        LinkedList<AbstractLine> startLines = new LinkedList<>();
        LinkedList<AbstractLine> endLines = new LinkedList<>();
        startLines.addLast(new Line(new TSTO(sM.getMaxStackSize())));
        startLines.addLast(new Line(new BOV(ErrorUtils.stackOverflowLabel)));
        for (GPRegister usedReg : rM.usedRegsIterable()) {
            startLines.addLast(new Line(new PUSH(usedReg)));
            endLines.addFirst(new Line(new POP(usedReg)));
        }
        compiler.addAllLine(iTSTO, startLines);

        compiler.addLabel(mEndLabel);
        compiler.addAllLine(endLines);
        compiler.addInstruction(new RTS());

        rM.doNotSaveRegs();
        // Done
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
