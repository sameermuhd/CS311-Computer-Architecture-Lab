.data
n:
	10
	.text
main:
	addi %x0, 0, %x3
	addi %x0, 1, %x4
	add %x3, %x4, %x5
	load %x0, $n, %x6
	addi %x0, 65535, %x7
	addi %x0, 0, %x8
	store %x3, 0, %x7	
	subi %x7, 1, %x7
	addi %x8, 1, %x8
	store %x4, 0, %x7	
	subi %x7, 1, %x7
	addi %x8, 1, %x8
for:
	blt %x8, %x6, loop
	end
loop:
	add %x3, %x4, %x5
	store %x5, 0, %x7	
	subi %x7, 1, %x7
	addi %x8, 1, %x8
	add %x0, %x4, %x3
	add %x0, %x5, %x4
	jmp for