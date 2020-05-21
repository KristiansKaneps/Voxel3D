package lv.kaneps.voxel3d.server.events;

import lv.kaneps.voxel3d.server.GameServer;

public class ServerStopEvent extends Event
{
	protected final GameServer server;

	public ServerStopEvent(GameServer server)
	{
		super(EventType.SERVER_STOP);
		this.server = server;
	}

	public GameServer getServer()
	{
		return server;
	}
}
