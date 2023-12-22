package fr.ensimag.deca.context;

import fr.ensimag.deca.context.exceptions.NonDisjointUnionException;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl47
 * @date 01/01/2024
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).
    private final EnvironmentExp parentEnvironment;
    private final HashMap<Symbol, ExpDefinition> env;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.env = new HashMap<>();
    }
    public void disjointUnion(EnvironmentExp envExp, Location location) throws NonDisjointUnionException{
        for (Symbol s1 :
                envExp.getKeys()) {
            if (this.get(s1) != null) throw new NonDisjointUnionException(location);
            ExpDefinition def = envExp.env.get(s1);
            this.env.put(s1, def);
        }
    }


    public Set<Symbol> getKeys() {
        if (this.parentEnvironment == null) {
            return this.env.keySet();
        }
        this.env.keySet().iterator();
        Set<Symbol> concat = new HashSet<>(this.env.keySet());
        concat.addAll(this.parentEnvironment.getKeys());
        return concat;
    }



    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    public void Empile(EnvironmentExp env)  {

    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        if (env.containsKey(key)) return env.get(key);

        if (parentEnvironment == null) return null;

        return parentEnvironment.get(key);
        // Done
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (env.containsKey(name)) {
            throw new DoubleDefException();
        }
        env.put(name, def);
        // Done
    }

}
