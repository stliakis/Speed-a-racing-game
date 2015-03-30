package tools.ui;

import tools.Director;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.ui.Button.OnEventListener;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ValueBar extends UIitem {
	public ProgressBar pb;
	Button minus,plus;
	float step=0;
	public ValueBar(float stepi,float x, float y, float sizex, float sizey, String progressBarIn, String progressBarOut,String minus,String plus,Screen screen) {
		super(screen);
		this.step=stepi;
		pos = new Vector(x, y);
		size = new Vector(sizex, sizey);
		this.pb=new ProgressBar(x,y,sizex/1.2f,sizey,progressBarIn,progressBarOut,screen);
		this.minus=new Button(x-this.pb.size.x/2-this.pb.size.x/7,y,this.pb.size.y*1.5f,this.pb.size.y*1.3f,minus,screen);
		this.minus.setOnEventListener(new OnEventListener() {
			public void onEventBegin(Input input, Object caller) {
				super.onEventBegin(input, caller);
				pb.setProgress(pb.getProgress()-step);
				onChange(pb.getProgress());
				((Button)caller).setEffect(0);
			}
		});
		this.plus=new Button(x+this.pb.size.x/2+this.pb.size.x/7,y,this.pb.size.y*1.5f,this.pb.size.y*1.3f,plus,screen);
		this.plus.setOnEventListener(new OnEventListener() {
			public void onEventBegin(Input input, Object caller) {
				super.onEventBegin(input, caller);
				pb.setProgress(pb.getProgress()+step);
				onChange(pb.getProgress());
				((Button)caller).setEffect(1);
			}
		});
	}
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		super.reload();
		pb.reload();
		minus.reload();
		plus.reload();
	}
	public void setProgress(float p){
		pb.setProgress(p);
	}
	public float getProgress() {
		return pb.getProgress();
	}
	public void onChange(float val){
		
	}
	@Override
	public void Render(SpriteBatch sb) {
		float x=pos.x;
		float y=pos.y;
		minus.color.a=color.a;
		plus.color.a=color.a;
		pb.color.a=color.a;
		minus.pos.set(x-this.pb.size.x/2-this.pb.size.x/7,y);
		plus.pos.set(x+this.pb.size.x/2+this.pb.size.x/7,y);
		pb.pos.set(x,y);
		
		pb.Render(sb);
		minus.Render(sb);
		plus.Render(sb);
	}
}
