package tools;

import java.util.HashMap;

import com.badlogic.gdx.audio.Music;

public class MusicPlayer {
	private static HashMap<String,Music> tracks=new HashMap<String,Music>();
	public static void load(String name,String file){
		tracks.put(name, Director.assets.get(file, Music.class));
	}
	public static Music currentPlaying=null;
	private static float volume=1;
	private static float realVol=1;
	public static void play(String name){
		if(currentPlaying!=null){
			currentPlaying.stop();
		}
		currentPlaying=tracks.get(name);
		if(Director.MUSIC){
			currentPlaying.play();
			currentPlaying.setLooping(true);
			currentPlaying.setVolume(volume);
		}
	}
	public static void play(Music m){
		if(Director.MUSIC){
			m.play();
			m.setLooping(true);
			m.setVolume(volume);
		}
	}
	public static void setVolume(float v){
		volume=v;
		realVol=v;
		if(currentPlaying!=null)currentPlaying.setVolume(v);
	}
	public static void crossfade(int id,float speed){
		
	}
	public static boolean isPlaying(String name){
		return (currentPlaying==tracks.get(name));
	}
	public static void stop(){
		if(currentPlaying!=null){
			currentPlaying.stop();
		}
	}
	public static boolean isPlaying(){
		return currentPlaying.isPlaying();
	}
	public static void playGradient(String name){
		if(currentPlaying!=null){
			currentPlaying.stop();
		}
		currentPlaying=tracks.get(name);
		if(Director.MUSIC){
			volume=0;
			currentPlaying.play();
			currentPlaying.setLooping(true);
			currentPlaying.setVolume(volume);
		}
	}
	public static void update(){
		if(currentPlaying!=null){
			if(volume<realVol){
				volume+=0.01f*Director.delta;
				currentPlaying.setVolume(volume);
			}
		}
	}

}
