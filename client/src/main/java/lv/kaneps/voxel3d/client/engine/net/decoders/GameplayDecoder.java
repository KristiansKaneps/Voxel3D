package lv.kaneps.voxel3d.client.engine.net.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.engine.net.messages.*;
import lv.kaneps.voxel3d.client.world.block.BlockPos;
import lv.kaneps.voxel3d.client.world.chunk.ChunkPos;
import lv.kaneps.voxel3d.client.world.entity.EntityType;
import lv.kaneps.voxel3d.client.world.entity.Location;
import lv.kaneps.voxel3d.common.net.PacketType;
import org.joml.Quaternionf;

import java.util.List;

public class GameplayDecoder extends AbstractDecoder
{
	private PacketType type = null;

	private int cp = 0;

	private static final int BLOCK_DATA_LEN = 4 * 4;
	private byte worldId;
	private int chunkId;
	private ChunkPos chunkPos;
	private int blockCount = 0;
	private int writerIdx = 0;
	private int[] blocks;
	private BlockPos[] pos;

	public GameplayDecoder(Client client)
	{
		super(client);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		if(type != null)
		{
			switch(type)
			{
				case CHUNK_UPDATE:
					ChunkUpdatePacket cMsg = decodeChunkMessage(in);
					if(cMsg == null) return;
					out.add(cMsg);
					break;
				case CHUNK_UNLOAD:
					if(in.readableBytes() < 5) return;
					out.add(new ChunkUnloadPacket(in.readByte(), in.readInt()));
					break;
				case WORLD_MISC:
					if(in.readableBytes() < 5) return;
					out.add(new WorldMiscPacket(in.readByte(), in.readFloat()));
					break;
				case PLAYER_UPDATE:
					if(in.readableBytes() < 28) return;
					out.add(new PlayerUpdatePacket(
							new Location(in.readFloat(), in.readFloat(), in.readFloat()),
							new Quaternionf(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat())
					));
					break;
				case ENTITY_UPDATE:
					if(in.readableBytes() < 37) return;
					out.add(new EntityUpdatePacket(
							in.readInt(), in.readByte(), EntityType.fromInt(in.readInt()),
							new Location(in.readFloat(), in.readFloat(), in.readFloat()),
							new Quaternionf(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat())
					));
					break;
			}

			type = null;
			return;
		}

		if(in.readableBytes() < 1) return;
		type = PacketType.fromByte(in.readByte());
	}

	private ChunkUpdatePacket decodeChunkMessage(ByteBuf in)
	{
		if(cp == 0)
		{
			if (in.readableBytes() < 6) return null;

			worldId = in.readByte();
			chunkId = in.readInt();

			cp = 1;
		}

		if(cp == 1)
		{
			if(in.readableBytes() < 16) return null;

			chunkPos = new ChunkPos(in.readInt(), in.readInt(), in.readInt());
			blockCount = in.readInt();

			blocks = new int[blockCount];
			pos = new BlockPos[blockCount];

			cp = 2;
		}

		if(cp == 2)
		{
			if(in.readableBytes() < blockCount * BLOCK_DATA_LEN) return null;
			for(; writerIdx < blocks.length; writerIdx++)
			{
				blocks[writerIdx] = in.readInt();
				pos[writerIdx] = new BlockPos(in.readInt(), in.readInt(), in.readInt());
			}
			writerIdx = 0;

			cp = 0;
			return new ChunkUpdatePacket(worldId, chunkId, chunkPos, blocks, pos);
		}
		return null;
	}
}
