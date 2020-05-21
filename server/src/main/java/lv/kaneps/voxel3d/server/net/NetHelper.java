package lv.kaneps.voxel3d.server.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.server.world.entity.Player;

public final class NetHelper
{
	private NetHelper() {}

	public static Player getPlayer(Server server, Channel channel)
	{
		return server.gs.getPlayer(server.getPlayerMapping(channel.id().asLongText()));
	}

	public static Player getPlayer(Server server, ChannelHandlerContext context)
	{
		return getPlayer(server, context.channel());
	}

	public static void removePlayer(Server server, Channel channel)
	{
		String channelId = channel.id().asLongText();
		int playerId = server.getPlayerMapping(channelId);
		server.removePlayerMapping(channelId);
		server.gs.removePlayer(playerId);
	}

	public static void removePlayer(Server server, ChannelHandlerContext context)
	{
		removePlayer(server, context.channel());
	}

	public static void removePlayer(Server server, Player player)
	{
		String channelId = player.conn.channel.id().asLongText();
		server.removePlayerMapping(channelId);
		server.gs.removePlayer(player.id);
	}
}
