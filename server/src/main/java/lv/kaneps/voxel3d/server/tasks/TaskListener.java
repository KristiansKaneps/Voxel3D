package lv.kaneps.voxel3d.server.tasks;

@FunctionalInterface
public interface TaskListener
{
	void onExit(Task task);
}
