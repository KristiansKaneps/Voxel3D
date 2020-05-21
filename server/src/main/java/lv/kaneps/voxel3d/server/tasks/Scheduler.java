package lv.kaneps.voxel3d.server.tasks;

import lv.kaneps.voxel3d.server.GameServer;

/**
 * TODO
 */
public class Scheduler
{
	protected final GameServer gs;

	public Scheduler(GameServer gs)
	{
		this.gs = gs;
	}

	public Task runSyncTask(Runnable runnable)
	{
		Task task = new Task(runnable);
		gs.doSyncTask(new TaskDispatcher(task));
		return task;
	}

	public Task runAsyncTask(Runnable runnable)
	{
		Task task = new Task(runnable);
		task.startAsync();
		return task;
	}
}
