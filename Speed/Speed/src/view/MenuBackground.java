package view;

import gameobjects.GameScreen;
import gameobjects.Scene;
import tools.Actions;
import tools.Director;
import tools.ScreenShader;
import tools.Shader;
import tools.Shader.ShaderParrametersListener;
import tools.general.gColor;
import tools.ui.Button;
import tools.ui.Screen;
import tools.world.gWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuBackground {
	ScreenShader blurScreen;
	public gWorld world;
	public Director director;
	private float scale = 0;
	public gColor color = new gColor(1, 1, 1, 1);
	public Button shade;

	public MenuBackground(gWorld world, Director director) {
		this.world = world;
		this.director = director;
		Shader shader = new Shader("shaders/default.ver", "shaders/default.frag");
		shader.setListener(new ShaderParrametersListener() {
			@Override
			public void setParameters(Shader shader) {
				/*
				 * shader.getShader().setUniformf("effectScale", scale);
				 * shader.getShader().setUniformf("blurColor", color.r, color.g,
				 * color.b, color.a); shader.getShader() .setUniformf("width",
				 * Gdx.graphics.getWidth());
				 * shader.getShader().setUniformf("height",
				 * Gdx.graphics.getHeight());
				 */
			}
		});
		blurScreen = new ScreenShader(shader);

	}

	public void start() {
		scale = 0;
	}

	public void init() {
		if (shade == null) {
			Screen screen = director.getScreen(StartScreen.class);
			shade = new Button(screen.x(50), screen.y(50), screen.x(100), screen.y(100), "ui/darkness.png", screen);
		}

		scale = 1;
		world.sendAction(world.getRoot().id, Scene.START_CRUISING);
	}

	public void render2(SpriteBatch sb) {
		shade.Render(sb);
	}

	public void render() {
		shade.color.a = Director.effect.scale / 2;
		shade.color.checkRange();
		// blurScreen.begin();
		world.render();
		// blurScreen.end(director.getScreen(GameScreen.class).getCamera());
	}
}
