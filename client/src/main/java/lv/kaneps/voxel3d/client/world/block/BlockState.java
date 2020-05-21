package lv.kaneps.voxel3d.client.world.block;

public class BlockState
{
	public final BlockEnum block;
	public final BlockPos pos;

	public BlockState(BlockEnum block, BlockPos pos)
	{
		this.block = block;
		this.pos = pos;
	}
}
