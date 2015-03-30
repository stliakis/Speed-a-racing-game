package view;

import gameobjects.GameScreen;
import gameobjects.Scene;
import tools.Actions;
import tools.Director;
import tools.ScreenShader;
import tools.Shader;
import tools.Shader.ShaderParrametersListener;
import tools.general.gColor;
import tools.world.gWorld;

import com.badlogic.gdx.Gdx;

public class MenuBackground {
	ScreenShader blurScreen;
	public gWorld world;
	public Director director;
	private float scale=0;
	public gColor color=new gColor(1,1,1,1);
	public MenuBackground(gWorld world,Director director){
		this.world=world;
		this.director=director;
		Shader shader=new Shader("shaders/default.ver","shaders/blur.frag");
		shader.setListener(new ShaderParrametersListener() {
			@Override
			public void setParameters(Shader shader) {
				shader.getShader().setUniformf("effectScale",scale );
				shader.getShader().setUniformf("blurColor",color.r,color.g,color.b,color.a);
				shader.getShader().setUniformf("width",Gdx.graphics.getWidth());
				shader.getShader().setUniformf("height", Gdx.graphics.getHeight());
			}
		});
		blurScreen=new ScreenShader(shader);
	}
	public void start(){
		scale=0;
	}
	public void init(){
		scale=1;
		world.sendAction(world.getRoot().id, Scene.START_CRUISING);
	}
	public void render(){
		scale=Director.effect.scale*2;
		blurScreen.begin();
		world.render();
		blurScreen.end(director.getScreen(GameScreen.class).getCamera());
	}
}
