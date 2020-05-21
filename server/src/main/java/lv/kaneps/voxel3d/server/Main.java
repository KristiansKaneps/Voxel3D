package lv.kaneps.voxel3d.server;

public class Main
{
	private Main() { }

	public static void main(String[] args) throws Exception
	{
		GameServer g = new GameServer();
		g.init();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try
			{
				g.exit();
			}
			catch (Exception e)
			{
			}
		}));

		g.start();
	}
}
