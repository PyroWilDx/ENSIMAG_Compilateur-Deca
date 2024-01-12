package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Deca Identifier
 *
 * @author gl47
 * @date 01/01/2024
 */
public class Identifier extends AbstractIdentifier {

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.setType(definition.getType());
        this.definition = definition;
    }

    @Override
    public MethodIdentNonTerminalReturn verifyMethodIdent(EnvironmentExp localEnv) throws ContextualError {
        ExpDefinition def = this.verifyIdentifier(localEnv);
        MethodDefinition methodDef = def.asMethodDefinition(this.getName() +
                "is not a method identifier.", getLocation());
        Signature sig = methodDef.getSignature();
        Type type = methodDef.getType();
        return new MethodIdentNonTerminalReturn(sig, type);
        // Done
    }

    @Override
    public FieldIdentNonTerminalReturn verifyFieldIdent(EnvironmentExp localEnv) throws ContextualError {
        ExpDefinition def = this.verifyIdentifier(localEnv);
        FieldDefinition fieldDefinition = def.asFieldDefinition("'" +
                this.name + "' must be a field.", getLocation());
        Visibility visib = fieldDefinition.getVisibility();
        ClassDefinition classDefinition  = fieldDefinition.getContainingClass();
        Type type = fieldDefinition.getType();
        return new FieldIdentNonTerminalReturn(visib, classDefinition, type);
        // Done
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Definition def = localEnv.get(this.name);
        if (def == null) {
            throw new ContextualError("Undeclared identifier : '"
                    + this.name.toString() + "'.", this.getLocation());
        }
        setDefinition(def);
        return def.getType();
        // Done
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     *
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        TypeDefinition typeDef = compiler.environmentType.defOfType(this.name);
        if (typeDef == null) {
            throw new ContextualError("Undeclared type identifier : '"
                    + this.name.toString() + "'.", this.getLocation());
        }
        setDefinition(typeDef);
        return typeDef.getType();
        // Done
    }

    @Override
    public ExpDefinition verifyIdentifier(EnvironmentExp localEnv) throws ContextualError {
        ExpDefinition def = localEnv.get(this.getName());
        if (def == null) {
            throw new ContextualError("Undeclared identifier : '" + this.getName()
                    + "'.", getLocation());
        }
        this.setDefinition(def);
        return def;
        // Done
    }

    @Override
    public Type verifyLValueIdent(EnvironmentExp localEnv) throws ContextualError {
        ExpDefinition def = this.verifyIdentifier(localEnv);
        if (!(def.isField() || def.isParam() || def.isVariable())) {
            throw new ContextualError("'" + this.name +
                    "' must be a field, a parameter or a variable, but it is a " + def.getNature() + ".", getLocation());
        }
        return def.getType();
    }

    private Definition definition;

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        CondManager cM = compiler.getCondManager();
        VTableManager vTM = compiler.getVTableManager();

        if (getType().isClass()) {
            vTM.setCurrClassName(getType().getName().getName());
        }

        GPRegister gpReg = rM.getFreeReg();
        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), gpReg));

        if (cM.isDoingCond() && cM.isNotDoingOpCmp()) {
            compiler.addInstruction(new CMP(0, gpReg));
            if (isNotInFalse) {
                if (branchLabel != null) compiler.addInstruction(new BNE(branchLabel));
            } else {
                if (branchLabel != null) compiler.addInstruction(new BEQ(branchLabel));
            }
        } else {
            if (!isNotInFalse) {
                compiler.addInstruction(new CMP(0, gpReg));
                compiler.addInstruction(new SEQ(gpReg));
            }
        }

        rM.freeReg(gpReg);
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

    @Override
    public Type verifyLValue(DecacCompiler compiler,
                             EnvironmentExp localEnv,
                             ClassDefinition currentClass) throws ContextualError {
        return this.verifyLValueIdent(localEnv);
    }
}
