package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
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

    private final ListDeclClass classes;
    private final AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        classes.verifyListClass(compiler);
        classes.verifyListClassMembers(compiler);
        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
        // Done
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        ErrorManager eM = compiler.getErrorManager();
        StackManager sM = new StackManager(false);
        compiler.setStackManager(sM);
        VTableManager vTM = compiler.getVTableManager();

        boolean generateObjectClass = !classes.getList().isEmpty();

        Label eLabel = null;
        if (generateObjectClass) {
            compiler.addComment("VTable of " + LabelUtils.OBJECT_CLASS_NAME);
            DAddr nAddr = sM.getOffsetAddr();
            LabelUtils.setObjectClassSymbol(compiler.environmentType.OBJECT.getName());
            VTable vT = new VTable(null, LabelUtils.OBJECT_CLASS_SYMBOL, nAddr);
            vTM.addVTable(LabelUtils.OBJECT_CLASS_NAME, vT);
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, nAddr));
            sM.incrVTableCpt();

            DAddr eAddr = sM.getOffsetAddr();
            vT.addMethod(LabelUtils.EQUALS_METHOD_NAME, 1);
            eLabel = LabelUtils.getMethodLabel(
                    LabelUtils.OBJECT_CLASS_NAME, LabelUtils.EQUALS_METHOD_NAME);
            compiler.addInstruction(new LOAD(new LabelOperand(eLabel), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, eAddr));
            sM.incrVTableCpt();
        }

        classes.codeGenVTable(compiler);

        compiler.addComment("Start of Main Program");
        compiler.addComment("Main Program");

        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());

        compiler.addComment("End of Main Program");

        if (sM.getMaxStackSize() > 0) {
            if (sM.getAddSp() > 0) {
                compiler.addInstruction(0, new ADDSP(sM.getAddSp()));
            }
            if (compiler.getCompilerOptions().doCheck()) {
                compiler.addInstruction(0, new BOV(eM.getStackOverflowLabel()));
                compiler.addInstruction(0, new TSTO(sM.getMaxStackSize()));
            }
        }

        if (generateObjectClass) {
            compiler.addComment("");
            compiler.addComment("Class " + LabelUtils.OBJECT_CLASS_NAME);
            compiler.addLabel(eLabel);
            compiler.addInstruction(
                    new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
            compiler.addInstruction(
                    new CMP(new RegisterOffset(-3, Register.LB), Register.R0));
            compiler.addInstruction(new SEQ(Register.R0));
            compiler.addInstruction(new RTS());
        }

        classes.codeGenListDeclClass(compiler);

        compiler.addComment("");

        compiler.addComment("Start of Error Labels");
        eM.codeGenAllErrors(compiler);
        compiler.addComment("End of Error Labels");
        // Done
    }

    @Override
    public void codeGenProgramGb(DecacCompiler compiler) {
        CondManager cM = compiler.getCondManager();
        StackManager sM = new StackManager(false);
        compiler.setStackManager(sM);
        VTableManager vTM = compiler.getVTableManager();

        compiler.add(new LineGb("INCLUDE \"hardware.inc\""));

        compiler.add(new LineGb(""));
        compiler.add(new LineGb("SECTION \"Header\", ROM0[$100]"));
        compiler.add(new LineGb(""));

        compiler.add(new LineGb("jp EntryPoint"));
        compiler.add(new LineGb("ds $150 - @, 0"));

        compiler.add(new LineGb(""));
        compiler.addLabel(new Label("EntryPoint"));
        compiler.addComment("Shut down audio circuitry");
        compiler.add(new LineGb("ld a, 0"));
        compiler.add(new LineGb("ld [rNR52], a"));

        compiler.add(new LineGb("ld SP, " + GameBoyManager.Addr0));

        boolean generateObjectClass = !classes.getList().isEmpty();

        if (generateObjectClass) {
            compiler.addComment("VTable of " + LabelUtils.OBJECT_CLASS_NAME);
            LabelUtils.setObjectClassSymbol(compiler.environmentType.OBJECT.getName());
            VTable vT = new VTable(null, LabelUtils.OBJECT_CLASS_SYMBOL, null);
            vTM.addVTable(LabelUtils.OBJECT_CLASS_NAME, vT);
            vT.addMethod(LabelUtils.EQUALS_METHOD_NAME, 1);
        }

        classes.codeGenVTableGb(compiler);

        compiler.addComment("Start of Main Program");
        compiler.addComment("Main Program");

        main.codeGenMainGb(compiler);
        Label doneLabel = new Label("Done");
        compiler.addLabel(doneLabel);
//        compiler.addInstruction(new HALT());
        compiler.addInstruction(new BRA(doneLabel));

        compiler.addComment("End of Main Program");

        if (generateObjectClass) {
//            compiler.addComment("");
//            compiler.addComment("Class " + LabelUtils.OBJECT_CLASS_NAME);
//            compiler.addLabel(LabelUtils.getMethodLabel(
//                    LabelUtils.OBJECT_CLASS_NAME, LabelUtils.EQUALS_METHOD_NAME));
//            Label falseLabel = cM.getUniqueLabel();
            // TODO (GB)
//            compiler.addInstruction(
//                    new LOAD_INT(GameBoyManager.getArgAddr(-2) + 8, Register.HL));
//            compiler.addInstruction(new LOAD_VAL(Register.HL, Register.A));
//            compiler.addInstruction(
//                    new LOAD_INT(GameBoyManager.getArgAddr(-3) + 8, Register.HL));
//            compiler.addInstruction(new LOAD_VAL(Register.HL, Register.HL.getLowReg()));

//            compiler.addInstruction(new CMP(Register.A, Register.HL.getLowReg()));
//            compiler.addInstruction(new BNE(falseLabel));

//            compiler.addInstruction(
//                    new LOAD_INT(GameBoyManager.getArgAddr(-2), Register.HL));
//            compiler.addInstruction(new LOAD_VAL(Register.HL, Register.A));
//            compiler.addInstruction(
//                    new LOAD_INT(GameBoyManager.getArgAddr(-3), Register.HL));
//            compiler.addInstruction(new LOAD_VAL(Register.HL, Register.HL.getLowReg()));
//            compiler.addInstruction(new BNE(falseLabel));
//
//            compiler.addInstruction(new LOAD_INT(1, Register.HL.getLowReg()));
//
//            compiler.addLabel(falseLabel);
//            compiler.addInstruction(new LOAD_INT(0, Register.HL.getLowReg()));
//            compiler.addInstruction(new RTS());
        }

        classes.codeGenListDeclClassGb(compiler);

        compiler.add(new LineGb(""));
        compiler.add(new LineGb("SECTION \"Tile data\", ROM0"));
        compiler.add(new LineGb(""));

        compiler.addLabel(new Label("Tiles"));
        compiler.add(new LineGb("db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff\n" +
                "\tdb $00,$ff, $00,$80, $00,$80, $00,$80, $00,$80, $00,$80, $00,$80, $00,$80\n" +
                "\tdb $00,$ff, $00,$7e, $00,$7e, $00,$7e, $00,$7e, $00,$7e, $00,$7e, $00,$7e\n" +
                "\tdb $00,$ff, $00,$01, $00,$01, $00,$01, $00,$01, $00,$01, $00,$01, $00,$01\n" +
                "\tdb $00,$ff, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00\n" +
                "\tdb $00,$ff, $00,$7f, $00,$7f, $00,$7f, $00,$7f, $00,$7f, $00,$7f, $00,$7f\n" +
                "\tdb $00,$ff, $03,$fc, $00,$f8, $00,$f0, $00,$e0, $20,$c0, $00,$c0, $40,$80\n" +
                "\tdb $00,$ff, $c0,$3f, $00,$1f, $00,$0f, $00,$07, $04,$03, $00,$03, $02,$01\n" +
                "\tdb $00,$80, $00,$80, $7f,$80, $00,$80, $00,$80, $7f,$80, $7f,$80, $00,$80\n" +
                "\tdb $00,$7e, $2a,$7e, $d5,$7e, $2a,$7e, $54,$7e, $ff,$00, $ff,$00, $00,$00\n" +
                "\tdb $00,$01, $00,$01, $ff,$01, $00,$01, $01,$01, $fe,$01, $ff,$01, $00,$01\n" +
                "\tdb $00,$80, $80,$80, $7f,$80, $80,$80, $00,$80, $ff,$80, $7f,$80, $80,$80\n" +
                "\tdb $00,$7f, $2a,$7f, $d5,$7f, $2a,$7f, $55,$7f, $ff,$00, $ff,$00, $00,$00\n" +
                "\tdb $00,$ff, $aa,$ff, $55,$ff, $aa,$ff, $55,$ff, $fa,$07, $fd,$07, $02,$07\n" +
                "\tdb $00,$7f, $2a,$7f, $d5,$7f, $2a,$7f, $55,$7f, $aa,$7f, $d5,$7f, $2a,$7f\n" +
                "\tdb $00,$ff, $80,$ff, $00,$ff, $80,$ff, $00,$ff, $80,$ff, $00,$ff, $80,$ff\n" +
                "\tdb $40,$80, $00,$80, $7f,$80, $00,$80, $00,$80, $7f,$80, $7f,$80, $00,$80\n" +
                "\tdb $00,$3c, $02,$7e, $85,$7e, $0a,$7e, $14,$7e, $ab,$7e, $95,$7e, $2a,$7e\n" +
                "\tdb $02,$01, $00,$01, $ff,$01, $00,$01, $01,$01, $fe,$01, $ff,$01, $00,$01\n" +
                "\tdb $00,$ff, $80,$ff, $50,$ff, $a8,$ff, $50,$ff, $a8,$ff, $54,$ff, $a8,$ff\n" +
                "\tdb $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80\n" +
                "\tdb $ff,$00, $ff,$00, $ff,$00, $ab,$7e, $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7e\n" +
                "\tdb $ff,$01, $fe,$01, $ff,$01, $fe,$01, $ff,$01, $fe,$01, $ff,$01, $fe,$01\n" +
                "\tdb $7f,$80, $ff,$80, $7f,$80, $ff,$80, $7f,$80, $ff,$80, $7f,$80, $ff,$80\n" +
                "\tdb $ff,$00, $ff,$00, $ff,$00, $aa,$7f, $d5,$7f, $aa,$7f, $d5,$7f, $aa,$7f\n" +
                "\tdb $f8,$07, $f8,$07, $f8,$07, $80,$ff, $00,$ff, $aa,$ff, $55,$ff, $aa,$ff\n" +
                "\tdb $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $ff,$80, $7f,$80, $ff,$80\n" +
                "\tdb $d5,$7f, $aa,$7f, $d5,$7f, $aa,$7f, $d5,$7f, $aa,$7f, $d5,$7f, $aa,$7f\n" +
                "\tdb $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7e, $d5,$7e, $eb,$3c\n" +
                "\tdb $54,$ff, $aa,$ff, $54,$ff, $aa,$ff, $54,$ff, $aa,$ff, $54,$ff, $aa,$ff\n" +
                "\tdb $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $00,$ff\n" +
                "\tdb $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7e, $d5,$7e, $2a,$ff\n" +
                "\tdb $ff,$01, $fe,$01, $ff,$01, $fe,$01, $ff,$01, $fe,$01, $ff,$01, $80,$ff\n" +
                "\tdb $7f,$80, $ff,$80, $7f,$80, $ff,$80, $7f,$80, $ff,$80, $7f,$80, $aa,$ff\n" +
                "\tdb $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $2a,$ff\n" +
                "\tdb $ff,$01, $fe,$01, $ff,$01, $fe,$01, $fe,$01, $fe,$01, $fe,$01, $80,$ff\n" +
                "\tdb $7f,$80, $ff,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $7f,$80, $00,$ff\n" +
                "\tdb $fe,$01, $fe,$01, $fe,$01, $fe,$01, $fe,$01, $fe,$01, $fe,$01, $80,$ff\n" +
                "\tdb $3f,$c0, $3f,$c0, $3f,$c0, $1f,$e0, $1f,$e0, $0f,$f0, $03,$fc, $00,$ff\n" +
                "\tdb $fd,$03, $fc,$03, $fd,$03, $f8,$07, $f9,$07, $f0,$0f, $c1,$3f, $82,$ff\n" +
                "\tdb $55,$ff, $2a,$7e, $54,$7e, $2a,$7e, $54,$7e, $2a,$7e, $54,$7e, $00,$7e\n" +
                "\tdb $01,$ff, $00,$01, $01,$01, $00,$01, $01,$01, $00,$01, $01,$01, $00,$01\n" +
                "\tdb $54,$ff, $ae,$f8, $50,$f0, $a0,$e0, $60,$c0, $80,$c0, $40,$80, $40,$80\n" +
                "\tdb $55,$ff, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00\n" +
                "\tdb $55,$ff, $6a,$1f, $05,$0f, $02,$07, $05,$07, $02,$03, $03,$01, $02,$01\n" +
                "\tdb $54,$ff, $80,$80, $00,$80, $80,$80, $00,$80, $80,$80, $00,$80, $00,$80\n" +
                "\tdb $55,$ff, $2a,$1f, $0d,$07, $06,$03, $01,$03, $02,$01, $01,$01, $00,$01\n" +
                "\tdb $55,$ff, $2a,$7f, $55,$7f, $2a,$7f, $55,$7f, $2a,$7f, $55,$7f, $00,$7f\n" +
                "\tdb $55,$ff, $aa,$ff, $55,$ff, $aa,$ff, $55,$ff, $aa,$ff, $55,$ff, $00,$ff\n" +
                "\tdb $15,$ff, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00\n" +
                "\tdb $55,$ff, $6a,$1f, $0d,$07, $06,$03, $01,$03, $02,$01, $03,$01, $00,$01\n" +
                "\tdb $54,$ff, $a8,$ff, $54,$ff, $a8,$ff, $50,$ff, $a0,$ff, $40,$ff, $00,$ff\n" +
                "\tdb $00,$7e, $2a,$7e, $d5,$7e, $2a,$7e, $54,$7e, $ab,$76, $dd,$66, $22,$66\n" +
                "\tdb $00,$7c, $2a,$7e, $d5,$7e, $2a,$7e, $54,$7c, $ff,$00, $ff,$00, $00,$00\n" +
                "\tdb $00,$01, $00,$01, $ff,$01, $02,$01, $07,$01, $fe,$03, $fd,$07, $0a,$0f\n" +
                "\tdb $00,$7c, $2a,$7e, $d5,$7e, $2a,$7e, $54,$7e, $ab,$7e, $d5,$7e, $2a,$7e\n" +
                "\tdb $00,$ff, $a0,$ff, $50,$ff, $a8,$ff, $54,$ff, $a8,$ff, $54,$ff, $aa,$ff\n" +
                "\tdb $dd,$62, $bf,$42, $fd,$42, $bf,$40, $ff,$00, $ff,$00, $f7,$08, $ef,$18\n" +
                "\tdb $ff,$00, $ff,$00, $ff,$00, $ab,$7c, $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7e\n" +
                "\tdb $f9,$07, $fc,$03, $fd,$03, $fe,$01, $ff,$01, $fe,$01, $ff,$01, $fe,$01\n" +
                "\tdb $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7e, $d5,$7e, $ab,$7c\n" +
                "\tdb $f7,$18, $eb,$1c, $d7,$3c, $eb,$3c, $d5,$3e, $ab,$7e, $d5,$7e, $2a,$ff\n" +
                "\tdb $ff,$01, $fe,$01, $ff,$01, $fe,$01, $ff,$01, $fe,$01, $ff,$01, $a2,$ff\n" +
                "\tdb $7f,$c0, $bf,$c0, $7f,$c0, $bf,$e0, $5f,$e0, $af,$f0, $57,$fc, $aa,$ff\n" +
                "\tdb $ff,$01, $fc,$03, $fd,$03, $fc,$03, $f9,$07, $f0,$0f, $c1,$3f, $82,$ff\n" +
                "\tdb $55,$ff, $2a,$ff, $55,$ff, $2a,$ff, $55,$ff, $2a,$ff, $55,$ff, $00,$ff\n" +
                "\tdb $45,$ff, $a2,$ff, $41,$ff, $82,$ff, $41,$ff, $80,$ff, $01,$ff, $00,$ff\n" +
                "\tdb $54,$ff, $aa,$ff, $54,$ff, $aa,$ff, $54,$ff, $aa,$ff, $54,$ff, $00,$ff\n" +
                "\tdb $15,$ff, $2a,$ff, $15,$ff, $0a,$ff, $15,$ff, $0a,$ff, $01,$ff, $00,$ff\n" +
                "\tdb $01,$ff, $80,$ff, $01,$ff, $80,$ff, $01,$ff, $80,$ff, $01,$ff, $00,$ff"));
        compiler.addLabel(new Label("TilesEnd"));

        compiler.add(new LineGb(""));
        compiler.add(new LineGb("SECTION \"Tilemap\", ROM0"));
        compiler.add(new LineGb(""));

        compiler.addLabel(new Label("Tilemap"));
        compiler.add(new LineGb("db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $01, $02, $03, $01, $04, $03, $01, $05, $00, $01, $05, $00, $06, $04, $07, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $08, $09, $0a, $0b, $0c, $0d, $0b, $0e, $0f, $08, $0e, $0f, $10, $11, $12, $13, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $14, $15, $16, $17, $18, $19, $1a, $1b, $0f, $14, $1b, $0f, $14, $1c, $16, $1d, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $1e, $1f, $20, $21, $22, $23, $24, $22, $25, $1e, $22, $25, $26, $22, $27, $1d, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $01, $28, $29, $2a, $2b, $2c, $2d, $2b, $2e, $2d, $2f, $30, $2d, $31, $32, $33, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $08, $34, $0a, $0b, $11, $0a, $0b, $35, $36, $0b, $0e, $0f, $08, $37, $0a, $38, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $14, $39, $16, $17, $1c, $16, $17, $3a, $3b, $17, $1b, $0f, $14, $3c, $16, $1d, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $1e, $3d, $3e, $3f, $22, $27, $21, $1f, $20, $21, $22, $25, $1e, $22, $40, $1d, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $41, $42, $43, $44, $30, $33, $41, $45, $43, $41, $30, $43, $41, $30, $33, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0\n" +
                "\tdb $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00,  0,0,0,0,0,0,0,0,0,0,0,0"));
        compiler.addLabel(new Label("TilemapEnd"));
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
