#!/usr/bin/env python3
import os
import subprocess

allTestedFiles = []
doParallel = False


def printOrAssert(out, expectedResult, doAssert):
    if doAssert:
        assert out == expectedResult
    else:
        print(out)


def doVerify(decaFilePath,
             expectedResult=b"",
             decacOptions="", decacFail=False,
             execError=False, execFail=False,
             input="",
             doAssert=True):
    allTestedFiles.append(decaFilePath)  # To Test -P Later...

    if doParallel and decacFail:
        return 0

    if not doParallel:
        print(f"=========== {decaFilePath} ===========")
        decacCmd = f"./src/main/bin/decac {decacOptions} ./src/test/deca/{decaFilePath}"
        if decacFail:
            decacCmd += " > /dev/null 2>&1"
            out = os.system(decacCmd)
            assert (os.WEXITSTATUS(out) != 0)
            return 0

        os.system(decacCmd)

    extIndex = decaFilePath.rfind(".")
    execCmd = f"./global/bin/ima ./src/test/deca/{decaFilePath[:extIndex]}.ass"
    if execError:
        try:
            subprocess.check_output(execCmd, input=input, shell=True)  # Sould Fail
            assert False
        except subprocess.CalledProcessError as e:
            printOrAssert(e.output, expectedResult, doAssert)
        return 0

    if execFail:
        try:
            subprocess.check_output(execCmd, input=input, shell=True)  # Sould Fail
            assert False
        except subprocess.CalledProcessError as e:
            printOrAssert(e.output, expectedResult, doAssert)
            return 0

    out = subprocess.check_output(execCmd, input=input, shell=True)
    printOrAssert(out, expectedResult, doAssert)


def doTests():
    """Tests"""

    """
    ==============================================
    TESTS QUI MARCHENT
    ==============================================
    """

    print("!!!!!!!!!!! Tests Valides !!!!!!!!!!!")

    doVerify("printString.deca",
             expectedResult=b"Hello World ! Second Argument\n"
                            b"Second Println\n"
                            b"Print Normal 1, Print Normal 2\n")

    doVerify("printIntFloat.deca",
             expectedResult=b"Chaine de Int : 1 2 42 -1 0 -42\n"
                            b"Chaine de Float : 1.22000e+00 -4.24242e+01 0.00000e+003.1416 -2.78000e+00\n")

    doVerify("printFloatHexa.deca",
             expectedResult=b"Chaine de Float Hexa : 0x1.3851ecp+0 -0x1.5364d8p+5 0x0p+03.1416 -0x1.63d70ap+1\n"
                            b"Meme Chaine de Float Hexa : 0x1.3851ecp+0 -0x1.5364d8p+5 0x0p+03.1416 -0x1.63d70ap+1\n")

    doVerify("include.deca",
             expectedResult=b"Hello World\n")

    doVerify("declVarEasy.deca",
             expectedResult=b"x = 1 | y = 2\n")

    doVerify("declVarMany.deca",
             expectedResult=b"x = 1\n"
                            b"y = 42 | z = 3.14160e+00\n")

    doVerify("declVarNoInit.deca",
             expectedResult=b"1 20 42\n"
                            b"0 0.00000e+00\n")

    doVerify("opArith.deca",
             expectedResult=b"1 + 1 = 2\n"
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

    doVerify("opArithConv.deca",
             expectedResult=b"1.1 + 2 = 3.10000e+00\n"
                            b"1.1 - 2 = -9.00000e-01\n"
                            b"42 - 42.0 = 0.00000e+00\n"
                            b"10 * 4.2 = 4.20000e+01\n"
                            b"4.0 / 3 = 1.33333e+00\n"
                            b"4 / 3.0 = 1.33333e+00\n")

    doVerify("opArithNoInit.deca",
             expectedResult=b"0.00000e+00\n")

    doVerify("divisionBy0.deca",
             expectedResult=b"Error: Division by 0\n",
             execError=True)

    doVerify("divisionBy0NoCheck.deca",
             expectedResult=b"1\n",
             decacOptions="-n")

    doVerify("modulo.deca",
             expectedResult=b"2\n"
                            b"Error: Division by 0\n",
             execError=True)

    doVerify("ifThenElse.deca",
             expectedResult=b"1234567\n", )

    doVerify("while.deca",
             expectedResult=b"0123456789\n")

    doVerify("whileIfThenElse.deca",
             expectedResult=b"4321\n")

    doVerify("readIntFloat.deca",
             expectedResult=b"3.20000e+00\n",
             input=b"1\n2.2")

    doVerify("registerOverflow.deca",
             expectedResult=b"42\n",
             decacOptions="-r 4")

    """
    ==============================================
    TESTS QUI NE MARCHENT PAS
    ==============================================
    """

    print()
    print("!!!!!!!!!!! Tests Invalides !!!!!!!!!!!")

    doVerify("context/invalid/langage_sans_objet/troisieme_passe/regle_3_17_1.deca",
             decacFail=True)

    return 0


def decacParallel():
    global doParallel
    doParallel = True
    print()
    print()
    print()
    print("=========== Option -P ===========")
    decacCmd = f"./src/main/bin/decac -P"
    for filePath in allTestedFiles:
        decacCmd += f" ./src/test/deca/{filePath}"
    os.system(decacCmd)

    doTests()

    return 0


def main():
    os.chdir("../../../")

    doTests()

    # With -P
    # decacParallel()

if __name__ == '__main__':
    main()
