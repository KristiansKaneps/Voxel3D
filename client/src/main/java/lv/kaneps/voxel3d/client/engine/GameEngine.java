package lv.kaneps.voxel3d.client.engine;

import lv.kaneps.voxel3d.client.Game;
import lv.kaneps.voxel3d.client.engine.net.Client;
import lv.kaneps.voxel3d.client.world.World;
import lv.kaneps.voxel3d.common.Loop;
import lv.kaneps.voxel3d.client.engine.window.ControlBinds;
import lv.kaneps.voxel3d.client.engine.window.Window;

public class GameEngine
{
	protected boolean _cleanupWindow = true;
	protected Window window;
	protected ControlBinds controlBinds;

	protected Client net;

	protected Renderer renderer;

	protected Loop lRenderer;

	protected final Game game;

	public GameEngine(Game game)
	{
		this.game = game;
	}

	public Game getGame()
	{
		return game;
	}

	public Window getWindow()
	{
		return window;
	}

	public ControlBinds getControlBinds()
	{
		return controlBinds;
	}

	public Renderer getRenderer()
	{
		return renderer;
	}

	public void init()
	{
		net = Client.createClient(this, "localhost", 25588);

		// init window
		window = new Window(this, Game.NAME, 1280, 720, true, false);
		window.init();

		// init control binds
		controlBinds = new ControlBinds(window);
		controlBinds.load();

		// add window listeners
		window.addCloseListener(() -> { _cleanupWindow = false; exit(); });
		window.addResizeListener((w, h) -> renderer.onResize(window, w, h));

		renderer = new Renderer(this);

		lRenderer = new Loop(renderer, "Game Renderer");
	}

	public void start()
	{
		World world = new World((byte) 0, "world");
		renderer.getScene().setWorld(world);

		window.show();
		net.start();
		lRenderer.startOnCurrentThread();
	}

	public void stop()
	{
		net.stop();
		window.close();
		lRenderer.stop();
	}

	public void cleanup()
	{
		lRenderer.cleanup();
		if(_cleanupWindow) window.cleanup();
	}

	public void exit()
	{
		stop();
		cleanup();
	}
}
