package lv.kaneps.voxel3d.client.engine.graphics;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * TODO: Can contain multiple game objects in a single block space
 */
public class Voxel implements IRenderable
{
	public Vector3f position;
	public Quaternionf rotation;
	public float scale;

	public final Material material;

	public Voxel(Material material)
	{
		this.material = material;
		this.rotation = new Quaternionf();
		this.scale = 0.5f;
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	@Override
	public Quaternionf getRotation()
	{
		return rotation;
	}

	@Override
	public float getScale()
	{
		return scale;
	}
}
