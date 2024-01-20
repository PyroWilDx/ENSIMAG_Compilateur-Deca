#!/usr/bin/env python3

import os
import subprocess

nbTest = 0
maxTest = 6


def doVerify(decaFilePath):
    global nbTest

    extIndex = decaFilePath.rfind(".")
    decaFilePathNoExt = decaFilePath[:extIndex]
    lastSlashIndex = decaFilePathNoExt.rfind("/")
    decaFileNameNoExt = decaFilePathNoExt[lastSlashIndex + 1:]

    print(f"\033[32m=========== {'/'.join(decaFilePath.split('/')[1:])} ===========\033[0m")

    decacCmd = f"decac -g test/deca/{decaFilePath}"
    os.system(decacCmd)

    os.system(f"Emulicious.jar '{os.getcwd()}/test/deca/{decaFilePathNoExt}.gb' > /dev/null 2>&1 &")
    nbTest += 1

    if nbTest == maxTest:
        input("Enter to Continue...")
        os.system("pkill -f Emulicious.jar")
        nbTest = 0


def doTests():
    """Test Étape C"""

    print()

    # doVerify("gameboy/fastTest.deca")

    doVerify("gameboy/base/declVar.deca")
    doVerify("gameboy/base/opArith.deca")
    doVerify("gameboy/base/opRegOverflow.deca")
    doVerify("gameboy/base/assign.deca")
    #
    # doVerify("gameboy/cond/boolLazyEval.deca")
    # doVerify("gameboy/cond/ifThenElseSimple.deca")
    # doVerify("gameboy/cond/ifThenElseComplex.deca")
    # doVerify("gameboy/cond/whileSimple.deca")
    # doVerify("gameboy/cond/whileComplex.deca")
    #
    # doVerify("gameboy/object/easy/fieldSelection.deca")
    # doVerify("gameboy/object/easy/fieldInitReg.deca")
    # doVerify("gameboy/object/easy/callSimple.deca")
    # doVerify("gameboy/object/easy/callParams.deca")
    # doVerify("gameboy/object/easy/callReturn.deca")
    # doVerify("gameboy/object/easy/noThisAccess.deca")
    # doVerify("gameboy/object/easy/thisSimple.deca")
    # doVerify("gameboy/object/easy/fieldAssign.deca")
    # doVerify("gameboy/object/easy/fieldInitFieldSimple.deca")
    #
    # doVerify("gameboy/object/mid/objReassign.deca")
    # doVerify("gameboy/object/mid/fieldInitMethod.deca")
    # doVerify("gameboy/object/mid/fieldInitFieldComplex.deca")
    # doVerify("gameboy/object/mid/varInMethod.deca")
    # doVerify("gameboy/object/mid/methodCallMethod.deca")
    # doVerify("gameboy/object/mid/recursiveMethod.deca")

    # doVerify("gameboy/object/extends/extendsFieldSimple.deca")
    # doVerify("gameboy/object/extends/extendsFields.deca")
    # doVerify("gameboy/object/extends/extendsMethods.deca")

    # doVerify("gameboy/object/others/equalsSimple.deca")
    # doVerify("gameboy/object/others/assignInside.deca")
    # doVerify("gameboy/object/others/asmSimple.deca")
    # doVerify("gameboy/object/others/methodRegOverflow.deca")

    # doVerify("gameboy/object/hard/linkedList.deca")
    # doVerify("gameboy/object/hard/binaryTree.deca")

    return 0


def main():
    os.chdir(os.getcwd().split("src")[0]+"/src")

    doTests()


if __name__ == '__main__':
    os.system("pkill -f Emulicious.jar")
    main()
    print()
    input("Enter to Exit...")
    os.system("pkill -f Emulicious.jar")
