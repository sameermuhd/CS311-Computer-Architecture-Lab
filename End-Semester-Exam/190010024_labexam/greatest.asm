	.data
a:
	10
b:
	30
c:
	20
	.text
main:
	load %x0, $a, %x3
	load %x0, $b, %x4
	load %x0, $c, %x5
	add %x0, %x0, %x6
	bgt %x3, %x4, loopMaxa
	jmp loopMaxb
loopMaxa:
	add %x0, %x3, %x6
	jmp continue
loopMaxb:
	add %x0, %x4, %x6
	jmp continue
continue:
	bgt %x6, %x5, endf
	add %x0, %x5, %x6
	jmp endf
endf:
	add %x0, %x6, %x10
	end
