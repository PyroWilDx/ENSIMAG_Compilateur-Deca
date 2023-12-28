#!/usr/bin/env python3
import os
import subprocess


def doVerify(decaFilePath, expectedResult, doAssert=True):
    print(f"=========== {decaFilePath} ===========")
    cpCmd = f"./src/main/bin/decac ./src/test/deca/{decaFilePath}"
    if expectedResult != "FAIL":
        os.system(cpCmd)
        extIndex = decaFilePath.rfind(".")
        execCmd = f"./global/bin/ima ./src/test/deca/{decaFilePath[:extIndex]}.ass"
        out = subprocess.check_output(execCmd, shell=True)
        if doAssert:
            assert (out == expectedResult)
        else:
            print(out)
    else:
        cpCmd += " > /dev/null 2>&1"
        out = os.system(cpCmd)
        assert (os.WEXITSTATUS(out) != 0)


os.chdir("../../../")

"""
==============================================
TESTS QUI MARCHENT
==============================================
"""

doVerify("printString.deca",
         b"Hello World ! Second Argument\n"
         b"Second Println\n"
         b"Print Normal 1, Print Normal 2\n"
         )

doVerify("printIntFloat.deca",
         b"Chaine de Int : 1 2 42 -1 0 -42\n"
         b"Chaine de Float : 1.22000e+00 -4.24242e+01 0.00000e+003.1416 -2.78000e+00\n"
         )

doVerify("printFloatHexa.deca",
         b"Chaine de Float Hexa : 0x1.3851ecp+0 -0x1.5364d8p+5 0x0p+03.1416 -0x1.63d70ap+1\n"
         b"Meme Chaine de Float Hexa : 0x1.3851ecp+0 -0x1.5364d8p+5 0x0p+03.1416 -0x1.63d70ap+1\n"
         )

doVerify("include.deca",
         b"Hello World\n"
         )

doVerify("variableDeclarationEasy.deca",
         b"x = 1 | y = 2\n"
         )

doVerify("variableDeclarationMany.deca",
         b"x = 1\n"
         b"y = 42 | z = 3.14160e+00\n"
         )

doVerify("variableDeclarationNoInit.deca",
         "TODO",
         False
         )

# doVerify("opArith.deca",
#          "TODO",
#          False
#          )

# doVerify("whileAndIfThenElse.deca",
#          b"4\n"
#          b"3\n"
#          b"2\n"
#          b"1\n"
#          )

"""
==============================================
TESTS QUI NE MARCHENT PAS
==============================================
"""

doVerify("context/invalid/langage_sans_objet/troisieme_passe/regle_3_17_1.deca",
         "FAIL")
