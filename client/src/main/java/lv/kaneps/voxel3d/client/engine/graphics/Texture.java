package lv.kaneps.voxel3d.client.engine.graphics;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
	public int texBank = GL_TEXTURE0;

	public int width, height, channels;

	private final int id;

	public Texture(int width, int height, int pixelFormat) throws Exception
	{
		this.id = glGenTextures();
		this.width = width;
		this.height = height;
		glBindTexture(GL_TEXTURE_2D, this.id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	}

	public Texture(String fileName) throws Exception
	{
		this(loadTexture(fileName));
	}

	public Texture(TexParam tex)
	{
		this(tex.id, tex.width, tex.height, tex.channels);
	}

	public Texture(int id, int width, int height, int channels)
	{
		this.id = id;
		this.width = width;
		this.height = height;
		this.channels = channels;
	}

	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public int getId()
	{
		return id;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public TexParam getTexParam()
	{
		return new TexParam(id, width, height, channels);
	}

	private static TexParam loadTexture(String fileName) throws Exception
	{
		int width;
		int height;
		int channels;
		ByteBuffer buf;

		// Load Texture file
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer _channels = stack.mallocInt(1);

			buf = stbi_load(fileName, w, h, _channels, 4);
			if (buf == null) throw new Exception("Image file [" + fileName  + "] not loaded: " + stbi_failure_reason());

			width = w.get();
			height = h.get();
			channels = 4;
		}

		int textureId = createTexture(buf, width, height);

		stbi_image_free(buf);

		return new TexParam(textureId, width, height, channels);
	}

	private static int createTexture(ByteBuffer buf, int width, int height)
	{
		// Create a new OpenGL texture
		int textureId = glGenTextures();
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, textureId);

		// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Upload the texture data
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		// Generate Mip Map
		glGenerateMipmap(GL_TEXTURE_2D);

		return textureId;
	}

	public void cleanup()
	{
		glDeleteTextures(id);
	}

	public static class TexParam
	{
		public final int id;
		public final int width, height;
		public final int channels;

		private TexParam(int id, int width, int height, int channels)
		{
			this.id = id;
			this.width = width;
			this.height = height;
			this.channels = channels;
		}
	}
}
