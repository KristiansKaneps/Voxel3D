package lv.kaneps.voxel3d.client.engine.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.engine.net.messages.ConnectedPacket;
import lv.kaneps.voxel3d.client.world.World;

import java.util.List;

public class ConnectedHandler extends AbstractHandler<ConnectedPacket>
{
	public ConnectedHandler(Client client)
	{
		super(client);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelActive(ctx);
		client.setChannelContext(ctx);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ConnectedPacket msg, List<Object> out) throws Exception
	{
		System.out.println("ConnectedHandler: Server req ACK");
		World world = new World(msg.worldId, msg.worldName);
		world.addPlayer(msg.id, client.getGame().getPlayerName());
		client.getGame().setPlayerEntityId(msg.id);
		client.getGame().getScene().setWorld(world);
		ctx.writeAndFlush(new ConnectedPacket(client.engine.getGame().getPlayerName())).addListener(f -> client.clientAckSent = f.isSuccess());
	}
}
