package view;

import tools.Director;
import tools.SoundEffectsPlayer;
import tools.Text2;
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Credits extends Screen {
	MenuBackground background;

	public Credits(final MenuBackground background) {
		setUirenderer(new UIRenderer());
		setCamera2D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		this.background = background;

		FontParrameters font = new FontParrameters("score", x(0.015f), 1, 1, 1);

		final Label lblDeveloper = new Label("Programming: Liakis  Stefanos", x(50), y(80), x(0.015f), Text2.ALIGN_CENTER, font, this);
		final Label lblMusic = new Label("Music: Kevin  Macleod", x(50), y(50), x(0.015f), Text2.ALIGN_CENTER, font, this);

		final Button btnBack = new Button(x(15), y(15), x(8), x(8), "ui/btnPrevious.png", this);
		btnBack.setOnEventListener(new OnEventListener() {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnBack.setEffect(0);
				SoundEffectsPlayer.play("button", 0.5f);
				getDirector().effect.start(getDirector(), StartScreen.class, 0.05f);
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

			}
		});
		getUirenderer().addOnFocusListener(new OnFocus() {
			@Override
			public void onFocus(UIRenderer renderer) {
				background.color.set(1, 1, 1, 1);
			}
		});

		getUirenderer().addItem(btnBack, lblDeveloper, lblMusic);
	}

	@Override
	public void Render() {
		background.render();
		super.Render();
	}
}
