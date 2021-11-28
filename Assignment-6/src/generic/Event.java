package generic;

public class Event {
	
	public enum EventType {ExecutionComplete, MemoryRead, MemoryResponse, MemoryWrite};
	
	long eventTime;
	Element requestingElement;
	Element processingElement;
	EventType eventType;
	
	public Event(long eventTime, EventType eventType, Element requestingElement, Element processingElement)
	{
		this.eventTime = eventTime;
		this.eventType = eventType;
		this.requestingElement = requestingElement;
		this.processingElement = processingElement;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public Element getRequestingElement() {
		return requestingElement;
	}

	public void setRequestingElement(Element requestingElement) {
		this.requestingElement = requestingElement;
	}

	public Element getProcessingElement() {
		return processingElement;
	}

	public void setProcessingElement(Element processingElement) {
		this.processingElement = processingElement;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
}
