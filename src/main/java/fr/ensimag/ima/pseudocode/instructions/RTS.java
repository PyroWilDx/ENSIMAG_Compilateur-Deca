package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.NullaryInstruction;

/**
 * @author Ensimag
 * @date 01/01/2024
 */
public class RTS extends NullaryInstruction {

    @Override
    public String getGameBoyAsm() {
        return "ret";
    }
}
