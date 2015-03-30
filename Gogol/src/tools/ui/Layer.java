package tools.ui;

import tools.Director;
import tools.general.Tools;

import com.badlogic.gdx.Input;

public abstract class Layer {
	public boolean visible;
	private Screen screen;
	private UIRenderer uirenderer;
	private Director director;
	
	public Director getDirector() {
		return director;
	}
	public void setDirector(Director director) {
		this.director = director;
	}
	
	public UIRenderer getUirenderer() {
		return uirenderer;
	}
	public void setUirenderer(UIRenderer uirenderer) {
		this.uirenderer = uirenderer;
	}
	public Layer(Screen screen){
		visible=true;
		this.screen=screen;
	}
	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
	public void closeNow(){
		uirenderer.visible=false;
	}
	public void close() {
		if(uirenderer!=null)uirenderer.leaving();
	}

	public void open() {
		if(uirenderer!=null)uirenderer.focusing();
	}

	public void render() {
		if(uirenderer!=null)visible=uirenderer.visible;
		if(uirenderer!=null && visible)uirenderer.Render(screen.getCamera());
	}

	public void action(int id) {

	}

	public void resize(float sx, float sy) {

	}
	public void touch(Input input) {
		if(uirenderer!=null && visible)uirenderer.Touch(input);
	}
	public void screenGotFocus(){
		
	}
	public void touchDown(int x, int y, int pointer) {

	}

	public void touchUp(int x, int y, int pointer) {

	}

	public void update(Director director, Screen screen) {

	}
	public float x(float percent) {
		return (percent / 100f) * screen.cameraSize.x;
	}

	public float y(float percent) {
		return (percent / 100f) * screen.cameraSize.y;
	}
}
