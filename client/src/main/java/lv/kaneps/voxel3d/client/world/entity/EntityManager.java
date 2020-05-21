package lv.kaneps.voxel3d.client.world.entity;

import lv.kaneps.voxel3d.client.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager
{
	protected final Object lock = new Object();
	protected final Map<Integer, Entity> entityIds = new HashMap<>();
	protected final List<Entity> entities = new ArrayList<>();

	public final World world;

	public EntityManager(World world)
	{
		this.world = world;
	}

	public void forEachEntity(EntityConsumer consumer) throws Exception
	{
		synchronized (lock)
		{
			for (Entity entity : entities)
				consumer.accept(entity);
		}
	}

	public List<Entity> getEntities()
	{
		return entities;
	}

	public Entity getEntity(int entityId, EntityType entityType)
	{
		synchronized (lock)
		{
			if (entityIds.containsKey(entityId))
				return entityIds.get(entityId);
			Entity entity = new Entity(entityId, entityType);
			entityIds.put(entityId, entity);
			entities.add(entity);
			return entity;
		}
	}

	public Player addPlayer(int entityId, String name)
	{
		synchronized (lock)
		{
			Player entity = new Player(entityId, name);
			entityIds.put(entityId, entity);
			entities.add(entity);
			return entity;
		}
	}

	public void removeEntity(int entityId)
	{
		synchronized (lock)
		{
			if(!entityIds.containsKey(entityId))
				return;
			Entity entity = entityIds.get(entityId);
			entities.remove(entity);
			entityIds.remove(entityId);
		}
	}

	public void cleanup()
	{
		synchronized (lock)
		{
			for (Entity entity : entities)
				entity.cleanup();
		}
	}
}
