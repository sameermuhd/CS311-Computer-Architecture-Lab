package generic;

public class MemoryWriteEvent extends Event {

	int addressToWriteTo;
	int value;
	
	public MemoryWriteEvent(long eventTime, Element requestingElement, Element processingElement, int address, int value) {
		super(eventTime, EventType.MemoryWrite, requestingElement, processingElement);
		this.addressToWriteTo = address;
		this.value = value;
	}

	public int getAddressToWriteTo() {
		return addressToWriteTo;
	}

	public void setAddressToWriteTo(int addressToWriteTo) {
		this.addressToWriteTo = addressToWriteTo;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
