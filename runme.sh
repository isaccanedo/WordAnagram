#!/bin/bash

if test -e "Solution.java";
then
javac Solution.java && java Solution
else
echo 'Java source file is missing.'
fi