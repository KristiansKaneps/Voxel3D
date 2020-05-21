package lv.kaneps.voxel3d.server.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class PlayerConnection
{
	public final Channel channel;

	public PlayerConnection(Channel channel)
	{
		this.channel = channel;
	}

	public ChannelFuture writeAndFlush(Object msg)
	{
		return channel.writeAndFlush(msg);
	}
}
