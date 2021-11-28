	.data
n:
	5
l:
	2
	-1
	7
	5
	3
	.text
main:
	load %x0, $n, %x3
	subi %x0, 1, %x4
	add %x0, %x0, %x5
	add %x0, %x0, %x6
loop:
	beq %x5, %x3, endl
	load %x5, $l, %x7
	bgt %x7, %x4, inter
	addi %x5, 1, %x5
	jmp loop
inter:
	divi %x7, 2, %x20
	beq %x31, %x0, countup
	addi %x5, 1, %5
	jmp loop
countup:
	addi %x6, 1, %x6
	addi %x5, 1, %x5
	jmp loop
endl:
	add %x0, %x6, %x10
	end
