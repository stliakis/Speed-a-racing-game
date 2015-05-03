package gameobjects.mechanisms;

import gameobjects.Scene;

import com.badlogic.gdx.Gdx;

import tools.world.Entity;
import tools.world.mechanisms.WorldMechanism;

public class ShipCameraFollower extends WorldMechanism {
	public static boolean reachedCamera = false;

	public ShipCameraFollower(Entity entity) {
		super(entity);
	}

	@Override
	public void init() {
		super.init();
		reachedCamera = false;
	}

	@Override
	public void update() {
		super.update();
		if (-entity.world.cameraPos.y + 2.5f < entity.pos.y) {
			reachedCamera = true;
		}
		if (!reachedCamera) {
			entity.vel.y = (((-entity.world.cameraPos.y + 10) - entity.pos.y)) / 5;
		} else
			entity.vel.y += (1 - entity.vel.y) / 100;

		entity.vel.x += (0 - entity.vel.x) / 20;

		Scene.SCORE += entity.vel.y;

		if (reachedCamera) {
			entity.world.cameraPos.x += ((-entity.pos.x - entity.world.cameraPos.x)) * Gdx.graphics.getDeltaTime() * 5;
			entity.world.cameraPos.y += ((-(entity.pos.y - 2.5f) - entity.world.cameraPos.y)) * Gdx.graphics.getDeltaTime() * 8;
		}

		entity.world.cameraPos.z += ((-(entity.pos.z + 1) - entity.world.cameraPos.z)) * Gdx.graphics.getDeltaTime() * 5;

		entity.world.cameraRot.y += ((entity.vel.x * 10 - entity.world.cameraRot.y)) * Gdx.graphics.getDeltaTime() * 20;
	}
}
