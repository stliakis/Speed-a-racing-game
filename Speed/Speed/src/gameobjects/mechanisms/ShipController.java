package gameobjects.mechanisms;

import gameobjects.Scene;
import gameobjects.Ship;
import tools.world.Entity;
import tools.world.mechanisms.WorldMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class ShipController extends WorldMechanism {
	public ShipController(Entity entity) {
		super(entity);

	}

	@Override
	public void die() {
		super.die();
		if (((Ship) entity).distance < Scene.RACE_LENGTH) {
			entity.world.sendAction(entity.world.getRoot().id, Scene.GAMEOVER);
		}
	}

	float lastShoot = 0;

	@Override
	public void update() {
		super.update();
		if (((Ship) entity).distance >= Scene.RACE_LENGTH)
			entity.world.sendAction(entity.world.getRoot().id, Scene.COMPLETE);

		lastShoot += Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.W)) {
			if (entity.vel.y < 3)
				entity.vel.y += Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			entity.vel.y -= 1 * Gdx.graphics.getDeltaTime();
			if (entity.vel.y < 1)
				entity.vel.y = 1;
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if(	((Ship)entity).nitroPercent>0){
				entity.vel.y+=Gdx.graphics.getDeltaTime()*2;
				((Ship)entity).nitroPercent-=Gdx.graphics.getDeltaTime();
				((Ship)entity).nitroEnabled=true;
			}
		}else{
			((Ship)entity).nitroEnabled=false;
		}
		if (entity.getSystem(ShipCameraFollower.class).reachedCamera) {
			if (Gdx.input.isKeyPressed(Keys.D)) {
				entity.vel.x += 10 * Gdx.graphics.getDeltaTime();
			} else if (Gdx.input.isKeyPressed(Keys.A)) {
				entity.vel.x -= 10 * Gdx.graphics.getDeltaTime();
			}
		}
	}

}
