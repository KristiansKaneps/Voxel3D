package lv.kaneps.voxel3d.server.world;

import lv.kaneps.voxel3d.server.GameServer;
import lv.kaneps.voxel3d.server.net.messages.WorldMiscPacket;
import lv.kaneps.voxel3d.server.world.block.Block;
import lv.kaneps.voxel3d.server.world.block.BlockPos;
import lv.kaneps.voxel3d.server.world.block.BlockState;
import lv.kaneps.voxel3d.server.world.chunk.Chunk;
import lv.kaneps.voxel3d.server.world.chunk.ChunkManager;
import lv.kaneps.voxel3d.server.world.chunk.ChunkPos;
import lv.kaneps.voxel3d.server.world.chunk.ChunkSerializer;
import lv.kaneps.voxel3d.server.world.entity.Entity;
import lv.kaneps.voxel3d.server.world.entity.Location;
import lv.kaneps.voxel3d.server.world.entity.Player;
import lv.kaneps.voxel3d.server.world.gen.IWorldGenerator;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class World implements ITickable
{
	public final Vector3f gravity = new Vector3f(0, 9.8f, 0);

	protected float sunAngle = -90f;

	protected final List<Entity> entities = new ArrayList<>();

	protected final ChunkManager chunkManager;

	protected IWorldGenerator generator;

	public final byte worldId;
	public final String name;

	public World(byte worldId, String name)
	{
		this.worldId = worldId;
		this.name = name;

		this.chunkManager = new ChunkManager(this);
	}

	public IWorldGenerator getGenerator()
	{
		return generator;
	}

	public void setGenerator(IWorldGenerator generator)
	{
		this.generator = generator;
	}

	public void setBlock(BlockPos pos, Block block)
	{
		int chunkX = Math.floorDiv(pos.x, Chunk.WIDTH);
		int chunkY = Math.floorDiv(pos.y, Chunk.HEIGHT);
		int chunkZ = Math.floorDiv(pos.z, Chunk.DEPTH);
		int blockX = pos.x % Chunk.WIDTH;
		int blockY = pos.y % Chunk.HEIGHT;
		int blockZ = pos.z % Chunk.DEPTH;
		Chunk chunk = chunkManager.getChunk(new ChunkPos(chunkX, chunkY, chunkZ));
		chunk.setBlock(blockX, blockY, blockZ, block);
	}

	public BlockState getBlockState(BlockPos pos)
	{
		int chunkX = Math.floorDiv(pos.x, Chunk.WIDTH);
		int chunkY = Math.floorDiv(pos.y, Chunk.HEIGHT);
		int chunkZ = Math.floorDiv(pos.z, Chunk.DEPTH);
		int blockX = pos.x % Chunk.WIDTH;
		int blockY = pos.y % Chunk.HEIGHT;
		int blockZ = pos.z % Chunk.DEPTH;
		Chunk chunk = chunkManager.getChunk(new ChunkPos(chunkX, chunkY, chunkZ));
		return chunk.getBlockState(blockX, blockY, blockZ);
	}

	public void addEntity(Entity entity)
	{
		entities.add(entity);
	}

	public void removeEntity(Entity entity)
	{
		entities.remove(entity);
	}

	public Entity getEntityAt(Location loc, float radius)
	{
		for(Entity entity : entities)
		{
			if(entity.getLocation().distanceSquared(loc.x, loc.y, loc.z) <= radius * radius)
				return entity;
		}
		return null;
	}

	public WorldData serializeWorldDataAt(Player p, float radius)
	{
		return new WorldData(worldId, ChunkSerializer.serialize(chunkManager.getChunksInRadius(p.loc, radius)), entities);
	}

	public void setSunAngle(float sunAngle)
	{
		this.sunAngle = sunAngle;
	}

	public float getSunAngle()
	{
		return sunAngle;
	}

	@Override
	public void tick(GameServer gs, double dt, double elapsedTime)
	{
		// update sun angle
		sunAngle += 1.1f;
		if (sunAngle >= 360)
			sunAngle = -90;

		// tick entities
		for(Entity entity : entities)
			entity.tick(gs, dt, elapsedTime);

		// tick chunks
		for(Chunk chunk : chunkManager.getChunks())
		{
			if(!chunk.mustUnload()) chunk.tick(gs, dt, elapsedTime);
			chunkManager.unloadChunk(chunk);
		}

		gs.broadcastPacket(new WorldMiscPacket(worldId, sunAngle));
	}
}
