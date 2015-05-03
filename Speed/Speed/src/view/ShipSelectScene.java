package view;

import gameobjects.GameScreen;
import gameobjects.Ship;
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
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import view.CompleteScreen;
import view.GameOverScreen;
import view.PauseScreen;

import com.badlogic.gdx.Gdx;

public class ShipSelectScene extends Entity {
	public static int TAG = gWorld.getNextTag();
	public ShipSelectScene( gWorld world) {
		super(world);
	}
	Entity shipSpeed,shipAccelaration,shipBrakes;
	@Override
	public void create() {
		super.create();
		tag = TAG;
		shipSpeed = create(new Ship(world, "shipBlue",false));
		shipAccelaration = create(new Ship(world, "shipRed",false));
		shipBrakes = create(new Ship(world, "shipGreen",false));

		shipSpeed.removeMechanism(CollitionMechanism.class);
		shipAccelaration.removeMechanism(CollitionMechanism.class);
		shipBrakes.removeMechanism(CollitionMechanism.class);

		addMechanism(new ActionMechanism(this) {
			@Override
			public void onReceive(Action action) {
				if (action.action == Actions.ACTION_CREATE) {
					entity.init();
					entity.alive = true;

					world.sendAction(shipSpeed, Actions.ACTION_CREATE);
					world.sendAction(shipAccelaration, Actions.ACTION_CREATE);
					world.sendAction(shipBrakes, Actions.ACTION_CREATE);
				} else
					super.onReceive(action);
			}
		});

		addMechanism(new IntervalMechanism(this, 0) {
			@Override
			public void init() {
				super.init();
				
			}

			@Override
			public void tick() {
				shipSpeed.pos.set(-world.cameraPos.x+shipPosX,-world.cameraPos.y+1.5f, -world.cameraPos.z);
				shipSpeed.rotation.y=60;
				shipSpeed.rotation.z+=Gdx.graphics.getDeltaTime()*30;;
				
				shipBrakes.pos.set(-world.cameraPos.x+shipPosX+3,-world.cameraPos.y+1.5f, -world.cameraPos.z);
				shipBrakes.rotation.y=60;
				shipBrakes.rotation.z+=Gdx.graphics.getDeltaTime()*30;;
				
				shipAccelaration.pos.set(-world.cameraPos.x+shipPosX+6,-world.cameraPos.y+1.5f, -world.cameraPos.z);
				shipAccelaration.rotation.y=60;
				shipAccelaration.rotation.z+=Gdx.graphics.getDeltaTime()*30;;
				
				shipPosX+=(shipPosXMB-shipPosX)*Gdx.graphics.getDeltaTime()*10;
			}
		});
	}
	public float shipPosX=0,shipPosXMB;
	public void shiftCamera(float val){
		shipPosXMB-=val;
	}
}
