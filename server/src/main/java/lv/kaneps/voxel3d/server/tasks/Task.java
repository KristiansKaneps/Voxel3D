package lv.kaneps.voxel3d.server.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Task
{
	private static final List<Integer> usedIds = new ArrayList<>();
	private static final AtomicInteger nextId = new AtomicInteger(0);

	private static int generateId()
	{
		if(usedIds.contains(nextId.get()))
		{
			nextId.incrementAndGet();
			generateId();
		}
		return nextId.getAndIncrement();
	}

	private static void freeId(int id)
	{
		usedIds.remove((Integer) id);
	}

	protected static final boolean MANUAL_TASK_GC = true;

	protected TaskListener taskListener;

	protected boolean isCancelled, isRunning, isCompleted;

	protected final int id;
	protected final Runnable runnable;

	protected Task(Runnable runnable)
	{
		this.id = generateId();
		this.runnable = runnable;

		this.isCancelled = false;
		this.isRunning = false;
		this.isCompleted = false;
	}

	public int getId()
	{
		return id;
	}

	public void cancel()
	{
		isCancelled = true;
		if(taskListener != null) taskListener.onExit(this);
	}

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public boolean isCompleted()
	{
		return isCompleted;
	}

	/**
	 * Only one TaskListener can be attached.
	 */
	public void attachListener(TaskListener taskListener)
	{
		if(isCancelled || isCompleted)
			taskListener.onExit(this);
		this.taskListener = taskListener;
	}

	protected synchronized void startSync()
	{
		if(isRunning || isCancelled || isCompleted) return;
		isRunning = true;
		runnable.run();
		isCompleted = true;
		if(taskListener != null) taskListener.onExit(this);
		freeId(id);
	}

	protected synchronized void startAsync()
	{
		if(isRunning || isCancelled || isCompleted) return;
		isRunning = true;
		new Thread(() -> { runnable.run(); onExit(); }).start();
	}

	protected void onExit()
	{
		isCompleted = true;
		if(taskListener != null)
			taskListener.onExit(this);
		freeId(id);
		if(MANUAL_TASK_GC) System.gc();
	}
}
