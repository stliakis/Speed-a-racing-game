package view;

import gameobjects.GameScreen;
import gameobjects.Scene;
import gameobjects.Ship;
import gameobjects.mechanisms.ShipCameraFollower;
import tools.Director;
import tools.Text2;
import tools.Text2.FontParrameters;
import tools.general.Tools;
import tools.general.Vector;
import tools.ui.Button;
import tools.ui.Button.OnEventListener;
import tools.ui.Layer;
import tools.ui.ProgressBar;
import tools.ui.Screen;
import tools.ui.UIRenderer;
import tools.ui.UIRenderer.OnFocus;
import tools.ui.UIRenderer.OnRenderListener;
import tools.world.Entity;
import tools.world.gWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HudLayer extends Layer {
	float scoreY;
	float scoreYvel, scoreYvel2;
	public static float SCORE = 0;
	public static float BEST;
	float textScale = 1;
	public GameScreen game;
	final Button exit;

	public HudLayer(Screen screen, final GameScreen game) {
		super(screen);
		this.game = game;
		setScreen((new Screen()).setCamera2D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
		setUirenderer(new UIRenderer());

		Button backMirrorFrame = new Button(x(70), y(90), x(20), x(9), "ui/mirror.png", getScreen());
		Button backMirror = new Button(x(70), y(90), x(20), x(9), "ui/btnPause.png", getScreen());
		backMirror.sprite.setRegion(game.rot.m_fboRegion);

		final ProgressBar nitro=new ProgressBar(x(50),y(10),x(60),y(5),"ui/nitro_full.png","ui/nitro_background.png",screen){
			@Override
			public void Render(SpriteBatch sb) {
				Ship player= (Ship) ((Scene) game.world.getRoot()).playersShip;
				if(player!=null){
					this.setProgress(player.nitroPercent);
					System.out.println(player.nitroPercent);
					this.visible=true;
				}
				else this.visible=false;
				
				super.Render(sb);
			}
		};
		
		exit = new Button(x(90), y(90), x(8), x(8), "ui/btnPause.png", getScreen());
		exit.setOnEventListener(new OnEventListener(Keys.ESCAPE, Keys.BACKSPACE) {

			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				getDirector().effect.start(getDirector(), PauseScreen.class, 0.05f);
				getDirector().effect.scale = 0;
				game.world.running = false;
			}
		});

		final FontParrameters font = new FontParrameters("default", 1, 1, 1, 1);
		final FontParrameters fontScore = new FontParrameters("score", 1, 1, 1, 1);
		getUirenderer().addItem(exit, backMirror, backMirrorFrame,nitro);
		getUirenderer().addOnRender(new OnRenderListener() {
			float alpha = 0;

			@Override
			public void OnRender(SpriteBatch sb) {
				float y = y(90);
				if (textScale > 1)
					textScale -= Gdx.graphics.getDeltaTime() * 2;
				if (alpha > 0)
					alpha -= Gdx.graphics.getDeltaTime() * 2;
				alpha = Tools.range(alpha, 0, 1);

				scoreYvel = ((y - scoreY) / 10);
				scoreYvel2 += ((scoreYvel - scoreYvel2) / 2) * Director.delta;
				scoreY += scoreYvel2 * Director.delta;

				exit.pos.y = scoreY;
				exit.size.set(x(8) * textScale);
				exit.color.a = getDirector().effect.scale;
				exit.color.checkRange();

				Ship player = (Ship) ((Scene) game.world.getRoot()).playersShip;
				Ship blue = (Ship) ((Scene) game.world.getRoot()).botShip1;
				Ship red = (Ship) ((Scene) game.world.getRoot()).botShip2;
				Ship green = (Ship) ((Scene) game.world.getRoot()).botShip3;

				if (scoreScale > 1)
					scoreScale -= Gdx.graphics.getDeltaTime() * 2;
				fontScore.getColor().set(1, 1, 1, Tools.range(getDirector().effect.scale, 0, 1));
				if (scoreScale == 0)
					scoreScale = 0.001f;
				if (blue.alive == true)
					Text2.drawText(sb, fontScore, (long) ((player.distance / Scene.RACE_LENGTH) * 100) + "%", Vector.vector.set(x(5), scoreY-y(10)), x(0.015f) * scoreScale * textScale, Text2.ALIGN_RIGHT);
				font.getColor().set(0.5f, 0.5f, 1, 1);
				if (blue.alive == true)
					Text2.drawText(sb, fontScore, "blue: " + (long) ((blue.distance / Scene.RACE_LENGTH) * 100) + "%", Vector.vector.set(x(95), scoreY - y(20)), x(0.015f) * scoreScale * textScale, Text2.ALIGN_LEFT);
				else
					Text2.drawText(sb, fontScore, "blue: crashed", Vector.vector.set(x(95), scoreY - y(20)), x(0.015f) * scoreScale * textScale, Text2.ALIGN_LEFT);
				font.getColor().set(1, 0.5f, 0.5f, 1);
				if (red.alive == true)
					Text2.drawText(sb, fontScore, "red: " + (long) ((red.distance / Scene.RACE_LENGTH) * 100) + "%", Vector.vector.set(x(95), scoreY - y(30)), x(0.015f) * scoreScale * textScale, Text2.ALIGN_LEFT);
				else
					Text2.drawText(sb, fontScore, "red: crashed", Vector.vector.set(x(95), scoreY - y(30)), x(0.015f) * scoreScale * textScale, Text2.ALIGN_LEFT);
				font.getColor().set(0.5f, 1, 0.5f, 1);
				if (green.alive == true)
					Text2.drawText(sb, fontScore, "green: " + (long) ((green.distance / Scene.RACE_LENGTH) * 100) + "%", Vector.vector.set(x(95), scoreY - y(40)), x(0.015f) * scoreScale * textScale, Text2.ALIGN_LEFT);
				else
					Text2.drawText(sb, fontScore, "green: crashed", Vector.vector.set(x(95), scoreY - y(40)), x(0.015f) * scoreScale * textScale, Text2.ALIGN_LEFT);
				font.getColor().set(1, 1, 1, 1);

				Text2.drawText(sb, fontScore,Tools.round(player.vel.y*40,1)+"kph", Vector.vector.set(x(5), scoreY), x(0.025f) * scoreScale * textScale, Text2.ALIGN_RIGHT);
				Text2.drawText(sb, fontScore, Gdx.graphics.getFramesPerSecond() + "", Vector.vector.set(x(5), y(10)), x(0.015f), Text2.ALIGN_RIGHT);
			}
		});

		getUirenderer().addOnFocusListener(new OnFocus() {
			@Override
			public void onFocus(UIRenderer renderer) {
				scoreY = y(110);
				scoreYvel = scoreYvel2 = 0;
				SCORE = 0;
				scoreScale = 1;
			}
		});
	}

	float scoreScale = 0;

	public static void increaseScore(float val) {
		SCORE += val;
	}

	public static void beat(float val) {
		// scoreScale=val;
	}
}
