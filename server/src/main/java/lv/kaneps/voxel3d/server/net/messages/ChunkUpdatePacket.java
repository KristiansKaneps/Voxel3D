package lv.kaneps.voxel3d.server.net.messages;

import io.netty.buffer.ByteBuf;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.common.net.messages.IPacket;
import lv.kaneps.voxel3d.server.world.block.BlockState;
import lv.kaneps.voxel3d.server.world.chunk.ChunkData;
import lv.kaneps.voxel3d.server.world.chunk.ChunkPos;

public class ChunkUpdatePacket implements IPacket
{
	public byte worldId;
	public ChunkPos pos;
	public ChunkData chunkData;

	public ChunkUpdatePacket(byte worldId, ChunkPos pos, ChunkData chunkData)
	{
		this.worldId = worldId;
		this.pos = pos;
		this.chunkData = chunkData;
	}

	@Override
	public PacketType getType()
	{
		return PacketType.CHUNK_UPDATE;
	}

	@Override
	public void serialize(ByteBuf out)
	{
		int size = chunkData.blockStates.size();

		out.writeByte(worldId);
		out.writeInt(chunkData.chunkId);
		out.writeInt(pos.x);
		out.writeInt(pos.y);
		out.writeInt(pos.z);
		out.writeInt(size);
		for (int i = 0; i < size; i++)
		{
			BlockState state = chunkData.blockStates.get(i);
			out.writeInt(state.block.getId());
			out.writeInt(state.pos.x);
			out.writeInt(state.pos.y);
			out.writeInt(state.pos.z);
		}
	}
}
