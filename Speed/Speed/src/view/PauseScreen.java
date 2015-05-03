package view;

import gameobjects.GameScreen;
import gameobjects.Scene;
import main.MainGame;
import tools.Actions;
import tools.Director;
import tools.MusicPlayer;
import tools.SoundEffectsPlayer;
import tools.Text2.FontParrameters;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.ui.Button;
import tools.ui.Button.OnEventListener;
import tools.ui.Label;
import tools.ui.Screen;
import tools.ui.UIRenderer;
import tools.ui.UIRenderer.OnFocus;
import tools.ui.UIRenderer.OnRenderListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseScreen extends Screen {
	MenuBackground background;

	public PauseScreen(final MenuBackground background) {
		setUirenderer(new UIRenderer());
		setCamera2D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		this.background = background;

		final Button btnExit = new Button(x(40), y(70), x(15), x(15), "ui/btnExit.png", this);
		btnExit.setOnEventListener(new OnEventListener(Keys.BACKSPACE) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnExit.setEffect(0);
				SoundEffectsPlayer.play("button", 0.5f);
				background.world.sendAction(background.world.getRoot().id, Scene.START_CRUISING);
				background.world.running = true;
				getDirector().effect.start(getDirector(), StartScreen.class, 0.05f);
			}
		});

		final Button btnRestart = new Button(x(60), y(70), x(15), x(15), "ui/btnRestart.png", this);
		btnRestart.setOnEventListener(new OnEventListener(Keys.R) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnRestart.setEffect(0);
				SoundEffectsPlayer.play("button", 0.5f);
				background.world.sendAction(background.world.getRoot().id, Actions.ACTION_CREATE);
				getDirector().effect.start(getDirector(), GameScreen.class, 0.05f);
				background.world.running = true;
			}
		});

		final Button btnResume = new Button(x(40), y(30), x(15), x(15), "ui/btnResume.png", this);
		btnResume.setOnEventListener(new OnEventListener(Keys.ESCAPE, Keys.SPACE) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnResume.setEffect(0);
				getDirector().effect.start(getDirector(), GameScreen.class, 0.05f);
				background.world.running = true;
			}
		});

		final Button btnMusic = new Button(x(60), y(30), x(15), x(15), "ui/btnMusic.png", this);
		btnMusic.setOnEventListener(new OnEventListener(Keys.M) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnMusic.setEffect(0);
				if (MainGame.config.SOUND_VOL == 1) {
					MainGame.config.SOUND_VOL = 0;
				} else {
					MainGame.config.SOUND_VOL = 1;
				}
				Tools.save(MainGame.config, "config.data");
			}
		});

		getUirenderer().addOnRender(new OnRenderListener() {
			@Override
			public void OnRender(SpriteBatch sb) {
				background.render2(sb);

				if (MainGame.config.SOUND_VOL == 1) {
					gColor.color.setVelocity4d(gColor.color2.set(1, 1, 1, 1), btnMusic.color, Gdx.graphics.getDeltaTime());
					btnMusic.color.plus(gColor.color).checkRange();

					MusicPlayer.setVolume(1);
				} else {
					gColor.color.setVelocity4d(gColor.color2.set(0.5f, 0.5f, 0.5f, 1), btnMusic.color, Gdx.graphics.getDeltaTime());
					btnMusic.color.plus(gColor.color).checkRange();

					MusicPlayer.setVolume(0);
				}

				for (int c = 0; c < getUirenderer().items.size(); c++) {
					if (getUirenderer().items.get(c) instanceof Label) {
						Label b = ((Label) getUirenderer().items.get(c));
						getDirector();
						b.pos.y = b.realPos.y - (1 - Director.effect.scale) * b.realPos.y * 1.2f;
						b.color.a = Tools.range(getDirector().effect.scale, 0, 1);
					}
					if (getUirenderer().items.get(c) instanceof Button) {
						Button b = ((Button) getUirenderer().items.get(c));
						getDirector();
						b.pos.y = b.realPos.y - (1 - Director.effect.scale) * b.realPos.y * 1.2f;
						b.color.a = Tools.range(getDirector().effect.scale, 0, 1);
					}
				}
				background.color.a = 1;
				background.color.g = getDirector().effect.scale;
				background.color.r = 1 - getDirector().effect.scale;
				background.color.b = 1 - getDirector().effect.scale;

			}
		});
		getUirenderer().addOnFocusListener(new OnFocus() {
			@Override
			public void onFocus(UIRenderer renderer) {
				background.start();
			}
		});

		getUirenderer().addItem(btnExit, btnMusic, btnRestart, btnResume);
	}

	@Override
	public void Render() {
		background.render();
		super.Render();
	}
}
