package lv.kaneps.voxel3d.common;

public class Loop
{
	protected Thread thread;

	protected final ILoopRunnable runnable;
	protected final String name;

	public Loop(ILoopRunnable runnable, String name)
	{
		this.runnable = runnable;
		this.name = name.toLowerCase().endsWith("thread") ? name : name + " Thread";
	}

	public boolean isRunning()
	{
		return runnable.isRunning();
	}

	public synchronized void startOnCurrentThread()
	{
		if(isRunning()) return;

		thread = Thread.currentThread();
		runnable.run();
	}

	public synchronized void start()
	{
		if(isRunning()) return;
		thread = new Thread(runnable, name);
		thread.start();
	}

	public synchronized void stop()
	{
		if(!isRunning()) return;
		runnable.stop();
	}

	public void cleanup()
	{
		runnable.cleanup();
	}

	public Thread getThread()
	{
		return thread;
	}

	public String getName()
	{
		return name;
	}
}
