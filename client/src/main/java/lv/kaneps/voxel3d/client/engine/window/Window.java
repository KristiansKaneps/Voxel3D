package lv.kaneps.voxel3d.client.engine.window;

import lv.kaneps.voxel3d.client.engine.GameEngine;
import lv.kaneps.voxel3d.client.engine.util.Color;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
	protected final List<ICloseListener> closeListeners = new ArrayList<>();
	protected final List<IResizeListener> resizeListeners = new ArrayList<>();
	protected IMouseListener mouseListener;

	// const reference mouse input container
	protected final MouseInput mouseInput;

	protected long hWindow;

	protected final GameEngine engine;
	protected String title;
	protected int width, height;
	protected boolean resizable, vSync;

	public Window(GameEngine engine, String title, int width, int height, boolean resizable, boolean vSync)
	{
		this.engine = engine;
		this.title = title;
		this.width = width;
		this.height = height;
		this.resizable = resizable;
		this.vSync = vSync;

		mouseInput = new MouseInput();
	}

	public long getHandle()
	{
		return hWindow;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public String getTitle()
	{
		return title;
	}

	public void init()
	{
		GLFWErrorCallback.createPrint(System.err).set();

		if(!glfwInit()) throw new IllegalStateException("Unable to init GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);

		hWindow = glfwCreateWindow(1280, 720, title, NULL, NULL);
		if(hWindow == NULL) throw new RuntimeException("Failed to create the GLFW window");

		// resize listener
		glfwSetWindowSizeCallback(hWindow, (window, w, h) -> {
			if(w <= 0 || h <= 0) return;
			width = w;
			height = h;
			synchronized (resizeListeners)
			{
				System.out.println("Window resize: " + w + "x" + h);
				for(IResizeListener l : resizeListeners)
					l.onResize(w, h);
			}
		});

		// close listener
		glfwSetWindowCloseCallback(hWindow, window -> {
			System.out.println("Window close");
			synchronized (closeListeners)
			{
				for(ICloseListener l : closeListeners)
					l.onClose();
			}
		});

		try(MemoryStack stack = stackPush())
		{
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
			IntBuffer pX0 = stack.mallocInt(1); // int*
			IntBuffer pY0 = stack.mallocInt(1); // int*
			pWidth.rewind(); pHeight.rewind(); pX0.rewind(); pY0.rewind();

			// get passed window size
			glfwGetWindowSize(hWindow, pWidth, pHeight);

			// get location of primary monitor
			glfwGetMonitorPos(glfwGetPrimaryMonitor(), pX0, pY0);

			// monitor res
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// window center location
			int xpos = pX0.get() + (vidMode.width() - pWidth.get()) / 2;
			int ypos = pY0.get() + (vidMode.height() - pHeight.get()) / 2;

			// center
			glfwSetWindowPos(hWindow, xpos, ypos);
		}

		// set opengl ctx
		glfwMakeContextCurrent(hWindow);

		// enable vsync
		glfwSwapInterval(vSync ? 1 : 0);

		// create capabilities
		GL.createCapabilities();

		// enable alpha blend
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// enable depth test
		glEnable(GL_DEPTH_TEST);

		// set clear color
		Color.SKY_BLUE.glClearColor();

		// output some info
		System.out.println("OS name: " + System.getProperty("os.name"));
		System.out.println("OS version: " + System.getProperty("os.version"));
		System.out.println("LWJGL version: " + org.lwjgl.Version.getVersion());
		System.out.println("OpenGL version: " + glGetString(GL_VERSION));
	}

	public void cleanup()
	{
		// free window callbacks
		glfwFreeCallbacks(hWindow);
		glfwDestroyWindow(hWindow);

		// terminate glfw
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void show()
	{
		// show window
		glfwShowWindow(hWindow);
	}

	public void hide()
	{
		// hide window
		glfwHideWindow(hWindow);
	}

	public void close()
	{
		// close window
		glfwSetWindowShouldClose(hWindow, true);
	}

	public boolean shouldClose()
	{
		return glfwWindowShouldClose(hWindow);
	}

	public void swapBuffers()
	{
		// swap color buffers
		glfwSwapBuffers(hWindow);
	}

	public void pollEvents()
	{
		// poll for window events (input etc.)
		glfwPollEvents();

		if(mouseListener == null)
			return;

		mouseInput.displVec.x = 0;
		mouseInput.displVec.y = 0;

		boolean rotateX = false, rotateY = false;

		if (mouseInput.previousPos.x > 0 && mouseInput.previousPos.y > 0 && mouseInput.inWindow)
		{
			double dx = mouseInput.currentPos.x - mouseInput.previousPos.x;
			double dy = mouseInput.currentPos.y - mouseInput.previousPos.y;
			rotateX = dx != 0;
			rotateY = dy != 0;
			if (rotateX)
				mouseInput.displVec.y = (float) dx;
			if (rotateY)
				mouseInput.displVec.x = (float) dy;
		}

		mouseListener.onMouseInput(mouseInput);

		mouseInput.previousPos.x = mouseInput.currentPos.x;
		mouseInput.previousPos.y = mouseInput.currentPos.y;
	}

	public void addCloseListener(ICloseListener listener)
	{
		synchronized (closeListeners)
		{
			closeListeners.add(listener);
		}
	}

	public void removeCloseListener(ICloseListener listener)
	{
		synchronized (closeListeners)
		{
			closeListeners.remove(listener);
		}
	}

	public void addResizeListener(IResizeListener listener)
	{
		synchronized (resizeListeners)
		{
			resizeListeners.add(listener);
		}
	}

	public void removeResizeListener(IResizeListener listener)
	{
		synchronized (resizeListeners)
		{
			resizeListeners.remove(listener);
		}
	}

	public void setMouseInput(IMouseListener listener)
	{
		boolean glfwInitMouseInput = this.mouseListener == null;
		this.mouseListener = listener;
		if(glfwInitMouseInput)
		{
			glfwSetInputMode(hWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
			glfwSetCursorPosCallback(hWindow, (windowHandle, xpos, ypos) -> {
				mouseInput.currentPos.x = xpos;
				mouseInput.currentPos.y = ypos;
			});
			glfwSetCursorEnterCallback(hWindow, (windowHandle, entered) -> {
				mouseInput.inWindow = entered;
			});
			glfwSetMouseButtonCallback(hWindow, (windowHandle, button, action, mode) -> {
				mouseInput.leftButtonPressed = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS;
				mouseInput.middleButtonPressed = button == GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW_PRESS;
				mouseInput.rightButtonPressed = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS;
			});
		}
	}
}
