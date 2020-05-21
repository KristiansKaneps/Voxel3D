package lv.kaneps.voxel3d.server.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lv.kaneps.voxel3d.server.net.Server;

public abstract class AbstractHandler<T> extends MessageToMessageDecoder<T>
{
	public final Server server;

	public AbstractHandler(Server server)
	{
		this.server = server;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
	}
}
