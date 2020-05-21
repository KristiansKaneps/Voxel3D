package lv.kaneps.voxel3d.client;

import lv.kaneps.voxel3d.client.engine.GameEngine;
import lv.kaneps.voxel3d.client.engine.Renderer;
import lv.kaneps.voxel3d.client.engine.graphics.Scene;

public class Game
{
	public static final String NAME = "Voxel game";

	public static final float MOUSE_SENSITIVITY = 0.2f;
	public static final float CAMERA_POS_STEP = 0.05f;

	protected int playerEntityId = -1;
	protected String playerName = "Player" + ((int) (Math.random() * 10));

	protected final GameEngine engine;

	protected boolean running = false;

	public Game()
	{
		engine = new GameEngine(this);
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerEntityId(int playerEntityId)
	{
		this.playerEntityId = playerEntityId;
	}

	public int getPlayerEntityId()
	{
		return playerEntityId;
	}

	public Scene getScene()
	{
		return engine.getRenderer().getScene();
	}

	public Renderer getRenderer()
	{
		return engine.getRenderer();
	}

	public GameEngine getEngine()
	{
		return engine;
	}

	public void init() throws Exception
	{
		// init engine
		engine.init();
	}

	public final synchronized void start() throws Exception
	{
		if(running) return;
		running = true;

		engine.start();
	}

	public final synchronized void exit() throws Exception
	{
		if(!running) return;
		running = false;

		engine.exit();
	}
}
