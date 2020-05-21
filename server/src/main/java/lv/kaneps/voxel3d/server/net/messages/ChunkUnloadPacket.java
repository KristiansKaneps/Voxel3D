package lv.kaneps.voxel3d.server.net.messages;

import io.netty.buffer.ByteBuf;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.common.net.messages.IPacket;

public class ChunkUnloadPacket implements IPacket
{
	public byte worldId;
	public int chunkId;

	public ChunkUnloadPacket(byte worldId, int chunkId)
	{
		this.worldId = worldId;
		this.chunkId = chunkId;
	}

	@Override
	public PacketType getType()
	{
		return PacketType.CHUNK_UNLOAD;
	}

	@Override
	public void serialize(ByteBuf out)
	{
		out.writeByte(worldId & 0xff);
		out.writeInt(chunkId);
	}
}