#!/bin/python

import sys
import os
import zipfile
import shutil
import filecmp
import subprocess
from threading import Timer

zip_file = sys.argv[1]

l = len(zip_file.split("/"))
print "Students :\t" + zip_file.split("/")[l-1].split("_")[0] + " and " + zip_file.split("/")[l-1].split("_")[1].split(".")[0]
print ""

submissions_temp_dir = "./submissions/" 

if not os.path.exists(submissions_temp_dir):
	os.mkdir(submissions_temp_dir)

zip_ref = zipfile.ZipFile(zip_file, 'r')
zip_ref.extractall(submissions_temp_dir)
zip_ref.close()

shutil.copyfile("build.xml", submissions_temp_dir + "/build.xml")

os.chdir(submissions_temp_dir)

stdout_file = open("./tmp.output", 'a')
popen_args = ["ant", "make-jar"]
proc = subprocess.Popen(popen_args, stdout = stdout_file, stderr = stdout_file)
timer = Timer(5, proc.kill)
try:
	timer.start()
	stdout, stderr = proc.communicate()
finally:
	timer.cancel()
stdout_file.close()

if not os.path.exists("jars/assembler.jar"):
	print "compilation failed. jar file not created"
	sys.exit(0)

test_cases_dir = "../test_cases"
total_marks = 0
scored_marks = 0
for testcase in os.listdir(test_cases_dir):
	if ".asm" in testcase:
		total_marks = total_marks + 1

		stdout_file = open("./tmp.output", 'a')
		popen_args = ["java", "-Xmx1g", "-jar", "jars/assembler.jar", test_cases_dir + "/" + testcase, "./" + testcase.split(".")[0] + ".observedoutput"]
		proc = subprocess.Popen(popen_args, stdout = stdout_file, stderr = stdout_file)
		timer = Timer(5, proc.kill)
		try:
			timer.start()
			stdout, stderr = proc.communicate()
		finally:
			timer.cancel()
		stdout_file.close()

		if os.path.exists("./" + testcase.split(".")[0] + ".observedoutput"):
			if filecmp.cmp(test_cases_dir + "/" + testcase.split(".")[0] + ".out", "./" + testcase.split(".")[0] + ".observedoutput") == True:
				scored_marks = scored_marks + 1
				print testcase + " : PASS!"
			else:
				print testcase + " : fail - incorrect object file contents"
		else:
			print testcase + " : fail - file not created"

os.chdir("..")

shutil.rmtree(submissions_temp_dir)

print "\ntotal score = " + str(scored_marks) + " out of " + str(total_marks)
