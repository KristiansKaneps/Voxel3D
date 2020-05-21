package lv.kaneps.voxel3d.client.engine.net.messages;

import io.netty.buffer.ByteBuf;
import lv.kaneps.voxel3d.client.world.entity.EntityType;
import lv.kaneps.voxel3d.client.world.entity.Location;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.common.net.messages.IPacket;
import org.joml.Quaternionf;

public class EntityUpdatePacket implements IPacket
{
	public int id;
	public byte worldId;
	public EntityType entityType;
	public Location loc;
	public Quaternionf rot;

	public EntityUpdatePacket(int id, byte worldId, EntityType entityType, Location loc, Quaternionf rot)
	{
		this.id = id;
		this.worldId = worldId;
		this.entityType = entityType;
		this.loc = loc;
		this.rot = rot;
	}

	@Override
	public PacketType getType()
	{
		return PacketType.ENTITY_UPDATE;
	}

	@Override
	public void serialize(ByteBuf out)
	{
		out.writeInt(id);
		out.writeByte(worldId & 0xff);
		out.writeInt(entityType.toInt());
		out.writeFloat(loc.x);
		out.writeFloat(loc.y);
		out.writeFloat(loc.z);
		out.writeFloat(rot.x);
		out.writeFloat(rot.y);
		out.writeFloat(rot.z);
		out.writeFloat(rot.w);
	}
}
