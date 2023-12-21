import os
import subprocess

os.chdir("../../../")

print("=========== printString.deca ===========")
os.system("./src/main/bin/decac ./src/test/deca/syntax/valid/printString.deca")
out = subprocess.check_output("./global/bin/ima ./src/test/deca/syntax/valid/printString.ass", shell=True)
assert (out == b'Hello World ! Second Argument\n'
               b'Second Println\n'
               b'Print Normal 1, Print Normal 2\n')

print("=========== printIntFloat.deca ===========")
os.system("./src/main/bin/decac ./src/test/deca/syntax/valid/printIntFloat.deca")
out = subprocess.check_output("./global/bin/ima ./src/test/deca/syntax/valid/printIntFloat.ass", shell=True)
print(out)
