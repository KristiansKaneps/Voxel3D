package lv.kaneps.voxel3d.client.engine.graphics;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class FrustumCullingFilter
{
	protected final Matrix4f projViewMatrix;
	protected FrustumIntersection frustumIntersection;

	public FrustumCullingFilter()
	{
		projViewMatrix = new Matrix4f();
		frustumIntersection = new FrustumIntersection();
	}

	public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix)
	{
		// calc proj view matrix
		projViewMatrix.set(projMatrix);
		projViewMatrix.mul(viewMatrix);

		// get frustum planes
		frustumIntersection.set(projViewMatrix);
	}

	public boolean insideFrustum(float x0, float y0, float z0, float boundingRadius)
	{
		return frustumIntersection.testSphere(x0, y0, z0, boundingRadius);
	}

	public <T extends IFrustumFilterable> void filter(T obj, float meshBoundingRadius)
	{
		Vector3f pos = obj.getFrustumPosition();
		obj.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, obj.getFrustumScale() * meshBoundingRadius));
	}

	public <T extends IFrustumFilterable> void filter(T obj)
	{
		Vector3f pos = obj.getFrustumPosition();
		obj.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, obj.getFrustumScale() * obj.getFrustumBoundingRadius()));
	}

	public <T extends IFrustumFilterable> void filter(T[] objects, float meshBoundingRadius)
	{
		float boundingRadius;
		Vector3f pos;
		for(T obj : objects)
		{
			boundingRadius = obj.getFrustumScale() * meshBoundingRadius;
			pos = obj.getFrustumPosition();
			obj.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
		}
	}

	public <T extends IFrustumFilterable> void filter(T[] objects)
	{
		float boundingRadius;
		Vector3f pos;
		for(T obj : objects)
		{
			boundingRadius = obj.getFrustumScale() * obj.getFrustumBoundingRadius();
			pos = obj.getFrustumPosition();
			obj.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
		}
	}

	public <T extends IFrustumFilterable> void filter(List<T> objects, float meshBoundingRadius)
	{
		float boundingRadius;
		Vector3f pos;
		for(IFrustumFilterable obj : objects)
		{
			boundingRadius = obj.getFrustumScale() * meshBoundingRadius;
			pos = obj.getFrustumPosition();
			obj.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
		}
	}

	public <T extends IFrustumFilterable> void filter(List<T> objects)
	{
		float boundingRadius;
		Vector3f pos;
		for(IFrustumFilterable obj : objects)
		{
			boundingRadius = obj.getFrustumScale() * obj.getFrustumBoundingRadius();
			pos = obj.getFrustumPosition();
			obj.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
		}
	}
}
