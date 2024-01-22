# Projet Génie Logiciel, Ensimag.
gl47, 01/01/2024.  


Nos scripts de tests pour chaque étape du compilateur et pour l'extension se trouvent dans le répertoire src/test/script/ :  

**tests_lexico.sh** : script des tests de la lexicographie de l'étape A.  
**tests_syntaxe.sh** : script des tests de la syntaxe de l'étape A.  

**tests_context_invalides.sh** : script des tests invalides de l'étape B.  
**tests_context_failed.sh** : liste des tests invalides de l'étape B qui affichent une erreur incorrecte.  

**tests_codegen.sh** : script des tests de l'étape C.  

**tests_gameboy.sh** : script des tests de l'extension GameBoy.

Explication des tests GameBoy : Ici, nous avons commenté le lancement de l'émulateur, le script lance juste la compilation des fichiers et s'arrête.
Pour lancer les tests avec l'émulateur, il faut faire les installations de RGBDS et Emulicious comme indiqué sur la documentation utilisateur.
Ensuite, il faut décommenter les lignes suivantes dans la fonction doVerify() de tests_gameboy.py :
- os.system(f"rgbasm -L -o {decaFileNameNoExt}.o {decaFileNameNoExt}.asm")
- os.system(f"rgblink -o {decaFileNameNoExt}.gb {decaFileNameNoExt}.o")
- os.system(f"rgbfix -v -p 0xFF {decaFileNameNoExt}.gb")
- os.system(f"Emulicious.jar {decaFileNameNoExt}.gb &")
Pour chaque fenêtre, si "Nintendo" s'affiche, le test est bon. Si "Hello World" s'affiche, le test n'est pas bon.
- 
