package lv.kaneps.voxel3d.client.world.chunk;

import lv.kaneps.voxel3d.client.world.block.BlockPos;
import lv.kaneps.voxel3d.client.world.chunk.ChunkPos;

public class ChunkData
{
	public byte worldId;
	public int chunkId;
	public ChunkPos chunkPos;
	public int[] blocks;
	public BlockPos[] pos;

	public ChunkData(byte worldId, int chunkId, ChunkPos chunkPos, int[] blocks, BlockPos[] pos)
	{
		this.worldId = worldId;
		this.chunkId = chunkId;
		this.chunkPos = chunkPos;
		this.blocks = blocks;
		this.pos = pos;
	}
}
