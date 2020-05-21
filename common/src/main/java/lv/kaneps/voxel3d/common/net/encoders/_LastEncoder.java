package lv.kaneps.voxel3d.common.net.encoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lv.kaneps.voxel3d.common.net.messages.IPacket;

public class _LastEncoder extends MessageToByteEncoder<IPacket>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, IPacket msg, ByteBuf out) throws Exception
	{
		appendHeader(msg, out);
		msg.serialize(out);
	}

	public static void appendHeader(IPacket msg, ByteBuf out)
	{
		out.writeByte(msg.getType().toByte());
	}
}
