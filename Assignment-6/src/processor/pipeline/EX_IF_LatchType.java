package processor.pipeline;

public class EX_IF_LatchType {

	boolean EX_IF_enable;
	int PC;

	public EX_IF_LatchType() {
		EX_IF_enable = false;
	}

	public EX_IF_LatchType(boolean ex_if_enable) {
		EX_IF_enable = ex_if_enable;
	}

	public EX_IF_LatchType(boolean ex_if_enable, int pc) {
		EX_IF_enable = ex_if_enable;
		PC = pc;
	}

	public boolean isEX_IF_enable() {
		return EX_IF_enable;
	}

	public void setEX_IF_enable(boolean ex_if_enable, int pc) {
		EX_IF_enable = ex_if_enable;
		PC = pc;
	}

	public void setEX_IF_enable(boolean ex_if_enable) {
		EX_IF_enable = ex_if_enable;
	}

	public void setPC(int pc) {
		PC = pc;
	}

	public int getPC() {
		return PC;
	}

}
