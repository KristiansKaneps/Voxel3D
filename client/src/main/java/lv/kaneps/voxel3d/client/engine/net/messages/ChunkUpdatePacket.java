package lv.kaneps.voxel3d.client.engine.net.messages;

import io.netty.buffer.ByteBuf;
import lv.kaneps.voxel3d.client.world.chunk.ChunkData;
import lv.kaneps.voxel3d.client.world.block.BlockPos;
import lv.kaneps.voxel3d.client.world.chunk.ChunkPos;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.common.net.messages.IPacket;

public class ChunkUpdatePacket extends ChunkData implements IPacket
{
	public ChunkUpdatePacket(byte worldId, int chunkId, ChunkPos chunkPos, int[] blocks, BlockPos[] pos)
	{
		super(worldId, chunkId, chunkPos, blocks, pos);
	}

	@Override
	public PacketType getType()
	{
		return PacketType.CHUNK_UPDATE;
	}

	@Override
	public void serialize(ByteBuf out)
	{
		out.writeByte(worldId);
		out.writeInt(chunkId);
		out.writeInt(chunkPos.x);
		out.writeInt(chunkPos.y);
		out.writeInt(chunkPos.z);
		out.writeInt(blocks.length);
		for (int i = 0; i < blocks.length; i++)
		{
			out.writeInt(blocks[i]);
			BlockPos pos = this.pos[i];
			out.writeInt(pos.x);
			out.writeInt(pos.y);
			out.writeInt(pos.z);
		}
	}
}
