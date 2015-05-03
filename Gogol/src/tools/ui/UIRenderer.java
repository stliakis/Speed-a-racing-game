package tools.ui;

import java.util.ArrayList;
import java.util.List;

import tools.Director;
import tools.ListenerManager;
import tools.Text;
import tools.Text2;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class UIRenderer {

	public SpriteBatch batch;
	ShaderProgram shader;
	
	public List<UIitem> items;
	public List<NumberDrawRequest> numbersToDraw;
	public static int BLEND_FUN1=GL20.GL_SRC_ALPHA,BLEND_FUN2= GL20.GL_ONE_MINUS_SRC_ALPHA;
	public float offSetX=0,offSetY=0,alpha=1;
	public boolean usePreSetAlpha=false;
	public static boolean renderListenerBefore=false;
	public UIRenderer() {
		batch = Director.spriteBatch;
		items = new ArrayList<UIitem>();
		numbersToDraw=new ArrayList<NumberDrawRequest>();
		for(int c=0;c<150;c++){
			numbersToDraw.add(new NumberDrawRequest());
		}
	}
	
	public void addItem(UIitem... itemslist) {
		for (UIitem item : itemslist) {
			item.uirenderer=this;
			items.add(item);
		}
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public void Render(Camera camera) {
		if(!visible)return;
		batch.setBlendFunction(BLEND_FUN1, BLEND_FUN2);
		batch.enableBlending();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		if(renderListenerBefore){
			for(OnRenderListener of=listeners.begin(OnRenderListener.class);of!=null;of=listeners.next()){
				of.OnRender(batch);
			}
		}
		
		for (int i = 0; i < items.size(); ++i) {
			UIitem item = items.get(i);
			item.pos.x+=offSetX;
			item.pos.y+=offSetY;
			if(usePreSetAlpha)item.color.a=alpha;
			item.Update();
			item.Render(batch);
			item.pos.x-=offSetX;
			item.pos.y-=offSetY;
			if(usePreSetAlpha)item.color.a=alpha;
		}

		for(int c=0;c<numDrawRequests;c++){
			NumberDrawRequest req=numbersToDraw.get(c);
			Text2.setColor( req.r, req.g, req.b, req.a);
			Text2.drawNumber(req.num, getSpriteBatch(),req.x,req.y,req.size);
		}
		
		if(!renderListenerBefore ){
			for(OnRenderListener of=listeners.begin(OnRenderListener.class);of!=null;of=listeners.next()){
				of.OnRender(batch);
			}
		}else{
			for(OnRenderListener of=listeners.begin(OnRenderListener.class);of!=null;of=listeners.next()){
				if(of.afterMain)of.OnRender(batch);
			}
		}
		
		batch.end();
		
		numDrawRequests=0;
	}
	int numDrawRequests=0;
	public void drawNumber(long num,float x,float y,float size,float r,float g,float b,float a){
		if(numDrawRequests<150){
			numbersToDraw.get(numDrawRequests).size=size;
			numbersToDraw.get(numDrawRequests).num=num;
			numbersToDraw.get(numDrawRequests).x=x;
			numbersToDraw.get(numDrawRequests).y=y;
			numbersToDraw.get(numDrawRequests).r=r;
			numbersToDraw.get(numDrawRequests).g=g;
			numbersToDraw.get(numDrawRequests).b=b;
			numbersToDraw.get(numDrawRequests).a=a;
			numDrawRequests++;
		}
	}
	public void drawNumber(long num,float x,float y,float size,float r,float g,float b,float a,int align){
		if(numDrawRequests<150){
			numbersToDraw.get(numDrawRequests).size=size;
			numbersToDraw.get(numDrawRequests).num=num;
			numbersToDraw.get(numDrawRequests).align=align;
			numbersToDraw.get(numDrawRequests).x=x;
			numbersToDraw.get(numDrawRequests).y=y;
			numbersToDraw.get(numDrawRequests).r=r;
			numbersToDraw.get(numDrawRequests).g=g;
			numbersToDraw.get(numDrawRequests).b=b;
			numbersToDraw.get(numDrawRequests).a=a;
			numDrawRequests++;
		}
	}

	public boolean visible=true;
	public void leaving(){
		visible=false;
		for(OnLeaving of=listeners.begin(OnLeaving.class);of!=null;of=listeners.next()){
			of.onLeaving(this);
			visible=true;
		}
	}
	public void focusing(){
		visible=true;
		for(OnFocus of=listeners.begin(OnFocus.class);of!=null;of=listeners.next()){
			of.onFocus(this);
		}
	}
	public void Reset() {
		for (int i = 0; i < items.size(); ++i) {
			UIitem item = items.get(i);
		}
	}

	public UIitem Touch(float x, float y) {
		int counter = 0;
		for (int i = 0; i < items.size(); ++i) {
			UIitem item = items.get(i);
			if (item.Touch(x, y))
				return item;
		}
		return null;
	}

	public UIitem Touch(Input input) {
		int counter = 0;
		for (int i = 0; i < items.size(); ++i) {
			UIitem item = items.get(i);
			if (item.Touch(input))
				return item;
		}
		return null;
	}

	public UIitem TouchDown(int x, int y, int pointer) {
		for (int i = 0; i < items.size(); ++i) {
			UIitem item = items.get(i);
			item.TouchDown(x, y, pointer);
		}
		return null;
	}
	public UIitem TouchUp(int x, int y, int pointer) {
		for (int i = 0; i < items.size(); ++i) {
			UIitem item = items.get(i);
			item.TouchUp(x, y, pointer);
		}
		return null;
	}
	
	public int TouchID(float x, float y) {
		int counter = 0;
		for (int i = 0; i < items.size(); ++i) {
			UIitem item = items.get(i);
			if (item.Touch(x, y))
				return counter;
			counter++;
		}
		return -1;
	}
	public void removeAll(){
		items.clear();
	}
	public int TouchID(Input input) {
		int counter = 0;
		for (int i = 0; i < items.size(); ++i) {
			UIitem item = items.get(i);
			if (item.Touch(input))
				return counter;
			counter++;
		}
		return -1;
	}
	public void readyToLeave(){
		visible=false;
	}
	public static class NumberDrawRequest{
		long num;
		int align;
		float x,y,size,r,g,b,a;
	}
	public ListenerManager listeners=new ListenerManager();
	
	public void addOnFocusListener(OnFocus onFocusListener) {
		listeners.add(onFocusListener);
	}
	public void addOnLeavingListener(OnLeaving onLeavingListener) {
		listeners.add(onLeavingListener);
	}
	public void addOnRender(OnRenderListener onRenderListener) {
		listeners.add(onRenderListener);
	}
	public static abstract class OnRenderListener{
		public boolean afterMain=false;
		public abstract void OnRender(SpriteBatch sb);
	}
	public static abstract class OnFocus{
		public abstract void onFocus(UIRenderer renderer);
	}
	public static abstract class OnLeaving{
		public void onLeaving(UIRenderer renderer){
			renderer.readyToLeave();
		}
	}
}
