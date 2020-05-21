package lv.kaneps.voxel3d.server.world.block;

public class BlockState
{
	public final Block block;
	public final BlockPos pos;

	public BlockState(Block block, BlockPos pos)
	{
		this.block = block;
		this.pos = pos;
	}
}
