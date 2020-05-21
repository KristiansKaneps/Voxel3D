package lv.kaneps.voxel3d.server.world.entity;

public enum EntityType
{
	PLAYER;

	public int toInt()
	{
		return ordinal();
	}

	public static EntityType fromInt(int value)
	{
		return values()[value];
	}
}
