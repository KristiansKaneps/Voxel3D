package lv.kaneps.voxel3d.client.engine.graphics.font;

import lv.kaneps.voxel3d.client.engine.util.GraphicsUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public enum FontEnum
{
	ARIAL(12, "client/textures/fonts/arial.png", "fonts/arial.fnt"),
	ARIAL_BOLD(12, "client/textures/fonts/arial_bold.png", "fonts/arial_bold.fnt"),
	ARIAL_BLACK_HQ(64, "client/textures/fonts/arial_black_hq.png", "fonts/arial_black_hq.fnt");

	private FontCharPos loadedFnt;

	public final int pt;
	public final String pngFilename, fntFilename;

	FontEnum(int pt, String pngFilename, String fntFilename)
	{
		this.pt = pt;
		this.pngFilename = pngFilename;
		this.fntFilename = fntFilename;
	}

	public float getPreferredScale()
	{
		return 12f / pt;
	}

	public FontCharPos getFontCharPos() throws Exception
	{
		if(loadedFnt != null)
			return loadedFnt;
		FontCharPos fnt = parseFnt();
		loadedFnt = fnt;
		return fnt;
	}

	private FontCharPos parseFnt() throws Exception
	{
		try (BufferedReader br = new BufferedReader(new InputStreamReader(GraphicsUtils.asInputStream(fntFilename))))
		{
			List<Integer> character = new ArrayList<>();
			List<Integer> xpos = new ArrayList<>(), ypos = new ArrayList<>();
			List<Integer> width = new ArrayList<>(), height = new ArrayList<>();
			List<Integer> xoffset = new ArrayList<>(), yoffset = new ArrayList<>();
			List<Integer> origwidth = new ArrayList<>(), origheight = new ArrayList<>();
			String line;
			while((line = br.readLine()) != null)
			{
				int numIdx = 0;
				String numStr = "";
				byte[] chars = line.getBytes(StandardCharsets.ISO_8859_1);
				for(int i = 0; i < chars.length + 1; i++)
				{
					if(i >= chars.length || chars[i] == '\t')
					{
						if(numStr.isEmpty())
							continue;

						int num = Integer.parseInt(numStr);
						numStr = "";

						switch (numIdx)
						{
							case 0:
								character.add(num);
								break;
							case 1:
								xpos.add(num);
								break;
							case 2:
								ypos.add(num);
								break;
							case 3:
								width.add(num);
								break;
							case 4:
								height.add(num);
								break;
							case 5:
								xoffset.add(num);
								break;
							case 6:
								yoffset.add(num);
								break;
							case 7:
								origwidth.add(num);
								break;
							case 8:
								origheight.add(num);
								break;
							default:
								throw new IllegalArgumentException("Unrecognized token while parsing {font}.fnt: " + fntFilename);
						}

						numIdx++;
						continue;
					}

					numStr += new String(new byte[]{chars[i]}, StandardCharsets.ISO_8859_1);
				}
			}
			return new FontCharPos(
					character.stream().mapToInt(Integer::intValue).toArray(),
					xpos.stream().mapToInt(Integer::intValue).toArray(),
					ypos.stream().mapToInt(Integer::intValue).toArray(),
					width.stream().mapToInt(Integer::intValue).toArray(),
					height.stream().mapToInt(Integer::intValue).toArray(),
					xoffset.stream().mapToInt(Integer::intValue).toArray(),
					yoffset.stream().mapToInt(Integer::intValue).toArray(),
					origwidth.stream().mapToInt(Integer::intValue).toArray(),
					origheight.stream().mapToInt(Integer::intValue).toArray()
			);
		}
	}
}
