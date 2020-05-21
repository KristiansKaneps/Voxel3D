package lv.kaneps.voxel3d.server.world;

import lv.kaneps.voxel3d.server.GameServer;

public interface ITickable
{
	void tick(GameServer gs, double dt, double elapsedTime);
}
