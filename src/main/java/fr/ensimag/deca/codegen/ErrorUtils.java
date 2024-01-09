package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class ErrorUtils {

    public static final Label stackOverflowLabel = new Label("stackOverflowError");
    public static final Label heapOverflowLabel = new Label("heapOverflowError");
    public static final Label divBy0Label = new Label("divisionBy0Error");
    public static final Label ioErrLabel = new Label("ioError");
    public static final Label nullPointerLabel = new Label("nullPointerError");

    public static void codeGenError(DecacCompiler compiler, String errMsg,
                                    Label bLabel) {
        compiler.addLabel(bLabel);
        compiler.addInstruction(new WSTR(errMsg));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

}
