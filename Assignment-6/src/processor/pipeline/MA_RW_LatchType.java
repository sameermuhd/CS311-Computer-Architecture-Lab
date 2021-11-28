package processor.pipeline;
import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int aluResult;
	int ldResult;
	boolean RW_Lock;
	boolean RW_Busy;

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
		RW_Lock = false;
		RW_Busy = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rw_enable) {
		RW_enable = rw_enable;
	}

	public boolean isRW_Locked() { return RW_Lock; }

	public void setRW_Lock(boolean rw_lock) { RW_Lock = rw_lock; }

	public void setRW_Busy(boolean rw_busy) { RW_Busy = rw_busy; }

	public boolean isRW_Busy() { return RW_Busy; }

}
