package tools.world.mechanisms;

import tools.world.Entity;

public abstract class InitMechanism extends WorldMechanism {
	public InitMechanism(Entity entity) {
		super(entity);
	}

	@Override
	public abstract void init();
}
