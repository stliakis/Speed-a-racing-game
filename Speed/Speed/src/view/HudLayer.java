package view;

import main.MainGame;
import gameobjects.GameScreen;
import gameobjects.Scene;
import tools.Director;
import tools.Text2;
import tools.Text2.FontParrameters;
import tools.general.Tools;
import tools.general.Vector;
import tools.ui.Button;
import tools.ui.Button.OnEventListener;
import tools.ui.Layer;
import tools.ui.Screen;
import tools.ui.UIRenderer;
import tools.ui.UIRenderer.OnFocus;
import tools.ui.UIRenderer.OnRenderListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HudLayer extends Layer {
	float scoreY;
	float scoreYvel,scoreYvel2;
	public static float SCORE=0;
	public static float BEST;
	float textScale=1;
	public GameScreen game;
	final Button exit;
	public HudLayer(Screen screen,final GameScreen game) {
		super(screen);
		this.game=game;
		setScreen((new Screen()).setCamera2D(new Vector(Gdx.graphics.getWidth(),Gdx.graphics.getHeight())));
		setUirenderer(new UIRenderer());
		
	    exit=new Button(x(90),y(90),x(8),x(8),"ui/btnPause.png",getScreen());
		exit.setOnEventListener(new OnEventListener(Keys.ESCAPE,Keys.BACKSPACE) {
			
			@Override
			public void onEventEnd(Input input, Object caller) {
				super.onEventEnd(input, caller);
				getDirector().effect.start(getDirector(), PauseScreen.class,0.05f);
				getDirector().effect.scale=0;
				game.world.running=false;
			}
		});
		
		final FontParrameters font=new FontParrameters("default", 1, 1, 1, 1);
		final FontParrameters fontScore=new FontParrameters("score", 1, 1, 1, 1);
		getUirenderer().addItem(exit);
		getUirenderer().addOnRender(new OnRenderListener() {
			float alpha=0;
			@Override
			public void OnRender(SpriteBatch sb) {
				float y=y(90);
				if(textScale>1)textScale-=Gdx.graphics.getDeltaTime()*2;
				if(alpha>0)alpha-=Gdx.graphics.getDeltaTime()*2;
				alpha=Tools.range(alpha, 0, 1);
				
				scoreYvel=((y-scoreY)/10);
				scoreYvel2+=((scoreYvel-scoreYvel2)/2)*Director.delta;
				scoreY+=scoreYvel2*Director.delta;
				
				exit.pos.y=scoreY;
				exit.size.set(x(8)*textScale);
				exit.color.a=getDirector().effect.scale;
				exit.color.checkRange();
				
				if(scoreScale>1)scoreScale-=Gdx.graphics.getDeltaTime()*2;
				fontScore.getColor().set(1, 1, 1,Tools.range(getDirector().effect.scale,0,1));
				Text2.drawText(sb,fontScore,(long) Scene.SCORE+"", Vector.vector.set(x(5), scoreY), x(0.03f)*scoreScale*textScale,Text2.ALIGN_RIGHT);
				Text2.drawText(sb,fontScore,"BEST:", Vector.vector.set(x(5.5f), scoreY-y(10)), x(0.015f)*scoreScale*textScale,Text2.ALIGN_RIGHT);
				fontScore.getColor().set(1, 0.5f, 0.5f,Tools.range(getDirector().effect.scale,0,1));
				Text2.drawText(sb,fontScore,MainGame.playerData.highScore+"", Vector.vector.set(x(5.75f), scoreY-y(17)), x(0.0125f)*scoreScale*textScale,Text2.ALIGN_RIGHT);
			}
		});
		
		getUirenderer().addOnFocusListener(new OnFocus() {
			@Override
			public void onFocus(UIRenderer renderer) {
				scoreY=y(110);
				scoreYvel=scoreYvel2=0;
				SCORE=0;
				scoreScale=1;
			}
		});
	}
    float  scoreScale=0;
	public static void increaseScore(float val){
		SCORE+=val;
	}
	public static void beat(float val){
	   // scoreScale=val;
	}
}
