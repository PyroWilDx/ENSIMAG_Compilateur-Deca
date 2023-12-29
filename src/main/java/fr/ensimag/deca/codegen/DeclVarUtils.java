package fr.ensimag.deca.codegen;

import fr.ensimag.deca.context.Type;

public class DeclVarUtils {

    private static int gbVarCount = 0;

    public static Type currDeclVarType = null;

    private DeclVarUtils() {}

    public static void incrGbVarCount() {
        gbVarCount++;
    }

    public static int getGbOffset() {
        return gbVarCount + 2;
    }

}
