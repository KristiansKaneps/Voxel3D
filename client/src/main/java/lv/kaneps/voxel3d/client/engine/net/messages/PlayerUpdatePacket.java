package lv.kaneps.voxel3d.client.engine.net.messages;

import io.netty.buffer.ByteBuf;
import lv.kaneps.voxel3d.client.world.entity.Location;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.common.net.messages.IPacket;
import org.joml.Quaternionf;

public class PlayerUpdatePacket implements IPacket
{
	public Location loc;
	public Quaternionf rot;

	public PlayerUpdatePacket(Location loc, Quaternionf rot)
	{
		this.loc = loc;
		this.rot = rot;
	}

	@Override
	public PacketType getType()
	{
		return PacketType.PLAYER_UPDATE;
	}

	@Override
	public void serialize(ByteBuf out)
	{
		out.writeFloat(loc.x);
		out.writeFloat(loc.y);
		out.writeFloat(loc.z);
		out.writeFloat(rot.x);
		out.writeFloat(rot.y);
		out.writeFloat(rot.z);
		out.writeFloat(rot.w);
	}
}
