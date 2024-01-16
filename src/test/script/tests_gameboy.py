#!/usr/bin/env python3

import os
import subprocess

allTestedFiles = []
doParallel = False


def prettyPrint(msg):
    print()
    # print("\033[32m==============================================\033[0m")
    print("\033[1m==============================================\033[0m")
    # print(f"\033[32m{msg}\033[0m")
    print(f"\033[1m{msg}\033[0m")
    # print("\033[32m==============================================\033[0m")
    print("\033[1m==============================================\033[0m")
    print()


def printOrAssert(out, expectedResult, doAssert, perf=False):
    doAssert = False
    if doAssert:
        if not perf:
            print("\033[0;31m", end="")
            assert expectedResult == out
            print("\033[0m", end="")
        else:
            expectedLength = len(expectedResult)
            print(f"\033[1;36m{out[expectedLength:-1].decode('utf-8')}\033[0m")
            print("\033[0;31m", end="")
            assert expectedResult == out[:expectedLength]
            print("\033[0m", end="")
    else:
        print(out)


def doVerify(decaFilePath,
             expectedResult=b"", decacExpected="",
             decacOptions="", decacFail=False,
             execError=False, execFail=False,
             input="",
             imaOptions="",
             doAssert=True):
    extIndex = decaFilePath.rfind(".")
    decaFilePathNoExt = decaFilePath[:extIndex]
    if (("-b" not in decacOptions) and ("-n" not in decacOptions) and
            ("-v" not in decacOptions) and ("-d" not in decacOptions)
            and (not decacFail)):
        allTestedFiles.append(decaFilePathNoExt)  # To Test -P Later...

    if doParallel and (decaFilePathNoExt not in allTestedFiles):
        return 0

    print(f"\033[32m=========== {'/'.join(decaFilePath.split('/')[1:])} ===========\033[0m")

    if not doParallel:
        decacCmd = f"decac {decacOptions} -g ./src/test/deca/{decaFilePath}"
        if decacFail: # Ne doit plus être utilisé
            decacCmd += " > /dev/null 2>&1"
            out = os.system(decacCmd)
            assert (os.WEXITSTATUS(out) != 0)
            return 0
        if ("-b" in decacOptions) or ("-p" in decacOptions) or ("-d" in decacOptions):
            out = subprocess.check_output(decacCmd, shell=True)
            printOrAssert(out, decacExpected, doAssert)
            if "-p" in decacOptions:
                return 0

        os.system(decacCmd)

    if ("-v" in decacOptions) or ("-p" in decacOptions):
        return 0

    return 0

    execCmd = f"ima {imaOptions} ./src/test/deca/{decaFilePathNoExt}.asm"
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
    printOrAssert(out, expectedResult, doAssert, "-s" in imaOptions)


