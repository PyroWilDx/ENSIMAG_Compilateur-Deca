#!/usr/bin/env python3

import os
import subprocess

nbTest = 0

def doVerify(decaFilePath):
    global nbTest

    extIndex = decaFilePath.rfind(".")
    decaFilePathNoExt = decaFilePath[:extIndex]
    lastSlashIndex = decaFilePathNoExt.rfind("/")
    decaFileNameNoExt = decaFilePathNoExt[lastSlashIndex + 1:]

    print(f"\033[32m=========== {'/'.join(decaFilePath.split('/')[1:])} ===========\033[0m")

    decacCmd = f"decac -g ./src/test/deca/{decaFilePath}"
    os.system(decacCmd)

    dl = "~/Downloads/GameBoy"
    os.system(f"mkdir -p {dl}")
    os.system(f"cp ./src/test/deca/{decaFilePathNoExt}.asm {dl}")

    lastDir = os.getcwd()
    os.chdir(os.path.join(os.path.expanduser("~"), "Downloads/GameBoy"))

    os.system(f"rgbasm -L -o {dl}/{decaFileNameNoExt}.o {dl}/{decaFileNameNoExt}.asm")
    os.system(f"rgblink -o {dl}/{decaFileNameNoExt}.gb {dl}/{decaFileNameNoExt}.o")
    os.system(f"rgbfix -v -p 0xFF {dl}/{decaFileNameNoExt}.gb")

    os.system(f"Emulicious.jar {dl}/{decaFileNameNoExt}.gb &")

    os.chdir(lastDir)

    nbTest += 1

    if nbTest == 6:
        input("Enter to Continue")
        os.system("pkill -f Emulicious.jar")
        nbTest = 0


def doTests():
    """Test Étape C"""

    print()

    # doVerify("gameboy/fastTest.deca")
    #
    # doVerify("gameboy/base/declVar.deca")
    #
    # doVerify("gameboy/base/opArith.deca")
    #
    # doVerify("gameboy/base/opRegOverflow.deca")
    #
    # doVerify("gameboy/base/assign.deca")
    #
    # doVerify("gameboy/cond/boolLazyEval.deca")
    #
    # doVerify("gameboy/cond/ifThenElseSimple.deca")
    #
    # doVerify("gameboy/cond/ifThenElseComplex.deca")
    #
    # doVerify("gameboy/cond/whileSimple.deca")
    #
    # doVerify("gameboy/cond/whileComplex.deca")
    #
    # doVerify("gameboy/object/fields/fieldSelection.deca")
    #
    # doVerify("gameboy/object/fields/fieldInitReg.deca")
    #
    # doVerify("gameboy/object/methods/methodSimple.deca")
    #
    # doVerify("gameboy/object/methods/callSimple.deca")
    #
    # doVerify("gameboy/object/methods/callParams.deca")
    #
    # doVerify("gameboy/object/methods/callReturn.deca")

    # doVerify("gameboy/object/this/thisSimple.deca")

    # doVerify("gameboy/object/this/noThisAccess.deca")

    #
    # doVerify("gameboy/object/fields/fieldInitFieldSimple.deca")
    #
    # doVerify("gameboy/object/fields/fieldInitMethod.deca")

    # doVerify("gameboy/object/fields/fieldInitFieldComplex.deca")

    # doVerify("gameboy/object/methods/varInMethod.deca")
    #
    # doVerify("gameboy/object/methods/recursiveMethod.deca")
    #
    # doVerify("gameboy/object/methods/methodCallMethod.deca")
    #
    # doVerify("gameboy/object/extends/extendsFieldSimple.deca")
    #
    # doVerify("gameboy/object/extends/extendsFields.deca")
    #
    # doVerify("gameboy/object/extends/extendsMethods.deca")
    #
    # doVerify("gameboy/object/polymorphisms/redefinedMethodOrder.deca")
    #
    # doVerify("gameboy/object/polymorphisms/fieldRedef.deca")
    #
    # doVerify("gameboy/object/polymorphisms/ex_Video5_Page11.deca")
    #
    # doVerify("gameboy/object/miscellaneous/equalsSimple.deca")
    #
    # doVerify("gameboy/object/miscellaneous/assignInside.deca")
    #
    # doVerify("gameboy/object/miscellaneous/asmSimple.deca")

    # doVerify("gameboy/object/miscellaneous/methodRegOverflow.deca")

    return 0


def main():
    os.chdir(os.getcwd().split("src")[0])

    doTests()


if __name__ == '__main__':
    os.system("pkill -f Emulicious.jar")
    main()
    print()
    input("Input to Exit...")
    os.system("pkill -f Emulicious.jar")
