package lv.kaneps.voxel3d.server.utils;

public final class ThreadUtils
{
	private ThreadUtils() { }

	public static void sleep(long millis)
	{
		try { Thread.sleep(millis); } catch (InterruptedException e) {  }
	}
}
