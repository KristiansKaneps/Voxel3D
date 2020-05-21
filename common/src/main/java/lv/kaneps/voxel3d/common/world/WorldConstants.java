package lv.kaneps.voxel3d.common.world;

public final class WorldConstants
{
	public static final int CHUNK_WIDTH = 32; // x
	public static final int CHUNK_HEIGHT = 32; // y
	public static final int CHUNK_DEPTH = 32; // z

	public static final float CHUNK_BOUNDING_RADIUS = (float) Math.sqrt(CHUNK_WIDTH * CHUNK_WIDTH / 4f + CHUNK_HEIGHT * CHUNK_HEIGHT / 4f + CHUNK_DEPTH * CHUNK_DEPTH / 4f);

	private WorldConstants() {}
}
