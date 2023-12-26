package fr.ensimag.deca.codegen;

public class DeclVarUtils {

    private static int gbVarCount = 0;

    private DeclVarUtils() {}

    public static void incrGbVarCount() {
        gbVarCount++;
    }

    public static int getGbOffset() {
        return gbVarCount + 2;
    }

}
