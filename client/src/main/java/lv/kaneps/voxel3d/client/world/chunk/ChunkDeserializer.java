package lv.kaneps.voxel3d.client.world.chunk;

import lv.kaneps.voxel3d.client.world.block.BlockEnum;
import lv.kaneps.voxel3d.client.world.block.BlockPos;
import lv.kaneps.voxel3d.client.world.block.BlockState;

public final class ChunkDeserializer
{
	private ChunkDeserializer() { }

	public static Chunk deserialize(ChunkData chunkData)
	{
		Chunk chunk = new Chunk(chunkData.chunkId, chunkData.chunkPos);
		int length = chunkData.blocks.length;
		for(int i = 0; i < length; i++)
		{
			int blockId = chunkData.blocks[i];
			BlockPos blockPos = chunkData.pos[i];
			chunk.addBlock(new BlockState(BlockEnum.byId(blockId), blockPos.copy()));
		}
		return chunk;
	}
}
