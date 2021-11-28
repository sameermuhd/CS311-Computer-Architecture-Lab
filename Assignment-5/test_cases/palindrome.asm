.data
a:
	4567654
	.text
main:
	load %x0, $a, %x3
	sub %x7, %x7, %x7
loop:
	divi %x3, 10, %x4
	addi %x31, 0, %x30
	muli %x7, 10, %x7
	add %x7, %x30, %x7
	divi %x3, 10, %x3
	bgt %x3, %x0, loop
	load %x0, $a, %x5
	beq %x5, %x7, palindrome
	sub %x10, %x10, %x10
	subi %x10, 1, %x10
	end
palindrome:
	sub %x10, %x10, %x10
	addi %x10, 1, %x10
	end