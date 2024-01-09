package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelUtils {

    public static Label getClassInitLabel(String className) {
        return new Label("init." + className);
    }

}
