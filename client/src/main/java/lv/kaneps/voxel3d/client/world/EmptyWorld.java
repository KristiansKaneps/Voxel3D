package lv.kaneps.voxel3d.client.world;

import lv.kaneps.voxel3d.client.engine.Renderer;
import lv.kaneps.voxel3d.client.world.chunk.Chunk;
import lv.kaneps.voxel3d.client.world.chunk.ChunkData;

public class EmptyWorld extends World
{
	public EmptyWorld()
	{
		super((byte) -1, "emptyWorld");
	}

	@Override
	public void addChunk(Chunk chunk)
	{

	}

	@Override
	public void render(Renderer renderer, double dt, double elapsedTime) throws Exception
	{

	}

	@Override
	public void cleanup()
	{
		super.cleanup();
	}
}
