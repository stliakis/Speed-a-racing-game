package gameobjects.mechanisms;

import gameobjects.Scene;
import tools.general.Tools;
import tools.world.Entity;
import tools.world.mechanisms.WorldMechanism;

import com.badlogic.gdx.Gdx;

public class ShipAI extends WorldMechanism {
	public ShipAI(Entity entity) {
		super(entity);

	}

	float reflexDis = 8;

	@Override
	public void init() {
		super.init();
		reflexDis = Tools.randf(8, 8);
	}

	float lastShoot = 0;

	@Override
	public void update() {
		super.update();

		if (!ShipCameraFollower.reachedCamera) {
			entity.vel.y = (((-entity.world.cameraPos.y + 10) - entity.pos.y)) / 5;
		} else if (entity.vel.y < Tools.randf(2.3f, 2.5f))
			entity.vel.y += Gdx.graphics.getDeltaTime() * 2.5f;
		entity.vel.x += (0 - entity.vel.x) * Gdx.graphics.getDeltaTime();

		for (Entity b = entity.world.begin(Scene.GROUP_CONCRETE); b != null; b = entity.world.next()) {
			if (b.pos.y > entity.pos.y && b.pos.y < entity.pos.y + reflexDis && Math.abs(b.pos.x - entity.pos.x) < b.scale.x + 0.1f) {
				if (entity.vel.y > Tools.randf(0.8f, 1.5f))
					entity.vel.y -= Gdx.graphics.getDeltaTime() * 15;
				boolean side = entity.pos.x > b.pos.x;

				if (entity.pos.x < -2f && side == false)
					side = true;
				if (entity.pos.x > 2f && side == true)
					side = false;

				if (side)
					entity.vel.x += Gdx.graphics.getDeltaTime() * 5;
				else
					entity.vel.x -= Gdx.graphics.getDeltaTime() * 5;

			}
		}

	}

}
