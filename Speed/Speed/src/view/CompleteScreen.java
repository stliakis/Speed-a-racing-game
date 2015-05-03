package view;

import gameobjects.GameScreen;
import gameobjects.Scene;
import gameobjects.Ship;
import main.MainGame;
import tools.Director;
import tools.SoundEffectsPlayer;
import tools.Text;
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

public class CompleteScreen extends Screen {
	MenuBackground background;

	public CompleteScreen(final MenuBackground background) {
		setUirenderer(new UIRenderer());
		setCamera2D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		this.background = background;

		final FontParrameters font = new FontParrameters("score", x(0.025f), 1, 1, 1);

		final Button btnExit = new Button(x(40), y(20), x(10), x(10), "ui/btnExit.png", this);
		btnExit.setOnEventListener(new OnEventListener(Keys.ESCAPE, Keys.BACKSPACE) {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnExit.setEffect(0);
				SoundEffectsPlayer.play("button", 0.5f);
				getDirector().effect.start(getDirector(), StartScreen.class, 0.05f);
			}
		});

		final Button btnRestart = new Button(x(60), y(20), x(10), x(10), "ui/btnRestart.png", this);
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

				Ship player = (Ship) ((Scene) background.world.getRoot()).playersShip;
				Ship blue = (Ship) ((Scene) background.world.getRoot()).botShip1;
				Ship red = (Ship) ((Scene) background.world.getRoot()).botShip2;
				Ship green = (Ship) ((Scene) background.world.getRoot()).botShip3;

				Text2.drawText(sb, font, "you: " + Tools.round(player.time, 0.01f), Vector.vector.set(x(25), y(80) + (1 - getDirector().effect.scale) * y(50)), x(0.03f), Text2.ALIGN_RIGHT);
				font.getColor().set(0.5f, 0.5f, 1, 1);
				if (blue.alive == true)
					Text2.drawText(sb, font, "blue: " + Tools.round(blue.time, 0.01f), Vector.vector.set(x(25), y(70) + (1 - getDirector().effect.scale) * y(50)), x(0.02f), Text2.ALIGN_RIGHT);
				else
					Text2.drawText(sb, font, "blue: crashed", Vector.vector.set(x(25), y(70) + (1 - getDirector().effect.scale) * y(50)), x(0.02f), Text2.ALIGN_RIGHT);
				font.getColor().set(1, 0.5f, 0.5f, 1);
				if (red.alive == true)
					Text2.drawText(sb, font, "red: " + Tools.round(red.time, 0.01f), Vector.vector.set(x(25), y(60) + (1 - getDirector().effect.scale) * y(50)), x(0.02f), Text2.ALIGN_RIGHT);
				else
					Text2.drawText(sb, font, "red: crashed", Vector.vector.set(x(25), y(60) + (1 - getDirector().effect.scale) * y(50)), x(0.02f), Text2.ALIGN_RIGHT);
				font.getColor().set(0.5f, 1, 0.5f, 1);
				if (green.alive == true)
					Text2.drawText(sb, font, "green: " + Tools.round(green.time, 0.01f), Vector.vector.set(x(25), y(50) + (1 - getDirector().effect.scale) * y(50)), x(0.02f), Text2.ALIGN_RIGHT);
				else
					Text2.drawText(sb, font, "green: crashed", Vector.vector.set(x(25), y(50) + (1 - getDirector().effect.scale) * y(50)), x(0.02f), Text2.ALIGN_RIGHT);
				font.getColor().set(1, 1, 1, 1);

				background.color.a = 1;
				background.color.r = 1 - getDirector().effect.scale;
				background.color.g = getDirector().effect.scale;
				background.color.b = getDirector().effect.scale;
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
