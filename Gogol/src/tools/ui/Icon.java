package tools.ui;

import tools.Director;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Icon extends UIitem {
	
	public Sprite sprite;
	public boolean visible=true;
	public float textsize = 1, angle = 0;
	int assetID;
	
	public Icon(float x, float y, float sizex, float sizey, String texture,Screen screen) {
		super(screen);
		pos = new Vector(x, y);
		size = new Vector(sizex, sizey);
		if(texture!=null){
			this.texture = Director.getAsset(texture, Texture.class);
			sprite = new Sprite(this.texture);
		}
		else{
			assetID=screen.addAsset(texture, Texture.class);
		}
		
		color = new gColor(1, 1, 1, 1);
		colortext = new gColor(1, 1, 1, 1);
	}
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		super.reload();
		if(assetID!=-1){
			this.texture =(Texture)screen.getAsset(assetID);
			sprite = new Sprite(this.texture);
		}
	}
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public void Render(SpriteBatch sb) {
		if(!visible)return;
		sprite.setColor(color.r, color.g, color.b, color.a);
		sprite.setRotation(angle);
	
		sprite.setPosition(getPos().x - getSize().x * scaleup / 2, getPos().y
				- getSize().y * scaleup / 2);
		sprite.setSize(getSize().x * scaleup, getSize().y * scaleup);
		sprite.setOrigin((getSize().x * scaleup) / 2,
				(getSize().y * scaleup) / 2);
		sprite.draw(sb);
		
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public boolean Touch(float x, float y) {
		if (!touchable) {
			return false;
		}
		float xi = screen.s2wX(x, screen.screenSize.x);
		float yi = screen.s2wY(screen.screenSize.y - y, screen.screenSize.y);

		if (xi > pos.x - size.x / 2 && xi < pos.x + size.x / 2) {
			if (yi > pos.y - size.y / 2 && yi < pos.y + size.y / 2) {
			//	color.set(0.7f, 0.7f,0.7f,1);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean Touch(Input input) {
		if (!touchable) {
			return false;
		}
		float x = screen.s2wX(input.getX(), screen.screenSize.x);
		float y = screen.s2wY(screen.screenSize.y - input.getY(),
				screen.screenSize.y);
		if (x > pos.x - size.x / 2 && x < pos.x + size.x / 2) {
			if (y > pos.y - size.y / 2 && y < pos.y + size.y / 2) {
				if (input.justTouched()) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean TouchDown(int xi, int yi, int pointer) {
		if (!touchable) {
			return false;
		}
		float x = screen.s2wX(xi, screen.screenSize.x);
		float y = screen.s2wY(screen.screenSize.y - yi, screen.screenSize.y);

		if (x > pos.x - size.x / 2 && x < pos.x + size.x / 2) {
			if (y > pos.y - size.y / 2 && y < pos.y + size.y / 2) {
				//color.set(0.7f, 0.7f,0.7f,1);
				return true;
			}
		}

		return false;
	}

	public boolean TouchDraged(Input input) {
		if (!touchable) {
			return false;
		}
		float x = screen.s2wX(input.getX(), screen.screenSize.x);
		float y = screen.s2wY(screen.screenSize.y - input.getY(),
				screen.screenSize.y);

		if (x > pos.x - size.x / 2 && x < pos.x + size.x / 2) {
			if (y > pos.y - size.y / 2 && y < pos.y + size.y / 2) {
				if (input.justTouched()) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void Update() {

	}
}
