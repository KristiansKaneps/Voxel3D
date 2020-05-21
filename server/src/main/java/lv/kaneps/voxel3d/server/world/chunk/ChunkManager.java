package lv.kaneps.voxel3d.server.world.chunk;

import lv.kaneps.voxel3d.server.world.World;
import lv.kaneps.voxel3d.server.world.block.Block;
import lv.kaneps.voxel3d.server.world.entity.Location;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkManager
{
	private final Map<Integer, ChunkPos> posMap = new HashMap<>();
	private final Map<Integer, Chunk> chunkMap = new HashMap<>();

	public final World world;

	public ChunkManager(World world)
	{
		this.world = world;
	}

	public List<Chunk> getChunksInRadius(Location loc, float radius)
	{
		loadChunksInSquareRadius(loc, radius);

		Vector3i cloc = new Vector3i(
				Math.floorDiv((int) loc.x, Chunk.WIDTH),
				Math.floorDiv((int) loc.y, Chunk.HEIGHT),
				Math.floorDiv((int) loc.z, Chunk.DEPTH)
		);

		List<Chunk> chunks = new ArrayList<>();
		for(Map.Entry<Integer, Chunk> entry : chunkMap.entrySet())
		{
			Chunk chunk = entry.getValue();
			if(cloc.distanceSquared(chunk.pos.x, chunk.pos.y, chunk.pos.z) <= radius * radius)
				chunks.add(chunk);
		}
		return chunks;
	}

	private void loadChunksInSquareRadius(Location loc, float radius)
	{
		int rCeil = (int) Math.ceil(radius);

		int cx = Math.floorDiv((int) loc.x, Chunk.WIDTH);
		int cy = Math.floorDiv((int) loc.y, Chunk.HEIGHT);
		int cz = Math.floorDiv((int) loc.z, Chunk.DEPTH);

		int x0 = cx - rCeil, y0 = cy - rCeil, z0 = cz - rCeil;
		int x1 = cx + rCeil, y1 = cy + rCeil, z1 = cz + rCeil;

		for(int x = x0; x <= x1; x++)
			for(int y = y0; y <= y1; y++)
				for(int z = z0; z <= z1; z++)
					getChunk(new ChunkPos(x, y, z));
	}

	public List<Chunk> getChunks()
	{
		List<Chunk> chunks = new ArrayList<>(chunkMap.size());
		for(Map.Entry<Integer, Chunk> entry : chunkMap.entrySet())
			chunks.add(entry.getValue());
		return chunks;
	}

	public Chunk getChunk(ChunkPos pos)
	{
		for(Map.Entry<Integer, ChunkPos> entry : posMap.entrySet())
			if(entry.getValue().equals(pos))
				return chunkMap.get(entry.getKey());
		Chunk chunk = loadChunk(pos);
		posMap.put(chunk.id, pos.copy());
		chunkMap.put(chunk.id, chunk);
		return chunk;
	}

	private Chunk loadChunk(ChunkPos pos)
	{
		Chunk chunk = new Chunk(pos);
		world.getGenerator().generateChunk(world, chunk);
		return chunk;
	}

	public void unloadChunk(Chunk chunk)
	{
		chunk.unload();
		chunk.onUnload();
		posMap.remove(chunk.id);
		chunkMap.remove(chunk.id);
	}

	public void unloadChunk(ChunkPos pos)
	{
		Chunk chunk = getChunk(pos);
		if(chunk != null) unloadChunk(chunk);
	}
}
