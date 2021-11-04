.data
a:
  11
	.text
main:
	load %x0, $a, %x3
	sub %x4, %x4, %x4
	divi %x3, 2, %x4
	sub %x6, %x6, %x6
	addi %x6, 2, %x6
for:
	bgt %x6, %x4, prime
	div %x3, %x6, %x7
	beq %x0, %x31, notprime
	addi %x6, 1, %x6
	jmp for
prime:
	sub %x10, %x10, %x10
	addi %x10, 1, %x10
	end
notprime:
	sub %x10, %x10, %x10
	subi %x10, 1, %x10
	end
