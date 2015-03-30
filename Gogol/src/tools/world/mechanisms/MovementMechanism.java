package tools.world.mechanisms;

import tools.Director;
import tools.world.Entity;
import tools.world.WorldRenderer;

public class MovementMechanism extends WorldMechanism {
	public MovementMechanism(Entity entity) {
		super(entity);
	}

	@Override
	public void init() {
		entity.setSpeed(entity.getRealSpeed());
	}

	@Override
	public final void render(WorldRenderer renderer) {
		super.render(renderer);
	}

	@Override
	public void update() {
		super.update();
		entity.pos.x += ((entity.vel.x) * entity.getSpeed())* Director.delta;
		entity.pos.y += ((entity.vel.y) * entity.getSpeed())* Director.delta;
		entity.pos.z += ((entity.vel.z) * entity.getSpeed())* Director.delta;
	}
}
