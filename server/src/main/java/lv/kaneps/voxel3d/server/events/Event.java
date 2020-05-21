package lv.kaneps.voxel3d.server.events;

public class Event
{
	protected final EventType eventType;

	public Event(EventType eventType)
	{
		this.eventType = eventType;
	}

	public final EventType getEventType()
	{
		return eventType;
	}
}
