package lv.kaneps.voxel3d.client.engine.graphics;

import java.util.HashMap;
import java.util.Map;

public enum TextureEnum
{
	BLOCK_SPRITE_MAP("client/textures/blocks/blocks.png"),
	BLOCK_STONE("client/textures/blocks/stone.png");

	private static final Map<TextureEnum, Texture> loaded = new HashMap<>();

	public final String filename;
	public final boolean texIsSpriteMap;

	TextureEnum(String filename)
	{
		this(filename, true);
	}

	TextureEnum(String filename, boolean texIsSpriteMap)
	{
		this.filename = filename;
		this.texIsSpriteMap = texIsSpriteMap;
	}

	public boolean isSpriteMap()
	{
		return texIsSpriteMap;
	}

	public Texture loadTexture() throws Exception
	{
		if(loaded.containsKey(this))
			return loaded.get(this);
		Texture tex = new Texture(filename);
		loaded.put(this, tex);
		return tex;
	}
}
