package tools;

import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class ScreenShader {
	public RenderOnTexture textureRenderer=new RenderOnTexture(1);
	public Vector effectCameraPos=new Vector(0,0,2);
	public Vector effectCameraRot=new Vector(0,0,0);
	public gColor effectCameraColor=new gColor(1,1,1,1);
	public Shader shader;
	public ScreenShader(Shader shader){
		this.shader=shader;
	}
	public void begin(){
		textureRenderer.begin();
	}
	public void end(Camera camera){
		TextureRegion region=textureRenderer.end();
		if(region!=null){
				Director.spriteBatch.setProjectionMatrix(camera.combined);
				Director.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				Director.spriteBatch.enableBlending();
				Director.spriteBatch.begin();
				Director.spriteBatch.setShader(shader.getShader());
				if(shader.getListener()!=null)shader.getListener().setParameters(shader);
				
				Vector3 size=new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),0);
				camera.unproject(size);
				camera.position.set(0,0,effectCameraPos.z);
				camera.update();
				Director.spriteBatch.setColor(effectCameraColor.r, effectCameraColor.g, effectCameraColor.b,effectCameraColor.a);
				Director.spriteBatch.draw(region,-size.x*2+effectCameraPos.x, -size.y*2+effectCameraPos.y, size.x*4,size.y*4);
				Director.spriteBatch.end();
				if(shader!=null)Director.spriteBatch.setShader(Director.defaultShader);
		}
	}
	
}
