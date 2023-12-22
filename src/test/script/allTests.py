import os
import subprocess

os.chdir("../../../")

print("=========== printString.deca ===========")
os.system("./src/main/bin/decac ./src/test/deca/printString.deca")
out = subprocess.check_output("./global/bin/ima ./src/test/deca/printString.ass", shell=True)
assert (out == b"Hello World ! Second Argument\n"
               b"Second Println\n"
               b"Print Normal 1, Print Normal 2\n")

print("=========== printIntFloat.deca ===========")
os.system("./src/main/bin/decac ./src/test/deca/printIntFloat.deca")
out = subprocess.check_output("./global/bin/ima ./src/test/deca/printIntFloat.ass", shell=True)
assert (out == b"Chaine de Int : 1 2 42 -1 0 -42\n"
               b"Chaine de Float : 1.22000e+00 -4.24242e+01 0.00000e+003.1416 -2.78000e+00\n")

print("=========== variableDeclaration.deca ===========")
os.system("./src/main/bin/decac ./src/test/deca/variableDeclaration.deca")
out = subprocess.check_output("./global/bin/ima ./src/test/deca/variableDeclaration.ass", shell=True)
print(out)
