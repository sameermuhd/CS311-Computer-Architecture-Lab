package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	boolean EX_Lock;

	public void setInstruction(Instruction instruction){
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return this.instruction;
	}

	public OF_EX_LatchType()
	{
		EX_enable = false;
		EX_Lock = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public boolean isEX_Locked() { return EX_Lock; }

	public void setEX_Lock(boolean ex_lock) { EX_Lock = ex_lock; }

}
