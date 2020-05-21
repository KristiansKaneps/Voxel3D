package lv.kaneps.voxel3d.client.engine.util;

import org.lwjgl.opengl.GL11;

public class Color
{
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color BLUE = new Color(0, 0, 255);
	public static final Color SKY_BLUE = new Color(0.53f, 0.81f, 0.915f, 0.0f);

	public int r, g, b, a;

	public Color(float r, float g, float b, float a)
	{
		this.r = (int) (r * 255);
		this.g = (int) (g * 255);
		this.b = (int) (b * 255);
		this.a = (int) (a * 255);
	}

	public Color(float r, float g, float b)
	{
		this(r, g, b, 1f);
	}

	public Color(int r, int g, int b, int a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Color(int r, int g, int b)
	{
		this(r, g, b, 255);
	}

	public void glClearColor()
	{
		GL11.glClearColor(redFloat(), greenFloat(), blueFloat(), alphaFloat());
	}

	public void glColor()
	{
		GL11.glColor4f(redFloat(), greenFloat(), blueFloat(), alphaFloat());
	}

	public float redFloat()
	{
		return (float) r / 255;
	}

	public float greenFloat()
	{
		return (float) g / 255;
	}

	public float blueFloat()
	{
		return (float) b / 255;
	}

	public float alphaFloat()
	{
		return (float) a / 255;
	}

	public Color darken(float factor)
	{
		int r = (int) (this.r * factor);
		int g = (int) (this.g * factor);
		int b = (int) (this.b * factor);
		if(r > 255) r = 255;
		if(g > 255) g = 255;
		if(b > 255) b = 255;
		return new Color(r, g, b, a);
	}

	public Color lighten(float factor)
	{
		return darken(1f / factor);
	}
}
