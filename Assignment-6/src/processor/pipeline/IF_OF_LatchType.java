package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean OF_Busy;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
		OF_Busy = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public void setOF_Busy(boolean of_busy) {OF_Busy = of_busy; }

	public boolean isOF_Busy() {return OF_Busy; }

}
