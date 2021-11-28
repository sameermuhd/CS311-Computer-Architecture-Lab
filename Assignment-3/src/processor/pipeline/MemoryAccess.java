package processor.pipeline;

import generic.Instruction;
import processor.Processor;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		if(EX_MA_Latch.isMA_enable()){
			Instruction currentInstruction = EX_MA_Latch.getInstruction();
			int aluResult = EX_MA_Latch.getAluResult();
			OperationType currentOperation = currentInstruction.getOperationType();

			System.out.println("\nMA Stage");
			if(currentOperation == OperationType.load){
				System.out.println("Operation = " + currentOperation.name());
				int ldResult = containingProcessor.getMainMemory().getWord(aluResult);
				MA_RW_Latch.setLdResult(ldResult);
				System.out.println("ld Result = " + ldResult);
			}
			else if(currentOperation == OperationType.store){
				System.out.println("Operation = " + currentOperation.name());
				int stWord = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
				containingProcessor.getMainMemory().setWord(aluResult, stWord);
				System.out.println("Storing = " + stWord + " into Memory location = " + aluResult);
			}
			MA_RW_Latch.setAluResult(aluResult);
			MA_RW_Latch.setInstruction(currentInstruction);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
		}
	}
}