def doTests():
    """Test Étape C"""

    """
    ============================================
    ============================================
    """
    if not doParallel:
        prettyPrint("TEST DE L'ÉTAPE C (VALIDE)")

    doVerify("gameboy/test.deca")

    doVerify("gameboy/valid/iostreams/printString.deca",
             expectedResult=b"Hello World ! Second Argument\n"
                            b"Second Println\n"
                            b"Print Normal 1, Println Normal 2\n")

    doVerify("gameboy/valid/iostreams/printIntFloat.deca",
             expectedResult=b"Chaine de Int : 1 2 42 -1 0 -42\n"
                            b"Chaine de Float : 1.22000e+00 -4.24242e+01 0.00000e+003.1416 -2.78000e+00\n")

    doVerify("gameboy/valid/iostreams/printFloatHexa.deca",
             expectedResult=b"0x1.3851ecp+0 -0x1.5364d8p+5 0x0p+03.1416 -0x1.63d70ap+1\n")

    doVerify("gameboy/valid/iostreams/includeSimple.deca",
             expectedResult=b"Hello World\n")

    doVerify("gameboy/valid/declarations/declVarSimple.deca",
             expectedResult=b"x = 1 | y = 2\n")

    doVerify("gameboy/valid/declarations/declVarMany.deca",
             expectedResult=b"x = 1\n"
                            b"y = 42 | z = 3.14160e+00\n")

    doVerify("gameboy/valid/operations/opArith.deca",
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

    doVerify("gameboy/valid/operations/opArithConv.deca",
             expectedResult=b"1.1 + 2 = 3.10000e+00\n"
                            b"1.1 - 2 = -9.00000e-01\n"
                            b"42 - 42.0 = 0.00000e+00\n"
                            b"10 * 4.2 = 4.20000e+01\n"
                            b"4.0 / 3 = 1.33333e+00\n"
                            b"4 / 3.0 = 1.33333e+00\n")

    doVerify("gameboy/valid/conditions/boolLazyEval.deca",
             expectedResult=b"")

    doVerify("gameboy/valid/conditions/ifThenElseSimple.deca",
             expectedResult=b"12345678910111213141516\n")

    doVerify("gameboy/valid/conditions/ifThenElseComplex.deca",
             expectedResult=b"OK\n")

    doVerify("gameboy/valid/conditions/whileSimple.deca",
             expectedResult=b"0123456789\n")

    doVerify("gameboy/valid/conditions/whileComplex.deca",
             expectedResult=b"168 * 42 * 10 = 70560\n"
                            b"x = 70560\n")

    doVerify("gameboy/valid/conditions/whileIfThenElse.deca",
             expectedResult=b"4321\n")

    doVerify("gameboy/valid/classes/fields/newSimple.deca")

    doVerify("gameboy/valid/classes/fields/fieldSimple.deca")

    doVerify("gameboy/valid/classes/fields/fieldSelection.deca",
             expectedResult=b"1.00000e+00 2 4 0 1.00000e+00\n"
                            b"8.00000e+00 3.20000e+01 16 0.00000e+00 1\n")

    doVerify("gameboy/valid/classes/fields/fieldInitReg.deca",
             expectedResult=b"1 20 2\n")

    doVerify("gameboy/valid/classes/fields/fieldInitFieldSimple.deca",
             expectedResult=b"10\n")

    doVerify("gameboy/valid/classes/fields/fieldInitMethod.deca",
             expectedResult=b"30 60 10\n")

    doVerify("gameboy/valid/classes/fields/fieldInitFieldComplex.deca",
             expectedResult=b"0 0 20\n")

    doVerify("gameboy/valid/classes/this/thisSimple.deca",
             expectedResult=b"2 2\n"
                            b"6 6\n")

    doVerify("gameboy/valid/classes/this/noThisAccess.deca",
             expectedResult=b"2 6\n"
                            b"60 120\n")

    doVerify("gameboy/valid/classes/methods/methodSimple.deca")

    doVerify("gameboy/valid/classes/methods/methodCallSimple.deca",
             expectedResult=b"Method Called\n")

    doVerify("gameboy/valid/classes/methods/methodCallParams.deca",
             expectedResult=b"x + y + z = 10 + 20 + 30 = 60\n"
                            b"x * y * z = 2 * 6 * 10 = 120\n")

    doVerify("gameboy/valid/classes/methods/methodCallReturn.deca",
             b"0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20\n")

    doVerify("gameboy/valid/classes/methods/earlyReturn.deca",
             expectedResult=b"10\n")

    doVerify("gameboy/valid/classes/methods/varInMethod.deca",
             expectedResult=b"2 4 16 22 44\n")

    doVerify("gameboy/valid/classes/methods/recursiveMethod.deca",
             expectedResult=b"40 80 120 160 200 240 280 320 360 400\n"
                            b"400 1 2\n")

    doVerify("gameboy/valid/classes/methods/methodCallMethod.deca",
             expectedResult=b"10 20 30\n"
                            b"80 70 60\n"
                            b"180 170 160\n"
                            b"110 120 130\n"
                            b"80 70 60\n"
                            b"180 170 160\n")

    doVerify("gameboy/valid/classes/extends/extendsFieldSimple.deca",
             expectedResult=b"1 2\n"
                            b"1 2 0\n")

    doVerify("gameboy/valid/classes/extends/extendsFields.deca",
             expectedResult=b"1 2\n"
                            b"4 2 0\n")

    doVerify("gameboy/valid/classes/extends/extendsMethods.deca",
             expectedResult=b"1 2 4 2 0\n")

    doVerify("gameboy/valid/classes/polymorphisms/redefinedMethodOrder.deca",
             expectedResult=b"A0 A10 A20 A30 A40\n"
                            b"A0 B10 B20 B30 A40 B50 B60\n"
                            b"A0 C10 B20 B30 C40 B50 C60 C70 C80\n")

    doVerify("gameboy/valid/classes/polymorphisms/fieldRedef.deca",
             expectedResult=b"1 2 1\n")

    doVerify("gameboy/valid/classes/polymorphisms/ex_Video5_Page11.deca",
             expectedResult=b"p1 : Point 2d : (1, 1)\n"
                            b"p3 before p2.diag(3) : Point 3d : (2, 2, 2)\n"
                            b"p3 after p2.diag(3) : Point 3d : (5, 5, 5)\n"
                            b"p2 : Point 3d : (5, 5, 5)\n")

    doVerify("gameboy/valid/classes/miscellaneous/equalsSimple.deca",
             expectedResult=b"OK1 OK2 OK3 OK4 OK5 OK6\n")

    doVerify("gameboy/valid/classes/miscellaneous/assignInside.deca",
             expectedResult=b"0 0\n"
                            b"10 0\n"
                            b"OK\n"
                            b"36 6\n"
                            b"36 100\n")

    # doVerify("gameboy/valid/classes/miscellaneous/asmSimple.deca",
    #          expectedResult=b"10 180\n",
    #          doAssert=False)

    doVerify("gameboy/valid/registers/opRegOverflow.deca",
             expectedResult=b"52 52\n",
             decacOptions="-r 4")

    doVerify("gameboy/valid/registers/methodRegOverflow.deca",
             expectedResult=b"600\n"
                            b"OK\n",
             decacOptions="-r 4")

    doVerify("gameboy/valid/provided/ecrit0.deca",
             expectedResult=b"ok\n"
                            b"ok\n")

    doVerify("gameboy/valid/provided/entier1.deca",
             expectedResult=b"1\n"
                            b"2\n")

    doVerify("gameboy/valid/provided/cond0.deca",
             expectedResult=b"ok\n")

    doVerify("gameboy/valid/provided/exdoc.deca",
             expectedResult=b"a.getX() = 1\n")

    doVerify("gameboy/valid/options/optionBanner.deca",
             expectedResult=b"Bonjour\n")

    doVerify("gameboy/valid/options/optionParse.deca")

    doVerify("gameboy/valid/options/optionVerification.deca",
             decacOptions="-v")

    doVerify("gameboy/invalid/errors/optionNoCheck.deca",
             expectedResult=b"1\n",
             decacOptions="-n")

    doVerify("gameboy/valid/options/optionDebug.deca",
             expectedResult=b"z = 6.00000e+00\n",
             # decacOptions="-p -d"
             )

    """
    ============================================
    ============================================
    """
    if not doParallel:
        prettyPrint("TEST DE L'ÉTAPE C (INTERACTIVE)")

    doVerify("gameboy/interactive/iostreams/readIntFloat.deca",
             expectedResult=b"3.20000e+00\n",
             input=b"1\n2.2")

    """
    ============================================
    ============================================
    """
    if not doParallel:
        prettyPrint("TEST DE L'ÉTAPE C (INVALIDE)")

    doVerify("gameboy/invalid/errors/declVarNoInit.deca",
             expectedResult=b"  ** IMA ** ERREUR ** Ligne 13 : \n"
                            b"    WINT avec R1 indefini\n",
             execError=True)

    doVerify("gameboy/invalid/errors/divisionBy0.deca",
             expectedResult=b"Error: Division by 0\n",
             execError=True)

    doVerify("gameboy/invalid/errors/moduloBy0.deca",
             expectedResult=b"2\n"
                            b"Error: Division by 0\n",
             execError=True)

    doVerify("gameboy/invalid/errors/floatOverflow.deca",
             expectedResult=b"Error: Float Operation Overflow\n",
             execError=True)

    doVerify("gameboy/interactive/errors/readError.deca",
             expectedResult=b"Error: Input/Output Error\n",
             execError=True,
             input=b"10")

    doVerify("gameboy/invalid/errors/stackOverflow.deca",
             expectedResult=b"Error: Stack Overflow\n",
             execError=True)

    doVerify("gameboy/invalid/errors/nullPointer.deca",
             expectedResult=b"Error: Dereferencing Null Pointer\n",
             execError=True)

    doVerify("gameboy/invalid/errors/heapOverflow.deca",
             expectedResult=b"Error: Heap Overflow\n",
             execError=True)

    doVerify("gameboy/invalid/errors/missingReturn.deca",
             expectedResult=b"Error: Exiting function A.missingReturn() without return\n",
             execError=True)

    """
    ============================================
    ============================================
    """
    if not doParallel:
        prettyPrint("TEST DE L'ÉTAPE C (PERF)")

    doVerify("gameboy/perf/provided/syracuse42.deca",
             expectedResult=b"8\n",
             imaOptions="-s")

    doVerify("gameboy/perf/provided/ln2.deca",
             expectedResult=b"6.93148e-01 = 0x1.62e448p-1\n",
             imaOptions="-s")

    doVerify("gameboy/perf/provided/ln2_fct.deca",
             expectedResult=b"6.93148e-01 = 0x1.62e448p-1\n",
             imaOptions="-s")

    return 0


def decacParallel():
    global doParallel
    doParallel = True

    prettyPrint("TEST DE L'ÉTAPE C AVEC OPTION -P")

    decacCmd = f"./src/main/bin/decac -P"
    for filePath in allTestedFiles:
        decacCmd += f" ./src/test/deca/{filePath}.deca"
    print("\033[1mRemoving .ass files...\033[0m")
    for filePath in allTestedFiles:  # To Ensure that -P Recompiles All
        os.system(f"\\rm ./src/test/deca/{filePath}.ass")
    print("\033[1mRemove Successful\033[0m")
    print()

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
    print()