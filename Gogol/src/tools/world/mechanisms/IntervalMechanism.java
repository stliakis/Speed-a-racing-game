package tools.world.mechanisms;

import tools.world.Entity;
import tools.world.WorldRenderer;


import com.badlogic.gdx.Gdx;

public abstract class IntervalMechanism extends WorldMechanism {
	private float interval;
	public float lasttick;
	float lastTick = 0;

	public float timeon;

	public IntervalMechanism(Entity entity, float interval) {
		super(entity);
		this.interval = interval / 1000f;
		lasttick = 0;
	}
	public void setInterval(float interval){
		this.interval=interval/1000f;
	}
	public float getLocalDelta() {
		return timeon - lastTick;
	}
	public float getLocalDeltaNormal() {
		return (timeon - lastTick)/1000f;
	}
	
	@Override
	public void init() {
		super.init();
		lasttick = 0;
		timeon = 0;
	}

	@Override
	public void render(WorldRenderer renderer) {
		super.render(renderer);
	}

	public abstract void tick();

	@Override
	public final void update() {
		super.update();
		timeon+=Gdx.graphics.getDeltaTime() * 1000;
		lasttick += Gdx.graphics.getDeltaTime();
		if (lasttick > interval) {
			lasttick=0;
			tick();
		}
		/*
		lasttick += Gdx.graphics.getDeltaTime();
		timeon += Gdx.graphics.getDeltaTime() * 1000;
		if (lasttick > interval) {
			lasttick = 0;
			tick();
			lastTick = timeon;
		}*/
	}
}
