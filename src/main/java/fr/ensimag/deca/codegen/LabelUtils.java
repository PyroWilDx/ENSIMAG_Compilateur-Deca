package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelUtils {

    public static String OBJECT_CLASS_NAME = "Object";
    public static String EQUALS_METHOD_NAME = "equals";

    public static Label getClassInitLabel(String className) {
        return new Label("init." + className);
    }

    public static Label getMethodLabel(String className, String methodName) {
        return new Label("code." + className + "." + methodName);
    }

    public static Label getMethodEndLabel(String className, String methodName) {
        return new Label("end." + className + "." + methodName);
    }

}
