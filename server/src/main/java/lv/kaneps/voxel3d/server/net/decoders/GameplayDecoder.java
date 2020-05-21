package lv.kaneps.voxel3d.server.net.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.common.net.PacketType;
import lv.kaneps.voxel3d.server.net.Server;
import lv.kaneps.voxel3d.server.net.messages.PlayerUpdatePacket;
import lv.kaneps.voxel3d.server.world.entity.Location;
import org.joml.Quaternionf;

import java.util.List;

public class GameplayDecoder extends AbstractDecoder
{
	private PacketType type = null;

	public GameplayDecoder(Server server)
	{
		super(server);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		if(type != null)
		{
			switch (type)
			{
				case PLAYER_UPDATE:
					if(in.readableBytes() < 28) return;
					Location loc = new Location(in.readFloat(), in.readFloat(), in.readFloat());
					Quaternionf rot = new Quaternionf(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
					out.add(new PlayerUpdatePacket(loc, rot));
					break;
			}

			type = null;
			return;
		}

		if (in.readableBytes() < 1) return;
		type = PacketType.fromByte(in.readByte());
	}
}
