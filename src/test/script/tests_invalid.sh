#! /bin/sh

for fichier in ../deca/context/invalid/langage_sans_objet/*/*.deca
do
  ./launchers/test_context $fichier
done