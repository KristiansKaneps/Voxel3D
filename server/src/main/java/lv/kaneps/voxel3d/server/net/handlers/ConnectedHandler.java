package lv.kaneps.voxel3d.server.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.server.events.PlayerJoinEvent;
import lv.kaneps.voxel3d.server.events.PlayerQuitEvent;
import lv.kaneps.voxel3d.server.net.NetHelper;
import lv.kaneps.voxel3d.server.net.PlayerConnection;
import lv.kaneps.voxel3d.server.net.Server;
import lv.kaneps.voxel3d.server.net.messages.ConnectedPacket;
import lv.kaneps.voxel3d.server.utils.Log;
import lv.kaneps.voxel3d.server.world.entity.Location;
import lv.kaneps.voxel3d.server.world.entity.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class ConnectedHandler extends AbstractHandler<ConnectedPacket>
{
	public Player player = new Player("", null);

	public ConnectedHandler(Server server)
	{
		super(server);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelActive(ctx);
		String chId = ctx.channel().id().asLongText();
		server.setChannelContext(chId, ctx);
		ctx.writeAndFlush(new ConnectedPacket(player.id, server.gs.logic.world.worldId, server.gs.logic.world.name));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelUnregistered(ctx);
		server.gs.dispatchEvent(new PlayerQuitEvent(player));
		NetHelper.removePlayer(server, ctx);
		Log.i("Player disconnected, username=" + player.name);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ConnectedPacket msg, List<Object> out) throws Exception
	{
		Log.i("Player connected, username=" + msg.playerName);
		player.name = msg.playerName;
		player.conn = new PlayerConnection(ctx.channel());
		player.loc = new Location(0, 0, 0);
		player.rot = new Quaternionf();
		server.addPlayerMapping(ctx.channel().id().asLongText(), player.id);
		server.gs.addPlayer(player);
		server.gs.dispatchEvent(new PlayerJoinEvent(player));
	}
}
