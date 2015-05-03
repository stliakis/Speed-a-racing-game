package tools.world;

import java.util.HashMap;
import java.util.Map;

import tools.Director;
import tools.RenderOnTexture;
import tools.Shader;
import tools.SpriteBatch;
import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class WorldRenderer {
	private Camera camera;
	public Shader defaultShader;
	public final Shader realDefault;
	public Vector effectCameraPos=new Vector(0,0,2);
	public Vector effectCameraRot=new Vector(0,0,0);
	public gColor effectCameraColor=new gColor(1,1,1,1);
	private SpriteBatch spritebatch;
	public static int BLEND_FUN1,BLEND_FUN2;
	public PerspectiveCamera effectCamera;
	
	Map<String,Shader> shaders=new HashMap<String,Shader>();
	RenderOnTexture textureRenderer;
	
	public Shader currentShader;
	public Shader worldShader=null;
	public void addShader(String name,Shader shader){
		this.shaders.put(name, shader);
		
		if (!shader.getShader().isCompiled()) {
			Gdx.app.log("Problem loading shader:"+name+":", shader.getShader().getLog());
		}
	}
	public void setWorldShader(String name){
		worldShader=shaders.get(name);
		defaultShader=worldShader;
	}

	public Shader getShader(String name){
		return shaders.get(name);
	}
	public void setClearWorldShader(){
		worldShader=null;
		if (!realDefault.getShader().isCompiled()) {
			Gdx.app.log("Problem loading shader:", realDefault.getShader().getLog());
		}
		currentShader = realDefault;
		defaultShader=realDefault;
		spritebatch.setShader(currentShader.getShader());
	}
	public WorldRenderer(Camera camera) {
		this.camera = camera;
		if(textureRenderer==null)textureRenderer=new RenderOnTexture(1);
		float aspectRatio = Gdx.graphics.getWidth()/ Gdx.graphics.getHeight();
		if(effectCamera==null)effectCamera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
		
		spritebatch = new SpriteBatch(2000);
		spritebatch.setProjectionMatrix(camera.combined);
		spritebatch.setBlendFunction(BLEND_FUN1, BLEND_FUN2);
		
		ShaderProgram.pedantic = false;
		
		realDefault=new Shader("shaders/default.ver","shaders/default.frag",Shader.DEFAULT);
		
		if (!realDefault.getShader().isCompiled()) {
			Gdx.app.log("Problem loading shader:"+realDefault.getFrag()+":", realDefault.getShader().getLog());
		}
		
		defaultShader=realDefault;
		spritebatch.setShader(defaultShader.getShader());
		
		setDefaultShader();
	}

	public void DrawCenter(Sprite sprite, Vector pos, Vector scale,
			gColor color, float angle) {
		if (color.a <= 0.01f) {
			return;
		}
		sprite.setSize(scale.x, scale.y);
		sprite.setPosition(pos.x - scale.x / 2, pos.y - scale.y / 2);
		sprite.setColor(color.r, color.g, color.b, color.a);
		sprite.setRotation(angle);
		spritebatch.draw(sprite.getTexture(), sprite.getVertices(), pos.z);

	}

	public void DrawLine(Sprite sprite, float x, float y, float x1, float y1,
			gColor color, float width) {
		Vector.vector.set(Vector.dis(x, y, x1, y1), width);
		Vector.vector2.set(x1, y1);
		sprite.setOrigin(Vector.vector.x / 2, Vector.vector.y / 2);
		DrawCenter(sprite, Vector.vector2, Vector.vector, color, Vector.vector3
				.set(x, y).angle(x1, y1));
	}

	public Camera getCamera() {
		return camera;
	}

	public SpriteBatch getSpritebatch() {
		return spritebatch;
	}

	public void RenderEnd() {
		spritebatch.end();
		
		if(worldShader!=null && worldShader.type==Shader.SCREEN_SHADER){
			TextureRegion region=textureRenderer.end();
			if(region!=null){
				
				Director.spriteBatch.setProjectionMatrix(effectCamera.combined);
				Director.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				Director.spriteBatch.enableBlending();

				Director.spriteBatch.begin();
				if(worldShader!=null){
					Director.spriteBatch.setShader(worldShader.getShader());
					if(worldShader.getListener()!=null)worldShader.getListener().setParameters(worldShader);
				}
				Vector3 size=new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),0);
				effectCamera.unproject(size);
				effectCamera.position.set(0,0,effectCameraPos.z);
				effectCamera.update();
				Director.spriteBatch.setColor(effectCameraColor.r, effectCameraColor.g, effectCameraColor.b,effectCameraColor.a);
				Director.spriteBatch.draw(region,-size.x*2+effectCameraPos.x, -size.y*2+effectCameraPos.y, size.x*4,size.y*4);
				Director.spriteBatch.end();
				if(worldShader!=null)Director.spriteBatch.setShader(Director.defaultShader);
			}
		}else{
			if(worldShader!=null && worldShader.type==Shader.SCENE_SHADER){
				
			}
		}
		
	}
	public static gColor CLEAR_COLOR=new gColor(0,0,0,1);
	public void RenderStart() {
		 Gdx.gl.glClearColor(CLEAR_COLOR.r,CLEAR_COLOR.g,CLEAR_COLOR.b,CLEAR_COLOR.a);
		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		 
		if(worldShader!=null && worldShader.type==Shader.SCREEN_SHADER){
			textureRenderer.begin();
		}
		spritebatch.setProjectionMatrix(camera.combined);
		spritebatch.enableBlending();
		
		spritebatch.setBlendFunction(BLEND_FUN1, BLEND_FUN2);
		spritebatch.setProjectionMatrix(camera.combined);
		spritebatch.begin();
		if(worldShader!=null && worldShader.type==Shader.SCENE_SHADER){
			spritebatch.setShader(worldShader.getShader());
			if(worldShader.getListener()!=null)worldShader.getListener().setParameters(worldShader);
		}
	}
	boolean hasSetShader=false;

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public void setDefaultShader() {
		currentShader = defaultShader;
		spritebatch.setShader(defaultShader.getShader());
	}

	public void setSpritebatch(SpriteBatch spritebatch) {
		this.spritebatch = spritebatch;
	}

}
