package lv.kaneps.voxel3d.client.world.chunk;

import lv.kaneps.voxel3d.client.engine.IRenderCallable;
import lv.kaneps.voxel3d.client.engine.Renderer;
import lv.kaneps.voxel3d.client.engine.graphics.*;
import lv.kaneps.voxel3d.client.world.block.BlockEnum;
import lv.kaneps.voxel3d.client.world.block.BlockPos;
import lv.kaneps.voxel3d.client.world.block.BlockState;
import lv.kaneps.voxel3d.common.world.WorldConstants;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static lv.kaneps.voxel3d.common.world.WorldConstants.*;

public class Chunk implements IRenderCallable, IRenderable, IFrustumFilterable
{
	private boolean insideFrustum = false;
	private boolean disableFrustumCulling = false;

	protected Voxel[] voxels;
	protected final List<BlockState> blocks = new ArrayList<>();
	protected boolean updateMeshes = true;

	private final Quaternionf rot = new Quaternionf();

	public final int id;
	public final ChunkPos pos;

	public Chunk(int id, ChunkPos pos)
	{
		this.id = id;
		this.pos = pos;
	}

	public void addBlock(BlockState block)
	{
		blocks.add(block);
		updateMeshes = true;
	}

	public void removeBlock(BlockState block)
	{
		blocks.remove(block);
		updateMeshes = true;
	}

	public void removeBlock(BlockPos pos)
	{
		Iterator<BlockState> iterator = blocks.iterator();
		while (iterator.hasNext())
			if (iterator.next().pos.equals(pos))
			{
				iterator.remove();
				return;
			}
		updateMeshes = true;
	}

	public BlockState getBlock(BlockPos pos)
	{
		for(BlockState block : blocks)
			if(block.pos.equals(pos))
				return block;
		return null;
	}

	private void buildMesh() throws Exception
	{
		if(!updateMeshes)
		{
			System.out.println("mesh doesn't need an update");
			return;
		}

		voxels = new Voxel[blocks.size()];
		int i = 0;
		for (BlockState state : blocks)
		{
			BlockPos pos = state.pos;
			BlockEnum block = state.block;
			Voxel voxel = new Voxel(new Material(block.tex.loadTexture(), block.reflectance));
			voxel.position = pos.toVector3f();
			voxels[i++] = voxel;
		}

		updateMeshes = false;
		System.out.println("mesh updated (total states=" + i + ")");
	}

	@Override
	public void render(Renderer renderer, double dt, double elapsedTime) throws Exception
	{
		if(updateMeshes) buildMesh();

		// Update view Matrix
		Matrix4f viewMatrix = renderer.getCamera().updateViewMatrix();

		// filter visible blocks
		FrustumCullingFilter frustumFilter = renderer.getFrustumFilter();
		frustumFilter.filter(this);

		if(!isInsideFrustum() && !isFrustumCullingDisabled())
			return;

		Mesh voxelMesh = MeshObject.CUBE.get();
		Matrix4f modelViewMatrix;

		for(Voxel voxel : voxels)
		{
			// Set model view matrix for this object
			modelViewMatrix = renderer.transformation().buildModelViewMatrix(voxel, viewMatrix);
			renderer.getSceneShader().setUniform("modelViewMatrix", modelViewMatrix);
			renderer.getSceneShader().setUniform("material", voxel.material);
			voxelMesh.setMaterial(voxel.material);
			voxelMesh.render();
		}
	}

	@Override
	public float getScale()
	{
		return 0.5f;
	}

	@Override
	public Vector3f getPosition()
	{
		return new Vector3f(pos.x, pos.y, pos.z);
	}

	@Override
	public Quaternionf getRotation()
	{
		return rot;
	}

	@Override
	public float getFrustumBoundingRadius()
	{
		return WorldConstants.CHUNK_BOUNDING_RADIUS;
	}

	@Override
	public float getFrustumScale()
	{
		return 1.75f;
	}

	@Override
	public Vector3f getFrustumPosition()
	{
		return new Vector3f(pos.x * CHUNK_WIDTH, pos.y * CHUNK_HEIGHT, pos.z * CHUNK_DEPTH);
	}

	@Override
	public boolean isInsideFrustum()
	{
		return insideFrustum;
	}

	@Override
	public void setInsideFrustum(boolean insideFrustum)
	{
		this.insideFrustum = insideFrustum;
	}

	@Override
	public boolean isFrustumCullingDisabled()
	{
		return disableFrustumCulling;
	}

	@Override
	public void disableFrustumCulling()
	{
		disableFrustumCulling = true;
	}

	@Override
	public void enableFrustumCulling()
	{
		disableFrustumCulling = false;
	}

	@Override
	public void cleanup()
	{

	}
}
