package lv.kaneps.voxel3d.client.engine.graphics;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface IRenderable
{
	Vector3f getPosition();
	Quaternionf getRotation();
	float getScale();
}
