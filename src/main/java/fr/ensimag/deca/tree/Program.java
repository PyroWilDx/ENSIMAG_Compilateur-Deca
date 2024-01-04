package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl47
 * @date 01/01/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    public ListDeclClass getClasses() {
        return classes;
    }

    public AbstractMain getMain() {
        return main;
    }

    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
//        classes.verifyListClass(compiler);
//        classes.verifyListClassMembers(compiler);
//        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
        // TODO (Avec Objet -> Enlever Comm)
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        StackManager sM = compiler.getStackManager();
        VTableManager vTM = compiler.getVTableManager();

        compiler.addComment("VTable of Object");
        DAddr nAddr = sM.getGbOffsetAddr();
        vTM.addClass("Object", nAddr);
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, nAddr));
        sM.incrVTableCpt();

        DAddr eAddr = sM.getGbOffsetAddr();
        vTM.addMethodToClass("Object", "equals", eAddr);
        Label eLabel = new Label("code.Object.equals");
        compiler.addInstruction(new LOAD(new LabelOperand(eLabel), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, eAddr));
        sM.incrVTableCpt();

        classes.codeGenVTable(compiler);

        compiler.addComment("Start of Main Program");
        compiler.addComment("Main Program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        compiler.addComment("End of Main Program");

        compiler.addInstruction(0, new TSTO(sM.getMaxStackSize()));
        compiler.addInstruction(1, new BOV(ErrorUtils.stackOverflowLabel));
        compiler.addInstruction(2, new ADDSP(sM.getAddSp()));

        compiler.addComment("");
        compiler.addComment("Class Object");
        compiler.addLabel(eLabel);
        // TODO (méthode equals de object)

        classes.codeGenListDeclClass(compiler);

        compiler.addComment("");

        compiler.addComment("Start of Error Labels");
        ErrorUtils.codeGenError(compiler,
                "Error: Stack Overflow",
                ErrorUtils.stackOverflowLabel);
        ErrorUtils.codeGenError(compiler,
                "Error: Division by 0",
                ErrorUtils.divBy0Label);
        ErrorUtils.codeGenError(compiler,
                "Error: Input/Output Error",
                ErrorUtils.ioErrLabel);
        compiler.addComment("End of Error Labels");
        // TODO (Avec Objet)
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
