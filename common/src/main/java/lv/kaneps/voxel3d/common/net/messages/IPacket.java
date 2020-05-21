package lv.kaneps.voxel3d.common.net.messages;

import io.netty.buffer.ByteBuf;
import lv.kaneps.voxel3d.common.net.PacketType;

public interface IPacket
{
	PacketType getType();
	void serialize(ByteBuf out);
}
