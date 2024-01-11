package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {

    private final Visibility visibility;
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
    public void codeGenVTable(DecacCompiler compiler, VTable vTable, int offset) {
        vTable.addField(getName().getName(), offset);
    }

    @Override
    public void codeGenSetFieldTo0(DecacCompiler compiler, int varOffset,
                                   boolean doLoad) {
        if (doLoad) {
            if (getInitTypeCode() == TypeCode.INT_OR_BOOL) {
                compiler.addInstruction(new LOAD(0, Register.R0));
            } else if (getInitTypeCode() == TypeCode.FLOAT) {
                compiler.addInstruction(new LOAD(0.f, Register.R0));
            } else {
                compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            }
        }
        compiler.addInstruction(
                new STORE(Register.R0, new RegisterOffset(varOffset, Register.R1)));
    }

    @Override
    public TypeCode codeGenDeclField(DecacCompiler compiler, int varOffset,
                                     TypeCode lastTypeCode) {
        TypeCode returnValue = null;

        RegManager rM = compiler.getRegManager();

        init.setVarTypeCode(getInitTypeCode());
        init.codeGenInit(compiler);

        GPRegister regValue;
        DVal lastImm = rM.getLastImm();
        if (lastImm == null) {
            regValue = rM.getLastReg();
        } else {
            regValue = Register.R0;
            if (init instanceof NoInitialization) {
                returnValue = getInitTypeCode();
                if (lastTypeCode == null || lastTypeCode != getInitTypeCode()) {
                    compiler.addInstruction(new LOAD(lastImm, regValue));
                }
            } else {
                compiler.addInstruction(new LOAD(lastImm, regValue));
            }
        }

        compiler.addInstruction(
                new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(
                new STORE(regValue, new RegisterOffset(varOffset, Register.R1)));
        if (regValue != Register.R0) {
            rM.freeReg(regValue);
        }

        return returnValue;
        // Done
    }

    @Override
    public TypeCode getInitTypeCode() {
        if (type.getType().isInt() || type.getType().isBoolean()) {
            return TypeCode.INT_OR_BOOL;
        } else if (type.getType().isFloat()) {
            return TypeCode.FLOAT;
        }
        return TypeCode.OBJECT;
    }

    @Override
    public EnvironmentExp verifyDeclFieldMembers(DecacCompiler compiler,
                                                 SymbolTable.Symbol superClass,
                                                 SymbolTable.Symbol className, int index) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        if (t.equals(compiler.environmentType.VOID)) {
            throw new ContextualError("Field type cannot be void.",
                    getLocation());
        }
        TypeDefinition def = compiler.environmentType.get(superClass);
        if (def.isClass()) {
            ClassDefinition superClassDef = (ClassDefinition) def;
            EnvironmentExp envExpSuper = superClassDef.getMembers();
            ExpDefinition expDef = envExpSuper.get(this.name.getName());
            if (expDef != null && expDef.isField()) {
                throw new ContextualError("A method '" + this.name.getName() +
                        "' already exists in super class.", getLocation());
            }
        }
        EnvironmentExp env = new EnvironmentExp(null);
        ClassDefinition classDefinition = (ClassDefinition) compiler.environmentType.get(className);
        ExpDefinition expDef = new FieldDefinition(t, getLocation(), visibility, classDefinition, index);
        try {
            env.declare(this.name.getName(), expDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new DecacInternalError("Symbol cannot have been declared twice.");
        }
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
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
        this.init.decompile(s);
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
