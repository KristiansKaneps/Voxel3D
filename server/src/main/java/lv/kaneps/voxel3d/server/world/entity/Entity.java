package lv.kaneps.voxel3d.server.world.entity;

import lv.kaneps.voxel3d.server.GameServer;
import lv.kaneps.voxel3d.server.world.ITickable;
import lv.kaneps.voxel3d.server.world.UIDGen;
import org.joml.Quaternionf;

public class Entity implements ITickable
{
	public Location loc = new Location(0, 0, 0);
	public Quaternionf rot = new Quaternionf();

	public final EntityType entityType;

	public final int id;

	public Entity(EntityType entityType)
	{
		this.id = UIDGen.newEntity();
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

	/**
	 * @return this entity's rotation copy
	 */
	public Quaternionf getRotation()
	{
		return new Quaternionf(rot.x, rot.y, rot.z, rot.w);
	}

	@Override
	public void tick(GameServer gs, double dt, double elapsed)
	{

	}
}
