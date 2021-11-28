package processor.memorysystem;

import generic.*;
import processor.Clock;
import processor.pipeline.MemoryAccess;


public class MainMemory implements Element {
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	public void handleEvent(Event e) {
		if (e.getEventType() == Event.EventType.MemoryRead) {
			System.out.println("Memory Read Event Going On");
			MemoryReadEvent event = (MemoryReadEvent) e;

			Simulator.getEventQueue().addEvent(
					new MemoryResponseEvent(
							Clock.getCurrentTime(),
							this,
							event.getRequestingElement(),
							getWord(event.getAddressToReadFrom())
					)
			);
		}
		else if(e.getEventType() == Event.EventType.MemoryWrite) {
			MemoryWriteEvent event = (MemoryWriteEvent) e;
			this.setWord(event.getAddressToWriteTo(), event.getValue());
			MemoryAccess memory_access = (MemoryAccess) event.getRequestingElement();
			memory_access.EX_MA_Latch.setMA_Busy(false);
			memory_access.EX_MA_Latch.setMA_enable(false);
			memory_access.MA_RW_Latch.setRW_enable(true);
			memory_access.MA_RW_Latch.setInstruction(memory_access.instruction);
			System.out.println("MA Store Event Handled");
		}
	}

}
