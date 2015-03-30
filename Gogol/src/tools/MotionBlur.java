package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class MotionBlur {
	
	RenderOnTexture backFrame=new RenderOnTexture(1),fronFrame=new RenderOnTexture(1);
	Camera camera=new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	public float scale=0.8f;
	public MotionBlur(){
	}
	public void begin(){
		fronFrame.begin();
	}
	
	TextureRegion region=new TextureRegion(new Texture(Gdx.files.internal("sprites/darkness.png")));
	public void end(){
		fronFrame.end();
		backFrame.beginWithoutClear();
		Director.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		render(region,1,1,1,0.7f);
		Director.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);
		render(fronFrame.m_fboRegion,1,1,1,0.5f);
		backFrame.end();
		
		Director.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);
		render(backFrame.m_fboRegion,1,1,1,1);
		
	}
	public void render(TextureRegion region,float r,float g,float b,float alpha){
		Director.spriteBatch.setProjectionMatrix(camera.combined);
		Director.spriteBatch.enableBlending();
		Director.spriteBatch.begin();
		Director.spriteBatch.setColor(r,g,b,alpha);
		Director.spriteBatch.draw(region,-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		Director.spriteBatch.end();
	}
}
