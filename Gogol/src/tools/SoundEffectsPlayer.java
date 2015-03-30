package tools;

import java.util.HashMap;

import tools.general.Tools;

import com.badlogic.gdx.audio.Sound;

public class SoundEffectsPlayer {

	private static HashMap<String,Sound> tracks=new HashMap<String,Sound>();
	public static void load(String name,String file){
		tracks.put(name, Director.assets.get(file, Sound.class));
	}
	public static Sound currentPlaying=null;
	private static float volume=1;
	public static void play(String name){
		if(currentPlaying!=null){
			currentPlaying.stop();
		}
		currentPlaying=tracks.get(name);
		currentPlaying.play(volume*Director.SOUNDFX);
	}
	public static void play(String name,float vol){
		if(currentPlaying!=null){
			currentPlaying.stop();
		}
		currentPlaying=tracks.get(name);
		currentPlaying.play(vol*volume*Director.SOUNDFX);
	}
	public static void setVolume(float v){
		volume=v;
	}
}
