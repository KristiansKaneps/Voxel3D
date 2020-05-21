package lv.kaneps.voxel3d.client.world.chunk;

import lv.kaneps.voxel3d.client.engine.math.Sphere;
import lv.kaneps.voxel3d.client.world.World;
import org.joml.Vector3f;

import java.util.*;

public class ChunkManager
{
	protected final Object lock = new Object();
	protected final Map<Integer, Chunk> chunkIds = new HashMap<>();
	protected final List<Chunk> chunks = new ArrayList<>();

	public final World world;

	public ChunkManager(World world)
	{
		this.world = world;
	}

	public boolean isChunkLoaded(ChunkPos pos)
	{
		synchronized (lock)
		{
			for(Chunk chunk : chunks)
				if(chunk.pos.equals(pos))
					return true;
			return false;
		}
	}

	public void forEachChunk(ChunkConsumer consumer) throws Exception
	{
		synchronized (lock)
		{
			for (Chunk chunk : chunks)
				consumer.accept(chunk);
		}
	}

	public List<Chunk> getChunks()
	{
		return chunks;
	}

	public Chunk getChunk(ChunkPos pos)
	{
		synchronized (lock)
		{
			for(Map.Entry<Integer, Chunk> entry : chunkIds.entrySet())
			{
				Chunk chunk = entry.getValue();
				if (chunk.pos.equals(pos)) return chunk;
			}
		}
		return null;
	}

	public void addChunk(Chunk chunk)
	{
		synchronized (lock)
		{
			chunkIds.put(chunk.id, chunk);
			chunks.add(chunk);
		}
	}

	public void removeChunk(int id)
	{
		synchronized (lock)
		{
			Chunk chunk = chunkIds.get(id);
			chunkIds.remove(id);
			if (chunk == null) return;
			chunks.remove(chunk);
		}
	}

	public void unloadChunksOutside(Sphere sphere)
	{
		synchronized (lock)
		{
			Iterator<Chunk> iterator = chunks.iterator();
			Chunk chunk;
			while(iterator.hasNext())
			{
				chunk = iterator.next();
				ChunkPos pos = chunk.pos;
				if(!sphere.isOutside(new Vector3f(pos)))
					continue;
				iterator.remove();
				chunkIds.remove(chunk.id);
			}
		}
	}

	public void cleanup()
	{
		synchronized (lock)
		{
			for (Chunk chunk : chunks)
				chunk.cleanup();
		}
	}
}
