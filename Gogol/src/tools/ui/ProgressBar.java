package tools.ui;

import tools.Director;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ProgressBar extends UIitem {
	
	public Sprite sprite,sprite2;
	public boolean visible=true;
	public float textsize = 1, angle = 0;
	int assetID=-1,assetID2=-1;
	public ProgressBar(float x, float y, float sizex, float sizey, String texture,String background,Screen screen) {
		super(screen);
		pos = new Vector(x, y);
		size = new Vector(sizex, sizey);
		realPos=new Vector(x,y);
		if(texture!=null){
			this.texture = Director.getAsset(texture, Texture.class);
			sprite = new Sprite(this.texture);
		}
		else{
			assetID=screen.addAsset(texture, Texture.class);
		}
		
		if(background!=null){
			Texture texturea = Director.getAsset(background, Texture.class);
			sprite2 = new Sprite(texturea);
		}
		else{
			assetID2=screen.addAsset(background, Texture.class);
		}
		color = new gColor(1, 1, 1, 1);
		colortext = new gColor(1, 1, 1, 1);
	}
	
	public ProgressBar(float x, float y, float sizex, float sizey, Texture texture,Texture background,Screen screen) {
		super(screen);
		pos = new Vector(x, y);
		size = new Vector(sizex, sizey);
		this.texture =texture;
		sprite = new Sprite(this.texture);
		realPos=new Vector(x,y);
		
		Texture texturea = background;
		sprite2 = new Sprite(texturea);
		color = new gColor(1, 1, 1, 1);
		colortext = new gColor(1, 1, 1, 1);
	}
	
	float timeon=0;
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		super.reload();
		if(assetID!=-1){
			this.texture =(Texture)screen.getAsset(assetID);
			sprite = new Sprite(this.texture);
		}
		if(assetID2!=-1){
			sprite = new Sprite((Texture)screen.getAsset(assetID2));
		}
		timeon=0;
	}
	@Override
	public String getText() {
		return text;
	}
	float progress,orogressMb;
	public float getProgress() {
		return orogressMb;
	}
	public void setProgress(float p){
		orogressMb=p;
		if(orogressMb>1)orogressMb=1;
		else if(orogressMb<0)orogressMb=0;
	}
	public void setProgressNow(float p){
		orogressMb=p;
		progress=p;
	}
	@Override
	public void Render(SpriteBatch sb) {
		if(!visible)return;
		
		sprite2.setColor(color.r, color.g, color.b, color.a);
		sprite2.setRotation(angle);
		sprite2.setPosition(getPos().x - getSize().x * scaleup / 2, getPos().y
				- getSize().y * scaleup / 2);
		sprite2.setSize(getSize().x * scaleup, getSize().y * scaleup);
		sprite2.setOrigin((getSize().x * scaleup) / 2,
				(getSize().y * scaleup) / 2);
		
		sprite2.setSize(getSize().x * scaleup, getSize().y * scaleup);
		sprite2.setOrigin((getSize().x * scaleup) / 2,
				(getSize().y * scaleup) / 2);
		sprite2.draw(sb);
		
		sprite.setColor(color.r, color.g, color.b, color.a);
		sprite.setRotation(angle);

		sprite.setRegion(0, 0, progress, 1);
		sprite.setSize((getSize().x * scaleup)*progress, getSize().y * scaleup);
		sprite.setPosition(getPos().x-getSize().x/2, getPos().y
				- getSize().y * scaleup / 2);
		
		sprite.setOrigin((getSize().x * scaleup) / 2,
				(getSize().y * scaleup) / 2);

		sprite.draw(sb);
		timeon+=Gdx.graphics.getDeltaTime();
		if(timeon>0.5f){
			progress+=((orogressMb-progress)/10)*Director.delta;
		}
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
