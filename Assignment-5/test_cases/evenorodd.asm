	.data
n:
	11
	.text
main:
	load %x0, $n, %x3
	divi %x3, 2, %x3
	beq %x0, %x31, even
	sub %x10, %x10, %x10
	addi %x10, 1, %x10
	end
even:
	sub %x10, $x10, %x10
	subi %x10, 1, %x10
	end
