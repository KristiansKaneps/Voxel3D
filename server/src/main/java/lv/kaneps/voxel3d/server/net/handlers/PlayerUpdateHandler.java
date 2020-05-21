package lv.kaneps.voxel3d.server.net.handlers;

import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.server.net.NetHelper;
import lv.kaneps.voxel3d.server.net.Server;
import lv.kaneps.voxel3d.server.net.messages.ChunkUpdatePacket;
import lv.kaneps.voxel3d.server.net.messages.EntityUpdatePacket;
import lv.kaneps.voxel3d.server.net.messages.PlayerUpdatePacket;
import lv.kaneps.voxel3d.server.utils.Log;
import lv.kaneps.voxel3d.server.world.World;
import lv.kaneps.voxel3d.server.world.WorldData;
import lv.kaneps.voxel3d.server.world.chunk.Chunk;
import lv.kaneps.voxel3d.server.world.chunk.ChunkData;
import lv.kaneps.voxel3d.server.world.entity.Entity;
import lv.kaneps.voxel3d.server.world.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerUpdateHandler extends AbstractHandler<PlayerUpdatePacket>
{
	protected final AtomicBoolean updateChunks = new AtomicBoolean(true);

	protected final AtomicInteger last_cx = new AtomicInteger(0);
	protected final AtomicInteger last_cy = new AtomicInteger(0);
	protected final AtomicInteger last_cz = new AtomicInteger(0);

	public PlayerUpdateHandler(Server server)
	{
		super(server);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, PlayerUpdatePacket msg, List<Object> out) throws Exception
	{
		World world = server.gs.logic.world;
		Player p = NetHelper.getPlayer(server, ctx);
		p.setLocation(msg.loc);
		p.setRotation(msg.rot);

		int cx = Math.floorDiv((int) msg.loc.x, Chunk.WIDTH);
		int cy = Math.floorDiv((int) msg.loc.y, Chunk.HEIGHT);
		int cz = Math.floorDiv((int) msg.loc.z, Chunk.DEPTH);

		if (!updateChunks.get() && last_cx.get() == cx && last_cy.get() == cy && last_cz.get() == cz)
			return;

		updateChunks.set(false);
		last_cx.set(cx);
		last_cy.set(cy);
		last_cz.set(cz);

		server.gs.getScheduler().runSyncTask(() -> {
			Log.d("PlayerUpdateHandler", ".runSyncTask(() -> {...})");

			WorldData data = world.serializeWorldDataAt(p, server.gs.viewDistance);

			int size = data.chunks.size();
			Log.d("PlayerUpdateHandler", "Sending " + size + " chunks to client");

			for(int i = 0; i < size; i++)
			{
				ChunkData chunk = data.chunks.get(i);
				ctx.writeAndFlush(new ChunkUpdatePacket(world.worldId, chunk.pos, chunk));
			}

			size = data.entities.size();
			Log.d("PlayerUpdateHandler", "Sending " + size + " entities to client");

			for(int i = 0; i < size; i++)
			{
				Entity entity = data.entities.get(i);
				ctx.writeAndFlush(new EntityUpdatePacket(entity.id, world.worldId, entity.entityType, entity.loc, entity.rot));
			}
		});
	}
}
