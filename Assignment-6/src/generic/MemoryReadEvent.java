package generic;

public class MemoryReadEvent extends Event {

	int addressToReadFrom;
	
	public MemoryReadEvent(long eventTime, Element requestingElement, Element processingElement, int address) {
		super(eventTime, EventType.MemoryRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
	}

	public int getAddressToReadFrom() {
		return addressToReadFrom;
	}

	public void setAddressToReadFrom(int addressToReadFrom) {
		this.addressToReadFrom = addressToReadFrom;
	}
}
