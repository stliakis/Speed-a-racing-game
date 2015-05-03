package gameobjects;

import java.util.Collections;

import tools.Actions;
import tools.Director;
import tools.RenderOnTexture;
import tools.ScreenShader;
import tools.Shader;
import tools.general.Vector;
import tools.ui.Layer;
import tools.ui.Screen;
import tools.world.WorldRenderer;
import tools.world.gWorld;
import tools.world.mechanisms.MeshMechanism;
import view.HudLayer;
import view.HudLayer;
import view.PauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends Screen {
	public gWorld world;
	public static Scene scene;
	public RenderOnTexture rot = new RenderOnTexture(1);

	public GameScreen() {
		setCamera3D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		world = new gWorld(this, new WorldRenderer(this.getCamera()) {
			{
				rot.m_fboRegion.flip(true, true);
			}

			public void RenderEnd() {
				super.RenderEnd();

				getCamera().rotate(180, 0, 0, 1);
				getCamera().translate(0, 3, 0);
				getCamera().update(false);

				rot.begin();

				super.RenderStart();
				world.getRoot().render(this);
				this.getSpritebatch().end();

				if (world.meshMechanisms.size() != 0) {
					MeshMechanism.begin(world.worldrenderer.getCamera());
					Collections.sort(world.meshMechanisms, MeshMechanism.compareMethod);
					for (int c = 0; c < world.meshMechanisms.size(); c++) {
						if (!world.meshMechanisms.get(c).entity.isAlive())
							continue;
						world.meshMechanisms.get(c).renderMesh(world.worldrenderer.getCamera());
					}
					MeshMechanism.end();
				}

				rot.end();
				getCamera().translate(0, -3, 0);
				getCamera().rotate(-180, 0, 0, 1);
				getCamera().update(false);
			}
		});

		scene = new Scene(this, world);
		world.setRootEntity(scene);

		HudLayer hudLayer = new HudLayer(this, this);

		this.AddLayer(hudLayer);
	}

	@Override
	public void gotFocus() {
		super.gotFocus();
		for (Layer l : layers)
			l.closeNow();
		if (getDirector().prevscreen instanceof PauseScreen == false) {
			world.sendAction(scene.id, Actions.ACTION_CREATE);
		}
		getLayer(HudLayer.class).open();
	}

	@Override
	public void Render() {
		super.Render();
		world.render();
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
