package tools.world.mechanisms;

import tools.Director;
import tools.general.Pool;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.WorldRenderer;
import tools.world.gWorld;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MotionTailSpriteAnimation extends WorldMechanism {
	Pool<Tail> tail;
	float startAlpha=0.5f,astep=0.01f;;
	public MotionTailSpriteAnimation(Entity entity,int nums,float startAlpha,float astep) {
		super(entity);
		enabled=true;
		tail=new Pool<Tail>(nums,Tail.class);
		this.startAlpha=startAlpha;
		this.astep=astep;
	}
	
	public void setEntity(Entity en){
		this.entity=en;
	}
	@Override
	public void init(){
		
	}
	public void die() {
		super.die();
		tail.clear();
	}
	public boolean enabled=false;
		
	public boolean invalidLayer(int l){
		return gWorld.LAYERED_RENDERING && entity.world.renderingLayer!=l;
	}
	
	public void render(WorldRenderer renderer) {
		super.render(renderer);
		SpriteAnimationMechanism sm=entity.getSystem(SpriteAnimationMechanism.class);
		if(invalidLayer(sm.layer))return;
		
		for(int c=0;c<tail.size();c++){
			Tail t=tail.get(c);
			SpriteAnimation sa=sm.animations.get(t.animation);
			sa.renderFrame(renderer,t.frame,t.a,t.pos,t.flipped);
		}
		
		
	}	

	public void update() {
		super.update();

		SpriteAnimationMechanism sm=entity.getSystem(SpriteAnimationMechanism.class);
		for(int c=0;c<tail.size();c++){
			tail.get(c).a-=astep*Director.delta;
			if(tail.get(c).a<=0)tail.release(c);
		}
		if(enabled){
			Tail t=tail.getFree();
			if(t!=null){
				if(sm.playingAnimation==-1)return;
				t.set(entity.pos.x, entity.pos.y,sm. entity.getAngle(),sm.playingAnimation,sm.animations.get(sm.playingAnimation).region,sm.animations.get(sm.playingAnimation).fliped, startAlpha);	
			}
		}
	}
	public static class Tail{
		float a,angle;
		Vector pos=new Vector();
		int animation=0;
		TextureRegion frame;
		boolean flipped;
		public Tail(){
			
		}
		public void set(float x,float y,float angle,int animation,TextureRegion frame,boolean flipped,float a){
			pos.set(x, y);
			this.a=a;
			this.frame=frame;
			this.animation=animation;
			this.angle=angle;
			this.flipped=flipped;
		}
	}
}
