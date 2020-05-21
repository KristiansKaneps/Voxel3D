package lv.kaneps.voxel3d.server.events;

import lv.kaneps.voxel3d.server.world.entity.Player;

public class PlayerEvent extends Event
{
	protected final Player player;

	protected PlayerEvent(EventType eventType, Player player)
	{
		super(eventType);
		this.player = player;
	}

	public Player getPlayer()
	{
		return player;
	}
}
