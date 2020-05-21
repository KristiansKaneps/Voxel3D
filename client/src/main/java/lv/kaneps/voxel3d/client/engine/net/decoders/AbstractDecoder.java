package lv.kaneps.voxel3d.client.engine.net.decoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lv.kaneps.voxel3d.client.engine.net.Client;

public abstract class AbstractDecoder extends ByteToMessageDecoder
{
	public final Client client;

	public AbstractDecoder(Client client)
	{
		this.client = client;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
	}
}
