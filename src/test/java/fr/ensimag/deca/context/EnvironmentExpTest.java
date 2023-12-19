package fr.ensimag.deca.context;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

public class EnvironmentExpTest {
    public static void main(String[] args) {
        EnvironmentExp env0 = new EnvironmentExp(null);
        EnvironmentExp env1 = new EnvironmentExp(env0);
        EnvironmentExp env2 = new EnvironmentExp(env1);

        SymbolTable symbolTable = new SymbolTable();
        Symbol xSymbol = symbolTable.create("x");
        Symbol ySymbol = symbolTable.create("y");
        Symbol zSymbol = symbolTable.create("z");
        Symbol uSymbol = symbolTable.create("u");

        ExpDefinition uExpDef = new VariableDefinition(new IntType(uSymbol), new Location(0, 0, "a.deca"));

        try {
            env0.declare(xSymbol, new VariableDefinition(new IntType(xSymbol), new Location(0, 0, "a.deca")));
            env0.declare(uSymbol, uExpDef);
            env1.declare(ySymbol, new VariableDefinition(new IntType(ySymbol), new Location(0, 0, "a.deca")));
            env2.declare(zSymbol, new VariableDefinition(new IntType(zSymbol), new Location(0, 0, "a.deca")));
            env2.declare(xSymbol, new VariableDefinition(new IntType(xSymbol), new Location(0, 0, "a.deca")));
            env2.declare(ySymbol, new VariableDefinition(new IntType(ySymbol), new Location(0, 0, "a.deca")));
            System.out.println("OK");
        } catch (EnvironmentExp.DoubleDefException e) {
            System.out.println("Erreur");
            assert(false);
        }

        try {
            env2.declare(ySymbol, new VariableDefinition(new IntType(ySymbol), new Location(0, 0, "a.deca")));
        } catch (EnvironmentExp.DoubleDefException e) {
            System.out.println("OK");
        }

        if (env2.get(uSymbol).equals(uExpDef)) {
            System.out.println("OK");
        } else {
            System.out.println("Erreur");
            assert(false);
        }
    }
}
