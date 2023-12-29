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

def doVerifyError(decaFilePath, expectedResult, doAssert=True):
    print(f"=========== {decaFilePath} ===========")
    cpCmd = f"./src/main/bin/decac ./src/test/deca/{decaFilePath}"
    os.system(cpCmd)
    extIndex = decaFilePath.rfind(".")
    execCmd = f"./global/bin/ima ./src/test/deca/{decaFilePath[:extIndex]}.ass"
    try:
        subprocess.check_output(execCmd, shell=True) # Sould Fail
        assert (False)
    except subprocess.CalledProcessError as e:
        if doAssert:
            assert (e.output == expectedResult)
        else:
            print(e.output)


os.chdir("../../../")

"""
==============================================
TESTS QUI MARCHENT
==============================================
"""

print("!!!!!!!!!!! Tests Valides !!!!!!!!!!!")

doVerify("printString.deca",
         b"Hello World ! Second Argument\n"
         b"Second Println\n"
         b"Print Normal 1, Print Normal 2\n")

doVerify("printIntFloat.deca",
         b"Chaine de Int : 1 2 42 -1 0 -42\n"
         b"Chaine de Float : 1.22000e+00 -4.24242e+01 0.00000e+003.1416 -2.78000e+00\n")

doVerify("printFloatHexa.deca",
         b"Chaine de Float Hexa : 0x1.3851ecp+0 -0x1.5364d8p+5 0x0p+03.1416 -0x1.63d70ap+1\n"
         b"Meme Chaine de Float Hexa : 0x1.3851ecp+0 -0x1.5364d8p+5 0x0p+03.1416 -0x1.63d70ap+1\n")

doVerify("include.deca",
         b"Hello World\n")

doVerify("variableDeclarationEasy.deca",
         b"x = 1 | y = 2\n")

doVerify("variableDeclarationMany.deca",
         b"x = 1\n"
         b"y = 42 | z = 3.14160e+00\n")

doVerify("variableDeclarationNoInit.deca",
         b"1 20 42\n")

doVerify("opArith.deca",
         b"1 + 1 = 2\n"
         b"1 - 1 = 0\n"
         b"1 - 42 = -41\n"
         b"1 - -42 = 43\n"
         b"0 * 1 = 0\n"
         b"1 * 0 = 0\n"
         b"1 * 1 = 1\n"
         b"10 * 42 = 420\n"
         b"10 / 3 = 3\n"
         b"0 / 1 = 0\n"
         b"1.1 + 3.2 = 4.30000e+00\n"
         b"1.1 - 3.2 = -2.10000e+00\n"
         b"3.14 * 3.14 = 9.85960e+00\n"
         b"10.0 / 3.0 = 3.33333e+00\n"
         b"0.0 / 1.0 = 0.00000e+00\n"
         b"20.8 / 4.0 = 5.20000e+00\n"
         b"4 * 6 / 2 / 2 * 10 = 60\n")

# doVerify("opArithConv.deca",
#          "TODO",
#          False)

doVerifyError("divisionBy0.deca",
              b"Error: Division by 0\n")

doVerify("ifThenElse.deca",
         b"1234567\n", )

doVerify("while.deca",
         b"0123456789\n")

doVerify("whileAndIfThenElse.deca",
         b"4321\n")

doVerify("readIntFloat.deca",
         "TODO", False)

doVerify("registerOverflow.deca",
         b"42\n")

"""
==============================================
TESTS QUI NE MARCHENT PAS
==============================================
"""

print()
print("!!!!!!!!!!! Tests Invalides !!!!!!!!!!!")

doVerify("context/invalid/langage_sans_objet/troisieme_passe/regle_3_17_1.deca",
         "FAIL")
