package tools.world.mechanisms;

import tools.Director;
import tools.general.Pool;
import tools.general.Tools;
import tools.general.Vector;
import tools.world.Entity;

public class VectorActMechanism extends WorldMechanism{
	Vector vec;
	Pool<VectorAct> pool=new Pool<VectorAct>(10,VectorAct.class);
	public float rangeTressHold=0.2f;
	
	public VectorActMechanism setTressHold(float t){
		this.rangeTressHold=t;
		return this;
	}
	public VectorActMechanism(Entity entity,Vector vec) {
		super(entity);
		this.vec=vec;
	}
	public void act(float x,float y,float time){
		pool.getFree().set(x, y, time);
		finishedEvent=false;
	}
	public VectorAct getCurrent(){
		if(pool.size()!=0)return pool.get(0);
		return null;
	}
	public void update() {
		super.update();
		if(pool.size()==0)return;
		
		if(vec.dis(pool.get(0).x,pool.get(0).y)>rangeTressHold){
			pool.get(0).setValues(vec);
			vec.x+=pool.get(0).sx*Director.delta;
			vec.y+=pool.get(0).sy*Director.delta;
		}else{
			pool.release(0);
		}
	}
	boolean finishedEvent=false;
	public boolean finishedEvent(){
		if(finishedEvent)return false;
		if(pool.size()==0){
			finishedEvent=true;
			return true;
		}
		return false;
	}
	public boolean finished(){
		if(pool.size()==0)return true;
		return false;
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		
	}
	@Override
	public void die() {
		// TODO Auto-generated method stub
		super.die();
		pool.clear();
	}
	public static class VectorAct{
		public float x,y,speed,sx,sy;
		public void set(float x,float y,float speed){
			this.x=x;
			this.y=y;
			this.speed=speed;
		}
		public void setValues(Vector vec){
			Vector.vector.setVelocity(Vector.vector2.set(x, y), vec, speed);
			this.sx=Vector.vector.x;
			this.sy=Vector.vector.y;
		}
	}
	
}
