package lv.kaneps.voxel3d.client.world.entity;

@FunctionalInterface
public interface EntityConsumer
{
	void accept(Entity entity) throws Exception;
}
