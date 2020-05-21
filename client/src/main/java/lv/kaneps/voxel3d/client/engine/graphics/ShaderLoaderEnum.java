package lv.kaneps.voxel3d.client.engine.graphics;

import static lv.kaneps.voxel3d.client.engine.util.GraphicsUtils.loadFS;
import static lv.kaneps.voxel3d.client.engine.util.GraphicsUtils.loadVS;

public enum ShaderLoaderEnum
{
	SCENE("scene_fragment.fs", "scene_vertex.vs"),
	HUD("hud_fragment.fs", "hud_vertex.vs");

	public final String fragmentFilename, vertexFilename;

	ShaderLoaderEnum(String fragmentFilename, String vertexFilename)
	{
		this.fragmentFilename = fragmentFilename;
		this.vertexFilename = vertexFilename;
	}

	public ShaderProgram load() throws Exception
	{
		ShaderProgram shader = new ShaderProgram();
		shader.createFragmentShader(loadFS(fragmentFilename));
		shader.createVertexShader(loadVS(vertexFilename));
		shader.link();
		return shader;
	}
}
