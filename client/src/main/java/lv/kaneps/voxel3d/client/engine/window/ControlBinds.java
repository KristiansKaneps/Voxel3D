package lv.kaneps.voxel3d.client.engine.window;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class ControlBinds
{
	protected final Map<Control, Integer> binds = new HashMap<>();

	protected Window window;

	public ControlBinds(Window window)
	{
		this.window = window;
	}

	public void load()
	{
		// todo: load from user file
		for(Control c : Control.values())
			if(c.isDefaultGlfwKeyPresent())
				binds.put(c, c.getDefaultGlfwKey());
	}

	public void bind(int glfwKey, Control control)
	{
		binds.put(control, glfwKey);
	}

	public boolean isPressed(Control control)
	{
		int glfwKey = binds.getOrDefault(control, control.getDefaultGlfwKey());
		return glfwKey != -1 && glfwGetKey(window.getHandle(), glfwKey) == GLFW_PRESS;
	}

	public boolean isReleased(Control control)
	{
		int glfwKey = binds.getOrDefault(control, control.getDefaultGlfwKey());
		return glfwKey != -1 && glfwGetKey(window.getHandle(), glfwKey) == GLFW_RELEASE;
	}

	public boolean isRepeated(Control control)
	{
		int glfwKey = binds.getOrDefault(control, control.getDefaultGlfwKey());
		return glfwKey != -1 && glfwGetKey(window.getHandle(), glfwKey) == GLFW_REPEAT;
	}
}
