package lv.kaneps.voxel3d.client.engine.graphics.font;

import lv.kaneps.voxel3d.client.engine.graphics.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static lv.kaneps.voxel3d.client.engine.util.GraphicsUtils.listToArray;

public class TextObject implements IRenderable
{
	protected static final float ZPOS = 0.0f;

	protected static final int VERTICES_PER_QUAD = 4;

	protected Texture tex;
	protected Mesh mesh;

	protected Vector3f position;
	protected float scale;
	protected Quaternionf rotation;

	protected String text;
	protected FontEnum font;

	public TextObject(String text)
	{
		this(text, FontEnum.ARIAL);
	}

	public TextObject(String text, FontEnum font)
	{
		super();
		this.text = text;
		this.font = font;
		position = new Vector3f();
		scale = 1;
		rotation = new Quaternionf();
		init();
	}

	protected void init()
	{
		try
		{
			if(tex == null) tex = new Texture(font.pngFilename);
			mesh = buildMesh(tex, font);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setText(String text) throws Exception
	{
		setText(text, font);
	}

	public void setFont(FontEnum font) throws Exception
	{
		setText(text, font);
	}

	public void setText(String text, FontEnum font) throws Exception
	{
		boolean changeTex = this.font != font;
		this.text = text;
		this.font = font;

		if(changeTex)
		{
			mesh.cleanup();
			tex = null;
		}
		else
			mesh.deleteBuffers();

		init();
	}

	public String getText()
	{
		return text;
	}

	public FontEnum getFont()
	{
		return font;
	}

	public Mesh getMesh()
	{
		return mesh;
	}

	protected Mesh buildMesh(Texture tex, FontEnum font) throws Exception
	{
		byte[] chars = text.getBytes(StandardCharsets.ISO_8859_1);
		int numChars = chars.length;

		List<Float> positions = new ArrayList<>();
		List<Float> textCoords = new ArrayList<>();
		float[] normals = new float[0];
		List<Integer> indices = new ArrayList<>();

		float prevW = 0f, prevH = 0f;
		for(int i = 0; i < numChars; i++)
		{
			byte c = chars[i];
			FontCharPos pos = font.getFontCharPos();

			int wTex = tex.width;
			int hTex = tex.height;

			float x = (float) pos.x(c);
			float y = (float) pos.y(c);
			float w = (float) pos.w(c);
			float h = (float) pos.h(c);

			// left top v
			positions.add(prevW); // x
			positions.add(0f); // y
			positions.add(ZPOS); // z
			textCoords.add(x / wTex);
			textCoords.add(y / hTex);
			indices.add(i * VERTICES_PER_QUAD);

			// left bottom v
			positions.add(prevW);
			positions.add(h);
			positions.add(ZPOS);
			textCoords.add(x / wTex);
			textCoords.add((y + h) / hTex);
			indices.add(i * VERTICES_PER_QUAD + 1);

			// right bottom v
			positions.add(prevW + w);
			positions.add(h);
			positions.add(ZPOS);
			textCoords.add((x + w) / wTex);
			textCoords.add((y + h) / hTex);
			indices.add(i * VERTICES_PER_QUAD + 2);

			// right top v
			positions.add(prevW + w);
			positions.add(0f);
			positions.add(ZPOS);
			textCoords.add((x + w) / wTex);
			textCoords.add(y / hTex);
			indices.add(i * VERTICES_PER_QUAD + 3);

			// add indices for left top v and bottom right v
			indices.add(i * VERTICES_PER_QUAD);
			indices.add(i * VERTICES_PER_QUAD + 2);

			prevW += w;
			prevH += h;
		}

		Mesh mesh = new Mesh(listToArray(positions), listToArray(textCoords), normals, indices.stream().mapToInt(Integer::intValue).toArray());
		mesh.setMaterial(new Material(tex));
		return mesh;
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public void setPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	public void setRotation(Quaternionf rotation)
	{
		this.rotation = rotation;
	}

	@Override
	public Quaternionf getRotation()
	{
		return rotation;
	}

	public void setScale(float scale)
	{
		this.scale = scale;
	}

	@Override
	public float getScale()
	{
		return scale;
	}
}
