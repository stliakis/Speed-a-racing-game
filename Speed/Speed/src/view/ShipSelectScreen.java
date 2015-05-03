package view;

import gameobjects.GameScreen;
import tools.Actions;
import tools.Director;
import tools.SoundEffectsPlayer;
import tools.Text2;
import tools.Text2.FontParrameters;
import tools.general.Tools;
import tools.general.Vector;
import tools.ui.Button;
import tools.ui.Button.OnEventListener;
import tools.ui.Label;
import tools.ui.ProgressBar;
import tools.ui.Screen;
import tools.ui.UIRenderer;
import tools.ui.UIRenderer.OnFocus;
import tools.ui.UIRenderer.OnRenderListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShipSelectScreen extends Screen {
	MenuBackground background;

	static int SHIP_SPEED=0,SHIP_ACELARATION=1,SHIP_BRAKES=2;
	int selectedShip;
	final ShipSelectScene scene;
	public ShipSelectScreen(final MenuBackground background) {
		setUirenderer(new UIRenderer());
		setCamera2D(new Vector(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		scene=new ShipSelectScene(background.world);
		this.background = background;
		
		
		FontParrameters font = new FontParrameters("score", x(0.015f), 1, 1, 1);
		
		
		final Label lbSpeed=new Label("Speed", x(20),y(34), x(0.008f), Text2.ALIGN_RIGHT, font, this);
		final ProgressBar pbspeed=new ProgressBar(x(50),y(30),x(60),y(5),"ui/nitro_full.png","ui/nitro_background.png",this);
		final Label lbAcceleration=new Label("Accelaration", x(20),y(24), x(0.008f), Text2.ALIGN_RIGHT, font, this);
		final ProgressBar pbaccelaration=new ProgressBar(x(50),y(20),x(60),y(5),"ui/nitro_full.png","ui/nitro_background.png",this);
		final Label lbBrakes=new Label("Brakes", x(20),y(14), x(0.008f), Text2.ALIGN_RIGHT, font, this);
		final ProgressBar pbbrakes=new ProgressBar(x(50),y(10),x(60),y(5),"ui/nitro_full.png","ui/nitro_background.png",this);
		getUirenderer().addItem(lbSpeed, pbspeed, lbAcceleration,pbaccelaration,lbBrakes,pbbrakes);
		
		
		final Button btnBack = new Button(x(5), y(5), x(8), x(8), "ui/btnPrevious.png", this);
		btnBack.setOnEventListener(new OnEventListener() {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				btnBack.setEffect(0);
				SoundEffectsPlayer.play("button", 0.5f);
				getDirector().effect.start(getDirector(), StartScreen.class, 0.05f);
			}
		});
		
		final Button btnNext = new Button(x(90), y(50), x(8), x(8), "ui/btnNext.png", this);
		btnNext.setOnEventListener(new OnEventListener() {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				if(selectedShip<2){
					selectedShip++;
					scene.shiftCamera(3);
				}
			}
		});
		
		final Button btnStart = new Button(x(90), y(15), x(10), x(10), "ui/start.png", this);
		btnStart.setOnEventListener(new OnEventListener() {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				SoundEffectsPlayer.play("button", 0.5f);
				getDirector().effect.start(getDirector(), GameScreen.class, 0.05f);
			}
		});
		
		final Button btnPrevius = new Button(x(10), y(50), x(8), x(8), "ui/btnPrevious.png", this);
		btnPrevius.setOnEventListener(new OnEventListener() {
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				if(selectedShip>0){
					selectedShip--;
					scene.shiftCamera(-3);
				}
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
					if (getUirenderer().items.get(c) instanceof ProgressBar) {
						ProgressBar b = ((ProgressBar) getUirenderer().items.get(c));
						b.pos.y = b.realPos.y - (1 - Director.effect.scale) * b.realPos.y * 1.2f;
						b.color.a = Tools.range(getDirector().effect.scale, 0, 1);
					}
				}

				if(selectedShip==SHIP_ACELARATION){
					pbaccelaration.setProgress(1);
					pbspeed.setProgress(0.5f);
					pbbrakes.setProgress(0.7f);
				}
				if(selectedShip==SHIP_SPEED){
					pbaccelaration.setProgress(0.8F);
					pbspeed.setProgress(1);
					pbbrakes.setProgress(0.4f);
				}
				if(selectedShip==SHIP_BRAKES){
					pbaccelaration.setProgress(0.5F);
					pbspeed.setProgress(0.7f);
					pbbrakes.setProgress(1);
				}
				
			}
		});
		getUirenderer().addOnFocusListener(new OnFocus() {
			@Override
			public void onFocus(UIRenderer renderer) {
				background.color.set(1, 1, 1, 1);
				background.world.sendAction(scene.id, Actions.ACTION_CREATE);
			}
		});

		getUirenderer().addItem(btnBack, btnNext, btnPrevius,btnStart);
	}

	@Override
	public void Render() {
		background.world.setRootEntityWithoutKilling(GameScreen.scene);
		background.render();
		
		background.world.setRootEntityWithoutKilling(scene);
		background.world.render();
		
		background.world.setRootEntityWithoutKilling(GameScreen.scene);
		
		super.Render();
	}
}
