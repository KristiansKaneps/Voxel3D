package lv.kaneps.voxel3d.client.engine.graphics;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class VoxelMesh
{
	private final int vaoId;

	private final int positionsVbo, texCoordsVbo, normalsVbo, indicesVbo;

	private final int vertexCount;

	private Material material;

	public VoxelMesh()
	{
		this(new float[]{
				1, -1, -1,
				1, -1, 1,
				-1, -1, 1,
				-1, -1, -1,
				1, 1, -1,
				1, 1, 1,
				-1, 1, 1,
				-1, 1, -1,
				1, -1, -1,
				1, -1, -1,
				1, -1, 1,
				1, -1, 1,
				-1, -1, -1,
				-1, -1, -1,
				1, 1, -1,
				1, 1, -1,
				-1, -1, 1,
				-1, -1, 1,
				1, 1, 1,
				1, 1, 1,
				-1, 1, 1,
				-1, 1, 1,
				-1, 1, -1,
				-1, 1, -1,
		     }, new float[]{
				0.5000f, 1.0000f,
				0.5000f, 0.5000f,
				1.0000f, 0.5000f,
				0.0000f, 0.5000f,
				0.0000f, 0.0000f,
				0.5000f, 0.0000f,
				0.0000f, 1.0000f,
				0.0002f, 1.0000f,
				0.5000f, 1.0000f,
				1.0000f, 1.0000f
		     }, new float[]{
				0.0000f, -1.000f, 0.0000f,
				0.0000f, 1.0000f, 0.0000f,
				1.0000f, 0.0000f, 0.0000f,
				0.0000f, 0.0000f, 1.0000f,
				-1.000f, 0.0000f, 0.0000f,
				0.0000f, 0.0000f, -1.000f
		     }, new int[]{

		     }
		);
	}

	public VoxelMesh(float[] positions, float[] texCoords, float[] normals, int[] indices)
	{
		vertexCount = indices.length;

		FloatBuffer posBuffer = null;
		FloatBuffer texCoordsBuffer = null;
		FloatBuffer vecNormalsBuffer = null;
		IntBuffer indicesBuffer = null;

		try
		{
			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);

			// Position VBO
			positionsVbo = glGenBuffers();
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, positionsVbo);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			// Texture coordinates VBO
			texCoordsVbo = glGenBuffers();
			texCoordsBuffer = MemoryUtil.memAllocFloat(texCoords.length);
			texCoordsBuffer.put(texCoords).flip();
			glBindBuffer(GL_ARRAY_BUFFER, texCoordsVbo);
			glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

			// Vertex normals VBO
			normalsVbo = glGenBuffers();
			vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			if (vecNormalsBuffer.capacity() > 0) vecNormalsBuffer.put(normals).flip();
			else vecNormalsBuffer = MemoryUtil.memAllocFloat(positions.length);
			glBindBuffer(GL_ARRAY_BUFFER, normalsVbo);
			glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

			// Index VBO
			indicesVbo = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVbo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		}
		finally
		{
			if (posBuffer != null)
				MemoryUtil.memFree(posBuffer);
			if (texCoordsBuffer != null)
				MemoryUtil.memFree(texCoordsBuffer);
			if (vecNormalsBuffer != null)
				MemoryUtil.memFree(vecNormalsBuffer);
			if (indicesBuffer != null)
				MemoryUtil.memFree(indicesBuffer);
		}
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	private void initRender()
	{
		Texture texture = material.getTexture();
		if (texture != null) {
			// Activate first texture bank
			glActiveTexture(GL_TEXTURE0);
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}
		Texture normalMap = material.getNormalMap();
		if (normalMap != null) {
			// Activate first texture bank
			glActiveTexture(GL_TEXTURE1);
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, normalMap.getId());
		}

		// Draw the mesh
		glBindVertexArray(getVaoId());
	}

	public void render()
	{
		initRender();

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		endRender();
	}

	private void endRender()
	{
		// Restore state
		glBindVertexArray(0);

		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void cleanup()
	{
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(positionsVbo);
		glDeleteBuffers(texCoordsVbo);
		glDeleteBuffers(normalsVbo);
		glDeleteBuffers(indicesVbo);

		// Delete the texture
		Texture texture = material.getTexture();
		if (texture != null) {
			texture.cleanup();
		}

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}

	public void deleteBuffers()
	{
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(positionsVbo);
		glDeleteBuffers(texCoordsVbo);
		glDeleteBuffers(normalsVbo);
		glDeleteBuffers(indicesVbo);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
