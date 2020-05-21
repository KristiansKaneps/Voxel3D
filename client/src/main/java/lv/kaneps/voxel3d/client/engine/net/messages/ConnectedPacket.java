package lv.kaneps.voxel3d.client.engine.net.messages;

import io.netty.buffer.ByteBuf;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.common.net.messages.IPacket;

import java.nio.charset.StandardCharsets;

public class ConnectedPacket implements IPacket
{
	public int id;
	public byte worldId;
	public String playerName, worldName;

	public ConnectedPacket(String playerName)
	{
		this.playerName = playerName;
	}

	public ConnectedPacket(int id, byte worldId, String worldName)
	{
		this.id = id;
		this.worldId = worldId;
		this.worldName = worldName;
	}

	@Override
	public PacketType getType()
	{
		return PacketType.CONNECTED;
	}

	@Override
	public void serialize(ByteBuf out)
	{
		byte[] name = this.playerName.getBytes(StandardCharsets.ISO_8859_1);

		out.writeByte(name.length & 0xff);
		out.writeBytes(name);
	}
}
