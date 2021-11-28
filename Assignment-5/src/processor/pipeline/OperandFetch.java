package processor.pipeline;

import generic.Instruction;
import generic.Operand;
import generic.Statistics;
import jdk.dynalink.beans.StaticClass;
import processor.Processor;

import java.util.*;

import generic.Instruction.OperationType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
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

	//enum ConflictOperations {add, addi, sub, subi, mul, muli, div, divi, and, andi, or, ori, xor, xori, slt, slti, sll, slli, srl, srli, sra, srai, load, store};

	public boolean isConflict(int rs1, int rs2){

		boolean returnValue;

		// Creating Conflict Operations Set
		Set<String> ConflictInstructions = new HashSet<String>();
		//Adding Instructions that can cause conflict
		ConflictInstructions.add("add");
		ConflictInstructions.add("addi");
		ConflictInstructions.add("sub");
		ConflictInstructions.add("subi");
		ConflictInstructions.add("mul");
		ConflictInstructions.add("muli");
		ConflictInstructions.add("div");
		ConflictInstructions.add("divi");
		ConflictInstructions.add("and");
		ConflictInstructions.add("andi");
		ConflictInstructions.add("or");
		ConflictInstructions.add("ori");
		ConflictInstructions.add("xor");
		ConflictInstructions.add("xori");
		ConflictInstructions.add("slt");
		ConflictInstructions.add("slti");
		ConflictInstructions.add("sll");
		ConflictInstructions.add("slli");
		ConflictInstructions.add("srl");
		ConflictInstructions.add("srli");
		ConflictInstructions.add("sra");
		ConflictInstructions.add("srai");
		ConflictInstructions.add("load");
		ConflictInstructions.add("store");

		//Creating set of division instructions
		Set<String> DivisionInstructions = new HashSet<String>();
		//Adding the division instructions
		DivisionInstructions.add("div");
		DivisionInstructions.add("divi");

		Instruction EX_Stage_Ins = OF_EX_Latch.getInstruction();
		Instruction MA_Stage_Ins = EX_MA_Latch.getInstruction();
		Instruction RW_Stage_Ins = MA_RW_Latch.getInstruction();

		//Conflict check due to RAW Hazard
		int ex_rd = -5;
		int ma_rd = -5;
		int rw_rd = -5;
		boolean isEXDiv = false;
		boolean isMADiv = false;
		boolean isRWDiv = false;
		if(EX_Stage_Ins != null) {
			String ins = EX_Stage_Ins.getOperationType().name();
			if (ConflictInstructions.contains(ins)) {
				if(EX_Stage_Ins.getDestinationOperand() != null){
					ex_rd = EX_Stage_Ins.getDestinationOperand().getValue();
				}
				if(DivisionInstructions.contains(ins)){
					isEXDiv = true;
				}
			}
		}
		if(MA_Stage_Ins != null){
			String ins = MA_Stage_Ins.getOperationType().name();
			if(ConflictInstructions.contains(ins)){
				if(MA_Stage_Ins.getDestinationOperand() != null){
					ma_rd = MA_Stage_Ins.getDestinationOperand().getValue();
				}
				if(DivisionInstructions.contains(ins)){
					isMADiv = true;
				}
			}
		}
		if(RW_Stage_Ins != null){
			String ins = RW_Stage_Ins.getOperationType().name();
			if(ConflictInstructions.contains(ins)){
				if(RW_Stage_Ins.getDestinationOperand() != null){
					rw_rd = RW_Stage_Ins.getDestinationOperand().getValue();
				}
				if(DivisionInstructions.contains(ins)){
					isRWDiv = true;
				}
			}
		}
		if(rs1 == ex_rd || rs1 == ma_rd || rs1 == rw_rd || rs2 == ex_rd || rs2 == ma_rd || rs2 == rw_rd){
			returnValue = true;
		}
		else {
			returnValue =  false;
		}

		//Conflict check due to Division
		if(isEXDiv || isMADiv || isRWDiv){
			if(rs1 == 31 || rs2 == 31){
				System.out.println("Conflict due to Division");
				returnValue = true;
			}
			else if(!returnValue){
				returnValue = false;
			}
		}
		/*System.out.println("\nConflict Checking");
		if(ex_rd != -5)
			System.out.println("EX rd = " + ex_rd);
		if(ma_rd != -5)
			System.out.println("MA rd = " + ma_rd);
		if(rw_rd != -5)
			System.out.println("RW rd = " + rw_rd);
		System.out.println("rs1 = " + rs1);
		System.out.println("rs2 = " + rs2);*/
		return returnValue;
	}

	public void conflictObserved(){
		System.out.println("Conflict and Lock");
		IF_EnableLatch.setIF_enable(false);
		OF_EX_Latch.setEX_Lock(true);
	}

	public void performOF()
	{
		if (!IF_OF_Latch.isOF_Busy()) {
			if (IF_EnableLatch.isIF_Busy()) {
				OF_EX_Latch.setEX_Lock(true);
			}
			else {
				if (IF_OF_Latch.isOF_enable()) {

					int Instruction = IF_OF_Latch.getInstruction();
					int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
					String binaryInstruction = toBinary(Instruction, 32);

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

					System.out.println("\nOF: " + currentOperation);

					//Creating set of branch instructions
					Set<String> BranchInstructions = new HashSet<String>();
					//Adding the branch instructions
					BranchInstructions.add("jmp");
					BranchInstructions.add("beq");
					BranchInstructions.add("bne");
					BranchInstructions.add("blt");
					BranchInstructions.add("bgt");

					if (BranchInstructions.contains(currentOperation.name())) {
						IF_EnableLatch.setIF_enable(false);
					}

					switch (currentOperation) {
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


							rs2.setOperandType(Operand.OperandType.Register);
							registerSource2 = Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
							rs2.setValue(registerSource2);


							rd.setOperandType(Operand.OperandType.Register);
							registerDestination = Integer.parseInt((binaryInstruction.substring(15, 20)), 2);
							rd.setValue(registerDestination);


							if (isConflict(registerSource1, registerSource2)) {
								this.conflictObserved();
								break;
							}

							currentInstruction.setSourceOperand1(rs1);
							currentInstruction.setSourceOperand2(rs2);
							currentInstruction.setDestinationOperand(rd);
							break;
						//below code till break corresponds to RI type, specifically for jmp
						case jmp:
							registerDestination = Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
							immediate = toInteger(binaryInstruction.substring(10, 32));
							if (immediate != 0) {
								jump.setOperandType(Operand.OperandType.Immediate);
								jump.setValue(immediate);
							} else {
								jump.setOperandType(Operand.OperandType.Register);
								jump.setValue(registerDestination);
							}
							currentInstruction.setDestinationOperand(jump);
							break;
						//below code till break corresponds to RI type, specifically for end
						case end:
							IF_EnableLatch.setIF_enable(false);
							break;
						//below code till break corresponds to R2I type instructions, specifically for beq, bnq, blt and bgt
						case beq:
						case bne:
						case blt:
						case bgt:
							rs1.setOperandType(Operand.OperandType.Register);
							registerSource1 = Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
							rs1.setValue(registerSource1);

							rs2.setOperandType(Operand.OperandType.Register);
							registerSource2 = Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
							rs2.setValue(registerSource2);

							imm.setOperandType(Operand.OperandType.Immediate);
							immediate = toInteger(binaryInstruction.substring(15, 32));
							imm.setValue(immediate);

							if (isConflict(registerSource1, registerSource2)) {
								this.conflictObserved();
								break;
							}

							currentInstruction.setSourceOperand1(rs1);
							currentInstruction.setSourceOperand2(rs2);
							currentInstruction.setDestinationOperand(imm);
							break;
						//below code till break corresponds to all remaining R2I type instructions
						default:
							rs1.setOperandType(Operand.OperandType.Register);
							registerSource1 = Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
							rs1.setValue(registerSource1);

							rs2.setOperandType(Operand.OperandType.Immediate);
							immediate = toInteger(binaryInstruction.substring(15, 32));
							rs2.setValue(immediate);

							rd.setOperandType(Operand.OperandType.Register);
							registerDestination = Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
							rd.setValue(registerDestination);

							if (isConflict(registerSource1, registerSource1)) {
								this.conflictObserved();
								break;
							}

							currentInstruction.setSourceOperand1(rs1);
							currentInstruction.setSourceOperand2(rs2);
							currentInstruction.setDestinationOperand(rd);
							break;
					}

					OF_EX_Latch.setInstruction(currentInstruction);
					OF_EX_Latch.setEX_enable(true);

					if (!OF_EX_Latch.isEX_Locked()) {
						IF_OF_Latch.setOF_enable(false);
					}

				/*if(immediate != -1)
					System.out.println("\nOF Stage" + " Current PC:" + currentPC + " Instruction:" + binaryInstruction + " rs1:" + registerSource1 + " rs2:" + registerSource2 + " rd:" + registerDestination + " imm:" + immediate);
				else
					System.out.println("\nOF Stage" + " Instruction:" + binaryInstruction + " rs1:" + registerSource1 + " rs2: " + registerSource2 + " rd:" + registerDestination);
				*/
				}
			}
		}
	}
}
