package lv.kaneps.voxel3d.client.world.entity;

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
