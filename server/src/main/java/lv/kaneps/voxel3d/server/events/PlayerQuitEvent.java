package lv.kaneps.voxel3d.server.events;

import lv.kaneps.voxel3d.server.world.entity.Player;

public class PlayerQuitEvent extends PlayerEvent
{
	public PlayerQuitEvent(Player player)
	{
		super(EventType.PLAYER_QUIT, player);
	}
}
