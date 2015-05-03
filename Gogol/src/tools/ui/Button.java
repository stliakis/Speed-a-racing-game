package tools.ui;

import tools.Director;
import tools.Director.ActionListener;
import tools.Text;
import tools.Text2;
import tools.Text2.FontParrameters;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button extends UIitem {

	
	public static abstract class OnEventListener{
		
		public int[] keys;
		public OnEventListener(int...keys){
			this();
			this.keys=new int[keys.length];
			for(int c=0;c<keys.length;c++){
				this.keys[c]=keys[c];
			}
		}
	
		public OnEventListener(){
			
		}
		public void onLongEvent(Object caller){
			
		}
		public  void onEventEnd(Input input,Object caller){
			
		}
		public void onEventBegin(Input input,Object caller){
			
		}
	}
	
	public OnEventListener onEventListener = null;
	public Sprite sprite;
	public String text;
	public boolean visible=true;;
	public float textsize = 1, angle = 0;
	private FontParrameters fontPars;
	public OnEventListener getOnEventListener() {
		return onEventListener;
	}

	public void setOnEventListener(OnEventListener onTouchListener) {
		this.onEventListener = onTouchListener;
	}
	public Button(float x, float y, float sizex, float sizey, String text,String texture, Screen screen) {
		this(x,y,sizex,sizey,text,null,texture,screen);
		realPos=new Vector(x,y);
	}
	int assetID=-1;
	public Button(float x, float y, float sizex, float sizey, String text,FontParrameters fontPars,String texture, Screen screen) {
		this(screen);
		realPos=new Vector(x,y);
		this.fontPars=fontPars;
		pos = new Vector(x, y);
		size = new Vector(sizex, sizey);
		this.screen = screen;
		if(texture!=null){
			this.texture = Director.getAsset(texture, Texture.class);
			sprite = new Sprite(this.texture);
		}
		else{
			assetID=screen.addAsset(texture, Texture.class);
		}

		
		color = new gColor(1, 1, 1, 1);
		colortext = new gColor(1, 1, 1, 1);
		this.text = text;
	}

	public Button(float x, float y, float sizex, float sizey, String texture,Screen screen) {
		this(x,y,sizex,sizey,null,null,texture,screen);

	}
	public Button(float x, float y, float sizex, float sizey, Texture texture,Screen screen) {
		super(screen);
		realPos=new Vector(x,y);
		pos = new Vector(x, y);
		size = new Vector(sizex, sizey);
		this.screen = screen;

		this.texture = texture;
		sprite = new Sprite(this.texture);

		
		color = new gColor(1, 1, 1, 1);
		colortext = new gColor(1, 1, 1, 1);
	}
	boolean rising=true;
	int effect=-1;
	boolean touchingTheButton=false;
	public Button(Screen screen){
		super(screen);
		
		final Button b=this;
		ActionListener action=new ActionListener() {
			public void action(int id) {
				if(Math.abs(System.currentTimeMillis()-lastRender)>100 || lastRender==0)return;
				if(!isActive() || (Math.abs(System.currentTimeMillis()-lastRender)>100 || lastRender==0) && visible)return;
				if(id==Director.ACTION_TOUCH_DOWN){
					if(touch(Gdx.input.getX(),Gdx.input.getY())){
						touchingTheButton=true;
						if(onEventListener!=null)onEventListener.onEventBegin(Gdx.input, b);
					}else{
						touchingTheButton=false;
					}
				}
				if(id==Director.ACTION_TOUCH_UP){
					//touchingTheButton=false;
					if(touch(Gdx.input.getX(),Gdx.input.getY()))if(onEventListener!=null)onEventListener.onEventEnd(Gdx.input, b);
				}
				if(id==Director.ACTION_KEY_UP){
					if(onEventListener!=null && onEventListener.keys!=null){
						for(int c=0;c<onEventListener.keys.length;c++){
							if(Gdx.input.isKeyPressed(onEventListener.keys[c]) && lastPress>200){
								onEventListener.onEventEnd(Gdx.app.getInput(),b);
							}
						}
					}
				}
				if(id==Director.ACTION_TOUCH_DRAGED){
					//if(touch(Gdx.input.getX(),Gdx.input.getY()))touchingTheButton=true;
					//else touchingTheButton=false;
				}
				
			}
		};
		Director.registerActionListener(action);
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
	
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	public void setEffect(int ef){
		effect=ef;
		rising=true;
	}
	long lastRender;
	@Override
	public void Render(SpriteBatch sb) {
		lastRender=System.currentTimeMillis();
		
		//if(touchingTheButton)scaleup+=((1.02f-scaleup)/10)*Director.delta;
		//else scaleup+=((1-scaleup)/10)*Director.delta;
		if(sprite==null || !visible)return;
		
		sprite.setColor(color.r, color.g, color.b, color.a);
		sprite.setRotation(angle);
		sprite.setPosition(getPos().x - getSize().x * scaleup / 2, getPos().y
				- getSize().y * scaleup / 2);
		sprite.setSize(getSize().x * scaleup, getSize().y * scaleup);
		sprite.setOrigin((getSize().x * scaleup) / 2,
				(getSize().y * scaleup) / 2);
		sprite.draw(sb);
		
		Text.setColor(colortext.r, colortext.g, colortext.b, color.a);
		if (text != null) {
			if(fontPars==null){
				Text2.drawText(sb, null,text, getPos(), textsize * scaleup,Text.ALIGN_CENTER);
			}else {
				Text2.setColor(fontPars.getColor().r, fontPars.getColor().g, fontPars.getColor().b, color.a);
				Text2.drawText(sb,fontPars, text, getPos(), fontPars.getSize() * scaleup,Text.ALIGN_CENTER);
			}
		}
		Text.setColor(1, 1, 1, 1);
		update(this);
		
		
		if(effect==0){
			color.a=(Math.abs(angle-30))/30;
			color.checkRange();
			if(rising){
				if(angle<30)angle+=3*Director.delta;
				else rising=false;
			}
			else{
				if(angle>0)angle-=4*Director.delta;
				else {
					effect=-1;
					angle=0;
					color.a=1;
				}
			}
			
		}
		if(effect==1){
		//	color.a=(Math.abs(angle+30))/30;
			color.checkRange();
			if(rising){
				if(angle>-30)angle-=3*Director.delta;
				else rising=false;
			}
			else{
				if(angle<0)angle+=4*Director.delta;
				else {
					effect=-1;
					angle=0;
				//	color.a=1;
				}
			}
			
		}
		
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	public boolean touch(float x, float y) {
		float xi = screen.s2wX(x, screen.screenSize.x);
		float yi = screen.s2wY(screen.screenSize.y - y, screen.screenSize.y);

		if (xi > pos.x - size.x / 2 && xi < pos.x + size.x / 2) {
			if (yi > pos.y - size.y / 2 && yi < pos.y + size.y / 2) {
				return true;
			}
		}

		return false;
	}
	

	boolean pressing=false;
	float longpressTime=0;
	float lastPress=0;
	boolean stopedPressing=false;
	public void update(Button button){
		
		if(onEventListener==null)return;
		boolean pres=false;
		lastPress+=Gdx.graphics.getDeltaTime()*1000;
		

		
		
		if(Gdx.input.isTouched()){
			if(button.touch(Gdx.input.getX(), Gdx.input.getY())){
				pres=true;
				stopedPressing=false;
			}
		}
		pressing=pres;
		if(pressing){
			longpressTime+=Gdx.graphics.getDeltaTime()*1000;
			if(longpressTime>500 && !stopedPressing && lastPress>200){
				onEventListener.onLongEvent(this);;
				pressing=false;
				longpressTime=0;
				stopedPressing=true;
			}
		}else{
			longpressTime=0;
			stopedPressing=true;
		}
		
	}
}
