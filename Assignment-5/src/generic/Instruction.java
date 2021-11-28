package generic;

public class Instruction {
	
	public enum OperationType {add, addi, sub, subi, mul, muli, div, divi, and, andi, or, ori, xor, xori, slt, slti, sll, slli, srl, srli, sra, srai, load, store, jmp, beq, bne, blt, bgt, end};
	
	int programCounter;
	OperationType operationType;
	Operand sourceOperand1;
	Operand sourceOperand2;
	Operand destinationOperand;
	
	public int getProgramCounter() {
		return programCounter;
	}
	public void setProgramCounter(int programCounter) {
		this.programCounter = programCounter;
	}
	public OperationType getOperationType() {
		return operationType;
	}
	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}
	public Operand getSourceOperand1() {
		return sourceOperand1;
	}
	public void setSourceOperand1(Operand sourceOperand1) {
		this.sourceOperand1 = sourceOperand1;
	}
	public Operand getSourceOperand2() {
		return sourceOperand2;
	}
	public void setSourceOperand2(Operand sourceOperand2) {
		this.sourceOperand2 = sourceOperand2;
	}
	public Operand getDestinationOperand() {
		return destinationOperand;
	}
	public void setDestinationOperand(Operand destinationOperand) {
		this.destinationOperand = destinationOperand;
	}	
	public String toString()
	{
		if(sourceOperand1 != null)
		{
			if(sourceOperand2 != null)
			{
				if(destinationOperand != null)
				{
					return "PC="+ programCounter + "\t" + operationType + "\t" + sourceOperand1 + "\t" + sourceOperand2 + "\t" + destinationOperand + "\n";
				}
				else
				{
					return "PC="+ programCounter + "\t" + operationType + "\t" + sourceOperand1 + "\t" + sourceOperand2 + "\tnull" + "\n";
				}
			}
			else
			{
				if(destinationOperand != null)
				{
					return "PC="+ programCounter + "\t" + operationType + "\t" + sourceOperand1 + "\tnull" + "\t" + destinationOperand + "\n";
				}
				else
				{
					return "PC="+ programCounter + "\t" + operationType + "\t" + sourceOperand1 + "\tnull" + "\tnull" + "\n";
				}
			}
		}
		else
		{
			if(sourceOperand2 != null)
			{
				if(destinationOperand != null)
				{
					return "PC="+ programCounter + "\t" + operationType + "\tnull" + "\t" + sourceOperand2 + "\t" + destinationOperand + "\n";
				}
				else
				{
					return "PC="+ programCounter + "\t" + operationType + "\tnull" + "\t" + sourceOperand2 + "\tnull" + "\n";
				}
			}
			else
			{
				if(destinationOperand != null)
				{
					return "PC="+ programCounter + "\t" + operationType + "\tnull" + "\tnull" + "\t" + destinationOperand + "\n";
				}
				else
				{
					return "PC="+ programCounter + "\t" + operationType + "\tnull" + "\tnull" + "\tnull" + "\n";
				}
			}
		}
	}
}
