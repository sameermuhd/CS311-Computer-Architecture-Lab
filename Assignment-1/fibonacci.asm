	.data
n:
	10
	.text
main:
	load %x0, $n, %x6	;x6 = n
	addi %x0, 0, %x3	;x3 = 0 [F0]
	addi %x0, 1, %x4	;x4 = 1 [F1]
	addi %x0, 0, %x5	;x5 = index i = 0
	addi %x0, 65535, %x8	;starting address x8 = 65535 = 2^16 -1
	addi %x0, 0, %x10	;stores 0
	addi %x0, 1, %x11	;stores 1
loop:
	beq %x5, %x6, endl	;loop condition check if x5 == x6, endl
	beq %x5, %x10, b0	;if x5 == x10, then b0
	beq %x5, %x11, b1	;if x5 == x11, then b1
	jmp loopcont
b0:
	store %x3, $n, %x8	;n[x8] = x3
	jmp loopup
b1:
	store %x4, $n, %x8	;n[x8] = x3
	jmp loopup
loopcont:
	add %x4, $x3, %x7	;x7 = Fi = x4 + x3 = F1 + F0
	store %x7, $n, %x8	;n[x8] = x7
	addi %x4, 0, %x3	;x3 = x4
	addi %x7, 0, %x4	;x4 = x7
loopup:
	subi %x8, 1, %x8	;x8 -= 1
	addi %x5, 1, %x5	;x5 -= 1
	jmp loop
endl:
	end
