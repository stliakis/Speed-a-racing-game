package tools.world.mechanisms;

import tools.Actions;
import tools.world.Entity;
import tools.world.WorldRenderer;

import com.badlogic.gdx.Gdx;

public class TimeoutMechanism extends WorldMechanism {
	private float interval;
	private float lasttick;
	float lastTick = 0;

	public float timeon;

	public TimeoutMechanism(Entity entity, float interval) {
		super(entity);
		this.interval = interval / 1000;
		lasttick = 0;
	}

	public float getLocalDelta() {
		return timeon - lastTick;
	}

	@Override
	public void init() {
		super.init();
		lasttick = 0;
		timeon = 0;
	}

	@Override
	public final void render(WorldRenderer renderer) {
		super.render(renderer);
	}

	public void tick() {
		entity.world.sendAction(entity.id, Actions.ACTION_DIE);
	}

	@Override
	public final void update() {
		super.update();
		lasttick += Gdx.graphics.getDeltaTime();
		timeon += Gdx.graphics.getDeltaTime() * 1000;
		if (lasttick > interval) {
			lasttick = 0;
			tick();
			lastTick = timeon;
		}
	}
}
