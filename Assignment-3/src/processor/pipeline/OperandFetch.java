package processor.pipeline;

import generic.Instruction;
import generic.Operand;
import processor.Processor;

import java.nio.ByteBuffer;

import generic.Instruction.OperationType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	//Method to find two's complement
	public static String twoscomplement(StringBuffer str)
	{
		int n = str.length();

		int i;
		for (i = n-1 ; i >= 0 ; i--)
			if (str.charAt(i) == '1')
				break;

		if (i == -1)
			return "1" + str;

		for (int k = i-1 ; k >= 0; k--)
		{
			//Flipping the values
			if (str.charAt(k) == '1')
				str.replace(k, k+1, "0");
			else
				str.replace(k, k+1, "1");
		}

		// return the 2's complement
		return str.toString();
	}

	public static String toBinary(int x, int len){
		if (len > 0) {
			return String.format("%" + len + "s",
					Integer.toBinaryString(x)).replace(" ", "0");
		}
		return null;
	}

	public static int toInteger(String binary){
		if(binary.charAt(0) == '1'){
			StringBuffer bufferBinary = new StringBuffer();
			bufferBinary.append(binary);
			binary = "-" + twoscomplement(bufferBinary);
		}
		else{
			binary = "+" + binary;
		}
		return Integer.parseInt(binary, 2);
	}

	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			int Instruction = IF_OF_Latch.getInstruction();
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			String binaryInstruction =  toBinary(Instruction, 32);

			OperationType[] operationTypes = OperationType.values(); //getting all operation types from OperationType enum
			int opCodeInt = Integer.parseInt(binaryInstruction.substring(0, 5), 2);
			OperationType currentOperation = operationTypes[opCodeInt];

			Instruction currentInstruction = new Instruction();
			Operand rs1 = new Operand();
			Operand rs2 = new Operand();
			Operand rd = new Operand();
			Operand jump = new Operand();
			Operand imm = new Operand();
			int registerSource1 = -1;
			int registerSource2 = -1;
			int registerDestination = -1;
			int immediate = -1;
			currentInstruction.setProgramCounter(currentPC);
			currentInstruction.setOperationType(currentOperation);
			switch(currentOperation){
				//below till break correspond to R3I type instructions
				case add:
				case sub:
				case mul:
				case div:
				case and:
				case or:
				case xor:
				case slt:
				case sll:
				case srl:
				case sra:
					rs1.setOperandType(Operand.OperandType.Register);
					registerSource1 = Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
					rs1.setValue(registerSource1);
					currentInstruction.setSourceOperand1(rs1);

					rs2.setOperandType(Operand.OperandType.Register);
					registerSource2 = Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
					rs2.setValue(registerSource2);
					currentInstruction.setSourceOperand2(rs2);

					rd.setOperandType(Operand.OperandType.Register);
					registerDestination = Integer.parseInt((binaryInstruction.substring(15, 20)), 2);
					rd.setValue(registerDestination);
					currentInstruction.setDestinationOperand(rd);
					break;
				//below code till break corresponds to RI type, specifically for jmp
				case jmp:
					registerDestination = Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
					immediate = toInteger(binaryInstruction.substring(10, 32));
					if(immediate != 0){
						jump.setOperandType(Operand.OperandType.Immediate);
						jump.setValue(immediate);
					}
					else{
						jump.setOperandType(Operand.OperandType.Register);
						jump.setValue(registerDestination);
					}
					currentInstruction.setDestinationOperand(jump);
					break;
				//below code till break corresponds to RI type, specifically for end
				case end:
					break;
				//below code till break corresponds to R2I type instructions, specifically for beq, bnq, blt and bgt
				case beq:
				case bne:
				case blt:
				case bgt:
					rs1.setOperandType(Operand.OperandType.Register);
					registerSource1 = Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
					rs1.setValue(registerSource1);
					currentInstruction.setSourceOperand1(rs1);

					rs2.setOperandType(Operand.OperandType.Register);
					registerSource2 = Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
					rs2.setValue(registerSource2);
					currentInstruction.setSourceOperand2(rs2);

					imm.setOperandType(Operand.OperandType.Immediate);
					immediate = toInteger(binaryInstruction.substring(15, 32));
					imm.setValue(immediate);
					currentInstruction.setDestinationOperand(imm);
					break;
				//below code till break corresponds to all remaining R2I type instructions
				default:
					rs1.setOperandType(Operand.OperandType.Register);
					registerSource1 = Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
					rs1.setValue(registerSource1);
					currentInstruction.setSourceOperand1(rs1);

					rs2.setOperandType(Operand.OperandType.Immediate);
					immediate = toInteger(binaryInstruction.substring(15, 32));
					rs2.setValue(immediate);
					currentInstruction.setSourceOperand2(rs2);

					rd.setOperandType(Operand.OperandType.Register);
					registerDestination = Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
					rd.setValue(registerDestination);
					currentInstruction.setDestinationOperand(rd);
					break;
			}

			OF_EX_Latch.setInstruction(currentInstruction);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);

			System.out.println("\nOF Stage");
			System.out.println("Instruction = " + binaryInstruction);
			if(registerSource1 != -1)
				System.out.println("rs1 = " + registerSource1);
			if(registerSource2 != -1)
				System.out.println("rs2 = " + registerSource2);
			if(registerDestination != -1)
				System.out.println("rd = " + registerDestination);
			if(immediate != -1)
				System.out.println("imm = " + immediate);
		}
	}

}
