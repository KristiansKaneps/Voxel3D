package lv.kaneps.voxel3d.server.world.block;

import lv.kaneps.voxel3d.server.world.chunk.ChunkPos;
import org.joml.Vector3i;

import static lv.kaneps.voxel3d.common.world.WorldConstants.*;

public class BlockPos extends Vector3i
{
	public BlockPos(int x, int y, int z)
	{
		super(x, y, z);
	}

	public BlockPos insideChunk(ChunkPos pos)
	{
		return new BlockPos(pos.x * CHUNK_WIDTH + x, pos.y * CHUNK_HEIGHT + y, pos.z * CHUNK_DEPTH + z);
	}

	public ChunkPos toChunkPos()
	{
		int x = Math.floorDiv(this.x, CHUNK_WIDTH);
		int y = Math.floorDiv(this.y, CHUNK_HEIGHT);
		int z = Math.floorDiv(this.z, CHUNK_DEPTH);
		return new ChunkPos(x, y, z);
	}

	public BlockPos getPositionInChunk()
	{
		int x = Math.abs(this.x % CHUNK_WIDTH);
		int y = Math.abs(this.y % CHUNK_HEIGHT);
		int z = Math.abs(this.z % CHUNK_DEPTH);
		return new BlockPos(x, y, z);
	}

	public BlockPos copy()
	{
		return new BlockPos(x, y, z);
	}
}
