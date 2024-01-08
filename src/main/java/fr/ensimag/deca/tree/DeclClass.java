package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.LinkedList;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl47
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {

    private final AbstractIdentifier name;
    private final AbstractIdentifier superClass;
    private final ListDeclField fields;
    private final ListDeclMethod methods;

    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass, ListDeclField fields, ListDeclMethod methods) {
        this.name = name;
        this.superClass = superClass;
        this.fields = fields;
        this.methods = methods;
    }
    public ListDeclField getFields(){
        return fields;
    }
    public AbstractIdentifier getName() {
        return name;
    }

    public AbstractIdentifier getSuperClass() {
        return superClass;
    }

    public ListDeclMethod getMethods() {
        return methods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        TypeDefinition defSuperClass = compiler.environmentType.get(this.superClass.getName());
        if (defSuperClass == null) {
            throw new ContextualError("Undeclared identifier",
                    getLocation());
        }
        if (!defSuperClass.isClass()) {
            throw new ContextualError("A class identifier is required",
                    getLocation());
        }
        if (!compiler.environmentType.declareClasse(this.name,
                this.superClass.getClassDefinition(), this.getLocation())) {
            throw new ContextualError("Class or type already exists.", this.getLocation());
        }
        // Done
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
        EnvironmentExp envExpF = this.fields.verifyListDeclFieldMembers(compiler, this.superClass.getName(), name.getName());
        EnvironmentExp envExpM = this.methods.verifyListDeclMethodMembers(compiler, this.superClass.getName());
        SymbolTable.Symbol clone = envExpM.disjointUnion(envExpF);
        if (clone != null) {
            throw new ContextualError("'" + clone.getName() +
                    "' is a field and a method at once.", getLocation());
        }
        EnvironmentExp voidClassEnv = ((ClassDefinition)compiler.environmentType.get(name.getName())).getMembers();
        voidClassEnv.disjointUnion(envExpM); // c'est vide donc pas de pb pour l'union disjointe !!
        // Done
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        ClassDefinition classDef = this.name.getClassDefinition();
        EnvironmentExp env = classDef.getMembers();
        this.fields.verifyListDeclFieldBody(compiler, env, classDef);
        this.methods.verifyListDeclMethodBody(compiler, env, classDef);
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void codeGenVTable(DecacCompiler compiler) {
        StackManager sM = compiler.getStackManager();
        VTableManager vTM = compiler.getVTableManager();

        compiler.addComment("VTable of " + name.getName().getName());

        DAddr startAddr = sM.getGbOffsetAddr();
        vTM.addClass(name.getName().getName(), startAddr);
        compiler.addInstruction(
                new LEA(vTM.getAddrOfClass(superClass.getName().getName()), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, startAddr));
        sM.incrVTableCpt();

        methods.codeGenVTable(compiler, name);
        // Done
    }

    @Override
    public void codeGenDeclClass(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        StackManager sM = compiler.getStackManager();

        compiler.addComment("");
        compiler.addComment("Class " + name.getName().getName());

        compiler.addLabel(new Label("init." + name.getName().getName()));
        int iTSTO = compiler.getProgramLineCount();

        if (superClass.getName().getName().equals("Object")) {
            fields.codeGenSetFieldsTo0(compiler);
        }

        rM.addScratchRegR0();
        rM.saveUsedRegs();
        fields.codeGenListDeclField(compiler);
        rM.removeScratchRegR0();

        LinkedList<AbstractLine> startLines = new LinkedList<>();
        LinkedList<AbstractLine> endLines = new LinkedList<>();
        startLines.addLast(new Line(new TSTO(sM.getMaxStackSize())));
        startLines.addLast(new Line(new BOV(ErrorUtils.stackOverflowLabel)));
        for (GPRegister usedReg : rM.getUsedRegs()) {
            startLines.addLast(new Line(new PUSH(usedReg)));
            endLines.addFirst(new Line(new POP(usedReg)));
        }
        compiler.addAllLine(iTSTO, startLines);

        compiler.addAllLine(endLines);

        rM.doNotSaveUsedRegs();

        compiler.addInstruction(new RTS());

        methods.codeGenListDeclMethod(compiler);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s,prefix,false);
        fields.prettyPrintChildren(s, prefix);
        methods.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
