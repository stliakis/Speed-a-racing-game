package tools.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.Action;
import tools.ReceiveListener;
import tools.general.Tools;
import tools.general.Vector;
import tools.ui.Screen;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.MeshMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class gWorld {
	static final int BOX_POSITION_ITERATIONS = 3;
	static final float BOX_STEP = 1 / 120f;
	static final int BOX_VELOCITY_ITERATIONS = 8;
	public Vector cameraPos, cameraRot;
	private static int nextFreeTag = -1;
	private static int nextFreeGroup = -1;
	private static byte nextFreeAction = -1;
	public static boolean LAYERED_RENDERING=true;
	public static int RENDER_LAYER1=0,RENDER_LAYER2=1,RENDER_LAYER3=2,RENDER_LAYER4=3;
	public int renderingLayer=0;
	public static int getNextTag() {
		nextFreeTag++;
		return nextFreeTag;
	}
	public static int getNextGroup() {
		nextFreeGroup++;
		return nextFreeGroup;
	}
	public static byte getNextAction() {
		nextFreeAction++;
		return nextFreeAction;
	}
	float accumulator;
	private Action actionTemp = new Action();
	private ArrayList<Entity> allEntities;
	private World box2dWorld;
	public Camera camera;

	public Vector3 cameraSizeRatio;
	Box2DDebugRenderer debugRenderer;
	private ArrayList<Entity>[] groups;
	private ArrayList<Entity>[] groupsAlives;
	private short ids = -1;

	public int indexID = 0;

	ReceiveListener receiveListener;

	private Entity rootEntity;

	float sceneEffects[] = new float[50];

	int sceneEffectsID = 0;

	public Screen screen;

	long timeStart = System.currentTimeMillis();

	Vector3 vec3 = new Vector3();

	public byte worldID;
	
	public List<MeshMechanism> meshMechanisms=new ArrayList<MeshMechanism>();
	
	public void prepareEntities(int...tags){
		
	}
	public void prepareEntities(){
		for(int c=0;c<allEntities.size();c++){
			Entity en=allEntities.get(c);
			for(int c2=0;c2<en.mechanisms.size();c2++){
				en.mechanisms.get(c2).reload();
			}
		}
	}
	
	public WorldRenderer worldrenderer;
	public gWorld(Screen screen) {
		cameraPos = new Vector(0, 0, 1);
		cameraRot = new Vector(0, 0, 1);
		camera = screen.getCamera();
		this.screen = screen;
		cameraSizeRatio = new Vector3(unproject(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()));

		worldrenderer = new WorldRenderer(camera);
		allEntities = new ArrayList<Entity>();

		groups = new ArrayList[40];
		groupsAlives = new ArrayList[40];
		for (int c = 0; c < groups.length; c++) {
			groups[c] = new ArrayList<Entity>(100);
			groupsAlives[c] = new ArrayList<Entity>(100);
		}

	}
	public gWorld(Screen screen,WorldRenderer worldRenderer) {
		this(screen);
		worldrenderer = worldRenderer;
	}
	
	public void debugDetails(){
		Tools.con("-------------------debug--------------------");
		Tools.con("entities:"+allEntities.size());
		
		Tools.con("-------------------end of debug--------------------");
	}
	private int iterator=0,iteratorGroup;
	public Entity begin(int group){
		iterator=0;
		iteratorGroup=group;
		if(getGroupsAlives()[iteratorGroup].size()<=iterator)return null;
		return getGroupsAlives()[iteratorGroup].get(iterator++);//-<<<<<<<<<<<<<<<
	}
	public Entity beginAll(int group){
		iterator=0;
		if(getGroups()[iteratorGroup].size()<=iterator)return null;
		return getGroups()[iteratorGroup].get(iterator++);
	}
	
	public Entity next(){
		if(getGroupsAlives()[iteratorGroup].size()<=iterator)return null;
		return getGroupsAlives()[iteratorGroup].get(iterator++);
	}
	public Entity nextAll(){
		if(getGroups()[iteratorGroup].size()<=iterator)return null;
		return getGroups()[iteratorGroup].get(iterator++);
	}
	public void addSceneEffect(int effect, float par1, float par2, float par3,
			float par4) {
		if (sceneEffectsID >= sceneEffects.length) {
			return;
		}
		sceneEffects[sceneEffectsID] = effect;
		sceneEffects[sceneEffectsID + 1] = par1;
		sceneEffects[sceneEffectsID + 2] = par2;
		sceneEffects[sceneEffectsID + 3] = par3;
		sceneEffects[sceneEffectsID + 4] = par4;

		sceneEffectsID += 5;
	}

	public short addToRoot(int tag) {
		return rootEntity.add(tag);
	}

	public void clearReceiveListener() {
		receiveListener = null;
	}

	public ArrayList<Entity> getAllEntities() {
		return allEntities;
	}

	public World getBox2dWorld() {
		return box2dWorld;
	}

	public Vector getCameraPos() {
		return cameraPos;
	}

	public Entity getEntity(int id) {
		if(id==-1)return null;
		return allEntities.get(id);
	}
	public void killAll(int mode) {
		for(Entity e=begin(mode);e!=null;e=next()){
			e.killAllTree();
		}
	}
	public Entity getEntityByTag(int tag) {
		for (int c = 0; c < allEntities.size(); c++) {
			if (!allEntities.get(c).alive || allEntities.get(c).tag != tag) {
				continue;
			}
			return allEntities.get(c);
		}
		return null;
	}

	public ArrayList<Entity>[] getGroups() {
		return groups;
	}

	public ArrayList<Entity>[] getGroupsAlives() {
		return groupsAlives;
	}

	public short getNextFreeId() {
		ids++;
		return ids;
	}

	public Entity getRoot() {
		return rootEntity;
	}

	public float getTime() {
		return System.currentTimeMillis() - timeStart;
	}

	public byte getWorldID() {
		return worldID;
	}
	public boolean running=true;
	public WorldRenderer getWorldrenderer() {
		return worldrenderer;
	}
	public void step(){
		this.render();
		if(running)this.update();
	}
	
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public float heightRealToWorld(float height) {
		return (cameraSizeRatio.y * 2 / Gdx.graphics.getHeight()) * height;
	}

	public void initBox2dWorld() {
		debugRenderer = new Box2DDebugRenderer();
		box2dWorld = new World(new Vector2(0, -100f), true);
		box2dWorld.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				getRoot().beginContact(contact);
			}

			@Override
			public void endContact(Contact contact) {
				getRoot().endContact(contact);
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				getRoot().postSolve(contact, impulse);
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				getRoot().preSolve(contact, oldManifold);
			}
		});

		World.setVelocityThreshold(1);
		box2dWorld.setContinuousPhysics(true);
	}

	public void receiveAction(Action action) {
		if (action.id < 0) {
			return;
		}
		action.from = worldID;
		if (receiveListener != null) {
			receiveListener.onReceive(action);
		}
		for (int c = 0; c < getEntity(action.id).getMechanisms().size(); c++) {
			if (!(getEntity(action.id).getMechanisms().get(c) instanceof ActionMechanism)) {
				continue;
			}
			((ActionMechanism) getEntity(action.id).getMechanisms().get(c))
					.onReceive(actionTemp);
		}
	}
	boolean firstRender=true;
	public void render() {
		if(running)update();
		if(firstRender){
			debugDetails();
			firstRender=false;
			
		}
		
		if (getRoot() == null) {
			return;
		}
		
		worldrenderer.getCamera().position.set(-cameraPos.x, -cameraPos.y,-cameraPos.z);
		worldrenderer.getCamera().rotate(cameraRot.z, 0, 0, 1);
		worldrenderer.getCamera().rotate(cameraRot.x, 1, 0, 0);
		worldrenderer.getCamera().rotate(cameraRot.y, 0, 1, 0);

		worldrenderer.RenderStart();
		
		worldrenderer.getCamera().update(false);
		
		if(LAYERED_RENDERING){
			renderingLayer=0;
			getRoot().render(worldrenderer);
			renderingLayer=1;
			getRoot().render(worldrenderer);
			renderingLayer=2;
			getRoot().render(worldrenderer);
			renderingLayer=3;
			getRoot().render(worldrenderer);
		}else getRoot().render(worldrenderer);
		
		
		worldrenderer.RenderEnd();
	
	
		if(meshMechanisms.size()!=0){
			MeshMechanism.begin(worldrenderer.getCamera());
			Collections.sort(meshMechanisms, MeshMechanism.compareMethod);
			for(int c=0;c<meshMechanisms.size();c++){
				if(!meshMechanisms.get(c).entity.isAlive())continue;
				meshMechanisms.get(c).renderMesh(worldrenderer.getCamera());
			}
			MeshMechanism.end();
		}
		worldrenderer.getCamera().rotate(-cameraRot.y, 0, 1, 0);
		worldrenderer.getCamera().rotate(-cameraRot.x, 1, 0, 0);
		worldrenderer.getCamera().rotate(-cameraRot.z, 0, 0, 1);
		
		worldrenderer.getCamera().position.set(cameraPos.x, cameraPos.y,cameraPos.z);

		worldrenderer.getCamera().update(false);

		if (debugRenderer != null && box2dWorld != null) {
			debugRenderer.render(getBox2dWorld(),
					worldrenderer.getCamera().combined);
		}
	}

	public void sendAction(Action action) {
		receiveAction(action);
	}

	public void sendAction(Entity id, byte action) {
		if(id==null)return;
		sendActionGlobal(id.id, action, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public void sendAction(Entity id, byte action, float par1, float par2) {
		if(id==null)return;
		sendActionGlobal(id.id, action, par1, par2, 0, 0, 0, 0, 0, 0);
	}

	public void sendAction(Entity id, byte action, float par1, float par2,
			float par3, float par4) {
		if(id==null)return;
		sendActionGlobal(id.id, action, par1, par2, par3, par4, 0, 0, 0, 0);
	}

	public void sendAction(Entity id, byte action, float par1, float par2,
			float par3, float par4, float par5, float par6) {
		if(id==null)return;
		sendActionGlobal(id.id, action, par1, par2, par3, par4, par5, par6, 0, 0);
	}
	
	public Entity sendAction(short id, byte action) {
		sendActionGlobal(id, action, 0, 0, 0, 0, 0, 0, 0, 0);
		if(id==-1)return null;
		return getEntity(id);
	}

	public Entity sendAction(short id, byte action, float par1, float par2) {
		sendActionGlobal(id, action, par1, par2, 0, 0, 0, 0, 0, 0);
		return getEntity(id);
	}
	
	public void sendAction(short id, byte action, float par1, float par2,float par3) {
		sendActionGlobal(id, action, par1, par2, par3, 0, 0, 0, 0, 0);
	}

	public void sendAction(short id, byte action, float par1, float par2,
			float par3, float par4) {
		sendActionGlobal(id, action, par1, par2, par3, par4, 0, 0, 0, 0);
	}

	public void sendAction(short id, byte action, float par1, float par2,
			float par3, float par4, float par5, float par6) {
		sendActionGlobal(id, action, par1, par2, par3, par4, par5, par6, 0, 0);
	}

	public void sendAction(short id, byte action, float par1, float par2,
			float par3, float par4, float par5, float par6, float par7,
			float par8) {
		if (id == -1) {
			// Tools.con("sending null id action:"+action);
			return;
		}
		actionTemp.id = id;
		actionTemp.action = action;
		actionTemp.time = getTime();
		actionTemp.par1 = par1;
		actionTemp.par2 = par2;
		actionTemp.par3 = par3;
		actionTemp.par4 = par4;
		actionTemp.par5 = par5;
		actionTemp.par6 = par6;
		actionTemp.par7 = par7;
		actionTemp.par8 = par8;
		if (actionTemp.id < 0) {
			return;
		}
		actionTemp.from = worldID;
		for (int c = 0; c < getEntity(id).getMechanisms().size(); c++) {
			if (!(getEntity(id).getMechanisms().get(c) instanceof ActionMechanism)) {
				continue;
			}
			((ActionMechanism) getEntity(id).getMechanisms().get(c))
					.onReceive(actionTemp);
		}
	}

	public void sendActionGlobal(short id, byte action, float par1, float par2,
			float par3, float par4, float par5, float par6, float par7,
			float par8) {
		if (id == -1) {
			// Tools.con("sending null id action:"+action);
			return;
		}
		actionTemp.id = id;
		actionTemp.action = action;
		actionTemp.time = getTime();
		actionTemp.par1 = par1;
		actionTemp.par2 = par2;
		actionTemp.par3 = par3;
		actionTemp.par4 = par4;
		actionTemp.par5 = par5;
		actionTemp.par6 = par6;
		receiveAction(actionTemp);
	}


	public void setBox2dWorld(World box2dWorld) {
		this.box2dWorld = box2dWorld;
	}

	public void setCameraPos(Vector cameraPos) {
		this.cameraPos = cameraPos;
	}

	public void setGroups(ArrayList<Entity>[] groups) {
		this.groups = groups;
	}

	public void setGroupsAlives(ArrayList<Entity>[] groupsAlives) {
		this.groupsAlives = groupsAlives;
	}

	public void setReceiveListener(ReceiveListener rl) {
		receiveListener = rl;
	}

	public void setRootEntity(Entity rootEntity) {
		if(this.rootEntity!=null){
			this.rootEntity.killAllTree();
		}
		this.rootEntity = rootEntity;
	}

	public void setRootEntityWithoutKilling(Entity rootEntity) {
		this.rootEntity = rootEntity;
	}
	
	public void setWorldID(byte worldID) {
		this.worldID = worldID;
	}

	public void setWorldrenderer(WorldRenderer worldrenderer) {
		this.worldrenderer = worldrenderer;
	}

	public void touch(Input input) {
		getRoot().touch(input);
	}

	public void touchDown(int x, int y, int pointer) {
		getRoot().touchDown(x, y, pointer);
	}

	public void touchUp(int x, int y, int pointer) {
		getRoot().touchUp(x, y, pointer);
	}

	public Vector3 unproject(float x, float y) {
		vec3.set(x, y, 0);
		camera.update();
		camera.unproject(vec3);
		vec3.x*=cameraPos.z;
		vec3.y*=cameraPos.z;
		vec3.z*=cameraPos.z;
		return vec3;
	}
	public Vector3 project(float x, float y) {
		vec3.set(x, y, 0);
		camera.project(vec3);
		vec3.x/=cameraPos.z;
		vec3.y/=cameraPos.z;
		vec3.z/=cameraPos.z;
		return vec3;
	}
	
	public void update() {
		if (getRoot() == null) {
			return;
		}
		getRoot().update();
		if (box2dWorld != null) {
			accumulator += Gdx.graphics.getDeltaTime();
			while (accumulator > BOX_STEP) {
				box2dWorld.step(BOX_STEP, BOX_VELOCITY_ITERATIONS,
						BOX_POSITION_ITERATIONS);
				accumulator -= BOX_STEP;
			}
			/*NOT DONE FOR GDX UPGRADE
			for (Iterator<Body> iter = box2dWorld.getBodies(); iter.hasNext();) {
				Body body = iter.next();
				if (body != null) {
					UserData data = (UserData) body.getUserData();
					if (data.readyToBeDeleted) {
						box2dWorld.destroyBody(body);
						body.setUserData(null);
						body = null;
					}
				}
			}
			*/
		}

	}
	public void creationFinished(){
		for(int c=0;c<tasksForFinished.size();c++){
			tasksForFinished.get(c).onDo();
		}
		tasksForFinished.clear();
	}
	public void addTaskForAfterCreation(TaskToDo ttd){
		tasksForFinished.add(ttd);
	}
	
	private List<TaskToDo> tasksForFinished=new ArrayList<TaskToDo>();
	public static interface TaskToDo{
		public void onDo();
	}

}
