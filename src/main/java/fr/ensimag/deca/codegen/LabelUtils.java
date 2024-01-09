package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;

public class LabelUtils {

    public static SymbolTable.Symbol OBJECT_CLASS_SYMBOL = null;
    public static final String OBJECT_CLASS_NAME = "Object";
    public static final String EQUALS_METHOD_NAME = "equals";

    public static void setObjectClassSymbol(SymbolTable.Symbol value) {
        OBJECT_CLASS_SYMBOL = value;
    }

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
