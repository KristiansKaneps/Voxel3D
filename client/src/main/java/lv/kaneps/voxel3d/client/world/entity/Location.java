package lv.kaneps.voxel3d.client.world.entity;

import org.joml.Vector3f;

public class Location extends Vector3f
{
	public Location()
	{
		super();
	}

	public Location(Vector3f vec)
	{
		this(vec.x, vec.y, vec.z);
	}

	public Location(float x, float y, float z)
	{
		super(x, y, z);
	}

	public Location copy()
	{
		return new Location(x, y, z);
	}
}
