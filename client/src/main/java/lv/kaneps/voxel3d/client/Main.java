package lv.kaneps.voxel3d.client;

public class Main
{
	private Main() { }

	public static void main(String[] args) throws Exception
	{
		Game g = new Game();
		g.init();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try { g.exit(); } catch(Exception e) { }
		}, "Game Shutdown Hook"));

		g.start();
	}
}