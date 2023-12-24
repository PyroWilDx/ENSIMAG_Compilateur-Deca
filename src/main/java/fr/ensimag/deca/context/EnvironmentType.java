package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl47
 * @date 01/01/2024
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // not added to envTypes, it's not visible for the user.

        Symbol objectSymb = compiler.createSymbol("object");
        OBJECT = new ClassType(objectSymb);

        typeUnaryOp = new HashMap<>();
        typeUnaryOp.put(new KeyTypeUnaryOp("-", INT), INT);
        typeUnaryOp.put(new KeyTypeUnaryOp("-", FLOAT), FLOAT);
        typeUnaryOp.put(new KeyTypeUnaryOp("!", BOOLEAN), BOOLEAN);

        typeArithOp = new HashMap<>();
        typeArithOp.put(new KeyTypeArithOp(INT, INT), INT);
        typeArithOp.put(new KeyTypeArithOp(INT, FLOAT), FLOAT);
        typeArithOp.put(new KeyTypeArithOp(FLOAT, INT), FLOAT);
        typeArithOp.put(new KeyTypeArithOp(FLOAT, FLOAT), FLOAT);

        typeBinaryOp = new HashMap<>();
        for (KeyTypeArithOp key : typeArithOp.keySet()) {
            Type type1 = key.getType1();
            Type type2 = key.getType2();
            Type type = typeArithOp.get(key);
            for (String op : new String[]{"+", "-", "/", "*"}) {
                typeBinaryOp.put(new KeyTypeBinaryOp(op, type1, type2), type);
            }
            for (String op : new String[]{"==", "!=", "<=", "<", ">=", ">"}) {
                typeBinaryOp.put(new KeyTypeBinaryOp(op, type1, type2), BOOLEAN);
            }
        }
        typeBinaryOp.put(new KeyTypeBinaryOp("%", INT, INT), INT);
        for (String op : new String[]{"&&", "||", "==", "!="}) {
            typeBinaryOp.put(new KeyTypeBinaryOp(op, BOOLEAN, BOOLEAN), BOOLEAN);
        }
        // TODO finir ces machins pour le langage avec objets
    }

    private final Map<Symbol, TypeDefinition> envTypes;
    private final Map<KeyTypeUnaryOp, Type> typeUnaryOp;
    public Type getTypeUnaryOp(String op, Type type) {
        KeyTypeUnaryOp key = new KeyTypeUnaryOp(op, type);
        if (!typeUnaryOp.containsKey(key)) return null;
        return typeUnaryOp.get(key);
    }
    private final Map<KeyTypeArithOp, Type> typeArithOp;
    public Type getTypeArithOp(Type type1, Type type2) {
        KeyTypeArithOp key = new KeyTypeArithOp(type1, type2);
        if (!typeArithOp.containsKey(key)) return null;
        return typeArithOp.get(key);
    }
    private final Map<KeyTypeBinaryOp, Type> typeBinaryOp;
    public Type getTypeBinaryOp(String op, Type type1, Type type2) {
        KeyTypeBinaryOp key = new KeyTypeBinaryOp(op, type1, type2);
        if (!typeBinaryOp.containsKey(key)) return null;
        return typeBinaryOp.get(key);
    }

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public boolean assignCompatible(Type type1, Type type2) {
        //TODO remplir tout ça pour la deuxieme condition possible (si subtype(env, T2, T1) peut être avec un dictionnaire de compatibilités...........
        return type1 == FLOAT && type2 == INT;
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final ClassType OBJECT;
}
