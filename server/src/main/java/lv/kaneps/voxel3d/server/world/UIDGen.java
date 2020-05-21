package lv.kaneps.voxel3d.server.world;

import java.util.ArrayList;
import java.util.List;

public final class UIDGen
{
	private static final List<Integer> usedEntityIds = new ArrayList<>();
	private static final List<Integer> usedChunkIds = new ArrayList<>();

	private static int entityId = 0;
	private static int chunkId = 0;

	private UIDGen() {}

	public static int newEntity()
	{
		if(usedEntityIds.contains(entityId))
		{
			entityId++;
			return newEntity();
		}

		usedEntityIds.add(entityId);
		return entityId++;
	}

	public static int newChunk()
	{
		if(usedChunkIds.contains(chunkId))
		{
			chunkId++;
			return newChunk();
		}

		usedChunkIds.add(chunkId);
		return chunkId++;
	}

	public static void freeEntityId(int id)
	{
		usedEntityIds.remove((Integer) id);
	}

	public static void freeChunkId(int id)
	{
		usedChunkIds.remove((Integer) id);
	}
}
