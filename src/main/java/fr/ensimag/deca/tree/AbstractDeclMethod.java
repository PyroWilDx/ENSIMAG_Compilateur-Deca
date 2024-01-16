package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclMethod extends Tree {

    public abstract boolean isOverride();

    public abstract int getMethodIndex();

    public abstract void codeGenVTable(DecacCompiler compiler, VTable vTable, int methodOffset);
    protected boolean override = false;
    protected int methodIndex;
    public abstract void codeGenDeclMethod(DecacCompiler compiler);
    public abstract SymbolTable.Symbol getName();
    public abstract AbstractIdentifier getTypeIdent();
    public abstract AbstractIdentifier getNameIdent();
    public abstract ListDeclParam getParams();

    public EnvironmentExp verifyDeclMethodMembers(DecacCompiler compiler,
                                                           SymbolTable.Symbol superClass,
                                                           int index) throws ContextualError {
        int realIndex = index;
        Type t = this.getTypeIdent().verifyType(compiler);
        Signature sig = this.getParams().verifyListDeclParamMembers(compiler);
        TypeDefinition def = compiler.environmentType.get(superClass);
        if (def.isClass()) {
            ClassDefinition superClassDef = (ClassDefinition) def;
            EnvironmentExp envExpSuper = superClassDef.getMembers();
            ExpDefinition expDef = envExpSuper.get(this.getNameIdent().getName());
            if (expDef != null) {
                this.override = true;
                if (!expDef.isMethod()) {
                    throw new ContextualError("A field '" +
                            this.getName() + "' already " +
                            "exists in super class.", getLocation());
                }
                MethodDefinition methodDefinition = (MethodDefinition) expDef;
                Signature sig2 = methodDefinition.getSignature();
                if (!sig.equals(sig2)) {
                    throw new ContextualError("Method '" + this.getName() +
                            "' defined in super class with " +
                            "another signature.", getLocation());
                }
                Type type2 = expDef.getType();
                if (!compiler.environmentType.subtype(t, type2)) {
                    throw new ContextualError("Return type of override must be" +
                            " subtype of the return type of the method declared in super class.", getLocation());
                }
                realIndex = methodDefinition.getIndex();
            }
        }
        EnvironmentExp env = new EnvironmentExp(null);
        ExpDefinition newDef = new MethodDefinition(t, getLocation(), sig, realIndex);
        this.methodIndex = realIndex;
        try {
            env.declare(this.getName(), newDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new DecacInternalError("Symbol cannot have been declared twice.");
        }
        return env;
        // Done
    }
    // Done

    public abstract void verifyDeclMethodBody(DecacCompiler compiler,
                                              EnvironmentExp localEnv,
                                              ClassDefinition currentClass) throws ContextualError;
}
