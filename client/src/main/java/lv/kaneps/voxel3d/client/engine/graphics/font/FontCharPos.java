package lv.kaneps.voxel3d.client.engine.graphics.font;

import java.util.HashMap;
import java.util.Map;

public class FontCharPos
{
	protected final Map<Integer, Integer> indexMap = new HashMap<>();

	public final int[] character;
	public final int[] xpos, ypos;
	public final int[] width, height;
	public final int[] xoffset, yoffset;
	public final int[] origwidth, origheight;

	public FontCharPos(int[] character, int[] xpos, int[] ypos, int[] width, int[] height, int[] xoffset, int[] yoffset, int[] origwidth, int[] origheight)
	{
		this.character = character;
		this.xpos = xpos;
		this.ypos = ypos;
		this.width = width;
		this.height = height;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.origwidth = origwidth;
		this.origheight = origheight;
		index();
	}

	public int x(int c)
	{
		return xpos[getCharacterIndex(c)];
	}

	public int y(int c)
	{
		return ypos[getCharacterIndex(c)];
	}

	public int w(int c)
	{
		return width[getCharacterIndex(c)];
	}

	public int h(int c)
	{
		return height[getCharacterIndex(c)];
	}

	public int xoffs(int c)
	{
		return xoffset[getCharacterIndex(c)];
	}

	public int yoffs(int c)
	{
		return yoffset[getCharacterIndex(c)];
	}

	public int origw(int c)
	{
		return origwidth[getCharacterIndex(c)];
	}

	public int origh(int c)
	{
		return origheight[getCharacterIndex(c)];
	}

	public int getCharacterIndex(int character)
	{
		return indexMap.getOrDefault(character, 0);
	}

	private void index()
	{
		for(int i = 0; i < character.length; i++)
			indexMap.put(character[i], i);
	}
}
