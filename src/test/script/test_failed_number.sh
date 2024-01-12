#! /bin/sh

n=$(expr $1 + 1);
file=$(sed -n "$n"p failed_files);
echo $file;
./launchers/test_context $file;