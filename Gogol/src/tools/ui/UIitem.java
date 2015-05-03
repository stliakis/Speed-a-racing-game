package tools.ui;

import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class UIitem {
	public gColor btnStart,color, colortext;
	public Vector pos, size;
	public float scaleup = 1;
	public boolean stady = false;
	public String text;
	public Texture texture;
	public boolean touchable = true;
	public boolean uiRendererRender=true;
	public Screen screen;
	public Vector realPos;
	public UIRenderer uirenderer;
	public UIitem(Screen screen) {
		this.screen=screen;
		pos = new Vector();
		size = new Vector();
		text = "";
		color = new gColor(1, 1, 1, 1);
		colortext = new gColor(1, 1, 1, 1);
	}
	public boolean active=true;;
	public Vector getPos() {
		return pos;
	}
	public void reload(){
		
	}
	public Vector getSize() {
		return size;
	}

	public String getText() {
		return text;
	}

	public Texture getTexture() {
		return texture;
	}

	public abstract void Render(SpriteBatch sb) ;

	

	public void setPos(Vector pos) {
		this.pos = pos;
	}

	public void setSize(Vector size) {
		this.size = size;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public boolean Touch(float x, float y) {
		return false;
	}

	public boolean Touch(Input input) {
		return false;
	}

	public boolean TouchDown(int xi, int yi, int pointer) {
		return false;
	}
	
	public boolean TouchUp(int xi, int yi, int pointer) {
		return false;
	}


	public void Update() {

	}

}
