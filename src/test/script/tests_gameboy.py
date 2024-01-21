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

    decacCmd = f"decac -gdebug ./src/test/deca/{decaFilePath}"
    os.system(decacCmd)

    bin = "./src/test/deca/gameboy/bin"
    os.system(f"mkdir -p {bin}")
    os.system(f"cp ./src/test/deca/{decaFilePathNoExt}.asm {bin}")

    lastDir = os.getcwd()
    os.chdir(bin)

    os.system(f"rgbasm -L -o {decaFileNameNoExt}.o {decaFileNameNoExt}.asm")
    os.system(f"rgblink -o {decaFileNameNoExt}.gb {decaFileNameNoExt}.o")
    os.system(f"rgbfix -v -p 0xFF {decaFileNameNoExt}.gb")

    #os.system(f"Emulicious.jar {decaFileNameNoExt}.gb &")
    os.system(f"Emulicious.jar {decaFileNameNoExt}.gb > /dev/null 2>&1 &")

    os.chdir(lastDir)

    nbTest += 1

    if nbTest == maxTest:
        input("Enter to Continue...")
        os.system("pkill -f Emulicious.jar")
        nbTest = 0


def doTests():
    """Test Étape C"""

    print()

    # doVerify("gameboy/bin/fastTest.deca")
    #
    # doVerify("gameboy/base/declVar.deca")
    # doVerify("gameboy/base/opArith.deca")
    # doVerify("gameboy/base/opRegOverflow.deca")
    # doVerify("gameboy/base/assign.deca")
    #
    # doVerify("gameboy/cond/boolLazyEval.deca")
    # doVerify("gameboy/cond/elseOpCmp.deca")
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
    #
    # doVerify("gameboy/object/extends/extendsFieldSimple.deca")
    # doVerify("gameboy/object/extends/extendsFields.deca")
    # doVerify("gameboy/object/extends/extendsMethods.deca")
    #
    # doVerify("gameboy/object/others/equalsSimple.deca")
    # doVerify("gameboy/object/others/elseOpCmp.deca")
    # doVerify("gameboy/object/others/assignInside.deca")
    # doVerify("gameboy/object/others/asmSimple.deca")
    # doVerify("gameboy/object/others/methodRegOverflow.deca")
    #
    # doVerify("gameboy/object/hard/recursiveReturn.deca")
    # doVerify("gameboy/object/hard/linkedList.deca")
    doVerify("gameboy/object/hard/binaryTree.deca")

    return 0


def main():
    os.chdir(os.getcwd().split("src")[0])

    print()

    print("\033[1mRemoving .asm files...\033[0m")
    os.system("\\rm ./src/test/deca/gameboy/bin/*.asm")
    print("\033[1mRemoving .o files...\033[0m")
    os.system("\\rm ./src/test/deca/gameboy/bin/*.o")
    print("\033[1mRemoving .gb files...\033[0m")
    os.system("\\rm ./src/test/deca/gameboy/bin/*.gb")
    print("\033[1mRemove Successful\033[0m")

    doTests()


if __name__ == '__main__':
    os.system("pkill -f Emulicious.jar")
    main()
    print()
    input("Enter to Exit...")
    os.system("pkill -f Emulicious.jar")
