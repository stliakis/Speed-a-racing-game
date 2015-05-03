package tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.general.Profiler;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.sheduler.Sheduler;
import tools.ui.Screen;
import tools.ui.UIRenderer;
import tools.world.WorldRenderer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class Director implements ApplicationListener, InputProcessor {
	public static boolean runningOnOuya=true;
	
	public static int ACTION_PAUSE = 0;
	public static int ACTION_RESUME = 1;
	public static int ACTION_BACK_BUTTON = 2;
	public static int ACTION_MENU_BUTTON = 3;
	public static int ACTION_DISPOSE = 4;
	public static int ACTION_KEY_DOWN = 5;
	public static int ACTION_KEY_UP = 6;
	public static int ACTION_TOUCH_DOWN = 7;
	public static int ACTION_TOUCH_UP = 8;
	public static int ACTION_TOUCH_DRAGED = 9;
	

	private List<Screen> screens;
	public Screen curscreen;
	public static float delta = 0;
	public Screen prevscreen;
	private Vector screenSizeNow;
	public static AssetManager assets;
	public static Effect effect;
	public static SpriteBatch spriteBatch;
	public static ShaderProgram defaultShader;
	LoadingScreen loadingScreen;

	public static float SOUNDFX=1;
	public static boolean MUSIC=false;
	static Map<String,String> assetsNames=new HashMap<String,String>();
	public static void enable_music(){
		MUSIC=true;
		if(MusicPlayer.currentPlaying!=null)MusicPlayer.play(MusicPlayer.currentPlaying);
	}
	public static void disable_music(){
		MUSIC=true;
		MusicPlayer.stop();
	}
	public static void enable_soundfx(){
		SOUNDFX=1;
	}
	public static void disable_soundfx(){
		SOUNDFX=0;
	}
	public Director() {
		screens = new ArrayList<Screen>();
		effect = new Effect();
		//Text2.loadFonts();
	}
	public Director(Effect effect) {
		screens = new ArrayList<Screen>();
		this.effect = effect;
		//Text2.loadFonts();
	}
	public void Action(int actionid) {
		for(int c=0;c<actionListeners.size();c++){
			actionListeners.get(c).action(actionid);
		}
		if(curscreen!=null)curscreen.Action(actionid);
	}
	
	public void addScreen(Screen... screensi) {
		for(Screen screen:screensi){
			screens.add(screen);
			screen.setDirector(this);
			if (screenSizeNow != null)
				screen.Resize(screenSizeNow.x, screenSizeNow.y);
		}
	}
	public List<LoadingScreen> loadingScreens=new ArrayList<LoadingScreen>();
	public void addLoadingScreen(LoadingScreen screen){
		loadingScreens.add(screen);
		screen.setDirector(this);
	}
	LoadingScreen currentLoadingScreen;
	public <T extends LoadingScreen> T setLoadingScreen(Class<T> clazz) {
		for(LoadingScreen screen:loadingScreens){
			if (clazz.isInstance(screen)) {
				currentLoadingScreen= clazz.cast(screen);
			}
		}
		return null;
	}
	
	public Screen backScreen() {
		return prevscreen;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		assets = new AssetManager();
		spriteBatch = new SpriteBatch();
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchMenuKey(true);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(this);
		
		//WorldRenderer.BLEND_FUN1=GL20.GL_SRC_ALPHA;
		//WorldRenderer.BLEND_FUN2=GL20.GL_ONE_MINUS_SRC_ALPHA;
		//WorldRenderer.CLEAR_COLOR.set(0.5f,0.1f,0.2f,1);
		//UIRenderer.BLEND_FUN1=GL20.GL_SRC_ALPHA;
		//UIRenderer.BLEND_FUN2=GL20.GL_ONE_MINUS_SRC_ALPHA;
		
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
		spriteBatch.enableBlending();
		
		defaultShader=SpriteBatch.createDefaultShader();
	}

	public void destroy() {
		for (Screen screen : screens)
			screen.destroy();
		
		assets.dispose();
		Tools.con("dispocing");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		destroy();
	}
	public Screen getScreen() {
		return curscreen;
	}
	public Screen getScreen(int id) {
		for (int c = 0; c < screens.size(); c++) {
			if (screens.get(c).id == id)
				return screens.get(c);
		}
		return null;
		
	}
	public <T extends Screen> T getScreen(Class<T> clazz) {
		for (int c = 0; c < screens.size(); c++) {
			if (clazz.isInstance(screens.get(c))) {
				return clazz.cast(screens.get(c));
			}
		}
		return null;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		Action(Director.ACTION_KEY_DOWN);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Action(Director.ACTION_KEY_UP);
		return false;
	}
	public static List<String> globalAssets=new ArrayList<String>();
	public static <T> void loadAsset(String file, Class<T> type) {
		//globalAssets.add(file);
		assets.load(file, type);
	}
	public static <T> void loadAsset(Class<T> type,String... file) {
		for(String fil:file){
			
			if(fil.contains("*")){
				/*
				String path=fil.split("\\*")[0];
				String filetype=fil.split("\\.")[1];
				FileHandle dirHandle=new FileHandle(new File(path));
				dirHandle = Gdx.files.internal(path);
				Tools.con("path:"+path);
				//if (Gdx.app.getType() == ApplicationType.Android) {
				//	   dirHandle = Gdx.files.internal(path);
				//} else {
				//	  dirHandle = Gdx.files.internal("./bin/"+path);
				//}
				for (FileHandle entry: dirHandle.list()) {
					if(!entry.isDirectory()){
						Tools.con("e:"+entry.path());
						if(!filetype.equals(entry.extension()))continue;
						String finalfile=entry.path();
						if(finalfile.contains("bin"))finalfile=finalfile.replace("./bin/", "");
						assets.load(finalfile, type);
					}
				}*/
			}else assets.load(fil, type);
		}
	}
	
	public abstract void onAssetsLoaded() ;
	public static <T> T getAsset (String fileName, Class<T> type) {
		return assets.get(fileName,type);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		Action(Director.ACTION_PAUSE);
	}
	boolean assetsLoaded=false;
	public boolean readyToLoadAssets=false;
	
	public boolean isReadyToLoadAssets() {
		return readyToLoadAssets;
	}
	public void setReadyToLoadAssets(boolean readyToLoadAssets) {
		this.readyToLoadAssets = readyToLoadAssets;
	}
	public static gColor CLEAR_COLOR=new gColor(0,0,0,1);
	@Override
	public void render() {
		Gdx.gl20.glClearColor(CLEAR_COLOR.r,CLEAR_COLOR.g,CLEAR_COLOR.b,CLEAR_COLOR.a);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(!assetsLoaded && readyToLoadAssets){
			if (Director.assets.update()) {
				onAssetsLoaded();
				assetsLoaded=true;
			}
			if(loadingScreen!=null){
				loadingScreen.setPercent(assets.getProgress());
			}
			else return;
		}
		
		Sheduler.update();

		effect.update();
		effect.render();
		curscreen.Update(this);
		curscreen.Render();
		curscreen.RenderLayers();
		curscreen.UpdateLayers(this, curscreen);

		if (Gdx.input.isTouched()) {
			curscreen.Touch(Gdx.app.getInput());
			curscreen.TouchLayers(Gdx.app.getInput());
		}
		
		MusicPlayer.update();
		
		delta = ((Gdx.graphics.getDeltaTime() * 60) * 0.01f) + delta * 0.99f;
		
	}

	@Override
	final public void resize(int width, int height) {
		screenSizeNow = new Vector(width, height);
		for (Screen screen : screens)
			screen.Resize(width, height);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		Action(Director.ACTION_RESUME);
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setLoadingScreen(LoadingScreen screen) {
		addScreen(screen);
		loadingScreen = screen;
		setScreen(screen.getClass());
	}

	public Screen setScreen(Class<? extends Screen> cl) {
		prevscreen = curscreen;
		if(curscreen!=null)curscreen.lostFocus();
		curscreen = getScreen(cl);

		curscreen.gotFocus();
		curscreen.layersGotFocus();
		return curscreen;
	}
	public void setScreenNow(Screen screen){
		prevscreen = curscreen;
		if(curscreen!=null)curscreen.lostFocus();
		curscreen = screen;

		curscreen.gotFocus();
		curscreen.layersGotFocus();
	}
	
	
	public Screen setScreen(Screen screen) {
		if(screen.hasAssets()){
			currentLoadingScreen.init( curscreen, screen);
			setScreenNow(currentLoadingScreen);
		}else{
			setScreenNow(screen);
		}
		return curscreen;
	}

	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		curscreen.touchDownCaller(x, y, pointer);
		Action(Director.ACTION_TOUCH_DOWN);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		Action(Director.ACTION_TOUCH_DRAGED);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		curscreen.touchUpCaller(x, y, pointer);
		Action(Director.ACTION_TOUCH_UP);
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	private static List<ActionListener> actionListeners=new ArrayList<ActionListener>();
	public static void registerActionListener(ActionListener lis){
		actionListeners.add(lis);
	}
	public interface ActionListener{
		public void action(int id);
	}
	
}
