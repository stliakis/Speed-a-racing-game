package view;

import gameobjects.GameScreen;
import tools.Director;
import tools.LoadingScreen;
import tools.general.Vector;
import tools.ui.Button;
import tools.ui.ProgressBar;
import tools.ui.UIRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class LogoScreen extends LoadingScreen {
	public ProgressBar pb;
	Button btnText;

	public LogoScreen() {
		setCamera2D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		uirenderer = new UIRenderer();

		btnText = new Button(x(50), y(50), x(35) * 1.5f, x(28) * 1.5f, new Texture(Gdx.files.internal("ui/logo2.png")), this);

		uirenderer.addItem(btnText);
	}

	float timeon = 0;

	@Override
	public void Update(Director director) {
		// TODO Auto-generated method stub
		super.Update(director);
		timeon += Gdx.graphics.getDeltaTime();

		if (timeon > 0.5f && timeon < 2) {
			if (btnText.color.a < 1)
				btnText.color.a += 0.05f * Director.delta;
			btnText.color.checkRange();
		}

		if (timeon > 2) {
			if (btnText.color.a > 0)
				btnText.color.a -= 0.05f * Director.delta;
			else
				getDirector().setReadyToLoadAssets(true);
			btnText.color.checkRange();

			getDirector();
			if (isReady() && !Director.effect.started) {
				getDirector();
				Director.effect.start(getDirector(), StartScreen.class, 0.05f);
			}
		}

		Director.CLEAR_COLOR.r += (0.0f - Director.CLEAR_COLOR.r) / 10;
		Director.CLEAR_COLOR.g += (0.0f - Director.CLEAR_COLOR.g) / 10;
		Director.CLEAR_COLOR.b += (0.1f - Director.CLEAR_COLOR.b) / 10;
	}

	float val = 0;

	@Override
	public void gotFocus() {
		// TODO Auto-generated method stub
		super.gotFocus();
		btnText.color.a = 0;
		Director.CLEAR_COLOR.set(0, 0, 0, 1);
	}
}
