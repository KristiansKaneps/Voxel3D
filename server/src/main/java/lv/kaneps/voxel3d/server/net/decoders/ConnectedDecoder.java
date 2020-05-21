package lv.kaneps.voxel3d.server.net.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.server.net.Server;
import lv.kaneps.voxel3d.server.net.messages.ConnectedPacket;
import lv.kaneps.voxel3d.server.utils.Log;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConnectedDecoder extends AbstractDecoder
{
	private int cp = 0;
	private int playerNameLen;
	private String playerName;

	public ConnectedDecoder(Server server)
	{
		super(server);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		if(cp == 0)
		{
			if (in.readableBytes() < 2) return;
			byte bType = in.readByte();
			PacketType type = PacketType.fromByte(bType);
			if (type != PacketType.CONNECTED)
			{
				Log.w("Incoming connection warning: Type =/= CONNECTED (received bType=" + bType + " instead), starting anew.");
				return;
			}

			playerNameLen = in.readByte() & 0xff;

			cp = 1;
		}

		if(cp == 1)
		{
			if(in.readableBytes() < playerNameLen)
				return;

			byte[] playerNameBytes = new byte[playerNameLen];
			in.readBytes(playerNameBytes);

			playerName = new String(playerNameBytes, StandardCharsets.ISO_8859_1);

			out.add(new ConnectedPacket(playerName));
			ctx.pipeline().remove(this);

			cp = 0;
		}
	}
}
