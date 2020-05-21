package lv.kaneps.voxel3d.server.world.chunk;

import org.joml.Vector3i;

public class ChunkPos extends Vector3i
{
	public ChunkPos(int x, int y, int z)
	{
		super(x, y, z);
	}

	public ChunkPos copy()
	{
		return new ChunkPos(x, y, z);
	}
}
