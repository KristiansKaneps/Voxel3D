package lv.kaneps.voxel3d.common.net;

public enum PacketType
{
	CONNECTED,

	CHUNK_UPDATE,
	CHUNK_UNLOAD,

	WORLD_MISC,

	PLAYER_UPDATE,

	ENTITY_UPDATE;

	public byte toByte()
	{
		return (byte) (ordinal() & 0xff);
	}

	public static PacketType fromByte(byte b)
	{
		if(b < 0 || b >= values().length)
			return null;
		return values()[b & 0xff];
	}

	public static PacketType fromByte(int b)
	{
		if(b < 0 || b >= values().length)
			return null;
		return values()[b];
	}
}
