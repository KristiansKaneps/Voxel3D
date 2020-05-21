package lv.kaneps.voxel3d.server.world.chunk;

import lv.kaneps.voxel3d.server.GameServer;
import lv.kaneps.voxel3d.server.world.ITickable;
import lv.kaneps.voxel3d.server.world.UIDGen;
import lv.kaneps.voxel3d.server.world.block.Block;
import lv.kaneps.voxel3d.server.world.block.BlockPos;
import lv.kaneps.voxel3d.server.world.block.BlockState;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

import static lv.kaneps.voxel3d.common.world.WorldConstants.*;

public class Chunk implements ITickable
{
	public static final int WIDTH = CHUNK_WIDTH; // x
	public static final int HEIGHT = CHUNK_HEIGHT; // y
	public static final int DEPTH = CHUNK_DEPTH; // z

	protected final int[][][] blocks = new int[WIDTH][HEIGHT][DEPTH];
	protected final List<Vector3i> actualBlocks = new ArrayList<>();

	protected boolean unload = false;

	public final int id;
	public final ChunkPos pos;

	public Chunk(ChunkPos pos)
	{
		this.id = UIDGen.newChunk();
		this.pos = pos;
	}

	public boolean mustUnload()
	{
		return unload;
	}

	public void unload()
	{
		unload = true;
	}

	public void onUnload()
	{
		UIDGen.freeChunkId(id);
	}

	protected void setBlock(int x, int y, int z, int id)
	{
		blocks[x][y][z] = id;

		if (id == 0) actualBlocks.remove(pos);
		else actualBlocks.add(new Vector3i(x, y, z));
	}

	public void setBlock(int x, int y, int z, Block block)
	{
		setBlock(x, y, z, block.getId());
	}

	protected int getBlock(int x, int y, int z)
	{
		return blocks[x][y][z];
	}

	public void removeBlock(int x, int y, int z)
	{
		setBlock(x, y, z, 0);
	}

	public BlockState getBlockState(int x, int y, int z)
	{
		int id = getBlock(x, y, z);
		return id == 0 ? null : new BlockState(Block.byId(id), new BlockPos(
				x + this.pos.x * WIDTH,
				y + this.pos.y * HEIGHT,
				z + this.pos.z * DEPTH
		));
	}

	public List<BlockState> getBlockStates()
	{
		List<BlockState> blockStates = new ArrayList<>();
		for(Vector3i pos : actualBlocks)
			blockStates.add(getBlockState(pos.x, pos.y, pos.z));
		return blockStates;
	}

	@Override
	public void tick(GameServer gs, double dt, double elapsed)
	{

	}
}
