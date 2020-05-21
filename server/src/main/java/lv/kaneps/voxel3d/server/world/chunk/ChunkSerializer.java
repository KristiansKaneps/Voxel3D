package lv.kaneps.voxel3d.server.world.chunk;

import java.util.ArrayList;
import java.util.List;

public final class ChunkSerializer
{
	private ChunkSerializer() {}

	public static ChunkData serialize(Chunk chunk)
	{
		return new ChunkData(chunk.id, chunk.pos, chunk.getBlockStates());
	}

	public static List<ChunkData> serialize(List<Chunk> chunks)
	{
		List<ChunkData> chunkDataList = new ArrayList<>(chunks.size());
		for(Chunk chunk : chunks) chunkDataList.add(serialize(chunk));
		return chunkDataList;
	}
}
