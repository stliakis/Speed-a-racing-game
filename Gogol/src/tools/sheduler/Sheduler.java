package tools.sheduler;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import tools.Director;
import tools.general.Pool;
import tools.general.Tools.OnFinishEvent;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;

public class Sheduler {
	private static Pool<VectorTask> vectorTasks=new Pool<VectorTask>(20,VectorTask.class);
	private static List<VectorTask> vectorTasksRuntimes=new ArrayList<VectorTask>();
	
	private static Pool<ColorTask> colorTasks=new Pool<ColorTask>(20,ColorTask.class);
	private static List<ColorTask> colorTasksRuntimes=new ArrayList<ColorTask>();
	
	public static void shedule(Task task){
		
	}
	
	public static void clearTasks(){
		for(int c=0;c<vectorTasks.size();c++){
			vectorTasks.get(c).onDone();
		}
		for(int c=0;c<vectorTasksRuntimes.size();c++){
			vectorTasksRuntimes.get(c).onDone();
		}
		
		for(int c=0;c<colorTasks.size();c++){
			colorTasks.get(c).onDone();
		}
		for(int c=0;c<colorTasksRuntimes.size();c++){
			colorTasksRuntimes.get(c).onDone();
		}
	}
	
	
	
	public static VectorTask addVectorTask(float x,float y,float time,Vector vector){
		VectorTask free=vectorTasks.getFree();
		free.vector=vector;
		free.mbx=x;
		free.time=time;
		free.mby=y;
		return free;
		
	}
	public static ColorTask addColorTask(float r,float g,float b,float a,float time,gColor color){
		ColorTask free=colorTasks.getFree();
		free.color=color;
		free.r=r;
		free.g=g;
		free.b=b;
		free.a=a;
		free.time=time;
		return free;
	}
	public static void addVectorTask(VectorTask task){
		vectorTasksRuntimes.add(task);
	}
	public static void addColorTask(ColorTask task){
		colorTasksRuntimes.add(task);
	}
	public static void update(){
		for(int c=0;c<vectorTasks.size();c++){
			vectorTasks.get(c).update();
		}
		for(int c=0;c<vectorTasksRuntimes.size();c++){
			vectorTasksRuntimes.get(c).update();
		}
		
		for(int c=0;c<colorTasks.size();c++){
			colorTasks.get(c).update();
		}
		for(int c=0;c<colorTasksRuntimes.size();c++){
			colorTasksRuntimes.get(c).update();
		}
	}
	
	
	public static abstract class Task{
		public abstract void onDone();
		public abstract void update();
	}
	public static class VectorTask extends Task{
		Vector vector;
		float mbx,mby;
		float time;
		Task nextTask;
		OnFinishEvent onFinish;
		float thresshold=1;
		public VectorTask setThressHold(float val){
			thresshold=val;
			return this;
		}
		public VectorTask(float x,float y,float time,Vector vector,OnFinishEvent event){
			this.vector=vector;
			mbx=x;
			mby=y;
			this.time=time;
			this.onFinish=event;
		}
		public VectorTask(){
			
		}
		public void onDone() {
			if(onFinish!=null)onFinish.onDone();
			if(vectorTasksRuntimes.contains(this))vectorTasksRuntimes.remove(this);
			else Sheduler.vectorTasks.release(this);
			vector.set(mbx, mby);
		}
		public void update() {
			/*
			float dis=vector.dis(mbx, mby);
			float speed=1;
			if(dis<100){
				if(speed>0)speed-=0.1f*Director.delta;
				if(speed<0)speed=0;
			}
			Vector.vector.setVelocity(Vector.vector2.set(mbx, mby), vector, time*Director.delta*speed);
			vector.plus(Vector.vector);
			*/
			vector.x+=((mbx-vector.x)/10)*Director.delta;
			vector.y+=((mby-vector.y)/10)*Director.delta;
			float dis=vector.dis(mbx, mby);
			
			if(dis<thresshold){
				onDone();
			}
		}
	}
	public static class ColorTask extends Task{
		gColor color;
		float r,g,b,a;
		float time;
		Task nextTask;
		OnFinishEvent onFinish;
		
		public ColorTask(float r,float g,float b,float a,float time,gColor color,OnFinishEvent event){
			this.color=color;
			this.r=r;
			this.g=g;
			this.b=b;
			this.a=a;
			this.time=time;
			this.onFinish=event;
		}
		public ColorTask(){
			
		}
		public void onDone() {
			if(onFinish!=null)onFinish.onDone();
			if(colorTasksRuntimes.contains(this))colorTasksRuntimes.remove(this);
			else Sheduler.colorTasks.release(this);
			color.set(r, g, b, a);
		}
		public void update() {
			gColor.color.setVelocity4d(gColor.color2.set(r, g, b, a), color, time*Director.delta);
			color.r+=gColor.color.r;
			color.g+=gColor.color.g;
			color.b+=gColor.color.b;
			color.a+=gColor.color.a;
			float dis=color.dif(r, g, b, a);
			if(dis<0.025f){
				onDone();
			}
			color.checkRange();
		}
	}
}
