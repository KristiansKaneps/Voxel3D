package lv.kaneps.voxel3d.server.world.entity;

import org.joml.Vector3f;

public class Location extends Vector3f
{
	public Location(float x, float y, float z)
	{
		super(x, y, z);
	}

	public Location copy()
	{
		return new Location(x, y, z);
	}
}
