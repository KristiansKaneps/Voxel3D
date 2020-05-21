package lv.kaneps.voxel3d.server.world;

import lv.kaneps.voxel3d.server.world.chunk.ChunkData;
import lv.kaneps.voxel3d.server.world.entity.Entity;

import java.util.List;

public class WorldData
{
	public final byte worldId;
	public final List<ChunkData> chunks;
	public final List<Entity> entities;

	protected WorldData(byte worldId, List<ChunkData> chunks, List<Entity> entities)
	{
		this.worldId = worldId;
		this.chunks = chunks;
		this.entities = entities;
	}
}
