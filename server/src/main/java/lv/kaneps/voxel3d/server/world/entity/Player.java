package lv.kaneps.voxel3d.server.world.entity;

import lv.kaneps.voxel3d.common.net.messages.IPacket;
import lv.kaneps.voxel3d.server.net.PlayerConnection;

public class Player extends Entity
{
	public PlayerConnection conn;

	public String name;

	public Player(String name, PlayerConnection conn)
	{
		super(EntityType.PLAYER);
		this.name = name;
		this.conn = conn;
	}

	public void sendPacket(IPacket packet)
	{
		conn.writeAndFlush(packet);
	}
}
