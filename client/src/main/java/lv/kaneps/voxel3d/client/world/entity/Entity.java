package lv.kaneps.voxel3d.client.world.entity;

import lv.kaneps.voxel3d.client.engine.IRenderCallable;
import lv.kaneps.voxel3d.client.engine.Renderer;
import lv.kaneps.voxel3d.client.engine.graphics.IFrustumFilterable;
import lv.kaneps.voxel3d.client.engine.graphics.IRenderable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity implements IRenderCallable, IRenderable, IFrustumFilterable
{
	private boolean insideFrustum = false;
	private boolean disableFrustumCulling = false;

	public Location loc = new Location();
	public Quaternionf rot = new Quaternionf();

	public final int id;
	public final EntityType entityType;

	public Entity(int id, EntityType entityType)
	{
		this.id = id;
		this.entityType = entityType;
	}

	public void setLocation(Location loc)
	{
		this.loc.set(loc);
	}

	/**
	 * @return this entity's location copy
	 */
	public Location getLocation()
	{
		return loc.copy();
	}

	public void setRotation(Quaternionf rot)
	{
		this.rot.set(rot);
	}

	@Override
	public Vector3f getPosition()
	{
		return loc;
	}

	/**
	 * @return this entity's rotation copy
	 */
	@Override
	public Quaternionf getRotation()
	{
		return new Quaternionf(rot.x, rot.y, rot.z, rot.w);
	}

	@Override
	public void render(Renderer renderer, double dt, double elapsedTime) throws Exception
	{

	}

	@Override
	public float getScale()
	{
		return 0.5f;
	}

	@Override
	public float getFrustumBoundingRadius()
	{
		return 1.25f;
	}

	@Override
	public float getFrustumScale()
	{
		return 0.75f;
	}

	@Override
	public Vector3f getFrustumPosition()
	{
		return loc;
	}

	@Override
	public boolean isInsideFrustum()
	{
		return insideFrustum;
	}

	@Override
	public void setInsideFrustum(boolean insideFrustum)
	{
		this.insideFrustum = insideFrustum;
	}

	@Override
	public boolean isFrustumCullingDisabled()
	{
		return disableFrustumCulling;
	}

	@Override
	public void disableFrustumCulling()
	{
		disableFrustumCulling = true;
	}

	@Override
	public void enableFrustumCulling()
	{
		disableFrustumCulling = false;
	}

	@Override
	public void cleanup()
	{

	}
}
