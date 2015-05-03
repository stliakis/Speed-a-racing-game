package view;

import gameobjects.GameScreen;
import gameobjects.Scene;
import main.MainGame;
import tools.Director;
import tools.SoundEffectsPlayer;
import tools.Text2;
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

public class GameOverScreen extends Screen {
	MenuBackground background;

	public GameOverScreen(final MenuBackground background) {
		setUirenderer(new UIRenderer());
		setCamera2D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		this.background = background;

		final FontParrameters font = new FontParrameters("score", x(0.025f), 1, 1, 1);

		final Button btnExit = new Button(x(40), y(30), x(15), x(15), "ui/btnExit.png", this);
		btnExit.setOnEventListener(new OnEventListener(Keys.ESCAPE, Keys.BACKSPACE) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnExit.setEffect(0);
				SoundEffectsPlayer.play("button", 0.5f);
				getDirector().effect.start(getDirector(), StartScreen.class, 0.05f);
			}
		});

		final Button btnRestart = new Button(x(60), y(30), x(15), x(15), "ui/btnRestart.png", this);
		btnRestart.setOnEventListener(new OnEventListener(Keys.R, Keys.SPACE) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnRestart.setEffect(0);
				SoundEffectsPlayer.play("button", 0.5f);
				getDirector().effect.start(getDirector(), GameScreen.class, 0.05f);
				background.world.running = true;
			}
		});

		getUirenderer().addOnRender(new OnRenderListener() {
			@Override
			public void OnRender(SpriteBatch sb) {
				background.render2(sb);

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
				Text2.setColor(1, 1, 1, Tools.range(getDirector().effect.scale, 0, 1));

				Text2.drawText(sb, font, "CRASHED!", Vector.vector.set(x(50), y(65) + (1 - getDirector().effect.scale) * y(50)), x(0.05f), Text2.ALIGN_CENTER);

				background.color.a = 1;
				background.color.r = getDirector().effect.scale;
				background.color.g = 1 - getDirector().effect.scale;
				background.color.b = 1 - getDirector().effect.scale;
			}
		});
		getUirenderer().addOnFocusListener(new OnFocus() {
			@Override
			public void onFocus(UIRenderer renderer) {
				background.start();

				if (Scene.SCORE > MainGame.playerData.highScore) {
					MainGame.playerData.highScore = Scene.SCORE;
					Tools.save(MainGame.playerData, "playerData.data");
				}

			}
		});

		getUirenderer().addItem(btnExit, btnRestart);
	}

	@Override
	public void Render() {
		background.render();
		super.Render();
	}
}
