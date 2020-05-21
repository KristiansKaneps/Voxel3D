package lv.kaneps.voxel3d.client.engine.window;

import org.joml.Vector2d;
import org.joml.Vector2f;

public class MouseInput
{
	protected final Vector2d previousPos;
	protected final Vector2d currentPos;

	protected final Vector2f displVec;

	protected boolean inWindow = false;

	protected boolean leftButtonPressed = false;
	protected boolean middleButtonPressed = false;
	protected boolean rightButtonPressed = false;

	public MouseInput()
	{
		previousPos = new Vector2d(-1, -1);
		currentPos = new Vector2d(0, 0);
		displVec = new Vector2f();
	}

	public Vector2f getDisplVec()
	{
		return displVec;
	}

	public boolean isLeftButtonPressed()
	{
		return leftButtonPressed;
	}

	public boolean isMiddleButtonPressed()
	{
		return middleButtonPressed;
	}

	public boolean isRightButtonPressed()
	{
		return rightButtonPressed;
	}
}
