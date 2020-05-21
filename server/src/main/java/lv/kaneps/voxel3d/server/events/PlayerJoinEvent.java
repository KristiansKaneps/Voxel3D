package lv.kaneps.voxel3d.server.events;

import lv.kaneps.voxel3d.server.world.entity.Player;

public class PlayerJoinEvent extends PlayerEvent
{
	public PlayerJoinEvent(Player player)
	{
		super(EventType.PLAYER_JOIN, player);
	}
}
