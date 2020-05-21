package lv.kaneps.voxel3d.client.engine.graphics;

public enum MeshObject
{
	CUBE("models/cube.obj");

	private Mesh mesh;

	private final String filename;

	MeshObject(String filename)
	{
		this.filename = filename;
	}

	public boolean isLoaded()
	{
		return mesh != null;
	}

	public Mesh get() throws Exception
	{
		if(mesh != null) return mesh;
		mesh = MeshOBJLoader.loadMesh(filename);
		return mesh;
	}

	public static void cleanupAll()
	{
		for(MeshObject meshObject : values())
			if(meshObject.isLoaded()) try { meshObject.get().cleanup(); } catch(Exception e) { }
	}
}
