package lv.kaneps.voxel3d.client.world.chunk;

@FunctionalInterface
public interface ChunkConsumer
{
	void accept(Chunk chunk) throws Exception;
}
