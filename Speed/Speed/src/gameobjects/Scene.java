package gameobjects;

import gameobjects.mechanisms.ShipAI;
import gameobjects.mechanisms.ShipCameraFollower;
import gameobjects.mechanisms.ShipController;
import tools.Action;
import tools.Actions;
import tools.Director;
import tools.Shapes.Shape;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import view.CompleteScreen;
import view.GameOverScreen;
import view.PauseScreen;

import com.badlogic.gdx.Gdx;

public class Scene extends Entity {
	public static int TAG = gWorld.getNextTag();
	public static byte START_CRUISING = gWorld.getNextAction(), GAMEOVER = gWorld.getNextAction(), COMPLETE = gWorld.getNextAction();
	public static int DESTRUCTABLE = gWorld.getNextGroup();
	public static int GROUP_CONCRETE = gWorld.getNextGroup();
	public static byte ACTION_FADE_OUT = gWorld.getNextAction();
	GameScreen gameScreen;
	public static long SCORE = 0;

	public Scene(GameScreen gameScreen, gWorld world) {
		super(world);
		this.gameScreen = gameScreen;
	}

	public Entity botShip1, botShip2, botShip3, botShip4, playersShip;
	public static boolean raining = false;
	public static float rainingDensity = 0;
	public static float RACE_LENGTH = 10000;

	@Override
	public void create() {
		super.create();
		tag = TAG;

		create(new RainSystem(world));
		create(new ParticleSystem(world));

		create(new Map(world));

		create(new Ground(world));

		for (int c = 0; c < 30; c++)
			create(new Block(world));
		for (int c = 0; c < 4; c++)
			create(new Explosion(world));

		botShip1 = create(new Ship(world, "shipBlue",true));
		botShip1.addMechanism(new ShipAI(botShip1));

		botShip2 = create(new Ship(world, "shipRed",true));
		botShip2.addMechanism(new ShipAI(botShip2));

		botShip3 = create(new Ship(world, "shipGreen",true));
		botShip3.addMechanism(new ShipAI(botShip3));

		playersShip = create(new Ship(world, "ship",true));
		playersShip.addMechanism(new ShipController(playersShip));
		playersShip.addMechanism(new ShipCameraFollower(playersShip));

		addMechanism(new ActionMechanism(this) {
			@Override
			public void onReceive(Action action) {
				if (action.action == Actions.ACTION_CREATE) {
					entity.init();
					entity.alive = true;

					for (int c = 0; c < world.getGroups()[Scene.DESTRUCTABLE].size(); c++) {
						Entity e = world.getGroups()[Scene.DESTRUCTABLE].get(c);
						world.sendAction(e.id, Scene.ACTION_FADE_OUT);
					}

					world.getRoot().killChilds(Ship.TAG);
					world.getRoot().killChilds(Map.TAG);
					world.getRoot().killChilds(Ground.TAG);

					world.sendAction(add(RainSystem.TAG), Actions.ACTION_CREATE);
					world.sendAction(add(ParticleSystem.TAG), Actions.ACTION_CREATE);

					world.sendAction(playersShip, Actions.ACTION_CREATE);

					world.sendAction(add(Ground.TAG), Actions.ACTION_CREATE);
					world.sendAction(add(Map.TAG), Actions.ACTION_CREATE);

					world.sendAction(botShip1, Actions.ACTION_CREATE);
					world.sendAction(botShip2, Actions.ACTION_CREATE);
					world.sendAction(botShip3, Actions.ACTION_CREATE);

					botShip1.pos.plus(-1.5f, -1.5f);
					botShip2.pos.plus(1.5f, 1.5f);
					botShip3.pos.plus(-1.5f, 1.5f);
					playersShip.pos.plus(1.5f, -1.5f);
				} else if (action.action == START_CRUISING) {
					world.getRoot().killChilds(Ship.TAG);
					world.sendAction(add(Map.TAG), Actions.ACTION_CREATE);
					world.sendAction(add(Ground.TAG), Actions.ACTION_CREATE);

					entity.init();
					entity.alive = true;
				} else if (action.action == GAMEOVER) {
					for (Entity block = world.begin(Block.GROUP); block != null; block = world.next()) {
						if (Math.abs(block.pos.x + world.cameraPos.x) < 5 && Math.abs(block.pos.y + world.cameraPos.y) < 20) {
							world.sendAction(block.id, Scene.ACTION_FADE_OUT);
						}
					}
					gameScreen.getDirector().effect.start(gameScreen.getDirector(), GameOverScreen.class, 0.02f);
					world.sendAction(id, START_CRUISING);
				} else if (action.action == COMPLETE) {
					gameScreen.getDirector().effect.start(gameScreen.getDirector(), CompleteScreen.class, 0.02f);
					world.sendAction(id, START_CRUISING);
				} else
					super.onReceive(action);
			}
		});

		addMechanism(new IntervalMechanism(this, 8) {
			@Override
			public void init() {
				super.init();

			}

			@Override
			public void tick() {
				if (Tools.randf(0, 1000) > 999.5f)
					raining = !raining;

				if (raining) {
					gColor.color.setVelocity4d(gColor.color2.set(0.15f, 0.15f, 0.3f), Director.CLEAR_COLOR, Gdx.graphics.getDeltaTime());
					Director.CLEAR_COLOR.plus(gColor.color);
					if (rainingDensity < 1)
						rainingDensity += Gdx.graphics.getDeltaTime() / 5;
				} else {
					gColor.color.setVelocity4d(gColor.color2.set(0.15f, 0.15f, 0.8f), Director.CLEAR_COLOR, Gdx.graphics.getDeltaTime());
					Director.CLEAR_COLOR.plus(gColor.color);

					if (rainingDensity > 0)
						rainingDensity -= Gdx.graphics.getDeltaTime() / 5;
				}

				for (int c = 0; c < 40 * rainingDensity; c++) {
					RainSystem rs = (RainSystem) world.getEntityByTag(RainSystem.TAG);
					if (rs != null) {
						rs.add(Tools.randf(-world.cameraPos.x - 10, -world.cameraPos.x + 10), Tools.randf(-world.cameraPos.y + 2, -world.cameraPos.y + 25), Tools.randf(10, 10), 0.5f, 0, -1);
					}
				}
			}
		});

		addMechanism(new IntervalMechanism(this, 0) {
			@Override
			public void tick() {
				if (gameScreen.getDirector().curscreen instanceof GameScreen == false && gameScreen.getDirector().curscreen instanceof PauseScreen == false) {
					world.cameraRot.set(90, 0, 0);
					world.cameraPos.z = -1;
					world.cameraPos.x += (0 - world.cameraPos.x) * Gdx.graphics.getDeltaTime();
					world.cameraPos.y -= Gdx.graphics.getDeltaTime() * 2;
				}
				Entity player = world.getEntityByTag(Ship.TAG);
				if (player != null || !playersShip.getSystem(ShipCameraFollower.class).reachedCamera) {
					world.cameraPos.y -= Gdx.graphics.getDeltaTime() * 4;
				}
				int co = count(Block.TAG);
				if (co < 100) {
					for (int c = 0; c < 100 - co; c++) {
						Vector rand = Vector.vector.set(Tools.round(Tools.randf(-5, 5), 2), Tools.round(Tools.randf(-world.cameraPos.y + 20, -world.cameraPos.y + 40), 2));

						boolean far = false;
						for (Entity block = world.begin(Block.GROUP); block != null; block = world.next()) {
							if (Math.abs(block.pos.y - rand.y) < 5)
								far = true;

							if (block.pos.dis2(rand) < 25)
								far = true;
						}
						if (far == false) {
							float r = Tools.randf(0, 100);
							world.sendAction(add(Block.TAG), Actions.ACTION_CREATE_POS, rand.x, rand.y);
						}
					}
				}
			}
		});
	}

