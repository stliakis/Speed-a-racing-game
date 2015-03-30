package tools.world.mechanisms;

import tools.Director;
import tools.general.Pool;
import tools.general.Tools;
import tools.general.Vector;
import tools.world.Entity;

public class RotationActMechanism extends WorldMechanism{
	Vector vec;
	Pool<RotationAct> pool=new Pool<RotationAct>(40,RotationAct.class);
	public float rangeTressHold=5;
	public RotationActMechanism(Entity entity,Vector vec) {
		super(entity);
		this.vec=vec;
	}
	public void act(float z,float time){
		RotationAct act=pool.getFree();
		if(act==null)return;
		act.set(z, time);
		finishedEvent=false;
	}
	public void wait(int time){
		pool.getFree().setWait(time);
		finishedEvent=false;
	}
	public RotationAct getCurrent(){
		if(pool.size()!=0)return pool.get(0);
		return null;
	}
	public void update() {
		super.update();
		if(pool.size()==0)return;
		if(pool.get(0).waiting){
			if(pool.get(0).waitingTimeon<pool.get(0).speed){
				pool.get(0).waitingTimeon+=Director.delta;
			}else{
				pool.release(0);
			}
		}else{
			if(Math.abs(vec.z-pool.get(0).z)>rangeTressHold){
				pool.get(0).setValues(vec);
				vec.z+=pool.get(0).sz*Director.delta*(entity.speed/entity.realSpeed);
			}else{
				pool.release(0);
			}
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
	public static class RotationAct{
		public float z,speed,sz;
		float waitingTimeon;
		public boolean waiting;
		public void set(float z,float speed){
			this.z=z;
			this.speed=speed;
			waiting=false;
		}
		public void setWait(float time){
			this.waiting=true;
			speed=time;
			waitingTimeon=0;
		}
		public void setValues(Vector vec){
			if(z>vec.z)sz=speed;
			else if(z<vec.z)sz=-speed;
		}
	}
	
}
