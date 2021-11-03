	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x3	;x3 = n
	add %x0, %x0, %x4	;x4 = 0 [x4 is index i]
loop1:
	beq %x4, %x3, out1	;loop condition check [if x3 == x4 then out1]
	addi %x4, 0, %x5	;x5 = x4 [x5 is max]
	addi %x4, 0, %x6	;x6 = x4 [x6 is index j]
loop2:
	beq %x6, %x3, out2	;loop condition check [if x3 == x6 then out2]
	load %x6, $a, %x7	;x7 = a[x6] = a[j]
	load %x5, $a, %x8	;x8 = a[x5] = a[max]
	bgt %x7, %x8, gtr	;if x7 > x8 then gtr
	addi %x6, 1, %x6	;j += 1
	jmp loop2			;loop2
gtr:
	addi %x6, 0, %x5	;x5 = x6 + 0 -> max = j
	addi %x6, 1, %x6	;j += 1
	jmp loop2			;loop2
out2:
	load %x5, $a, %x8   ;This begins swap x8 = a[x5]
	load %x4, $a, %x9	;x9 = a[x4]
	addi %x9, 0, x11	;x11 = x9
	addi %x8, 0, %x9	;x9 = x8
	addi %x11, 0, x8	;x8 = x11
	store %x8, $a, %x5	;a[x5] = x8
	store %x9, $a, %x4	;a[x4] = x9
	addi %x4, 1, %x4	;i += 1
	jmp loop1			;loop1
out1:
	end