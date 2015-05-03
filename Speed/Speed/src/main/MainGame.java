package main;

import gameobjects.GameScreen;
import tools.Director;
import tools.Effect;
import tools.MusicPlayer;
import tools.SoundEffectsPlayer;
import tools.Text2;
import tools.general.Profiler;
import tools.general.Tools;
import tools.ui.UIRenderer;
import tools.world.WorldRenderer;
import tools.world.gWorld;
import tools.world.mechanisms.SpriteAnimation;
import view.CompleteScreen;
import view.Credits;
import view.GameOverScreen;
import view.LogoScreen;
import view.MenuBackground;
import view.PauseScreen;
import view.ShipSelectScreen;
import view.StartScreen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Director {

	public static Config config;
	public static PlayerData playerData;
	public static boolean paused = false;

	public MainGame() {

	}

	@Override
	public void create() {
		super.create();
		Text2.registerFont("score", "fonts/dig.ttf", 256);
		Text2.registerFont("menu", "fonts/score.ttf", 256);

		// game
		loadAsset(Texture.class,"ui/nitro_full.png","ui/nitro_background.png", "sprites/white.png", "sprites/map.png", "sprites/glow.png", "ui/button1.png", "ui/btnExit.png", "ui/btnAbout.png", "ui/btnMusic.png", "ui/btnNext.png", "ui/btnPrevious.png", "ui/btnRestart.png",
				"ui/btnPause.png", "ui/btnResume.png", "sprites/burner.png", "sprites/glare.png", "sprites/raindrop.png", "sprites/shipShadow.png", "ui/mirror.png", "sprites/booster.png", "ui/darkness.png", "ui/start.png");

		loadAsset(Music.class, "music/Junkie XL - Mushroom [HD].mp3");

		loadAsset(Sound.class, "sounds/button.wav", "sounds/star.wav", "sounds/gameover.mp3", "sounds/engine.wav", "sounds/sparks.wav");

		setLoadingScreen(new LogoScreen());
		Profiler.begin();

		config = (Config) Tools.load("config.data");
		if (config == null)
			config = new Config();

		playerData = (PlayerData) Tools.load("playerData.data");
		if (playerData == null)
			playerData = new PlayerData();

		/* replace the linear screen transition effect with a bouncy one */
		effect = new Effect() {
			float vel = 0, velMB;

			@Override
			public void update() {
				if (started) {
					if (!rising) {
						if (scale > 0)
							scale -= speed * Director.delta;
						else {
							scale = 0;
							rising = true;
							director.setScreen(nextScreen);
						}
					} else {
						velMB = ((1 - scale) / 10);
						vel += ((velMB - vel) / 5) * Director.delta;
						scale += vel * Director.delta;
					}
				}
			}
		};
	}

	@Override
	public void onAssetsLoaded() {
		Profiler.end();

		MusicPlayer.load("main", "music/Junkie XL - Mushroom [HD].mp3");

		SoundEffectsPlayer.load("button", "sounds/button.wav");

		GameScreen game = new GameScreen();
		MenuBackground menuBackground = new MenuBackground(game.world, this);
		addScreen(game, new ShipSelectScreen(menuBackground) ,new Credits(menuBackground), new StartScreen(menuBackground), new PauseScreen(menuBackground), new GameOverScreen(menuBackground), new CompleteScreen(menuBackground));

		Director.MUSIC = true;
		MusicPlayer.setVolume(0.2f);
		SpriteAnimation.USING_RATIO = true;
		Director.SOUNDFX = 1;

		if (MainGame.config.SOUND_VOL == 1)
			MusicPlayer.setVolume(1);
		else
			MusicPlayer.setVolume(0);

		WorldRenderer.BLEND_FUN1 = GL20.GL_SRC_ALPHA;
		WorldRenderer.BLEND_FUN2 = GL20.GL_ONE_MINUS_SRC_ALPHA;
		UIRenderer.renderListenerBefore = true;
		UIRenderer.BLEND_FUN1 = GL20.GL_SRC_ALPHA;
		UIRenderer.BLEND_FUN2 = GL20.GL_ONE_MINUS_SRC_ALPHA;
		WorldRenderer.CLEAR_COLOR = Director.CLEAR_COLOR;
		gWorld.LAYERED_RENDERING = false;
		MusicPlayer.play("main");
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		paused = true;
		if (getScreen(GameScreen.class) != null)
			getScreen(GameScreen.class).world.running = false;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		paused = false;
		if (getScreen(GameScreen.class) != null)
			getScreen(GameScreen.class).world.running = true;
	}
}
