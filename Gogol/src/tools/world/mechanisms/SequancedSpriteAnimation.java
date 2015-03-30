package tools.world.mechanisms;

import tools.Director;
import tools.general.Vector;
import tools.world.Entity;
import tools.world.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SequancedSpriteAnimation {
	public Animation animation;
	public TextureRegion currentFrame;
	Entity entity;
	boolean finished = true;
	public TextureRegion[] frames;
	int playingState;
	Sprite sprite;
	public float stateTime;
	Texture texture;
	private Vector posOffset=new Vector(0,0);


	public Vector getPosOffset() {
		return posOffset;
	}
	public SequancedSpriteAnimation setPosOffset(float x,float y) {
		this.posOffset.x=x;
		this.posOffset.y=y;
		return this;
	}
	public void play() {
		finished = false;
		playingState = 1;
		animation.setPlayMode(Animation.PlayMode.NORMAL);
	}

	public void playLooping() {
		finished = false;
		playingState = 0;
		animation.setPlayMode(Animation.PlayMode.LOOP);
	}
	
	public void render(WorldRenderer renderer,float delta) {
		if (finished) {
			return;
		}
		stateTime += delta;
		sprite.setRegion(animation.getKeyFrame(stateTime,(playingState == 0) ? true : false));
		sprite.setOrigin(entity.getScale().x / 2, entity.getScale().y / 2);
		Vector.vector.set(entity.pos);
		Vector.vector.plus(posOffset);
		renderer.DrawCenter(sprite, Vector.vector, entity.getScale(),
				entity.getColor(), entity.getAngle());
		if (playingState == 1) {
			if (animation.isAnimationFinished(stateTime)) {
				finished = true;
				stateTime = 0;
			}
		}
	}

}
