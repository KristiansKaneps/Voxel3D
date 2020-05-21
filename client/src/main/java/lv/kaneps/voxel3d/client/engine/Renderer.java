package lv.kaneps.voxel3d.client.engine;

import lv.kaneps.voxel3d.client.engine.graphics.*;
import lv.kaneps.voxel3d.client.engine.net.messages.PlayerUpdatePacket;
import lv.kaneps.voxel3d.client.world.World;
import lv.kaneps.voxel3d.client.world.entity.Location;
import lv.kaneps.voxel3d.common.ILoopRunnable;
import lv.kaneps.voxel3d.common.ITimeSupplier;
import lv.kaneps.voxel3d.client.engine.window.*;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static lv.kaneps.voxel3d.client.Game.CAMERA_POS_STEP;
import static lv.kaneps.voxel3d.client.Game.MOUSE_SENSITIVITY;
import static org.lwjgl.opengl.GL11.*;

public class Renderer implements ILoopRunnable, ITimeSupplier, IMouseListener
{
	public static final int RENDER_DISTANCE = 1;

	/**
	 * Field of View in Radians
	 */
	public static final float FOV = (float) Math.toRadians(60.0f);

	public static final float Z_NEAR = 0.01f;
	public static final float Z_FAR = 1000.f;
	public static final int MAX_POINT_LIGHTS = 5;
	public static final int MAX_SPOT_LIGHTS = 5;

	protected final FrustumCullingFilter frustumFilter;

	private Transformation transformation;

	private ShaderProgram sceneShader, hudShader;

	private Scene scene;
	private HUD hud;

	private final Camera camera;
	private final Vector3f cameraInc;

	protected boolean running = false;
	public final GameEngine engine;

	public Renderer(GameEngine engine)
	{
		this.engine = engine;

		camera = new Camera();
		cameraInc = new Vector3f();
		hud = new HUD();

		scene = new Scene();

		transformation = new Transformation();
		frustumFilter = new FrustumCullingFilter();
	}

	public Transformation transformation()
	{
		return transformation;
	}

	public ShaderProgram getSceneShader()
	{
		return sceneShader;
	}

	public ShaderProgram getHudShader()
	{
		return hudShader;
	}

	public Camera getCamera()
	{
		return camera;
	}

	public Vector3f getCameraInc()
	{
		return cameraInc;
	}

	public Scene getScene()
	{
		return scene;
	}

	public HUD getHUD()
	{
		return hud;
	}

	public FrustumCullingFilter getFrustumFilter()
	{
		return frustumFilter;
	}

	public Matrix4f getProjectionMatrix()
	{
		return transformation.updateProjectionMatrix(FOV, engine.window.getWidth(), engine.window.getHeight(), Z_NEAR, Z_FAR);
	}

	private void createShaders() throws Exception
	{
		sceneShader = ShaderLoaderEnum.SCENE.load();
		// Create uniforms for modelView and projection matrices and texture
		sceneShader.createUniform("projectionMatrix");
		sceneShader.createUniform("modelViewMatrix");
		sceneShader.createUniform("texture_sampler");
		// Create uniform for material
		sceneShader.createMaterialUniform("material");
		// Create lighting related uniforms
		sceneShader.createUniform("specularPower");
		sceneShader.createUniform("ambientLight");
		sceneShader.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
		sceneShader.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
		sceneShader.createDirectionalLightUniform("directionalLight");

		// Create uniforms for Orthographic-model projection matrix and base colour
		hudShader = ShaderLoaderEnum.HUD.load();
		hudShader.createUniform("projModelMatrix");
		hudShader.createUniform("color");
		hudShader.createUniform("hasTexture");
	}

	private void createCallbacks() throws Exception
	{
		engine.window.setMouseInput(this);

		onResize(engine.window, engine.window.getWidth(), engine.window.getHeight());
	}

	private void init() throws Exception
	{
		createShaders();
		createCallbacks();
	}

	private void render(double dt, double elapsed) throws Exception
	{
		// Update view Matrix
		Matrix4f viewMatrix = camera.updateViewMatrix();

		// update frustum filter
		frustumFilter.updateFrustum(getProjectionMatrix(), viewMatrix);

		scene.render(this, dt, elapsed);
		hud.render(this, dt, elapsed);
	}

	private void handleInput(ControlBinds controls) throws Exception
	{
		if (controls.isPressed(Control.EXIT_GAME)) engine.getGame().exit();

		float ratio = controls.isPressed(Control.SPRINT) ? 4f : 1f;

		cameraInc.set(0, 0, 0);
		if (controls.isPressed(Control.WALK_FORWARD))
		{
			cameraInc.z = -1 * ratio;
		}
		else if (controls.isPressed(Control.WALK_BACKWARD))
		{
			cameraInc.z = 1 * ratio;
		}
		if (controls.isPressed(Control.WALK_LEFT))
		{
			cameraInc.x = -1 * ratio;
		}
		else if (controls.isPressed(Control.WALK_RIGHT))
		{
			cameraInc.x = 1 * ratio;
		}
		if (controls.isPressed(Control.CROUCH))
		{
			cameraInc.y = -1 * ratio;
		}
		else if (controls.isPressed(Control.JUMP))
		{
			cameraInc.y = 1 * ratio;
		}

		engine.net.writeAndFlush(new PlayerUpdatePacket(new Location(camera.getPosition()), new Quaternionf(camera.getRotation().x, camera.getRotation().y, camera.getRotation().z, 1)));

		// Update camera position
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

		scene.onCameraLocationUpdate(camera);
	}

	public void onResize(Window window, int width, int height)
	{
		hud.onScreenResize(window, width, height);
	}

	public void clear()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void run()
	{
		running = true;

		try
		{
			// initiate renderer
			init();

			final Window window = engine.getWindow();
			final ControlBinds controls = engine.getControlBinds();

			double secsPerFrame = 1.0 / 120.0;
			double prev = getTime();
			double steps = 0.0;

			while (isRunning() && !engine.getWindow().shouldClose())
			{
				double current = getTime();
				double dt = current - prev;
				prev = current;
				steps += dt;

				clear();

				glViewport(0, 0, window.getWidth(), window.getHeight());
				render(dt, steps);

				window.swapBuffers();
				window.pollEvents();

				handleInput(controls);

				sync(current, secsPerFrame);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		running = false;
	}

	private void sync(double loopStartTime, double step)
	{
		double endTime = loopStartTime + step;
		while(getTime() < endTime)
			try { Thread.sleep(1); } catch (InterruptedException e) { }
	}

	@Override
	public void stop()
	{
		System.out.println("Renderer stopped");
		running = false;
	}

	@Override
	public boolean isRunning()
	{
		return running;
	}

	@Override
	public void cleanup()
	{
		hudShader.cleanup();
		sceneShader.cleanup();

		scene.cleanup();
		hud.cleanup();

		MeshObject.cleanupAll();
	}

	@Override
	public void onMouseInput(MouseInput input)
	{
		// Update camera based on mouse
		Vector2f rotVec = input.getDisplVec();
		camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
	}
}
