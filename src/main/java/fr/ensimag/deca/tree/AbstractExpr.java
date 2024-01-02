package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * <p>
     * implements non-terminals "expr" and "lvalue"
     * of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     (contains the "env_types" attribute)
     * @param localEnv     Environment in which the expression should be checked
     *                     (corresponds to the "env_exp" attribute)
     * @param currentClass Definition of the class containing the expression
     *                     (corresponds to the "class" attribute)
     *                     is null in the main bloc.
     * @return the Type of the expression
     * (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
                                    EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     * <p>
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
                                     EnvironmentExp localEnv, ClassDefinition currentClass,
                                     Type expectedType) throws ContextualError { // regle 3.28
        Type type2 = this.verifyExpr(compiler, localEnv, currentClass);
        if (!compiler.environmentType.assignCompatible(expectedType, type2)) {
            throw new ContextualError("type '" + type2.toString() +
                    "' must be compatible with type '" + expectedType.toString() + "'", this.getLocation()); // cf condition regle 3.28
        }
        //throw new UnsupportedOperationException("not yet implemented");
        return this; // TODO j'ai pas compris cette histoire de return un convfloat, il suffit que ce soit compatible non ?
    }

    public void verifyExprPrint(DecacCompiler compiler, EnvironmentExp localEnv,
                                ClassDefinition currentClass) throws ContextualError {
        Type exprType = this.verifyExpr(compiler, localEnv, currentClass);
        if (!exprType.equals(compiler.environmentType.INT) &&
                !exprType.equals(compiler.environmentType.FLOAT) &&
                !exprType.equals(compiler.environmentType.STRING)) {
            throw new ContextualError("Invalid argument type for print : '"
                    + exprType.getName() +
                    "'\nArgument type must be 'int', 'float' or 'string'", this.getLocation());
        }
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType) throws ContextualError {
        verifyExpr(compiler, localEnv, currentClass);
        // Done
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv     Environment in which the condition should be checked.
     * @param currentClass Definition of the class containing the expression, or null in
     *                     the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
                         ClassDefinition currentClass) throws ContextualError {
        Type exprType = verifyExpr(compiler, localEnv, currentClass);
        if (!exprType.isBoolean()) {
            throw new ContextualError("Condition is of type '"
                    + exprType.getName() + "', it must be boolean", getLocation());
        }
        setType(exprType);
        // Done
    }

    private boolean printHex = false;

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        codeGenInst(compiler);
        DVal lastImmediate = compiler.getRegManager().getLastImmediate();
        if (lastImmediate == null) {
            GPRegister reg = compiler.getRegManager().getLastReg();
            compiler.addInstruction(new LOAD(reg, Register.R1));
            compiler.getRegManager().freeReg(reg);
        } else {
            compiler.addInstruction(new LOAD(lastImmediate, Register.R1));
        }

        if (getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else if (getType().isFloat()) {
            if (!getPrintHex()) compiler.addInstruction(new WFLOAT());
            else compiler.addInstruction(new WFLOATX());
        }
        // Done
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // See Children
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    public void setPrintHex(boolean value) {
        printHex = value;
    }

    public boolean getPrintHex() {
        return printHex;
    }
}
