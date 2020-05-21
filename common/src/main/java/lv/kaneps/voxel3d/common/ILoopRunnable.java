package lv.kaneps.voxel3d.common;

public interface ILoopRunnable extends Runnable
{
	void stop();

	boolean isRunning();

	void cleanup();
}