	static {
		Shape.registerShape("verticalQuad", new float[] { -0.5f, 0, -0.5f, 0, 1, 0.5f, 0, -0.5f, 1, 1, 0.5f, 0, 0.5f, 1, 0, -0.5f, 0, 0.5f, 0, 0 }, null);

		Shape.registerShape("cube", new float[] { -0.5f, 0.5f, 0.5f, 1, 1, 1, 0.5f, 0.5f, 0.5f, 1, 1, 1, 0.5f, 0.5f, -0.5f, 1, 1, 1, -0.5f, 0.5f, 0.5f, 1, 1, 1, -0.5f, 0.5f, -0.5f, 1, 1, 1, 0.5f, 0.5f, -0.5f, 1, 1, 1, -0.5f, -0.5f, 0.5f,
				1, 1, 1, 0.5f, -0.5f, 0.5f, 1, 1, 1, 0.5f, -0.5f, -0.5f, 1, 1, 1, -0.5f, -0.5f, 0.5f, 1, 1, 1, -0.5f, -0.5f, -0.5f, 1, 1, 1, 0.5f, -0.5f, -0.5f, 1, 1, 1, -0.5f, 0.5f, 0.5f, 1, 1, 1, -0.5f, -0.5f, 0.5f, 1, 1, 1, -0.5f,
				-0.5f, -0.5f, 1, 1, 1, -0.5f, -0.5f, -0.5f, 1, 1, 1, -0.5f, 0.5f, -0.5f, 1, 1, 1, -0.5f, 0.5f, 0.5f, 1, 1, 1, 0.5f, 0.5f, 0.5f, 1, 1, 1, 0.5f, -0.5f, 0.5f, 1, 1, 1, 0.5f, -0.5f, -0.5f, 1, 1, 1, 0.5f, -0.5f, -0.5f, 1, 1, 1,
				0.5f, 0.5f, -0.5f, 1, 1, 1, 0.5f, 0.5f, 0.5f, 1, 1, 1, }, null);
	}
}
