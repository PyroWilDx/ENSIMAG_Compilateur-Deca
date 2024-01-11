#! /bin/sh

echo ==========================================================;
echo "                   LANGUAGE SANS OBJET"
echo ==========================================================;
for fichier in ../deca/context/invalid/langage_sans_objet/*/*.deca
do
  basename "$fichier" | sed 's/\.deca//g' | sed 's/_/\ /' | sed 's/_/\./';
  ./launchers/test_context "$fichier"  1>debug 2>error;
  sed -r 's/.*regle.*:([0-9]*):([0-9]*):/Ligne \1:/g' error;
  echo =============== REPONSE ATTENDUE ===============;
  sed -n '5p' < "$fichier" | sed 's/\/\/\ *//g';
  echo;
  echo;
done
echo;echo;
echo ==========================================================;
echo "                   LANGUAGE SANS OBJET"
echo ==========================================================;
for fichier in ../deca/context/invalid/langage_essentiel/*/*.deca
do
  basename "$fichier" | sed 's/\.deca//g' | sed 's/_/\ /' | sed 's/_/\./';
  ./launchers/test_context "$fichier"  1>debug 2>error;
  sed -r 's/.*regle.*:([0-9]*):([0-9]*):/Ligne \1:/g' error;
  echo =============== REPONSE ATTENDUE ===============;
  sed -n '5p' < "$fichier" | sed 's/\/\/\ *//g';
  echo;
  echo;
done