package lv.kaneps.voxel3d.client.engine.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.engine.net.messages.WorldMiscPacket;
import lv.kaneps.voxel3d.client.world.World;

import java.util.List;

public class WorldMiscHandler extends AbstractHandler<WorldMiscPacket>
{
	public WorldMiscHandler(Client client)
	{
		super(client);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, WorldMiscPacket msg, List<Object> out) throws Exception
	{
		System.out.println("WorldMiscHandler: world (id=" + msg.worldId + ") misc");
		World world = client.engine.getGame().getScene().getWorld();
		if(world != null) world.sunAngle = msg.sunAngle;
	}
}
