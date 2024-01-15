#! /bin/sh

# Auteur : gl47
# Version initiale : 01/01/2024

# Notre script de test de la lexicographie.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


test_lex_valide () {
    test_synt "$1" > "${1%.deca}".res
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_lex sur $1 XXXXXXXXXXXXXXXXXXXXXXXXX"
        exit 1
    else
      echo "Succes attendu de test_lex sur $1"
    fi
}
echo [ TESTS VALIDES POUR LE LEXEUR]

test_lex_valide "src/test/deca/syntax/valid/helloWorld/hello.deca"
test_lex_valide "src/test/deca/syntax/valid/helloWorld/hello.deca"

for cas_de_test in src/test/deca/syntax/valid/sansObjet/*.deca
do
    test_lex_valide "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/valid/avecObjet/*.deca
do
    test_lex_valide "$cas_de_test"
done

echo [ TESTS INVALIDES POUR LE LEXEUR]

if test_lex src/test/deca/syntax/invalid/helloWorld/chaine_incomplete.deca 2>&1 \
    | grep -q -e 'chaine_incomplete.deca:10:'
then
    echo "Echec attendu pour test_lex sur chaine_incomplete.deca."
else
    echo "Erreur non detectee par test_lex pour chaine_incomplete.deca XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    exit 1
fi

if test_lex src/test/deca/syntax/invalid/include/include_incorrect.deca 2>&1 \
    | grep -q -e 'include_incorrect.deca:10:'
then
    echo "Echec attendu pour test_lex sur include_incorrect.deca."
else
    echo "Erreur non detectee par test_lex pour include_incorrect.deca XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    exit 1
fi

if test_lex src/test/deca/syntax/invalid/sansObjet/crochets.deca 2>&1 \
    | grep -q -e 'crochets.deca:11:'
then
    echo "Echec attendu pour test_lex sur crochets.deca."
else
    echo "Erreur non detectee par test_lex pour crochets.deca XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    exit 1
fi

if test_lex src/test/deca/syntax/invalid/sansObjet/deuxpoints.deca 2>&1 \
    | grep -q -e 'deuxpoints.deca:11:'
then
    echo "Echec attendu pour test_lex sur deuxpoints.deca."
else
    echo "Erreur non detectee par test_lex pour deuxpoints.deca XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    exit 1
fi

if test_lex src/test/deca/syntax/invalid/sansObjet/interrogation.deca 2>&1 \
    | grep -q -e 'interrogation.deca:12:'
then
    echo "Echec attendu pour test_lex sur interrogation.deca."
else
    echo "Erreur non detectee par test_lex pour interrogation.deca XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    exit 1
fi