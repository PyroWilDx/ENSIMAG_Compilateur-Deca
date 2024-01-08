package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.LinkedList;

public class DeclMethod extends AbstractDeclMethod {
    private final AbstractIdentifier type;
    private final AbstractIdentifier name;
    private final ListParam params;
    // TODO peutetre creer une classe bloc pour factoriser avec Main et se rapprocher du sujet
    // TODO mais dans ce cas penser à changer le parser
    private final ListDeclVar listDeclVar;
    private final ListInst listInst;
    private String classNameStr;
    private Label mStartLabel;
    private Label mEndLabel;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name,
                      ListParam params, ListDeclVar listDeclVar, ListInst listInst) {
        this.type = type;
        this.name = name;
        this.params = params;
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
        this.mStartLabel = null;
        this.mEndLabel = null;
    }

    @Override
    public void codeGenVTable(DecacCompiler compiler, AbstractIdentifier className) {
        StackManager sM = compiler.getStackManager();
        VTableManager vTM = compiler.getVTableManager();

        classNameStr = className.getName().getName();

        mStartLabel = new Label("code." + classNameStr +
                "." + name.getName().getName());
        mEndLabel = new Label("end." + classNameStr +
                "." + name.getName().getName());
        compiler.addInstruction(new LOAD(new LabelOperand(mStartLabel), Register.R0));

        DAddr mAddr = sM.getGbOffsetAddr();
        compiler.addInstruction(new STORE(Register.R0, mAddr));
        sM.incrVTableCpt();

        vTM.addMethodToClass(classNameStr, name.getName().getName(), mAddr);

        for (AbstractParam param : params.getList()) {
            // TODO (param.setOperand ??)
        }
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
        listInst.codeGenListInst(compiler); // TODO (A voir s'il faut créer un nouveau codegen)

        LinkedList<AbstractLine> startLines = new LinkedList<>();
        LinkedList<AbstractLine> endLines = new LinkedList<>();
        startLines.addLast(new Line(new TSTO(sM.getMaxStackSize())));
        startLines.addLast(new Line(new BOV(ErrorUtils.stackOverflowLabel)));
        for (GPRegister usedReg : rM.getUsedRegs()) {
            startLines.addLast(new Line(new PUSH(usedReg)));
            endLines.addFirst(new Line(new POP(usedReg)));
        }
        compiler.addAllLine(iTSTO, startLines);

        String className = "";
        compiler.addInstruction(new WSTR("Error: Exiting function " + className + name.getName().getName()));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        compiler.addLabel(mEndLabel);
        compiler.addAllLine(endLines);
        compiler.addInstruction(new RTS());

        rM.doNotSaveUsedRegs();
        // Done
    }

//    // TODO (à déplacer)
//    public void codeGenCallMethod(DecacCompiler compiler) {
//        RegManager rM = compiler.getRegManager();
//        VTableManager vTM = compiler.getVTableManager();
//
//        int nbParam = params.size() + 1;
//        compiler.addInstruction(new ADDSP(nbParam));
//
//        GPRegister gpReg = rM.getFreeReg();
//        compiler.addInstruction(
//                new LOAD(identifier.getExpDefinition().getOperand(), gpReg));
//
//        compiler.addInstruction(new CMP(new NullOperand(), gpReg));
//        compiler.addInstruction(new BEQ(ErrorUtils.nullPointerLabel));
//
//        compiler.addInstruction(
//                new STORE(gpReg, new RegisterOffset(0, Register.SP)));
//        rM.freeReg(gpReg);
//
//        int currParamIndex = -1;
//        for (AbstractInst inst : insts.getList()) {
//            inst.codeGenInst(compiler);
//            gpReg = rM.getLastReg();
//            compiler.addInstruction(
//                    new STORE(gpReg, new RegisterOffset(currParamIndex, Register.SP)));
//            rM.freeReg(gpReg);
//            currParamIndex--;
//        }
//
//        gpReg = rM.getFreeReg();
////        compiler.addInstruction(
////                new LOAD(new RegisterOffset(0, Register.SP), gpReg));
////        compiler.addInstruction(new CMP(new NullOperand(), gpReg));
////        compiler.addInstruction(new BEQ(ErrorUtils.nullPointerLabel));
//
////        compiler.addInstruction(
////                new LOAD(new RegisterOffset(0, gpReg), gpReg));
////        compiler.addInstruction(new BSR(new RegisterOffset(mOffset, gpReg)));
//        compiler.addInstruction(vTM.getAddrOfMethod(className, methodName));
//        rM.freeReg(gpReg);
//
//        compiler.addInstruction(new SUBSP(nbParam));
//    }

