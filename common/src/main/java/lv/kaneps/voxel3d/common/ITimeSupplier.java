package lv.kaneps.voxel3d.common;

public interface ITimeSupplier
{
	default double getTime()
	{
		return System.nanoTime() / 1000000000.0;
	}
}
