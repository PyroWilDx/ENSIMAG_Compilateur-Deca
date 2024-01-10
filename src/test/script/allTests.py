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
    extIndex = decaFilePath.rfind(".")
    decaFilePathNoExt = decaFilePath[:extIndex]
    if (("-b" not in decacOptions) and ("-n" not in decacOptions) and
            ("-v" not in decacOptions) and ("-d" not in decacOptions)
            and (not decacFail)):
        allTestedFiles.append(decaFilePathNoExt)  # To Test -P Later...

    if doParallel and (decaFilePathNoExt not in allTestedFiles):
        return 0

    print(f"=========== {decaFilePath} ===========")

    if not doParallel:
        decacCmd = f"./src/main/bin/decac {decacOptions} ./src/test/deca/{decaFilePath}"
        if decacFail:
            decacCmd += " > /dev/null 2>&1"
            out = os.system(decacCmd)
            assert (os.WEXITSTATUS(out) != 0)
            return 0

        os.system(decacCmd)

    if ("-v" in decacOptions) or ("-p" in decacOptions):
        return 0

    execCmd = f"./global/bin/ima ./src/test/deca/{decaFilePathNoExt}.ass"
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

    if not doParallel:
        print("!!!!!!!!!!! Tests Valides !!!!!!!!!!!")

    doVerify("codegen/valid/iostream/printString.deca",
             expectedResult=b"Hello World ! Second Argument\n"
                            b"Second Println\n"
                            b"Print Normal 1, Println Normal 2\n")

    doVerify("codegen/valid/iostream/printIntFloat.deca",
             expectedResult=b"Chaine de Int : 1 2 42 -1 0 -42\n"
                            b"Chaine de Float : 1.22000e+00 -4.24242e+01 0.00000e+003.1416 -2.78000e+00\n")

    doVerify("codegen/valid/iostream/printFloatHexa.deca",
             expectedResult=b"0x1.3851ecp+0 -0x1.5364d8p+5 0x0p+03.1416 -0x1.63d70ap+1\n")

    doVerify("syntax/valid/include/include.deca",
             expectedResult=b"Hello World\n")

    doVerify("codegen/valid/declarations/declVarEasy.deca",
             expectedResult=b"x = 1 | y = 2\n")

    doVerify("codegen/valid/declarations/declVarMany.deca",
             expectedResult=b"x = 1\n"
                            b"y = 42 | z = 3.14160e+00\n")

    doVerify("codegen/invalid/declVarNoInit.deca",
             expectedResult=b"  ** IMA ** ERREUR ** Ligne 18 : \n"
                            b"    WINT avec R1 indefini\n",
             execError=True)

    doVerify("codegen/valid/operations/opArith.deca",
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

    doVerify("codegen/valid/operations/opArithConv.deca",
             expectedResult=b"1.1 + 2 = 3.10000e+00\n"
                            b"1.1 - 2 = -9.00000e-01\n"
                            b"42 - 42.0 = 0.00000e+00\n"
                            b"10 * 4.2 = 4.20000e+01\n"
                            b"4.0 / 3 = 1.33333e+00\n"
                            b"4 / 3.0 = 1.33333e+00\n")

    doVerify("codegen/invalid/divisionBy0.deca",
             expectedResult=b"Error: Division by 0\n",
             execError=True)

    doVerify("codegen/invalid/moduloBy0.deca",
             expectedResult=b"2\n"
                            b"Error: Division by 0\n",
             execError=True)

    doVerify("codegen/valid/conditions/boolLazyEval.deca",
             expectedResult=b"")

    doVerify("codegen/valid/conditions/ifThenElse.deca",
             expectedResult=b"1234567\n")

    doVerify("codegen/valid/conditions/while.deca",
             expectedResult=b"0123456789\n")

    doVerify("codegen/valid/conditions/whileIfThenElse.deca",
             expectedResult=b"4321\n")

    doVerify("codegen/interactive/readIntFloat.deca",
             expectedResult=b"3.20000e+00\n",
             input=b"1\n2.2")

    doVerify("codegen/valid/registers/registerOverflow.deca",
             expectedResult=b"5252\n",
             decacOptions="-r 4")

    doVerify("codegen/valid/options/optionBanner.deca",
             expectedResult=b"Bonjour\n",
             decacOptions="-b")

    doVerify("codegen/valid/options/optionParse.deca",
             decacOptions="-p")

    doVerify("codegen/valid/options/optionVerification.deca",
             expectedResult=b"",
             decacOptions="-v")

    doVerify("codegen/valid/options/optionNoCheck.deca",
             expectedResult=b"1\n",
             decacOptions="-n")

    # doVerify("codegen/valid/options/optionDebug.deca",
    #          expectedResult=b"z = 6\n",
    #          decacOptions="-d -d -d")

    doVerify("codegen/valid/classes/fieldsSimpleClass.deca")

    doVerify("codegen/valid/classes/fieldsComplexClass.deca")

    """
    ==============================================
    TESTS QUI NE MARCHENT PAS
    ==============================================
    """

    if doParallel:
        return 0

    # print()
    # print("!!!!!!!!!!! Tests Invalides !!!!!!!!!!!")
    #
    # doVerify("context/invalid/langage_sans_objet/troisieme_passe/regle_3_17_1.deca",
    #          decacFail=True)

    return 0


def decacParallel():
    global doParallel
    doParallel = True
    print()
    print("====================== Option -P ======================")
    decacCmd = f"./src/main/bin/decac -P"
    for filePath in allTestedFiles:
        decacCmd += f" ./src/test/deca/{filePath}.deca"
    print("Removing .ass files...")
    for filePath in allTestedFiles:  # To Ensure that -P Recompiles All
        os.system(f"\\rm ./src/test/deca/{filePath}.ass")
    print("Remove Successful")

    os.system(decacCmd)

    doTests()

    return 0


def main():
    os.chdir(os.getcwd().split("src")[0])

    doTests()

    # With -P
    # decacParallel()


if __name__ == '__main__':
    main()
