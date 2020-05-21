package lv.kaneps.voxel3d.client.world.chunk;

import lv.kaneps.voxel3d.client.world.block.BlockPos;
import org.joml.Vector3i;

public class ChunkPos extends Vector3i
{
	public ChunkPos(int x, int y, int z)
	{
		super(x, y, z);
	}

	public BlockPos toBlockPos(BlockPos pos)
	{
		return pos.insideChunk(this);
	}
}
