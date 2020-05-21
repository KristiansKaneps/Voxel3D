package lv.kaneps.voxel3d.server.events;

public interface Cancellable
{
	void setCancelled(boolean cancelled);
	boolean isCancelled();
}
