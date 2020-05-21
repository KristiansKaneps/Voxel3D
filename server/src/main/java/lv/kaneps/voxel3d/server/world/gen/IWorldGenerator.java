package lv.kaneps.voxel3d.server.world.gen;

import lv.kaneps.voxel3d.server.world.World;
import lv.kaneps.voxel3d.server.world.chunk.Chunk;

public interface IWorldGenerator
{
	void generateChunk(World world, Chunk chunk);
}
