package tools;

import tools.general.Tools;
import tools.general.Vector;
import tools.ui.Button;
import tools.ui.Button.OnEventListener;
import tools.ui.Layer;
import tools.ui.Screen;
import tools.ui.UIRenderer;
import tools.ui.UIRenderer.OnRenderListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DialogBoxLayer extends Layer{
	private static Button background,btnok,btnyes,btnno,btncancel;
	UIRenderer renderer;
	Screen screen;
	String text;
	OnDialogAction dialoAction;
	public static int BTN_OK=0,BTN_CANCEL=1,BTN_YES=2,BTN_NO=3;
	public DialogBoxLayer(final Screen screen) {
		super(screen);
		if(background==null)background=new Button(screen.x(50),screen.y(50),screen.x(70),screen.x(35), new Texture(Gdx.files.internal("ui/dialogBoxBkg.png")),screen);
		if(btnok==null){
			btnok=new Button(screen.x(50),screen.y(50),screen.x(30),screen.x(15),"ok", new Texture(Gdx.files.internal("ui/button1.png")),screen);
			btnok.onEventListener=new OnEventListener() {
				public void onEventEnd(Input input,Object caller) {
					if(dialoAction!=null){
						close();
						dialoAction.OnAction(BTN_OK);
					}
				}
			};
		}
		if(btnyes==null)btnyes=new Button(screen.x(50),screen.y(50),screen.x(30),screen.x(15),"yes",new Texture(Gdx.files.internal("ui/button1.png")),screen);
		if(btnno==null)btnno=new Button(screen.x(50),screen.y(50),screen.x(30),screen.x(15),"no",new Texture(Gdx.files.internal("ui/button1.png")),screen);
		if(btncancel==null)btncancel=new Button(screen.x(50),screen.y(50),screen.x(30),screen.x(15),"cancel",new Texture(Gdx.files.internal("ui/button1.png")),screen);
		
		renderer=new UIRenderer();
		renderer.setOnRender(new OnRenderListener() {
			public void OnRender(SpriteBatch sb) {
				Text.drawTextCenter(sb, Tools.resizeString(text, 10), Vector.vector.set(screen.x(60), screen.y(50)), 0.5f);
			}
		});
		this.screen=screen;
	}
	public void close() {
		super.close();
		visible=false;
	}
	public void render() {
		if(!visible)return;
		renderer.Render(screen.getCamera());
	}
	
	public void openOk(String text,OnDialogAction cb){
		renderer.removeAll();
		this.text=text;
		this.dialoAction=cb;
		btnok.pos.set(screen.x(50),screen.y(40));
		renderer.addItem(background,btnok);
		visible=true;
	}
	public void touch(Input input) {
		renderer.Touch(input);
	}
	public static interface OnDialogAction{
		public void OnAction(int action);
	}
}
