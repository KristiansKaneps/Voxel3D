package lv.kaneps.voxel3d.client.world.block;

import lv.kaneps.voxel3d.client.engine.graphics.*;

import static lv.kaneps.voxel3d.client.engine.graphics.TextureEnum.BLOCK_STONE;

public enum BlockEnum
{
	STONE(BLOCK_STONE, 0f);

	public final TextureEnum tex;
	public final float reflectance;

	BlockEnum(TextureEnum tex, float reflectance)
	{
		this.tex = tex;
		this.reflectance = reflectance;
	}

	public int getId()
	{
		return ordinal() + 1;
	}

	public static BlockEnum byId(int id)
	{
		return values()[id - 1];
	}
}
