# Compilateur Deca (2024)

#### ――――――――――

- *Language* : Java.
- *Tools* : Antlr4, Rgbds.
- *ꞮDE* : ꞮntelliJ Ɪdea.

#### ――――――――――

Réalisation d'un compilateur pour le langage de programmation orienté objet Deca (un sous-langage de Java).

Le compilateur peut générer du code pour une machine abstraite nommée IMA, mais surtout pour la toute première GameBoy (1989) !

En plus du compilateur, nous avons créé une librairie pour la GameBoy, permettant la gestion de l'écran et des touches.

Le langage Deca peut alors être utilisé comme un langage orienté objet de haut niveau pour programmer des jeux sur GameBoy.

Nous avons notamment programmé un Snake sur la GameBoy en Deca :

<img src="https://i.imgur.com/0MYgNvD.gif" width="420">

Un [Manuel Utilisateur](docs/Manuel-Utilisateur.pdf) est disponible pour apprendre à utiliser notre compilateur Deca.

Le langage Deca est très ressemblant à Java (cf. [```src/test/deca/codegen/valid```](src/test/deca/codegen/valid) pour voir la structure typique d'un programme Deca).

#### ――――――――――
