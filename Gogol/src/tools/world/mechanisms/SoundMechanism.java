package tools.world.mechanisms;

import java.util.ArrayList;
import java.util.List;

import tools.Director;
import tools.world.Entity;

import com.badlogic.gdx.Gdx;

public class SoundMechanism extends WorldMechanism {
	static float lastShoot = 0;
	int curPlaying = 0;
	float maxTime = 50;
	long ids[]=new long[10];
	List<com.badlogic.gdx.audio.Sound> sounds;
	public static boolean STEREO_SOUND=true;
	public static float SOUND_RANGE=10;
	public static float PITCH=1;
	public SoundMechanism(Entity entity, String... strings) {
		super(entity);
		sounds=new ArrayList<com.badlogic.gdx.audio.Sound>(strings.length);
		for (int c = 0; c < strings.length; c++) {
			sounds.add(Director.getAsset(strings[c], com.badlogic.gdx.audio.Sound.class));
		}
	}
	public Sound add(String file){
		com.badlogic.gdx.audio.Sound sound=Director.getAsset(file, com.badlogic.gdx.audio.Sound.class);
		sounds.add(sound);
		Sound sf=new Sound(this, sounds.indexOf(sound));
		return sf;
	}
	@Override
	public void die() {

	}
	public void muteAllSounds(){
		for (int c = 0; c < sounds.size(); c++) {
			sounds.get(c).stop();
		}
	}
	public void isPlaying() {
		// sounds.get(id).
	}

	public void play(int id) {
		if (Director.SOUNDFX==0) {
			return;
		}
		lastShoot = 0;
		/*
		if(STEREO_SOUND){
			float pan=(-entity.world.cameraPos.x-entity.pos.x)/SOUND_RANGE;
			if(pan>1)pan=1;
			else if(pan<-1)pan=-1;
			if(pan>0){
				pan=1-pan;
			}else if(pan<0){
				pan=-1+pan;
			}

			
			Tools.con("pan:"+pan);
			ids[id] = sounds.get(id).play(Director.SOUNDFX,1,pan); 
		}*/
		ids[id] = sounds.get(id).play(Director.SOUNDFX,PITCH,0);
		curPlaying = id;
	
	}
	public void play(int id,float pitch,float pan) {
		if (Director.SOUNDFX==0) {
			return;
		}
		lastShoot = 0;
        ids[id] = sounds.get(id).play(Director.SOUNDFX,pitch*PITCH,pan);
		curPlaying = id;
	}
	public void play(int id, float vol) {
		if (Director.SOUNDFX==0) {
			return;
		}
		lastShoot = 0;
		ids[id] = sounds.get(id).play(vol*Director.SOUNDFX,PITCH,0);
		curPlaying = id;
	}

	public void playLooping(int id) {
		if (Director.SOUNDFX==0) {
			return;
		}
		lastShoot = 0;
		ids[id] = sounds.get(id).loop(Director.SOUNDFX,PITCH,0);
		curPlaying = id;
	}
	public void setVolume(int id,float vol){
		sounds.get(id).setVolume(ids[id],vol);
	}
	@Override
	public void update() {
		lastShoot += Gdx.graphics.getDeltaTime() * 1000;
		//for (int c = 0; c < sounds.size(); c++) {
		//	sounds.get(c).setVolume(ids[c], Director.SOUNDFX);
		//}
		
		super.update();
	}
	public static class Sound{
		int id;
		SoundMechanism sm;
		Sound(SoundMechanism sm,int id){
			this.id=id;
			this.sm=sm;
		}
		public void play(){
			sm.play(id);
		}
		public void play(float vol){
			sm.play(id, vol);
		}
		public void playLooping(){
			sm.playLooping(id);
		}
	}
}
