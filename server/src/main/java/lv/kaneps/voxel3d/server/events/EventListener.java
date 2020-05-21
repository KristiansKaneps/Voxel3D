package lv.kaneps.voxel3d.server.events;

public interface EventListener
{
	void onPlayerJoin(PlayerJoinEvent event);
	void onPlayerQuit(PlayerQuitEvent event);

	void onServerStop(ServerStopEvent event);
}
