package lv.kaneps.voxel3d.server.net.decoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lv.kaneps.voxel3d.server.net.Server;

public abstract class AbstractDecoder extends ByteToMessageDecoder
{
	public final Server server;

	public AbstractDecoder(Server server)
	{
		this.server = server;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
	}
}
