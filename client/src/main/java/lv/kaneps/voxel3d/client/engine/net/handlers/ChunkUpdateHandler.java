package lv.kaneps.voxel3d.client.engine.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.engine.net.messages.ChunkUpdatePacket;
import lv.kaneps.voxel3d.client.world.World;
import lv.kaneps.voxel3d.client.world.chunk.ChunkDeserializer;

import java.util.List;

public class ChunkUpdateHandler extends AbstractHandler<ChunkUpdatePacket>
{
	public ChunkUpdateHandler(Client client)
	{
		super(client);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ChunkUpdatePacket msg, List<Object> out) throws Exception
	{
		World w = client.getGame().getScene().getWorld();

		if(w != null && w.worldId == msg.worldId)
		{
			System.out.println("Loading chunk at worldId=" + msg.worldId + " chunkPos=" + msg.chunkPos);
			w.addChunk(ChunkDeserializer.deserialize(msg));
		}
	}
}
