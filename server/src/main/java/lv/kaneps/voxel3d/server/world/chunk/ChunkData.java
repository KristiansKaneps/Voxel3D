package lv.kaneps.voxel3d.server.world.chunk;

import lv.kaneps.voxel3d.server.world.block.BlockState;

import java.util.List;

public class ChunkData
{
	public final int chunkId;
	public final ChunkPos pos;
	public final List<BlockState> blockStates;

	public ChunkData(int chunkId, ChunkPos pos, List<BlockState> blockStates)
	{
		this.chunkId = chunkId;
		this.pos = pos;
		this.blockStates = blockStates;
	}

	@Override
	public String toString()
	{
		return "ChunkData(chunkId=" + chunkId + " ;pos=" + pos + "; blockStates.size()=" + blockStates.size() + ")";
	}
}
