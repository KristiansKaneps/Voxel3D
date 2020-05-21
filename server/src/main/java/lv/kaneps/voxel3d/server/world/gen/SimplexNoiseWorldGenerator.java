package lv.kaneps.voxel3d.server.world.gen;

import lv.kaneps.voxel3d.common.SimplexNoise;
import lv.kaneps.voxel3d.server.utils.Log;
import lv.kaneps.voxel3d.server.world.block.Block;
import lv.kaneps.voxel3d.server.world.World;
import lv.kaneps.voxel3d.server.world.chunk.Chunk;
import lv.kaneps.voxel3d.server.world.chunk.ChunkPos;

public class SimplexNoiseWorldGenerator implements IWorldGenerator
{
	protected float smoothFactor, maxHeight, minHeight;

	public SimplexNoiseWorldGenerator(float smoothFactor, float maxHeight, float minHeight)
	{
		this.smoothFactor = smoothFactor;
		this.maxHeight = maxHeight;
		this.minHeight = minHeight;
	}

	@Override
	public void generateChunk(World world, Chunk chunk)
	{
		ChunkPos pos = chunk.pos;
		int x0 = pos.x * Chunk.WIDTH;
		int z0 = pos.z * Chunk.DEPTH;
		int x1 = x0 + Chunk.WIDTH;
		int z1 = z0 + Chunk.DEPTH;

		for(int x = x0; x < x1; x++)
			for(int z = z0; z < z1; z++)
			{
				int y = Math.abs(Math.min(31, Math.max(0, (int) (SimplexNoise.noise(x / smoothFactor, z / smoothFactor) * (maxHeight + minHeight) + minHeight))));
				chunk.setBlock(Math.abs(x % Chunk.WIDTH), y, Math.abs(z % Chunk.HEIGHT), Block.STONE);
			}
		Log.i("WorldGen", "SimplexNoiseWorldGenerator done");
	}
}