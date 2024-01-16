package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.codegen.GameBoy;

/**
 * The #null operand.
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class NullOperand extends DVal {

    @Override
    public String toString() {
        if (GameBoy.doCp) return GameBoy.getImmToken() + "0";
        else return GameBoy.getImmToken() + "null";
    }

}
