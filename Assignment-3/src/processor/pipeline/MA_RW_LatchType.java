package processor.pipeline;
import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int aluResult;
	int ldResult;

	public void setInstruction(Instruction instruction){
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setAluResult(int ALUResult){
		this.aluResult = ALUResult;
	}

	public int getAluResult(){
		return this.aluResult;
	}

	public void setLdResult(int LDResult){
		this.ldResult = LDResult;
	}

	public int getLdResult(){
		return this.ldResult;
	}
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

}
