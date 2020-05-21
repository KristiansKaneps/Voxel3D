package lv.kaneps.voxel3d.client.engine.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.engine.net.messages.PlayerUpdatePacket;

import java.util.List;

public class PlayerUpdateHandler extends AbstractHandler<PlayerUpdatePacket>
{
	public PlayerUpdateHandler(Client client)
	{
		super(client);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, PlayerUpdatePacket msg, List<Object> out) throws Exception
	{
		System.out.println("PlayerUpdateHandler: player update");
	}
}
