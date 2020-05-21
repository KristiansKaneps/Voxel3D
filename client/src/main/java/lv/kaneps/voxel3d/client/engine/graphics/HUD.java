package lv.kaneps.voxel3d.client.engine.graphics;

import lv.kaneps.voxel3d.client.engine.IRenderCallable;
import lv.kaneps.voxel3d.client.engine.Renderer;
import lv.kaneps.voxel3d.client.engine.graphics.font.FontEnum;
import lv.kaneps.voxel3d.client.engine.graphics.font.TextObject;
import lv.kaneps.voxel3d.client.engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class HUD implements IRenderCallable
{
	protected int screenWidth, screenHeight;

	protected double dtAccumulator = 0;
	protected int dtAccCounter = 0;

	protected TextObject txtObjFps, txtObjX, txtObjY, txtObjZ;

	public HUD()
	{
		txtObjFps = new TextObject("0 FPS", FontEnum.ARIAL_BLACK_HQ);
		txtObjFps.setPosition(12f, 0, 0);
		txtObjFps.setScale(3 * FontEnum.ARIAL_BLACK_HQ.getPreferredScale());

		txtObjX = new TextObject("x=?", FontEnum.ARIAL);
		txtObjX.setPosition(12f, 70f, 0);
		txtObjX.setScale(FontEnum.ARIAL.getPreferredScale());

		txtObjY = new TextObject("y=?", FontEnum.ARIAL);
		txtObjY.setPosition(12f, 90f, 0);
		txtObjY.setScale(FontEnum.ARIAL.getPreferredScale());

		txtObjZ = new TextObject("z=?", FontEnum.ARIAL);
		txtObjZ.setPosition(12f, 110f, 0);
		txtObjZ.setScale(FontEnum.ARIAL.getPreferredScale());
	}

	private Vector3f prevCamPos = new Vector3f(69, 69, 69);

	@Override
	public void render(Renderer renderer, double dt, double elapsedTime) throws Exception
	{
		ShaderProgram hudShader = renderer.getHudShader();
		hudShader.bind();

		Matrix4f ortho = renderer.transformation().getOrtho2DProjectionMatrix(0, screenWidth, screenHeight, 0);

		if(dtAccCounter >= 120)
		{
			float fps = (float) (dtAccCounter / dtAccumulator);
			txtObjFps.setText(((int) fps) + " FPS");
			dtAccCounter = 0;
			dtAccumulator = 0;
		}
		dtAccumulator += dt;
		dtAccCounter++;

		Vector3f camPos = renderer.getCamera().getPosition();
		if(Math.abs(camPos.lengthSquared() - prevCamPos.lengthSquared()) > 0.025f)
		{
			txtObjX.setText(String.format("x=%.2f", camPos.x));
			txtObjY.setText(String.format("y=%.2f", camPos.y));
			txtObjZ.setText(String.format("z=%.2f", camPos.z));
			prevCamPos.set(camPos);
		}

		Mesh mesh = txtObjFps.getMesh();
		hudShader.setTextObjectUniform(mesh.getMaterial(), renderer.transformation().buildOrthoProjModelMatrix(txtObjFps, ortho));
		mesh.render();

		mesh = txtObjX.getMesh();
		hudShader.setTextObjectUniform(mesh.getMaterial(), renderer.transformation().buildOrthoProjModelMatrix(txtObjX, ortho));
		mesh.render();

		mesh = txtObjY.getMesh();
		hudShader.setTextObjectUniform(mesh.getMaterial(), renderer.transformation().buildOrthoProjModelMatrix(txtObjY, ortho));
		mesh.render();

		mesh = txtObjZ.getMesh();
		hudShader.setTextObjectUniform(mesh.getMaterial(), renderer.transformation().buildOrthoProjModelMatrix(txtObjZ, ortho));
		mesh.render();

		hudShader.unbind();
	}

	@Override
	public void onScreenResize(Window window, int width, int height)
	{
		this.screenWidth = width;
		this.screenHeight = height;
	}

	public void cleanup()
	{
		if(txtObjFps != null) txtObjFps.getMesh().cleanup();
		if(txtObjX != null) txtObjX.getMesh().cleanup();
		if(txtObjY != null) txtObjY.getMesh().cleanup();
		if(txtObjZ != null) txtObjZ.getMesh().cleanup();
	}
}
