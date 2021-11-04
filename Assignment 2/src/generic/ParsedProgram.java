package generic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

public class ParsedProgram {
	static ArrayList<Instruction> code = new ArrayList<Instruction>();
	static int mainFunctionAddress;
	static int firstCodeAddress;
	static ArrayList<Integer> data = new ArrayList<Integer>();
	
	public static void setMainFunctionAddress(int addr)
	{
		mainFunctionAddress = addr;
	}
	
	public static void setFirstCodeAddress(int addr)
	{
		firstCodeAddress = addr;
	}
	
	public static Instruction getInstructionAt(int programCounter)
	{
		return code.get(programCounter - firstCodeAddress);
	}
	
	
	static HashMap<String, Integer> symtab = new HashMap<String,Integer>();
	

	public static int parseDataSection(String assemblyProgramFile)
	{
		FileInputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(assemblyProgramFile);
		}
		catch(FileNotFoundException e) {
			Misc.printErrorAndExit(e.toString());
		}
		
		Scanner sc=new Scanner(inputStream);
		int address = 0;							//Store data staring from 1st memory location. At 0th location we denote the start of the code section.
		
				
		while(sc.hasNextLine()) //TODO 3 loops?
		{			
			String line=sc.nextLine();
				
			if(line.contains(".data"))										//Processing the .data section
			{
				line=sc.next();
					
				do
				{
					if(Pattern.matches("[a-zA-Z]+([0-9]*)(:)", line))
					{
						ParsedProgram.symtab.put(line.replaceAll("[^a-zA-Z]",""), address);//TODO removed statements that were adding data to the code arraylist
																							
						while(Pattern.matches("-?\\d+", line=sc.next()))
						{
							data.add(Integer.parseInt(line));
							address++;
							
						}
					}
				}while(!line.contains(".text"));
				
				break;
			}
		}
		
		sc.close();
		
		setFirstCodeAddress(address);
					
		return address;
	}	
	
	public static void parseCodeSection(String assemblyProgramFile, int firstCodeAddress)
	{
		FileInputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(assemblyProgramFile);
		}
		catch(FileNotFoundException e) {
			Misc.printErrorAndExit(e.toString());
		}
		
		Scanner sc=new Scanner(inputStream);
		int address = firstCodeAddress;
		
		while(sc.hasNextLine())
		{
			String line=sc.nextLine();
			if(line.contains(".text"))
			{
				break;				
			}
		}
		
		while(sc.hasNextLine())
		{
			String line;
			if(Pattern.matches("[a-zA-Z]+([0-9]*)(:)", line=sc.nextLine()))
			{
				ParsedProgram.symtab.put(line.replaceAll(":",""), address);
				if(line.replaceAll(":","").compareTo("main") == 0)
				{
					ParsedProgram.setMainFunctionAddress(address);
				}
			}
			else
			{
				ParsedProgram.code.add(address-firstCodeAddress, getInstructionFromString(line, address));
				address++;
			}
		}
		sc.close();
	}
	
	private static Instruction getInstructionFromString(String line, int address)
	{
		Instruction newInstruction = new Instruction();
		newInstruction.setProgramCounter(address);
		
		Scanner sc = new Scanner(line);
		newInstruction.setOperationType(OperationType.valueOf(sc.next()));
		
		switch(newInstruction.getOperationType())
		{
			//R3I type
			case add : 
			case sub : 
			case mul : 
			case div : 
			case and : 
			case or : 
			case xor : 
			case slt : 
			case sll : 
			case srl : 
			case sra :	{
							newInstruction.setSourceOperand1(getRegisterOperandFromString(sc.next()));
							newInstruction.setSourceOperand2(getRegisterOperandFromString(sc.next()));
							newInstruction.setDestinationOperand(getRegisterOperandFromString(sc.next()));
							break;
						} 
			
			//R2I type
			case addi :
			case subi :
			case muli :
			case divi : 
			case andi : 
			case ori : 
			case xori : 
			case slti : 
			case slli : 
			case srli : 
			case srai :
			case load :
			case store :	{
								newInstruction.setSourceOperand1(getRegisterOperandFromString(sc.next()));
								String str = sc.next();
								if(Pattern.matches("-?\\d+(,)",str))
								{
									//absolute immediate
									newInstruction.setSourceOperand2(getImmediateOperandFromString(str));
								}
								else
								{
									//label / symbol
									newInstruction.setSourceOperand2(getLabelOperandFromString(str));
								}
								newInstruction.setDestinationOperand(getRegisterOperandFromString(sc.next()));
								break;
							} 
			
			case beq : 
			case bne : 
			case blt : 
			case bgt : 	{
							newInstruction.setSourceOperand1(getRegisterOperandFromString(sc.next()));
							newInstruction.setSourceOperand2(getRegisterOperandFromString(sc.next()));
							String str = sc.next();
							if(Pattern.matches("[0-9]+(,)",str))
							{
								//absolute immediate
								newInstruction.setDestinationOperand(getImmediateOperandFromString(str));
							}
							else
							{
								//label / symbol
								newInstruction.setDestinationOperand(getLabelOperandFromString(str));
							}
							break;
						}
			
			//RI type :
			case jmp :		{
								String str = sc.next();
								if(Pattern.matches("[0-9]+(,)",str))
								{
									//absolute immediate
									newInstruction.setDestinationOperand(getImmediateOperandFromString(str));
								}
								else if(Pattern.matches("%x([0-9]{1,2})",str)) {
									newInstruction.setDestinationOperand(getRegisterOperandFromString(str));
								}
								else
								{
									//label / symbol
									newInstruction.setDestinationOperand(getLabelOperandFromString(str));
								}
								break;
							}
			
			case end :	break;
				
			default: Misc.printErrorAndExit("unknown instruction!!");
		}
		
		sc.close();
		
		return newInstruction;		
	}
	
	private static Operand getRegisterOperandFromString(String str)
	{
		Operand operand = new Operand();
		operand.setOperandType(OperandType.Register);
		operand.setValue(Integer.parseInt(str.replaceAll("[^0-9]", "")));
		return operand;
	}
	
	private static Operand getImmediateOperandFromString(String str)
	{
		Operand operand = new Operand();
		operand.setOperandType(OperandType.Immediate);
		operand.setValue(Integer.parseInt(str.replaceAll("[^-?\\d+]","")));
		return operand;
	}
	
	private static Operand getLabelOperandFromString(String str)
	{
		Operand operand = new Operand();
		operand.setOperandType(OperandType.Label);
		operand.setLabelValue(str.replaceAll("[$,]", ""));
		return operand;
	}
	
	static void printState()
	{
		System.out.println("Symbol Table :");
		System.out.println(Arrays.asList(symtab));
		System.out.println("\nParsed instructions :");
		System.out.println(Arrays.asList(code));
	}
}
