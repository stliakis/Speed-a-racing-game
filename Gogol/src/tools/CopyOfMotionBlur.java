package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.world.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;


public class CopyOfMotionBlur {
	List<Frame> rot=new ArrayList<Frame>();
	int frames=20;
	public CopyOfMotionBlur(){
		for(int c=0;c<frames;c++)rot.add(new  Frame());
	}
	int frame=-1;
	public void changeFrame(){
		frame++;
		if(frame>=frames)frame=0;
	}
	float lastChange=1;
	public void begin(){
		lastChange+=Gdx.graphics.getDeltaTime();
		if(lastChange>0.1f){
			changeFrame();
		}
		rot.get(frame).alpha=1;
		rot.get(frame).begin();
		
		for(int c=0;c<frames;c++){
			if(rot.get(c).alpha>0 && c!=frame){
				rot.get(c).alpha-=0.1f;
			}
		}
		
		Collections.sort(rot);
		System.out.println("fps:"+Gdx.graphics.getFramesPerSecond());
	}
	
	public void end(){
		rot.get(frame).rot.end();
		for(int c=0;c<frames;c++){
			rot.get(c).endAndRender(c==frame);
		}
		
	}
	public static class Frame implements Comparable{
		
		float alpha;
		RenderOnTexture rot=new RenderOnTexture(1,false,true);
		OrthographicCamera camera;
		public Frame() {
	        camera=new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		public void begin(){
		    if(alpha<=0)return;
		    rot.begin();
		}
		public TextureRegion end(){
			rot.end();
		    return null;
		}
		public TextureRegion endAndRender(boolean current){
		    if(alpha>0){
				Director.spriteBatch.setProjectionMatrix(camera.combined);
				Director.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				Director.spriteBatch.enableBlending();
				Director.spriteBatch.begin();
				Director.spriteBatch.setColor(1,1,1,alpha);
				Director.spriteBatch.draw(rot.m_fboRegion,-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
				Director.spriteBatch.end();
		    }
		    return null;
		}
		@Override
		public int compareTo(Object arg0) {
			if(((Frame)arg0).alpha>alpha)return 1;
			else return -1;
		}
	}
}
