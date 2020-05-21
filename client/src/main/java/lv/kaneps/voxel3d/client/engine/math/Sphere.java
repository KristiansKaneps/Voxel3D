package lv.kaneps.voxel3d.client.engine.math;

import org.joml.Vector3f;

public class Sphere
{
	public Vector3f pos;
	public float radius;

	public Sphere(Vector3f pos, float radius)
	{
		this.pos = pos;
		this.radius = radius;
	}

	public boolean isInside(Vector3f point)
	{
		return pos.distanceSquared(point) < radius * radius;
	}

	public boolean isOutside(Vector3f point)
	{
		return pos.distanceSquared(point) > radius * radius;
	}

	public boolean isOnSphere(Vector3f point)
	{
		return pos.distanceSquared(point) == radius * radius;
	}
}
