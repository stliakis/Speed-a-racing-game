package view;

import gameobjects.GameScreen;
import gameobjects.Scene;
import tools.Actions;
import tools.Director;
import tools.ScreenShader;
import tools.Shader;
import tools.Shader.ShaderParrametersListener;
import tools.SoundEffectsPlayer;
import tools.Text2.FontParrameters;
import tools.general.Tools;
import tools.general.Vector;
import tools.ui.Button;
import tools.ui.Button.OnEventListener;
import tools.ui.Label;
import tools.ui.Screen;
import tools.ui.UIRenderer;
import tools.ui.UIRenderer.OnFocus;
import tools.ui.UIRenderer.OnRenderListener;
import tools.world.gWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartScreen extends Screen {
	MenuBackground background;

	public StartScreen(final MenuBackground background) {
		setUirenderer(new UIRenderer());
		setCamera2D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		this.background = background;

		final Button btnPLay = new Button(x(50), y(50), x(25), x(25), "ui/start.png", this);
		btnPLay.setOnEventListener(new OnEventListener(Keys.ENTER, Keys.SPACE) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				Director.effect.start(getDirector(), ShipSelectScreen.class, 0.05f);
				SoundEffectsPlayer.play("button", 0.5f);
			}
		});

		final Button btnExit = new Button(x(15), y(15), x(8), x(8), "ui/btnExit.png", this);
		btnExit.setOnEventListener(new OnEventListener(Keys.ESCAPE) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				getDirector();
				SoundEffectsPlayer.play("button", 0.5f);
				Gdx.app.exit();
			}
		});

		final Button btnAbout = new Button(x(85), y(15), x(8), x(8), "ui/btnAbout.png", this);
		btnAbout.setOnEventListener(new OnEventListener(Keys.DEL) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				SoundEffectsPlayer.play("button", 0.5f);
				Director.effect.start(getDirector(), Credits.class, 0.05f);
			}
		});

		getUirenderer().addOnRender(new OnRenderListener() {
			@Override
			public void OnRender(SpriteBatch sb) {
				background.render2(sb);

				for (int c = 0; c < getUirenderer().items.size(); c++) {
					if (getUirenderer().items.get(c) instanceof Button) {
						Button b = ((Button) getUirenderer().items.get(c));
						b.pos.y = b.realPos.y - (1 - Director.effect.scale) * b.realPos.y * 1.2f;
						b.color.a = Tools.range(getDirector().effect.scale, 0, 1);
					}
				}
			}
		});
		getUirenderer().addOnFocusListener(new OnFocus() {
			@Override
			public void onFocus(UIRenderer renderer) {
				if (getDirector().prevscreen instanceof Credits == false) {
					background.init();
					background.color.set(1, 1, 1, 1);
				}

			}
		});
		getUirenderer().addItem(btnPLay, btnExit, btnAbout);
	}

	@Override
	public void Render() {
		background.render();
		super.Render();
	}
}
