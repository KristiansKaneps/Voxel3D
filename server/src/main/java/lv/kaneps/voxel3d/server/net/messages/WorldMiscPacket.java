package lv.kaneps.voxel3d.server.net.messages;

import io.netty.buffer.ByteBuf;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.common.net.messages.IPacket;

public class WorldMiscPacket implements IPacket
{
	public byte worldId;
	public float sunAngle;

	public WorldMiscPacket(byte worldId, float sunAngle)
	{
		this.worldId = worldId;
		this.sunAngle = sunAngle;
	}

	@Override
	public PacketType getType()
	{
		return PacketType.WORLD_MISC;
	}

	@Override
	public void serialize(ByteBuf out)
	{
		out.writeByte(worldId);
		out.writeFloat(sunAngle);
	}
}
