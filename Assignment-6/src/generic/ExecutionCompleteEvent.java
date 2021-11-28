package generic;

public class ExecutionCompleteEvent extends Event {
	
	public ExecutionCompleteEvent(long eventTime, Element requestingElement, Element processingElement)
	{
		super(eventTime, EventType.ExecutionComplete, requestingElement, processingElement);
	}

}
