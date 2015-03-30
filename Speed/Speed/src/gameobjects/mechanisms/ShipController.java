package gameobjects.mechanisms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import tools.world.Entity;
import tools.world.mechanisms.WorldMechanism;

public class ShipController extends WorldMechanism {
	public ShipController(Entity entity) {
		super(entity);
	}
	@Override
	public void update() {
		super.update();
		if(Gdx.input.isKeyPressed(Keys.W)){
			entity.vel.y+=Gdx.graphics.getDeltaTime();
			if(entity.vel.y>2)entity.vel.y=2;
		}
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			entity.vel.y-=1*Gdx.graphics.getDeltaTime();
			if(entity.vel.y<1)entity.vel.y=1;
		}
		if(entity.getSystem(ShipCameraFollower.class).reachedCamera){
			if(Gdx.input.isKeyPressed(Keys.D)){
				entity.vel.x+=5*Gdx.graphics.getDeltaTime();
			}
			else if(Gdx.input.isKeyPressed(Keys.A)){
				entity.vel.x-=5*Gdx.graphics.getDeltaTime();
			}
		}
	}
}
