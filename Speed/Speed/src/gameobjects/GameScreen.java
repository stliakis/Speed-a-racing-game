package gameobjects;

import tools.Actions;
import tools.Director;
import tools.MotionBlur;
import tools.RenderOnTexture;
import tools.ScreenShader;
import tools.Shader;
import tools.general.Vector;
import tools.ui.Screen;
import tools.world.gWorld;
import view.HudLayer;
import view.PauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends Screen {
	public gWorld world;
	private Scene scene;
	
	public GameScreen(){
		setCamera3D(new Vector(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		world=new gWorld(this);
		
		scene=new Scene(this,world);
		world.setRootEntity(scene);
		
		HudLayer hudLayer=new HudLayer(this, this);
		this.AddLayer(hudLayer);
	}

	@Override
	public void gotFocus() {
		super.gotFocus();
		if(getDirector().prevscreen instanceof PauseScreen==false){
			world.sendAction(scene.id, Actions.ACTION_CREATE);
		}
		getLayer(HudLayer.class).open();
	}
	//public static MotionBlur mb=new MotionBlur();
	
	ScreenShader shader=new ScreenShader(new Shader("shaders/speed.vert","shaders/speed.frag")){
		public void end(com.badlogic.gdx.graphics.Camera camera) {
			TextureRegion region=textureRenderer.end();
			if(region!=null){
					Director.spriteBatch.setProjectionMatrix(camera.combined);
					Director.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_SRC_ALPHA);
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
					
					region.flip(false, true);
					Director.spriteBatch.draw(region,-size.x*2+effectCameraPos.x, -size.y*2+effectCameraPos.y, size.x*4,size.y*4);
					region.flip(false, true);
					Director.spriteBatch.end();
					if(shader!=null)Director.spriteBatch.setShader(Director.defaultShader);
			}else System.out.println("asd");
		};
	};
		
	@Override
	public void Render() {
		super.Render();
		//mb.begin();
		//shader.begin();
		world.render();
		//shader.end(getCamera());
		//mb.end();
	}
	
	@Override
	public void Touch(Input input) {
		super.Touch(input);
		world.touch(input);
	}
	@Override
	public void touchDown(int x, int y, int pointer) {
		super.touchDown(x, y, pointer);
		world.touchDown(x, y, pointer);
	}
	@Override
	public void touchUp(int x, int y, int pointer) {
		super.touchUp(x, y, pointer);
		world.touchUp(x, y, pointer);
	}
}
