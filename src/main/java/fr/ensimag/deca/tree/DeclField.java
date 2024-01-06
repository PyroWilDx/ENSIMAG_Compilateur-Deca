package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
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
    public EnvironmentExp verifyDeclFieldMembers(DecacCompiler compiler,
                                                 SymbolTable.Symbol superClass,
                                                 SymbolTable.Symbol className, int index) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        if (t.equals(compiler.environmentType.VOID)) {
            throw new ContextualError("Field type cannot be void",
                    getLocation());
        }
        TypeDefinition def = compiler.environmentType.get(superClass);
        if (def.isClass()) {
            ClassDefinition superClassDef = (ClassDefinition) def;
            EnvironmentExp envExpSuper = superClassDef.getMembers();
            ExpDefinition expDef = envExpSuper.get(this.name.getName());
            if (expDef != null && expDef.isField()) {
                throw new ContextualError("A method '" + this.name.getName() +
                        "' already exists in super class", getLocation());
            }
        }
        EnvironmentExp env = new EnvironmentExp(null);
        ClassDefinition classDefinition = (ClassDefinition) compiler.environmentType.get(className);
        ExpDefinition expDef = new FieldDefinition(t, getLocation(), visibility, classDefinition, index);
        env.declare(this.name.getName(), expDef);
        return env;
        // Done
    }

    @Override
    public void verifyDeclFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition classDef) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        this.init.verifyInitialization(compiler, t, localEnv, classDef);
        // Done
    }

    @Override
    public SymbolTable.Symbol getName() {
        return name.getName();
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
