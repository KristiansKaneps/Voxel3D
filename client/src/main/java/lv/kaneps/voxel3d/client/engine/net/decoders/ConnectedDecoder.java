package lv.kaneps.voxel3d.client.engine.net.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.engine.net.messages.ConnectedPacket;
import lv.kaneps.voxel3d.common.net.PacketType;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConnectedDecoder extends AbstractDecoder
{
	private int cp = 0;
	private int id;
	private int worldNameLen;
	private byte worldId;
	private String worldName;

	public ConnectedDecoder(Client client)
	{
		super(client);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		if(cp == 0)
		{
			System.out.println("Connected decoder");
			if (in.readableBytes() < 7) return;
			PacketType type = PacketType.fromByte(in.readByte());
			if (type != PacketType.CONNECTED)
			{
				System.out.println("Type =/= CONNECTED");
			}

			id = in.readInt();
			worldId = in.readByte();
			worldNameLen = in.readByte() & 0xff;

			cp = 1;
		}

		if(cp == 1)
		{
			if(in.readableBytes() < worldNameLen)
				return;

			byte[] worldNameBytes = new byte[worldNameLen];
			in.readBytes(worldNameBytes);

			worldName = new String(worldNameBytes, StandardCharsets.ISO_8859_1);

			System.out.println("ConnectedDecoder: worldId=" + worldId + " worldName=" + worldName);

			out.add(new ConnectedPacket(id, worldId, worldName));
			ctx.pipeline().remove(this);

			cp = 0;
		}
	}
}
