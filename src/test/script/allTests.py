import os
import subprocess


def doVerify(decaFilePath, expectedResult, doAssert=True):
    print(f"=========== {decaFilePath} ===========")
    os.system(f"./src/main/bin/decac ./src/test/deca/{decaFilePath}")
    extIndex = decaFilePath.rfind(".");
    out = subprocess.check_output(f"./global/bin/ima ./src/test/deca/{decaFilePath[:extIndex]}.ass", shell=True)
    if doAssert:
        assert (out == expectedResult)
    else:
        print(out)


os.chdir("../../../")

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

# doVerify("variableDeclarationEasy.deca",
#          "TODO",
#          False
#          )

# doVerify("variableDeclaration.deca",
#          "TODO",
#          False
#          )

doVerify("opArith.deca",
         "TODO",
         False
         )
