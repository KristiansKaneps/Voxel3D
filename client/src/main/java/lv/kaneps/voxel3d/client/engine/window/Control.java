package lv.kaneps.voxel3d.client.engine.window;

import static org.lwjgl.glfw.GLFW.*;

public enum Control
{
	EXIT_GAME(GLFW_KEY_Q),

	WALK_FORWARD(GLFW_KEY_W),
	WALK_BACKWARD(GLFW_KEY_S),
	WALK_LEFT(GLFW_KEY_A),
	WALK_RIGHT(GLFW_KEY_D),

	JUMP(GLFW_KEY_SPACE),
	CROUCH(GLFW_KEY_LEFT_SHIFT),
	SPRINT(GLFW_KEY_LEFT_CONTROL),

	ATTACK(GLFW_MOUSE_BUTTON_LEFT),
	CHOOSE(GLFW_MOUSE_BUTTON_MIDDLE),
	SCOPE(GLFW_MOUSE_BUTTON_RIGHT);

	private final int defGlfwKey;
	private final boolean defKeyPresent;

	Control(int defGlfwKey)
	{
		this.defGlfwKey = defGlfwKey;
		defKeyPresent = true;
	}

	Control()
	{
		defGlfwKey = -1;
		defKeyPresent = false;
	}

	public int getDefaultGlfwKey()
	{
		return defGlfwKey;
	}

	public boolean isDefaultGlfwKeyPresent()
	{
		return defKeyPresent;
	}
}
