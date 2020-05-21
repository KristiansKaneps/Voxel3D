package lv.kaneps.voxel3d.client.engine.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lv.kaneps.voxel3d.client.engine.net.Client;

public abstract class AbstractHandler<T> extends MessageToMessageDecoder<T>
{
	public final Client client;

	public AbstractHandler(Client client)
	{
		this.client = client;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
	}
}
