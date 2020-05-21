package lv.kaneps.voxel3d.server;

import lv.kaneps.voxel3d.common.Loop;
import lv.kaneps.voxel3d.common.net.messages.IPacket;
import lv.kaneps.voxel3d.server.events.Event;
import lv.kaneps.voxel3d.server.events.EventDispatcher;
import lv.kaneps.voxel3d.server.events.EventListener;
import lv.kaneps.voxel3d.server.events.ServerStopEvent;
import lv.kaneps.voxel3d.server.net.Server;
import lv.kaneps.voxel3d.server.tasks.Scheduler;
import lv.kaneps.voxel3d.server.tasks.Task;
import lv.kaneps.voxel3d.server.tasks.TaskDispatcher;
import lv.kaneps.voxel3d.server.utils.Log;
import lv.kaneps.voxel3d.server.utils.ThreadUtils;
import lv.kaneps.voxel3d.server.world.entity.Player;
import lv.kaneps.voxel3d.server.world.UIDGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer
{
	public float viewDistance = 4;

	public final Map<Integer, Player> players = new HashMap<>();

	public void addPlayer(Player player)
	{
		synchronized (players)
		{
			players.put(player.id, player);
			logic.world.addEntity(player);
		}
	}

	public void removePlayer(int id)
	{
		synchronized (players)
		{
			if(!players.containsKey(id)) return;
			Player player = players.get(id);
			players.remove(id);
			logic.world.removeEntity(player);
			UIDGen.freeEntityId(id);
		}
	}

	public void removePlayer(String name)
	{
		synchronized (players)
		{
			for (Map.Entry<Integer, Player> entry : players.entrySet())
			{
				Player player = entry.getValue();
				if(player == null) continue;
				if (player.name.equalsIgnoreCase(name))
					removePlayer(player.id);
			}
		}
	}

	public Player getPlayer(int id)
	{
		synchronized (players)
		{
			return players.get(id);
		}
	}

	public Player getPlayer(String name)
	{
		synchronized (players)
		{
			for (Map.Entry<Integer, Player> entry : players.entrySet())
			{
				Player player = entry.getValue();
				if(player == null) return null;
				if (player.name.equalsIgnoreCase(name))
					return player;
			}
			return null;
		}
	}

	public void broadcastPacket(IPacket packet)
	{
		net.broadcast(packet);
	}

	protected final List<EventListener> eventListeners = new ArrayList<>();
	protected EventDispatcher eventDispatcher;
	protected final Scheduler scheduler;

	public Logic logic;
	protected Loop lLogic;

	protected Server net;

	public GameServer()
	{
		eventDispatcher = new EventDispatcher();
		scheduler = new Scheduler(this);
		net = Server.createServer(this, 25588);
	}

	public void init() throws Exception
	{
		logic = new Logic(this);
		lLogic = new Loop(logic, "Logic");
	}

	public <T extends Event> void dispatchEvent(T event)
	{
		scheduler.runSyncTask(() -> eventDispatcher.dispatchEvent(event));
	}

	public Scheduler getScheduler()
	{
		return scheduler;
	}

	public void doSyncTask(TaskDispatcher taskDispatcher)
	{
		logic.doSyncTask(taskDispatcher);
	}

	public final synchronized void start() throws Exception
	{
		lLogic.start();
		net.start();
	}

	private synchronized void stop()
	{
		lLogic.stop();
		net.stop();
	}

	public final synchronized void exit() throws Exception
	{
		Task task = scheduler.runSyncTask(() -> eventDispatcher.dispatchEvent(new ServerStopEvent(this)));
		task.attachListener(t -> stop());

		new Thread(() -> {
			int counter = 0;
			while(!task.isCompleted() && counter <= 100) // wait for ~10 seconds before forcefully stopping the server
			{
				ThreadUtils.sleep(100);
				counter++;
			}
			if(task.isCompleted()) return;
			Log.e("ServerStopEvent is being handled for far too long. Forcefully stopping the server...");
			ThreadUtils.sleep(500);
			scheduler.runSyncTask(GameServer.this::stop);
		}, "Game Server Exit Countdown Thread").start();
	}
}
