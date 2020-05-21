package lv.kaneps.voxel3d.client.world.entity;

public class Player extends Entity
{
	public String name;

	public Player(int id, String name)
	{
		super(id, EntityType.PLAYER);
		this.name = name;
	}
}
