package lv.kaneps.voxel3d.client.world;

import lv.kaneps.voxel3d.client.engine.IRenderCallable;
import lv.kaneps.voxel3d.client.engine.Renderer;
import lv.kaneps.voxel3d.client.engine.math.Sphere;
import lv.kaneps.voxel3d.client.world.chunk.Chunk;
import lv.kaneps.voxel3d.client.world.chunk.ChunkManager;
import lv.kaneps.voxel3d.client.world.chunk.ChunkPos;
import lv.kaneps.voxel3d.client.world.entity.*;

public class World implements IRenderCallable
{
	public final ChunkManager chunkManager;
	public final EntityManager entityManager;

	public float sunAngle = -90f;

	public final byte worldId;
	public final String name;

	public World(byte worldId, String name)
	{
		this.worldId = worldId;
		this.name = name;

		chunkManager = new ChunkManager(this);
		entityManager = new EntityManager(this);
	}

	public boolean isChunkLoaded(ChunkPos pos)
	{
		return chunkManager.isChunkLoaded(pos);
	}

	public void addChunk(Chunk chunk)
	{
		chunkManager.addChunk(chunk);
	}

	public void unloadChunk(int chunkId)
	{
		chunkManager.removeChunk(chunkId);
	}

	public void unloadChunksOutside(Sphere sphere)
	{
		chunkManager.unloadChunksOutside(sphere);
	}

	public Entity getEntity(int entityId, EntityType entityType)
	{
		return entityManager.getEntity(entityId, entityType);
	}

	public Player addPlayer(int entityId, String name)
	{
		return entityManager.addPlayer(entityId, name);
	}

	public float getSunAngle()
	{
		return sunAngle;
	}

	@Override
	public void render(Renderer renderer, double dt, double elapsedTime) throws Exception
	{
		entityManager.forEachEntity(entity -> entity.render(renderer, dt, elapsedTime));
		chunkManager.forEachChunk(chunk -> chunk.render(renderer, dt, elapsedTime));
	}

	@Override
	public void cleanup()
	{
		entityManager.cleanup();
		chunkManager.cleanup();
	}
}
