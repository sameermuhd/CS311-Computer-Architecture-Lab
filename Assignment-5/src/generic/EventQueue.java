package generic;

import java.util.Comparator;
import java.util.PriorityQueue;

import processor.Clock;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue()
	{
		queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void addEvent(Event event)
	{
		queue.add(event);
	}

	public void processEvents()
	{
		while(!queue.isEmpty() && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			Event event = queue.poll();
			event.getProcessingElement().handleEvent(event);
		}
	}
}

class EventComparator implements Comparator<Event>
{
	@Override
    public int compare(Event x, Event y)
    {
		if(x.getEventTime() < y.getEventTime())
		{
			return -1;
		}
		else if(x.getEventTime() > y.getEventTime())
		{
			return 1;
		}
		else
		{
			return 0;
		}
    }
}
