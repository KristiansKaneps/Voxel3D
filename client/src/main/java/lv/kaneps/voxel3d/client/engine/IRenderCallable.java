package lv.kaneps.voxel3d.client.engine;

import lv.kaneps.voxel3d.client.engine.window.Window;

public interface IRenderCallable
{
	void render(Renderer renderer, double dt, double elapsedTime) throws Exception;

	default void onScreenResize(Window window, int width, int height) { }

	void cleanup();
}
