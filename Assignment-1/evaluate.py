#!/bin/python

import os
import shutil
import sys
import subprocess
from threading import Timer

def evaluate(submitted_program):

	jarfile = "./emulator.jar"
	testcases_directory = "./test_cases/"

	l = len(submitted_program.split("/"))
	program_name = submitted_program.split("/")[l-1].split("_")[0].split(".")[0]
	total_marks = 0
	scored_marks = 0
	for cur_file in os.listdir(testcases_directory):
		if program_name in cur_file and ".input" in cur_file:
			total_marks = total_marks + 1
#find starting and ending address
			expected_file = open(testcases_directory + cur_file.split(".")[0] + ".expected")
			starting_address = "x"
			ending_address = "x"
			memory_required = False
			for line in expected_file:
				if memory_required == True:
					if len(line.split(":")) > 1:
						if starting_address == "x":
							starting_address = line.split(":")[0].split()[0]
						else:
							ending_address = line.split(":")[0].split()[0]
				if "Main Memory Contents" in line:
					memory_required = True
			expected_file.close()

			if starting_address == "x":
				starting_address = "0"
				ending_address = "0"
			if ending_address == "x":
				ending_address = starting_address
#create new assembly file based on test case
			new_assembly_file = open("./tmp.asm", 'w')
			input_file = open(testcases_directory + cur_file)
			for line in input_file:
				new_assembly_file.write(line)
			input_file.close()

			text_encountered = False
			submitted_file = open(submitted_program)
			for line in submitted_file:
				if ".text" in line:
					text_encountered = True
				if text_encountered == True:
					new_assembly_file.write(line)
			submitted_file.close()
			new_assembly_file.close()
# spawn emulator
			stdout_file = open("./tmp.output", 'w')
			popen_args = ["java", "-Xmx1g", "-jar", jarfile, "./tmp.asm", starting_address, ending_address]
			proc = subprocess.Popen(popen_args, stdout = stdout_file, stderr = stdout_file)
			timer = Timer(5, proc.kill)
			try:
				timer.start()
				stdout, stderr = proc.communicate()
			finally:
				timer.cancel()
			stdout_file.close()
# evaluate against expected output
			expected_file = open(testcases_directory + cur_file.split(".")[0] + ".expected")
			result_file = open("./tmp.output")
			expected_line = expected_file.readline()
			first_line_found = False
			evaluation = True
			for line in result_file:
				if first_line_found == True and line != expected_line:
					evaluation = False
					break
				if expected_line == line:
					first_line_found = True
					expected_line = expected_file.readline()
					if expected_line == None or expected_line == "":
						break
			if first_line_found == False:
				evaluation = False

			if evaluation == True:
				scored_marks = scored_marks + 1
				print(cur_file + "\t: PASS!")
			else:
				print(cur_file + "\t: Fail")
#debug print
			debug = False
			if debug == True:
				print("testcase = " + cur_file)
				print("\n output expected to contain = ")
				expected_file = open(testcases_directory + cur_file.split(".")[0] + ".expected")
				for line in expected_file:
					print(line)
				expected_file.close()
				print("\n obtained output = ")
				output_file = open("./tmp.output")
				for line in output_file:
					print(line)
				output_file.close()

			os.remove("./tmp.asm")
			os.remove("./tmp.output")

	return [total_marks, scored_marks]
