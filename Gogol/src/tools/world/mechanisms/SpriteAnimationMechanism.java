package tools.world.mechanisms;

import java.util.ArrayList;
import java.util.List;

import tools.Shader;
import tools.general.Vector;
import tools.world.Entity;
import tools.world.WorldRenderer;
import tools.world.gWorld;
import tools.world.mechanisms.SpriteAnimation.OnFinishedListener;
import tools.world.mechanisms.SpriteAnimation.OnFrameChange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SpriteAnimationMechanism extends WorldMechanism {
	public static List<Shader> shaders = new ArrayList<Shader>(0);
	public List<SpriteAnimation> animations=new ArrayList<SpriteAnimation>();
	float animationSpeed;
	private boolean isUsingShader = false;
	public int playingAnimation = -1;
	public Shader shader = null;
	Sprite sprite;
	Texture texture;
	public int layer=0;
	public boolean visible = true;
	public SpriteAnimationMechanism(Entity entity,int layer, SpriteAnimation...animations){
		this(entity,animations);
		this.layer=layer;
	}
	public SpriteAnimationMechanism(Entity entity, SpriteAnimation...animations) {
		super(entity);

		if(animations.length!=0){
			sprite = new Sprite(animations[0].texture);
			for (SpriteAnimation an : animations) {
				an.entity = entity;
				an.sprite = sprite;
				this.animations.add(an);
			}
		}
	}
	public void finish(){
		if(playingAnimation!=-1){
			animations.get(playingAnimation).finished=true;
			playingAnimation=-1;
		}
	}
	public void setFrameChangeListener(OnFrameChange lis,int... animation){
		for(int i:animation){
			animations.get(i).frameChangeListener=lis;
		}
	}
	public void setListener(OnFinishedListener lis,int... animation){
		for(int i:animation){
			animations.get(i).setListener(lis);
		}
	}
	public void setListener(int animation,OnFinishedListener lis){
		animations.get(animation).setListener(lis);
	}
	public SpriteAnimationMechanism(Entity entity, SpriteAnimation[] animations,
			String vertShader, String fragShader, boolean createNew) {
		super(entity);
		// texture=new Texture(Gdx.files.internal(textu));

		sprite = new Sprite(animations[0].texture);
		boolean containsShader = false;
		if (!createNew) {
			for (Shader shader : shaders) {
				if (shader.compare(fragShader, vertShader)) {
					this.shader = shader;
					containsShader = true;
				}
			}
			if (!containsShader) {
				shader = new Shader(vertShader, fragShader);
				shaders.add(shader);
			}
		} else {
			shader = new Shader(vertShader, fragShader);
			shaders.add(shader);
		}

		for (SpriteAnimation an : animations) {
			an.entity = entity;
			an.sprite = sprite;
			this.animations.add(an);
		}
		isUsingShader = true;
	}
	public void next(){
		animations.get(playingAnimation).next();
	}
	public void setOrigin(float x,float y){
		animations.get(playingAnimation).setOrigin(x, y);
	}
	public Vector getCurScale(){
		Vector.vector2.set(entity.getScale());
		Vector.vector2.x*=animations.get(playingAnimation).scale;
		Vector.vector2.y*=animations.get(playingAnimation).scale;
		return Vector.vector2;
	}
	public boolean finishedAnimation() {
		if (playingAnimation < 0 || playingAnimation >= animations.size()) {
			return true;
		}
		return animations.get(playingAnimation).finished;
	}

	public ShaderProgram getShader() {
		return shader.getShader();
	}

	public boolean isUsingShader() {
		return isUsingShader;
	}

	public void play(int animation) {
		playingAnimation = animation;
		animations.get(playingAnimation).fliped=false;
		animations.get(playingAnimation).play();
	}
	public void play(int animation,boolean flipped) {
		playingAnimation = animation;
		animations.get(playingAnimation).fliped=flipped;
		animations.get(playingAnimation).play();
		
	}
	public void play(int animation,boolean flipped,com.badlogic.gdx.graphics.g2d.Animation.PlayMode pm) {
		playingAnimation = animation;
		animations.get(playingAnimation).fliped=flipped;
		animations.get(playingAnimation).MODE=pm;
		animations.get(playingAnimation).play();
	}
	public void playLooping(int animation,boolean flipped) {
		playingAnimation = animation;
		animations.get(playingAnimation).fliped=flipped;
		animations.get(playingAnimation).playLooping();
	}
	
	public void playLooping(int animation) {
		playingAnimation = animation;
		animations.get(playingAnimation).fliped=false;
		animations.get(playingAnimation).playLooping();
	}
	public int addAnimation(SpriteAnimation an){
		if(sprite==null)sprite = new Sprite(an.texture);
		
		animations.add(an);
		an.entity = entity;
		an.sprite = sprite;
		return animations.indexOf(an);
	}
	public boolean invalidLayer(){
		return gWorld.LAYERED_RENDERING && entity.world.renderingLayer!=layer;
	}
	@Override
	public void render(WorldRenderer renderer) {
		if(gWorld.LAYERED_RENDERING && entity.world.renderingLayer!=layer)return;
		
		if (!visible) {
			return;
		}
		super.render(renderer);
		if (isUsingShader) {
			setShader(renderer);
		} else {
			if (renderer.currentShader != renderer.defaultShader) {
				renderer.setDefaultShader();
			}
		}
		renderSprite(renderer);
	}
	int lastPlayingAnimation=0;
	
	public void renderSprite(WorldRenderer renderer) {
		if (playingAnimation == -1) {
			animations.get(lastPlayingAnimation).renderLast(renderer);
			return;
		}
		if(entity.world.running){
			lastPlayingAnimation=playingAnimation;
			animations.get(playingAnimation).render(renderer,Gdx.graphics.getDeltaTime());
		}else{
			animations.get(lastPlayingAnimation).renderLast(renderer);
		}
		

	}

	public void restart() {
		animations.get(playingAnimation).stateTime = 0;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public void setShader(WorldRenderer renderer) {
		if (shader == null) {
			if (renderer.currentShader != renderer.defaultShader) {
				renderer.setDefaultShader();
			}
		} else {
			if (renderer.currentShader != shader) {
				renderer.currentShader=shader;
				renderer.getSpritebatch().setShader(renderer.currentShader.getShader());
			}
		}
	}

	public void setUsingShader(boolean isUsingShader) {
		this.isUsingShader = isUsingShader;
	}

}
