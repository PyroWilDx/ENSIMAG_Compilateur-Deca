package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class BGT extends BranchInstruction {

    public BGT(Label op) {
        super(op);
    }

    @Override
    public String getGameBoyAsm() {
        return "jp !C,";
    }
}
