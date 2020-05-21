package lv.kaneps.voxel3d.server.world.block;

public enum Block
{
	STONE("stone"),
	GRASS("grass"),
	DIRT("dirt"),
	SAND("sand");

	public final String name;

	Block(String name)
	{
		this.name = name;
	}

	public int getId()
	{
		return ordinal() + 1;
	}

	public static Block byId(int id)
	{
		return values()[id - 1];
	}
}
