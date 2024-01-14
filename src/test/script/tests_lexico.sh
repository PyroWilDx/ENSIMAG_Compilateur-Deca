#! /bin/sh

# Auteur : gl47
# Version initiale : 01/01/2024

# Notre script de test de la lexicographie.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


if test_lex src/test/deca/syntax/invalid/helloWorld/simple_lex.deca 2>&1 \
    | head -n 1 | grep -q 'simple_lex.deca:[0-9]'
then
    echo "Echec inattendu de test_lex sur simple_lex.deca XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    exit 1
else
    echo "Succes attendu de test_synt sur simple_lex.deca"
fi

if test_lex src/test/deca/syntax/invalid/helloWorld/chaine_incomplete.deca 2>&1 \
    | grep -q -e 'chaine_incomplete.deca:10:'
then
    echo "Echec attendu pour test_synt sur chaine_incomplete.deca."
else
    echo "Erreur non detectee par test_lex pour chaine_incomplete.deca XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    exit 1
fi


