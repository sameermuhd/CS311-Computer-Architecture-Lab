package processor.pipeline;

import generic.Simulator;
import generic.Statistics;
import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_Locked()){
			MA_RW_Latch.setRW_Lock(false);
		}
		else if(MA_RW_Latch.isRW_enable()) {

			MA_RW_Latch.setRW_enable(false);
			Statistics.setNumberOfRWStageInstructions(Statistics.getNumberOfRWStageInstructions() + 1);
			Instruction currentInstruction = MA_RW_Latch.getInstruction();
			System.out.println("RW: " + currentInstruction);
			OperationType currentOperation = currentInstruction.getOperationType();
			int currentPC = currentInstruction.getProgramCounter();
			int rd = -1;
			int ldResult = -1;
			int aluResult = -1;
			switch (currentOperation){
				case store:
				case jmp:
				case beq:
				case blt:
				case bgt:
					break;
				case end:
					Simulator.setSimulationComplete(true);
					break;
				case load:
					ldResult = MA_RW_Latch.getLdResult();
					rd = currentInstruction.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd, ldResult);
					break;
				default:
					rd = currentInstruction.getDestinationOperand().getValue();
					aluResult = MA_RW_Latch.getAluResult();
					containingProcessor.getRegisterFile().setValue(rd, aluResult);
					break;
			}
			if(currentOperation != OperationType.end){
				IF_EnableLatch.setIF_enable(true);
			}

			/*if(rd != -1){
				if(ldResult != -1)
					System.out.println("\nRW Stage " + "Current PC: " + currentPC +" Storing = " + ldResult + " at register = " + rd);
				else
					System.out.println("\nRW Stage " + "Current PC: " + currentPC +" Storing = " + aluResult + " at register = " + rd);
			}
			else{
				System.out.println("\nRW Stage " + "Current PC: " + currentPC);
			}
			*/
		}
	}

}
