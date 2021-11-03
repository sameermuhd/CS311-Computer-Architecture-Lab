	.data
a:
	10
	.text
main:
	load %x0, $a, %x3	;Value of a is loaded into Register 3
	add %x0, %x0, %x4	;Register 4 now has value 0
loop:
	beq %x3, %x0, check	;This loop basically stores a number equivalent to reverse of a in Register 4. This line Jumps to check if Register 3 is 0
	muli %x4, 10, %x4	;Multiply Value in Register 4 by 10
	divi %x3, 10, %x3	;Divide Register 3 value by 10
	add %x31, %x4, %x4	;Add remainder when x3 is divided by 10 to x4
	jmp loop			;Jump to beginning of loop
check:
	load %x0, $a, %x3	;check checks the reversed value of a with a itself to see if they matchLoad value of a into register 3
	xor %x3, %x4, %x5	;Compare reversed value of a with value of a
	beq %x5, %x0, success	;Jump to success if they match
fail:
	subi %x0, 1, %x10	;fail is executed if a is not a palindrome. Store -1 in Register 10
	end					;End the program
success:
	addi %x0, 1, %x10	;This part is executed if a is a palindrome. Store 1 in Register 10
	end					;End the program