//    // TODO (à déplacer)
//    public void codeGenNew(DecacCompiler compiler) {
//        RegManager rM = compiler.getRegManager();
//        VTableManager vTM = compiler.getVTableManager();
//
//        GPRegister gpReg = rM.getFreeReg();
//        compiler.addInstruction(new NEW(nbAttributs + 1, gpReg));
//        compiler.addInstruction(new BOV(ErrorUtils.heapOverflowLabel));
//        compiler.addInstruction(new LEA(vTM.getAddrOfClass(className, Register.R0)));
//        compiler.addInstruction(
//                new STORE(Register.R0, new RegisterOffset(0, gpReg)));
////        compiler.addInstruction(new PUSH(gpReg));
//        compiler.addInstruction(new BSR(vTM.getLabelINITdeLACLASSE()));
////        compiler.addInstruction(new POP(gpReg));
//        rM.freeReg(gpReg);
//    }

//    // TODO (à déplacer)
//    public void codeGenInstanceOf(DecacCompiler compiler) {
//        RegManager rM = compiler.getRegManager();
//        CondManager cM = compiler.getCondManager();
//        VTableManager vTM = compiler.getVTableManager();
//
//        compiler.addInstruction(
//                new LOAD(vTM.getAddrOfClass(targetClassName), Register.R0));
//        GPRegister gpReg = rM.getFreeReg();
//        compiler.addInstruction(
//                new LOAD(identifier.getExpDefinition().getOperand(), gpReg));
//        int idCpt = cM.getAndIncrIdCpt();
//        Label startLabel = new Label("startInstanceOf" + idCpt);
//        Label endTrueLabel = new Label("endTrueInstanceOf" + idCpt);
//        Label endFalseLabel = new Label("endFalseInstanceOf" + idCpt);
//        Label endLabel = new Label("endInstanceOf" + idCpt);
//
//        compiler.addLabel(startLabel);
//
//        compiler.addInstruction(
//                new LOAD(new RegisterOffset(0, gpReg), gpReg));
//
//        compiler.addInstruction(new CMP(new NullOperand(), gpReg));
//        compiler.addInstruction(new BEQ(endFalseLabel));
//
//        compiler.addInstruction(new CMP(Register.R0, gpReg));
//        compiler.addInstruction(new BEQ(endTrueLabel));
//
//        compiler.addInstruction(new BRA(startLabel));
//
//        compiler.addLabel(endTrueLabel);
//        compiler.addInstruction(new LOAD(1, gpReg));
//        compiler.addInstruction(new BRA(endLabel));
//
//        compiler.addLabel(endFalseLabel);
//        compiler.addInstruction(new LOAD(0, gpReg));
//
//        compiler.addLabel(endLabel);
//        rM.freeReg(gpReg);
//    }

    @Override
    public SymbolTable.Symbol getName() {
        return null;
    }

    @Override
    public EnvironmentExp verifyDeclMethodMembers(DecacCompiler compiler, SymbolTable.Symbol superClass, int index) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        Signature sig = this.params.verifyListDeclParamMembers(compiler);
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
                if (!sig.equals(sig2)) {
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
    public void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type returnType = this.type.verifyType(compiler);
        EnvironmentExp envParams = this.params.verifyListDeclParamBody(compiler);
        EnvironmentExp envReturn = this.listDeclVar.verifyListDeclVariable(compiler, localEnv, envParams, currentClass);
        EnvironmentExp envEmpile = EnvironmentExp.empile(envReturn, localEnv);
        this.listInst.verifyListInst(compiler, envEmpile, currentClass, returnType);
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
        type.prettyPrint(s,prefix,false);
        name.prettyPrint(s,prefix,false);
        params.prettyPrint(s,prefix,false);
        listDeclVar.prettyPrint(s,prefix,false);
        listInst.prettyPrint(s,prefix,true);

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
        throw new DecacInternalError("not implemented yet");
    }
}
