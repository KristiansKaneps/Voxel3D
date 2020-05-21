package lv.kaneps.voxel3d.client.engine.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.engine.net.messages.ChunkUnloadPacket;
import lv.kaneps.voxel3d.client.world.World;

import java.util.List;

public class ChunkUnloadHandler extends AbstractHandler<ChunkUnloadPacket>
{
	public ChunkUnloadHandler(Client client)
	{
		super(client);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ChunkUnloadPacket msg, List<Object> out) throws Exception
	{
		World w = client.getGame().getScene().getWorld();

		if(w != null && w.worldId == msg.worldId)
		{
			System.out.println("Loading chunk at worldId=" + msg.worldId + " chunkId=" + msg.chunkId);
			w.unloadChunk(msg.chunkId);
		}
	}
}
