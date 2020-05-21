package lv.kaneps.voxel3d.client.engine.net.encoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lv.kaneps.voxel3d.client.engine.net.Client;

public abstract class AbstractEncoder<T> extends MessageToByteEncoder<T>
{
	public final Client client;

	public AbstractEncoder(Client client)
	{
		this.client = client;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
	}
}
