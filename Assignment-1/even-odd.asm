	.data
a:
	10
	.text
main:
	load %x0, $a, %x3    ;loading the input into x3
	divi %x3, 2, %x4     ;x4 = x3 / 2
	beq %x31, 0, even    ;if x3 % 2 == 0, jump to even
	jmp odd     		 ;else jump to odd
even:
	subi %x0, 1, %x10    ;writing -1 to x10 if even and end
	end
odd:
	addi %x0, 1, %x10    ;writing 1 to x10 if odd and end
	end
