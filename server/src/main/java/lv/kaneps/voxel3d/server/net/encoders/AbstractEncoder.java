package lv.kaneps.voxel3d.server.net.encoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lv.kaneps.voxel3d.common.net.encoders._LastEncoder;
import lv.kaneps.voxel3d.common.net.messages.IPacket;
import lv.kaneps.voxel3d.server.net.Server;

public abstract class AbstractEncoder<T extends IPacket> extends MessageToByteEncoder<T>
{
	public final Server server;

	public AbstractEncoder(Server server)
	{
		this.server = server;
	}

	@Override
	protected final void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception
	{
		_LastEncoder.appendHeader(msg, out);
		encodePacket(ctx, msg, out);
	}

	public abstract void encodePacket(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
	}
}
