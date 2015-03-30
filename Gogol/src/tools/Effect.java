package tools;

import tools.general.Tools;
import tools.ui.Screen;


public class Effect {
	public float scale = 1;
	public float speed = 0.075f;
	public Screen nextScreen;
	public boolean started = false, rising = false;
	public Director director;
	public boolean doubleScreen=false;
	public void start(Director idirector, Class<? extends Screen> nextScreen) {
		director=idirector;
		scale = 1;
		started = true;
		this.nextScreen = director.getScreen(nextScreen);
		rising = false;
		doubleScreen=false;
	}
	
	public void start(Director idirector, Class<? extends Screen> nextScreen,float speed) {
		director=idirector;
		scale = 1;
		started = true;
		this.speed=speed;
		this.nextScreen = director.getScreen(nextScreen);
		rising = false;
		doubleScreen=false;
	}
	Screen prev;
	public void startDoubleRender(Director idirector, Class<? extends Screen> nextScreen,float speed) {
		director=idirector;
		scale = 1;
		started = true;
		this.speed=speed;
		prev=director.getScreen();
		this.nextScreen = director.getScreen(nextScreen);
		rising = false;
		director.setScreen(nextScreen);
		doubleScreen=true;
	}
	public void render(){
		if(doubleScreen){
			if (started){
				prev.Render();
				prev.Update();
			}
		}
	}
	public void update() {
		if (started) {
			if (!rising) {
				if (scale > 0)
					scale -= speed*Director.delta;
				else {
					scale = 0;
					rising = true;
					if(!doubleScreen){
						director.setScreen(nextScreen);
					}
				}
			} else {
				if (scale < 1)
					scale += speed*Director.delta;
				else {
					scale = 1;
					rising = false;
					started = false;
				}
			}
		}
	}
}
