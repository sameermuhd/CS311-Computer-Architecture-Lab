	.data
a:
	10
	.text
main:
	load %x0, $a, %x4		;load the value of a into Register 4
	addi %x0, 2, %x3		;Store the value 2 in Register 3
	addi %x0, 1, %x10		;Store the value 1 in Register 10
loop:
	beq %x3, %x4, success	;Then we loop until the value a-1. If any number divides a, then we jump to fail. We jump to success if no number less than a divides a-1.
	div %x4, %x3, %x5		;Divide a with value in Register 4
	beq %x0, %x31, fail		;If the remainder is 0, then jumpt to fail
	addi %x3, 1, %x3		;Increase Register 3 value by 1
	jmp loop				;Jump to loop again
fail:
	subi %x10, 2, %x10		;Subract 2 from Register 10 (Has 1 stored in it) if there exists a divisor
	end						;End the program
success:
	end						;End the program
