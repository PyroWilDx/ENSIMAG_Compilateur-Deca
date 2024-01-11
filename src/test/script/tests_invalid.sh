#! /bin/sh

for fichier in ../deca/context/invalid/langage_sans_objet/*/*.deca
do
  ./launchers/test_context $fichier  1>debug 2>error;
  sed -r 's/.*regle.*:([0-9]*):([0-9]*):/Ligne \1:/g' error;
  echo =============== REPONSE ATTENDUE ===============;
  sed -n '5p' < $fichier | sed 's/\/\/\ *//g';
  echo;
  echo;
done