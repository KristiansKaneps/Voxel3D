package lv.kaneps.voxel3d.server.tasks;

/**
 * Starts synchronous tasks
 */
public final class TaskDispatcher
{
	protected final Task task;

	protected TaskDispatcher(Task task)
	{
		this.task = task;
	}

	public final void start()
	{
		task.startSync();
	}
}
