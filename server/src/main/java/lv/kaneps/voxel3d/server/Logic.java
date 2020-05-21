package lv.kaneps.voxel3d.server;

import lv.kaneps.voxel3d.common.ILoopRunnable;
import lv.kaneps.voxel3d.common.ITimeSupplier;
import lv.kaneps.voxel3d.server.tasks.TaskDispatcher;
import lv.kaneps.voxel3d.server.utils.Log;
import lv.kaneps.voxel3d.server.world.World;
import lv.kaneps.voxel3d.server.world.gen.SimplexNoiseWorldGenerator;

import java.util.LinkedList;

public class Logic implements ILoopRunnable, ITimeSupplier
{
	protected final LinkedList<TaskDispatcher> taskDispatchers = new LinkedList<>();

	protected boolean running = false;

	public final GameServer server;

	public Logic(GameServer server)
	{
		this.server = server;
	}

	protected void doSyncTask(TaskDispatcher taskDispatcher)
	{
		synchronized (taskDispatchers)
		{
			taskDispatchers.add(taskDispatcher);
		}
	}

	public World world;

	protected void init()
	{
		world = new World((byte) 0, "world");
		world.setGenerator(new SimplexNoiseWorldGenerator(50, 10, -1));
	}

	private void logic(double dt, double elapsedTime)
	{
		world.tick(server, dt, elapsedTime);
	}

	@Override
	public void run()
	{
		running = true;

		double secsPerUpdate = 1.0 / 30.0;
		double prev = getTime();
		double steps = 0.0;

		int tickCount = 0;
		double tpsAccumulator = 0;

		init();

		while(isRunning())
		{
			double current = getTime();
			double dt = current - prev;
			prev = current;
			steps += dt;

			synchronized (taskDispatchers)
			{
				if(taskDispatchers.size() > 0)
					taskDispatchers.remove().start();
			}

			logic(dt, steps);

			tickCount++;
			tpsAccumulator += 1.0d / dt;
			if(tickCount >= 90)
			{
				Log.d(String.format("Average TPS = %.2f", tpsAccumulator / tickCount));
				tickCount = 0;
				tpsAccumulator = 0;
			}

			sync(current, secsPerUpdate);
		}

		running = false;
	}

	private void sync(double loopStartTime, double step)
	{
		double endTime = loopStartTime + step;
		while(getTime() < endTime)
			try { Thread.sleep(1); } catch (InterruptedException e) {}
	}

	@Override
	public void stop()
	{
		System.out.println("Logic stopped");
		running = false;
	}

	@Override
	public boolean isRunning()
	{
		return running;
	}

	@Override
	public void cleanup()
	{
		System.gc();
	}
}