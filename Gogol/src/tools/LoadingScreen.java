package tools;

import tools.general.Tools;
import tools.ui.Screen;

public class LoadingScreen extends Screen {
	private float percent;
	Screen cs, ns;
	public void init(Screen cs,Screen ns){
		this.cs=cs;
		this.ns=ns;
		ns.fetchAssets();
		cs.dropAssets();
		ready=false;
	}
	boolean ready=false;
	public void Render() {
		super.Render();
		if(getDirector().readyToLoadAssets){
			percent=getDirector().assets.getProgress();
			if(getDirector().assets.update()){
				ready=true;
			}
		}
	}
	public boolean isReady(){
		return ready;
	}
	public void nextScreen(){
		getDirector().setScreenNow(ns);
	}
	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}
}
