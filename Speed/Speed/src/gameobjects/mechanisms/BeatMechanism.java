package gameobjects.mechanisms;

import tools.world.Entity;
import tools.world.mechanisms.WorldMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.analysis.KissFFT;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BeatMechanism  extends WorldMechanism{
	KissFFT fft;
	MusicFile song;
	float[] spectrum = new float[2048];
	public float currentAverage=0;
	public float currentMax=0;
	public BeatMechanism(Entity entity) {
		super(entity);
		fft = new KissFFT(2048);
		
	}
	String currentPlaying=null;
	public void Play(final String songi)
	{
		if(currentPlaying==null  ||currentPlaying!=songi){
			if(currentPlaying!=songi){
				if(song!=null){
			    	song.destroy();
				}
				Timer.schedule(new Task() {
					@Override
					public void run() {
						song=new MusicFile(songi,fft,spectrum);
				    	song.Play();
				    	System.gc();
					}
				}, 0.5f);

		    	currentPlaying=songi;
			}
		}
	}
	int NB_BARS=32;
	@Override
	public void update() {
		super.update();
		if(song==null)return;
	    int histoX = 0;
	    currentAverage=0;
	    currentMax=0;
		for (int i = 0; i < NB_BARS; i++) {
			 if (i<NB_BARS/2) {
			   histoX = NB_BARS/2-i;
			 } else {
			   histoX = i-NB_BARS/2;
			 }
			 int nb = (2048/NB_BARS)/2;
			 
			 currentAverage+=avg(histoX, nb);
			 if(avg(histoX,nb)>currentMax)currentMax=avg(histoX, nb);
		}
		
		currentAverage=currentAverage/NB_BARS;

	}
	private float avg(int pos, int nb) {
		int sum = 0;
		for (int i = 0; i < nb; i++) {
			sum += spectrum[pos + i];
		}
		return (sum / nb);
	}
	
	
}

class MusicFile {
	static short[] samples = new short[2048];
	int samplesCount;
	String file;
	public float beatpower;
	static Mpg123Decoder decoder;
	static KissFFT fft;
	float[] spectrum;
	static AudioDevice device;
	static FileHandle externalFile;
	public MusicFile(String file,KissFFT fft,float[] spectrum )
	{
		if(decoder!=null)decoder=null;
		this.fft=fft;
		this.spectrum=spectrum;
		this.file=file;
		externalFile = Gdx.files.external("tmp/audio-spectrum.mp3");
		Gdx.files.internal(file).copyTo(externalFile);
		decoder = new Mpg123Decoder(externalFile);
		device = Gdx.audio.newAudioDevice(decoder.getRate()*2,true);
		Gdx.files.external("tmp/audio-spectrum.mp3").delete();
	}
	public boolean playing=true;
	public boolean paused=false;
	public Thread playbackThread;
	public void Play()
	{
		playbackThread = new Thread(new Runnable() {
			@Override
			public void run() {
				int readSamples = 0;
				while ( playing) {
					if(!paused && decoder!=null){
						if((readSamples = decoder.readSamples(samples, 0,samples.length))<=0){
							decoder.dispose();
							decoder = new Mpg123Decoder(externalFile);
						}
						fft.spectrum(samples, spectrum);
						device.writeSamples(samples, 0, readSamples);
					}
				}
			}
		});
		playbackThread.setDaemon(true);
		
		playbackThread.start();
	}
	
	public void unload()
	{
	   playing=false;
	}
	public void destroy()
	{
		playing=false;
		fft=null;
		device=null;
		decoder=null;
		externalFile.delete();
	}
}

