package lv.kaneps.voxel3d.client.engine.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.engine.net.messages.EntityUpdatePacket;
import lv.kaneps.voxel3d.client.world.World;
import lv.kaneps.voxel3d.client.world.entity.Entity;

import java.util.List;

public class EntityUpdateHandler extends AbstractHandler<EntityUpdatePacket>
{
	public EntityUpdateHandler(Client client)
	{
		super(client);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, EntityUpdatePacket msg, List<Object> out) throws Exception
	{
		System.out.println("EntityUpdateHandler: entity update");

		World world = client.getGame().getScene().getWorld();

		if(world != null && msg.worldId == world.worldId)
		{
			Entity entity = world.getEntity(msg.id, msg.entityType);
			entity.setLocation(msg.loc);
			entity.setRotation(msg.rot);
		}
	}
}
