#! /bin/sh

# Auteur : gl47
# Version initiale : 01/01/2024

# Test minimaliste de la syntaxe.
# On lance test_synt sur un fichier valide, et les tests invalides.

# dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# exemple, en stoquant la valeur attendue quelque part, et en
# utilisant la commande unix "diff".
#
# Il faudrait aussi lancer ces tests sur tous les fichiers deca
# automatiquement. Un exemple d'automatisation est donné avec une
# boucle for sur les tests invalides, il faut aller encore plus loin.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# exemple de définition d'une fonction
test_synt_invalide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_synt sur $1."
    else
        echo "Succes inattendu de test_synt sur $1. XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
        exit 1
    fi
}

test_synt_valide () {
    # $1 = premier argument.
    #test_synt "$1" > "resultatTestSynt$1.res"
    test_synt "$1" > "${1%.deca}".res
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_synt sur $1 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
        exit 1
    else
      if diff -q "${1%.deca}_resultatCorrect.res" "${1%.deca}".res
      then
        echo "Succes attendu et arbres compatibles de test_synt sur $1"
      else
        echo "arbre incompatible,pourtant Succes attendu de test_synt sur $1XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
      fi
    fi
}

echo "Langage Hello World"
for cas_de_test in src/test/deca/syntax/invalid/helloWorld/*.deca
do
    test_synt_invalide "$cas_de_test"
done

test_synt_valide "src/test/deca/syntax/valid/helloWorld/hello.deca"

echo "Langage Hello World avec include"

test_synt_valide "src/test/deca/syntax/valid/include/include.deca"


echo "Langage Sans Objets"
for cas_de_test in src/test/deca/syntax/invalid/sansObjet/*.deca
do
    test_synt_invalide "$cas_de_test"
done

test_synt_valide "src/test/deca/syntax/valid/sansObjet/sansObjet.deca"

echo "Langage Avec Objets"
for cas_de_test in src/test/deca/syntax/valid/avecObjet/*.deca
do
    test_synt_valide "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/invalid/avecObjet/*.deca
do
    test_synt_invalide "$cas_de_test"
done
