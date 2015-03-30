package tools.world.mechanisms;

import com.badlogic.gdx.Gdx;

import tools.Director;
import tools.general.Pool;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.WorldRenderer;

public class MotionTailAnimation extends WorldMechanism {
	Pool<Tail> tail;
	float startAlpha=0.5f,astep=0.01f;;
	public MotionTailAnimation(Entity entity,int nums,float startAlpha,float astep) {
		super(entity);
		enabled=true;
		tail=new Pool<Tail>(nums,Tail.class);
		this.startAlpha=startAlpha*4;
		this.astep=astep;
	}

	@Override
	public void init(){
		
	}
	public void die() {
		super.die();
		tail.clear();
	}
	public boolean enabled=false;
		
	public void render(WorldRenderer renderer) {
		super.render(renderer);
		SpriteMechanism sm=entity.getSystem(SpriteMechanism.class);
		if(sm.invalidLayer())return;
		
		for(int c=0;c<tail.size();c++){
			Tail t=tail.get(c);
			renderer.DrawCenter(sm.sprite, Vector.vector.set(t.x,t.y), sm.scale, gColor.color.set(sm.color).setA(t.a), t.angle);
		}
	}	
	float lastAdd=0;
	public void update() {
		super.update();

		
		for(int c=0;c<tail.size();c++){
			tail.get(c).a-=astep*Director.delta*(entity.speed/entity.realSpeed);
			if(tail.get(c).a<=0)tail.release(c);
		}
		
		
		if(enabled){
			lastAdd+=Gdx.graphics.getDeltaTime()*1000*(entity.speed/entity.realSpeed);
			if(lastAdd>10){
				lastAdd=0;
				Tail t=tail.getFree();
				if(t!=null)t.set(entity.pos.x, entity.pos.y, entity.getAngle(), startAlpha);	
			}
		}
	}
	public static class Tail{
		float x,y,a,angle;
		public Tail(){
			
		}
		public void set(float x,float y,float angle,float a){
			this.x=x;
			this.y=y;
			this.a=a;
			this.angle=angle;
		}
	}
}
