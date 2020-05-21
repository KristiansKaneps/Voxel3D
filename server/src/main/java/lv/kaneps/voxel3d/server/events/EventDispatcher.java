package lv.kaneps.voxel3d.server.events;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher
{
	protected final List<EventListener> eventListeners;

	public EventDispatcher()
	{
		eventListeners = new ArrayList<>();
	}

	public void addEventListener(EventListener eventListener)
	{
		eventListeners.add(eventListener);
	}

	public void removeEventListener(EventListener eventListener)
	{
		eventListeners.remove(eventListener);
	}

	public <T extends Event> void dispatchEvent(T event)
	{
		for(EventListener e : eventListeners)
			switch(event.getEventType())
			{
				case PLAYER_JOIN:
					e.onPlayerJoin((PlayerJoinEvent) event);
					break;
				case PLAYER_QUIT:
					e.onPlayerQuit((PlayerQuitEvent) event);
			}
	}
}
