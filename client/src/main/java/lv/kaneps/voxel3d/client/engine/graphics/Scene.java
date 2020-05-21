package lv.kaneps.voxel3d.client.engine.graphics;

import lv.kaneps.voxel3d.client.engine.IRenderCallable;
import lv.kaneps.voxel3d.client.engine.Renderer;
import lv.kaneps.voxel3d.client.engine.graphics.light.DirectionalLight;
import lv.kaneps.voxel3d.client.engine.graphics.light.PointLight;
import lv.kaneps.voxel3d.client.engine.graphics.light.SceneLight;
import lv.kaneps.voxel3d.client.engine.graphics.light.SpotLight;
import lv.kaneps.voxel3d.client.engine.math.Sphere;
import lv.kaneps.voxel3d.client.world.World;
import lv.kaneps.voxel3d.common.world.WorldConstants;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

public class Scene implements IRenderCallable
{
	public World world;
	public SceneLight sceneLight;

	public float specularPower = 10f;

	public Scene()
	{
		sceneLight = new SceneLight();

		sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));

		// directional light
		float lightIntensity = 1.0f;
		Vector3f lightPosition = new Vector3f(-1, 0, 0);
		sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
	}

	public void setWorld(World world)
	{
		if(this.world != null) this.world.cleanup();
		this.world = world;
	}

	public World getWorld()
	{
		return world;
	}

	private final Vector3i lastChunkUnload = new Vector3i();

	public void onCameraLocationUpdate(Camera camera)
	{
		if(world == null) return;
		Vector3f pos = camera.getPosition();
		int cx = Math.floorDiv((int) pos.x, WorldConstants.CHUNK_WIDTH);
		int cy = Math.floorDiv((int) pos.y, WorldConstants.CHUNK_HEIGHT);
		int cz = Math.floorDiv((int) pos.z, WorldConstants.CHUNK_DEPTH);

		if(lastChunkUnload.x == cx && lastChunkUnload.y == cy && lastChunkUnload.z == cz)
			return;
		lastChunkUnload.set(cx, cy, cz);

		world.unloadChunksOutside(new Sphere(new Vector3f(cx, cy, cz), Renderer.RENDER_DISTANCE));
	}

	@Override
	public void render(Renderer renderer, double dt, double elapsedTime) throws Exception
	{
		ShaderProgram sceneShader = renderer.getSceneShader();
		sceneShader.bind();

		// Update projection Matrix
		Matrix4f projectionMatrix = renderer.engine.getRenderer().getProjectionMatrix();
		Matrix4f viewMatrix = renderer.transformation().updateViewMatrix(renderer.getCamera());

		sceneShader.setUniform("projectionMatrix", projectionMatrix);
		sceneShader.setUniform("texture_sampler", 0);

		renderLights(renderer, sceneShader, viewMatrix, sceneLight);

		if(world != null) world.render(renderer, dt, elapsedTime);

		sceneShader.unbind();
	}

	private void renderLights(Renderer renderer, ShaderProgram sceneShader, Matrix4f viewMatrix, SceneLight sceneLight) throws Exception
	{
		sceneShader.setUniform("ambientLight", sceneLight.getAmbientLight());
		sceneShader.setUniform("specularPower", specularPower);

		// Process Point Lights
		int numLights = sceneLight.getPointLightList() != null ? sceneLight.getPointLightList().length : 0;
		for (int i = 0; i < numLights; i++)
		{
			// Get a copy of the point light object and transform its position to view coordinates
			PointLight currPointLight = new PointLight(sceneLight.getPointLightList()[i]);
			Vector3f lightPos = currPointLight.getPosition();
			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;
			sceneShader.setUniform("pointLights", currPointLight, i);
		}

		// Process Spot Lights
		numLights = sceneLight.getSpotLightList() != null ? sceneLight.getSpotLightList().length : 0;
		for (int i = 0; i < numLights; i++)
		{
			// Get a copy of the spot light object and transform its position and cone direction to view coordinates
			SpotLight currSpotLight = new SpotLight(sceneLight.getSpotLightList()[i]);
			Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
			dir.mul(viewMatrix);
			currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
			Vector3f lightPos = currSpotLight.getPointLight().getPosition();

			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;

			sceneShader.setUniform("spotLights", currSpotLight, i);
		}

		// Get a copy of the directional light object and transform its position to view coordinates
		DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
		Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
		dir.mul(viewMatrix);
		currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
		sceneShader.setUniform("directionalLight", currDirLight);

		// Update (sun's) directional light direction, intensity and colour
		if(world != null)
		{
			if (world.sunAngle > 90)
			{
				sceneLight.getDirectionalLight().setIntensity(0);
			}
			else if (world.sunAngle <= -80 || world.sunAngle >= 80)
			{
				float factor = 1 - (float) (Math.abs(world.sunAngle) - 80) / 10.0f;
				sceneLight.getDirectionalLight().setIntensity(factor);
				sceneLight.getDirectionalLight().getColor().y = Math.max(factor, 0.9f);
				sceneLight.getDirectionalLight().getColor().z = Math.max(factor, 0.5f);
			}
			else
			{
				sceneLight.getDirectionalLight().setIntensity(1);
				sceneLight.getDirectionalLight().getColor().x = 1;
				sceneLight.getDirectionalLight().getColor().y = 1;
				sceneLight.getDirectionalLight().getColor().z = 1;
			}

			double angRad = Math.toRadians(world.sunAngle);
			sceneLight.getDirectionalLight().getDirection().x = (float) Math.sin(angRad);
			sceneLight.getDirectionalLight().getDirection().y = (float) Math.cos(angRad);
		}
	}

	@Override
	public void cleanup()
	{
		if(world != null) world.cleanup();
	}
}
