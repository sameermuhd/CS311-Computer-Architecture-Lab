	.data
a:
	1
d:
	3
n:
	7
	.text
main:
	load %x0, $a, $x3
	load %x0, $d, %x4
	load %x0, $n, %x5
	add %x0, %x0, %x6
	add %x0, %x3, %x7
	addi %x0, 65535, %x8
loop:
	beq %x6, %x5, endl
	store %x7, $a, %x8
	add %x7, %x4, %x7
	addi %x6, 1, %x6
	subi %x8, 1, %x8
	jmp loop
endl:
	end
