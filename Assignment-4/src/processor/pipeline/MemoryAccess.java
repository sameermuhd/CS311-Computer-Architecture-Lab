package processor.pipeline;

import generic.Instruction;
import processor.Processor;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_Enable_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_Enable_Latch;
	}
	
	public void performMA()
	{
		if(EX_MA_Latch.isMA_Locked()){
			MA_RW_Latch.setRW_Lock(true);
			MA_RW_Latch.setInstruction(null);
			EX_MA_Latch.setMA_Lock(false);
		}
		else if(EX_MA_Latch.isMA_enable()){
			Instruction currentInstruction = EX_MA_Latch.getInstruction();
			int aluResult = EX_MA_Latch.getAluResult();
			OperationType currentOperation = currentInstruction.getOperationType();
			int currentPC = currentInstruction.getProgramCounter();

			if(currentOperation == OperationType.load){
				int ldResult = containingProcessor.getMainMemory().getWord(aluResult);
				MA_RW_Latch.setLdResult(ldResult);
				System.out.println("\nMA Stage: " + " Current PC: " + currentPC + " Operation: " + currentOperation.name() + " ld Result: " + ldResult);
			}
			else if(currentOperation == OperationType.store){
				int stWord = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
				containingProcessor.getMainMemory().setWord(aluResult, stWord);
				System.out.println("\nMA Stage: " + " Current PC: " + currentPC + " Operation: " + currentOperation.name() + " Storing: " + stWord + " into Memory location: " + aluResult);
			}
			else if(currentOperation == OperationType.end){
				IF_EnableLatch.setIF_enable(false);
			}
			MA_RW_Latch.setAluResult(aluResult);
			MA_RW_Latch.setInstruction(currentInstruction);
			MA_RW_Latch.setRW_enable(true);
		}
	}
}
