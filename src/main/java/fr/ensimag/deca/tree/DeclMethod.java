package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.*;
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
    public EnvironmentExp verifyDeclMethod(DecacCompiler compiler, SymbolTable.Symbol superClass, int index) throws ContextualError {
        // TODO
        Type t = this.type.verifyType(compiler);
        Signature sig = this.params.verifyListDeclParam(compiler);
        TypeDefinition def = compiler.environmentType.get(superClass);
        if (def.isClass()) {
            ClassDefinition superClassDef = (ClassDefinition) def;
            EnvironmentExp envExpSuper = superClassDef.getMembers();
            ExpDefinition expDef = envExpSuper.get(this.name.getName());
            if (expDef != null) {
                if (!expDef.isMethod()) {
                    throw new ContextualError("A field '" +
                            this.name.getName() + "' already " +
                            "exists in super class", getLocation());
                }
                MethodDefinition methodDefinition = (MethodDefinition) expDef;
                Signature sig2 = methodDefinition.getSignature();
                if (! sig.equals(sig2)) {
                    throw new ContextualError("Method defined in super class with" +
                            "another signature.", getLocation());
                }
                Type type2 = expDef.getType();
                if (!compiler.environmentType.subtype(t, type2)) {
                    throw new ContextualError("Return type of override must be" +
                            " subtype of the return type of the method declared in super class", getLocation());
                }
            }
        }
        EnvironmentExp env = new EnvironmentExp(null);
        ExpDefinition newDef = new MethodDefinition(t, getLocation(), sig, index);
        env.declare(this.name.getName(), newDef);
        return env;
        // Done
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
