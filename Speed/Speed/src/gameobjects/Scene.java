package gameobjects;

import gameobjects.mechanisms.BeatMechanism;
import gameobjects.mechanisms.ShipCameraFollower;
import gameobjects.mechanisms.ShipController;
import tools.Action;
import tools.Actions;
import tools.Director;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.Shapes.Shape;
import view.GameOverScreen;
import view.PauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;

public class Scene extends Entity {
	public static int TAG=gWorld.getNextTag();
	public static byte START_CRUISING=gWorld.getNextAction(),RESTART=gWorld.getNextAction(),GAMEOVER=gWorld.getNextAction();
	GameScreen gameScreen;
	public static long SCORE=0;
	public Scene(GameScreen gameScreen,gWorld world) {
		super(world);
		this.gameScreen=gameScreen;
	}
	public static boolean raining=false;
	public static float rainingDensity=0;
	@Override
	public void create() {
		super.create();
		tag=TAG;
		
		create(new RainSystem(world));
		
		for(int c=0;c<1;c++)create(new Ground(world));
		for(int c=0;c<1000;c++)create(new Block(world));
		
		for(int c=0;c<2;c++)create(new Explosion(world));
		
		final Entity playersShip=create(new Ship(world));
		playersShip.addMechanism(new ShipController(playersShip));
		playersShip.addMechanism(new ShipCameraFollower(playersShip));
		
		for(int c=0;c<1000;c++)create(new Particle(world));
		
		addMechanism(new ActionMechanism(this){
			@Override
			public void onReceive(Action action) {
				super.onReceive(action);
				if(action.action==Actions.ACTION_CREATE){
					world.sendAction(playersShip, Actions.ACTION_CREATE);
				}else if(action.action==START_CRUISING){
					world.getRoot().killChilds(Ship.TAG);
					world.sendAction(add(Ground.TAG), Actions.ACTION_CREATE);
					entity.init();
					entity.alive=true;
				}else if(action.action==RESTART){
					world.getRoot().killChilds(Ship.TAG);
					world.sendAction(playersShip, Actions.ACTION_CREATE);
					for(Entity block=world.begin(Block.GROUP);block!=null;block=world.next()){
						if(Math.abs(block.pos.x+world.cameraPos.x)<5 && Math.abs(block.pos.y+world.cameraPos.y)<20){
							world.sendAction(block.id, Block.ACTION_FADE_OUT);
						}
					}
				}
				else if(action.action==GAMEOVER){
					for(Entity block=world.begin(Block.GROUP);block!=null;block=world.next()){
						if(Math.abs(block.pos.x+world.cameraPos.x)<5 && Math.abs(block.pos.y+world.cameraPos.y)<20){
							world.sendAction(block.id, Block.ACTION_FADE_OUT);
						}
					}
					gameScreen.getDirector().effect.start(gameScreen.getDirector(), GameOverScreen.class,0.02f);
					world.sendAction(id, START_CRUISING);
				}
				else super.onReceive(action);
			}
		});
		
		final BeatMechanism beat=(BeatMechanism) addMechanism(new BeatMechanism(this));
		
		addMechanism(new IntervalMechanism(this,8) {
			@Override
			public void init() {
				super.init();
				world.sendAction(add(RainSystem.TAG), Actions.ACTION_CREATE);
			}
			@Override
			public void tick() {
				if(Tools.randf(0,1000)>999.5f)raining=!raining;
				
				if(raining){
					gColor.color.setVelocity4d(gColor.color2.set(0.15f,0.15f, 0.3f), Director.CLEAR_COLOR, Gdx.graphics.getDeltaTime());
					Director.CLEAR_COLOR.plus(gColor.color);
					if(rainingDensity<1)rainingDensity+=Gdx.graphics.getDeltaTime()/5;
				}else{
					gColor.color.setVelocity4d(gColor.color2.set(0.15f,0.15f, 0.8f), Director.CLEAR_COLOR, Gdx.graphics.getDeltaTime());
					Director.CLEAR_COLOR.plus(gColor.color);
					
					if(rainingDensity>0)rainingDensity-=Gdx.graphics.getDeltaTime()/5;
				}
				
				for(int c=0;c<40*rainingDensity;c++){
					RainSystem rs=(RainSystem) world.getEntityByTag(RainSystem.TAG);
					rs.add(Tools.randf(-world.cameraPos.x-10, -world.cameraPos.x+10),Tools.randf(-world.cameraPos.y+2, -world.cameraPos.y+25),Tools.randf(10, 10)
							,0.5f,0,-1);
				}
			}
		});

		addMechanism(new IntervalMechanism(this,0) {
			@Override
			public void init() {
				super.init();
				beat.Play("music/Junkie XL - Mushroom [HD].mp3");
			}
			@Override
			public void tick() {
				if(gameScreen.getDirector().curscreen instanceof GameScreen==false &&
						gameScreen.getDirector().curscreen instanceof PauseScreen==false){
					world.cameraRot.set(90,0,0);
					world.cameraPos.z=-1;
				}
				Entity player=world.getEntityByTag(Ship.TAG);
				if(player==null || !playersShip.getSystem(ShipCameraFollower.class).reachedCamera){
					world.cameraPos.y-=Gdx.graphics.getDeltaTime()*2;
				}
				int co=count(Block.TAG);
				if(co<100){
					for(int c=0;c<100-co;c++){
						Vector rand=Vector.vector.set(
								Tools.round(Tools.randf(-world.cameraPos.x-10, -world.cameraPos.x+10), 2),
								Tools.round(Tools.randf(-world.cameraPos.y+20, -world.cameraPos.y+40),2)
						);
						
						if((gameScreen.getDirector().curscreen instanceof GameScreen==false ||
								player!=null && !playersShip.getSystem(ShipCameraFollower.class).reachedCamera) && Math.abs(rand.x)<5){
							continue;
						}
						boolean far=false;
						for(Entity block=world.begin(Block.GROUP);block!=null;block=world.next()){
							if(block.pos.dis2(rand)<20)far=true;;
						}
						if(far==false){
							world.sendAction(add(Block.TAG), Actions.ACTION_CREATE_POS,rand.x,rand.y);
						}
					}
				}
			}
		});
		
	}
}
