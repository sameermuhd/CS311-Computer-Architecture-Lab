package generic;

import java.io.FileInputStream;
import java.io.*;
import java.nio.ByteBuffer;
import java.io.IOException;

import generic.Operand.OperandType;

import javax.swing.*;

public class Simulator {

	static FileInputStream inputcodeStream = null;

	public static String toBinaryConv(int x, int len){
		if (len > 0) {
			return String.format("%" + len + "s",
					Integer.toBinaryString(x)).replace(" ", "0");
		}
		return null;
	}
	public static String toBinary(Operand ins, int len) {
		int int_ins;
		if(ins == null){
			int_ins = 0;
		}
		else if(ins.getOperandType() == OperandType.Label){
			int_ins = ParsedProgram.symtab.get(ins.getLabelValue());
		}
		else{
			int_ins = ins.getValue();
		}

		return toBinaryConv(int_ins, len);
	}

	public static void setupSimulation(String assemblyProgramFile) {
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	public static void assemble(String objectProgramFile) {
		try {
			//1. open the objectProgramFile in binary mode
			OutputStream outputFile = new FileOutputStream(objectProgramFile);
			BufferedOutputStream outputcodeStream = new BufferedOutputStream(outputFile);

			//2. write the firstCodeAddress to the file
			byte[] byte_addressCode = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
			outputcodeStream.write(byte_addressCode);

			//3. write the data to the file
			for (Integer temp_data : ParsedProgram.data) {
				byte[] byte_data = ByteBuffer.allocate(4).putInt(temp_data).array();
				outputcodeStream.write(byte_data);
			}

			//4. assemble one instruction at a time, and write to the file
			for (Instruction current_ins : ParsedProgram.code) {
				String binary_ins = "";
				boolean r3_type = false;
				boolean r2i_type = false;
				boolean ri_type = false;
				String opCode;
				Instruction.OperationType ins_string = current_ins.getOperationType();
				switch (ins_string.name()) {
					case "add":
						r3_type = true;
						opCode = "00000";
						break;
					case "addi":
						r2i_type = true;
						opCode = "00001";
						break;
					case "sub":
						r3_type = true;
						opCode = "00010";
						break;
					case "subi":
						r2i_type = true;
						opCode = "00011";
						break;
					case "mul":
						r3_type = true;
						opCode = "00100";
						break;
					case "muli":
						r2i_type = true;
						opCode = "00101";
						break;
					case "div":
						r3_type = true;
						opCode = "00110";
						break;
					case "divi":
						r2i_type = true;
						opCode = "00111";
						break;
					case "and":
						r3_type = true;
						opCode = "01000";
						break;
					case "andi":
						r2i_type = true;
						opCode = "01001";
						break;
					case "or":
						r3_type = true;
						opCode = "01010";
						break;
					case "ori":
						r2i_type = true;
						opCode = "01011";
						break;
					case "xor":
						r3_type = true;
						opCode = "01100";
						break;
					case "xori":
						r2i_type = true;
						opCode = "01101";
						break;
					case "slt":
						r3_type = true;
						opCode = "01110";
						break;
					case "slti":
						r2i_type = true;
						opCode = "01111";
						break;
					case "sll":
						r3_type = true;
						opCode = "10000";
						break;
					case "slli":
						r2i_type = true;
						opCode = "10001";
						break;
					case "srl":
						r3_type = true;
						opCode = "10010";
						break;
					case "srli":
						r2i_type = true;
						opCode = "10011";
						break;
					case "sra":
						r3_type = true;
						opCode = "10100";
						break;
					case "srai":
						r2i_type = true;
						opCode = "10101";
						break;
					case "load":
						r2i_type = true;
						opCode = "10110";
						break;
					case "store":
						r2i_type = true;
						opCode = "10111";
						break;
					case "jmp":
						ri_type = true;
						opCode = "11000";
						break;
					case "beq":
						r2i_type = true;
						opCode = "11001";
						break;
					case "bne":
						r2i_type = true;
						opCode = "11010";
						break;
					case "blt":
						r2i_type = true;
						opCode = "11011";
						break;
					case "bgt":
						r2i_type = true;
						opCode = "11100";
						break;
					case "end":
						ri_type = true;
						opCode = "11101";
						break;
					default:
						opCode = "";
						break;
				}
				if (r3_type) {
					binary_ins += opCode;
					Operand rs1 = current_ins.getSourceOperand1();
					Operand rs2 = current_ins.getSourceOperand2();
					Operand rd = current_ins.getDestinationOperand();

					String rd_value = toBinary(rd, 5);
					String rs1_value = toBinary(rs1, 5);
					String rs2_value = toBinary(rs2, 5);
					String unused_bits = toBinaryConv(0, 12);

					binary_ins += (rs1_value + rs2_value + rd_value + unused_bits);
				}
				else if (r2i_type) {
					binary_ins += opCode;
					int pc = current_ins.getProgramCounter();
					Operand rs1 = current_ins.getSourceOperand1();
					Operand rs2 = current_ins.getSourceOperand2();
					Operand rd = current_ins.getDestinationOperand();

					String rs1_value = toBinary(rs1, 5);
					String imm_value;
					String rs2_value;
					String rd_value;
					if (opCode.equals("11001") || opCode.equals("11010") || opCode.equals("11011") || opCode.equals("11100")) {
						rs2_value = toBinary(rs2, 5);
						imm_value = toBinary(rd, 5);
						assert imm_value != null;
						int imm_value_int = Integer.parseInt(imm_value, 2) - pc;
						String imm_temp = toBinaryConv(imm_value_int, 17);
						String imm_value2 = imm_temp.substring(imm_temp.length() - 17);
						binary_ins += (rs1_value + rs2_value + imm_value2);
					}
					else{
						rs2_value = toBinary(rs2, 17);
						rd_value = toBinary(rd, 5);
						binary_ins += (rs1_value + rd_value + rs2_value);
					}
				}
				else if (ri_type) {
					binary_ins += opCode;
					Operand rd = current_ins.getDestinationOperand();
					int pc = current_ins.getProgramCounter();

					if (opCode.equals("11000")) {
						String unused_bits = toBinaryConv(0, 5);
						String rd_value = toBinary(rd, 5);
						assert rd_value != null;
						int rd_value_int = Integer.parseInt(rd_value, 2) - pc;
						String rd_temp = toBinaryConv(rd_value_int, 22);
						String rd_value2 = rd_temp.substring(rd_temp.length() - 22);
						binary_ins += (unused_bits + rd_value2);
					}
					else if (opCode.equals("11101")) {
						String unused_bits = toBinaryConv(0, 27);
						binary_ins += (unused_bits);
					}
				}
				else {
					continue;
				}
				int int_ins = (int) Long.parseLong(binary_ins, 2);
				byte[] instBinary = ByteBuffer.allocate(4).putInt(int_ins).array();
				outputcodeStream.write(instBinary);
			}
			//5. close the file
			outputcodeStream.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
