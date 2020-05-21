package lv.kaneps.voxel3d.client.engine.graphics;

import org.joml.Vector3f;

public interface IFrustumFilterable
{
	float getFrustumBoundingRadius();
	float getFrustumScale();
	Vector3f getFrustumPosition();

	boolean isInsideFrustum();
	void setInsideFrustum(boolean insideFrustum);

	boolean isFrustumCullingDisabled();
	void disableFrustumCulling();
	void enableFrustumCulling();
}
