package tools.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tools.Action;
import tools.Actions;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.MeshMechanism;
import tools.world.mechanisms.PhysiscsMechanism;
import tools.world.mechanisms.WorldMechanism;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Entity {
	
	public ActionMechanism actionMechanism;
	private Action actionTemp = new Action();
	public boolean alive;
	public float closerDis;
	public CollitionMechanism collisionMechanism;
	public gColor color, realColor;
	public boolean empty;
	public List<Entity> entities;
	int[] groups;
	public boolean hasChislds = false;
	public short id;
	int indexID = 0;
	public Entity parrent = null;
	
	public PhysiscsMechanism physicsMechanism;
	public Vector pos, rotation, vel, scale, colScale, realScale;
	public float origin;
	public float speed, realSpeed;

	public List<WorldMechanism> mechanisms;

	public int tag, classId;

	public gWorld world;
	public boolean renderAfterKids=false;

	
	public void removeMechanism(Class clazz){
		for (int c = 0; c < mechanisms.size(); c++) {
			if (clazz.isInstance(mechanisms.get(c))) {
				mechanisms.remove(c);
			}
		}
	}
	
	public Entity(gWorld world) {
		this.world = world;
		setId(world.getNextFreeId());
		
		//Tools.con("id:"+id+"  class:"+getClass());
		
		world.getAllEntities().add(id, this);
		empty = true;
		alive = false;
		mechanisms = new ArrayList<WorldMechanism>();
		entities = new ArrayList<Entity>();

		pos = new Vector();
		vel = new Vector();
		scale = new Vector();
		realScale = new Vector();
		colScale = new Vector();
		rotation = new Vector();

		color = new gColor(1, 1, 1, 1);
		realColor = new gColor(1, 1, 1, 1);
		create();
	}
	public void create(){
		
	}
	
	public void killAllTree(){
		//if(hasAliveKids()){
			for (int i = 0; i < entities.size(); ++i) {
				//if (entities.get(i).hasAliveKids()) {
				//	world.sendAction(entities.get(i).id, Actions.ACTION_DIE);
					entities.get(i).killAllTree();
			//	}
			}
		empty=true;
		alive=false;
		world.sendAction(id, Actions.ACTION_DIE);
		//alive=false;
	}
	public void killTreeLeefes(){
		//if(hasAliveKids()){
			for (int i = 0; i < entities.size(); ++i) {
				//if (entities.get(i).hasAliveKids()) {
				//	world.sendAction(entities.get(i).id, Actions.ACTION_DIE);
					entities.get(i).killAllTree();
			//	}
			}
		empty=true;
		//alive=false;
	}
	public Entity lastAdded;
	
	public short add(int TAG) {

		for (int i = 0; i < entities.size(); ++i) {
			Entity ent = entities.get(i);
			if (ent.alive || TAG != ent.tag || !ent.empty) {
				continue;
			}
			lastAdded=ent;
			return (short) ent.getId();
		}
		return -1;
	}
	
	public static class EntityIterable{
		public ArrayList<Entity> ents;
		public int it;
		EntityIterable(){
			ents=new ArrayList<Entity>();
			this.it=0;
		}
	}
	public HashMap<Integer,EntityIterable> tagged=new HashMap<Integer,EntityIterable>();
	
	public void addTagged(Entity e){
		if(tagged.get(e.tag)==null){
			tagged.put(e.tag, new EntityIterable());
			tagged.get(e.tag).ents.add(e);
		}else{
			tagged.get(e.tag).ents.add(e);
		}
	}
	public EntityIterable getAllByTag(int tag){
		return tagged.get(tag);
	}
	
	public static int time=0;
	
	public boolean hasAliveKids(){
		for (int i = 0; i < entities.size(); ++i) {
			if (entities.get(i).alive)return true;
		}
		return false;
	}
	public Entity getParrent(int tag){
		Entity par=this;
		do{
			par=par.parrent;
			if(par==null)return null;
		}while(par.tag!=tag);
		return par;
	}
	
	public int count(int tag){
		int c=0;
		for (int i = 0; i < entities.size(); ++i) {
			Entity ent = entities.get(i);
			if (!ent.alive || tag != ent.tag) {
				continue;
			}
			c++;
		}
		return c;
	}
	public WorldMechanism addMechanism(WorldMechanism mechanism) {
		mechanism.setEntity(this);
		mechanisms.add(mechanism);
		if (mechanism instanceof ActionMechanism) {
			actionMechanism = (ActionMechanism) mechanism;
		} else if (mechanism instanceof PhysiscsMechanism) {
			physicsMechanism = (PhysiscsMechanism) mechanism;
		} else if (mechanism instanceof CollitionMechanism) {
			collisionMechanism = (CollitionMechanism) mechanism;
		}else if (mechanism instanceof MeshMechanism){
			world.meshMechanisms.add((MeshMechanism) mechanism);
		}
		return mechanism;
	}

	public void beginContact(Contact contact) {
		if (physicsMechanism != null
				&& (physicsMechanism.body == contact.getFixtureA().getBody() || physicsMechanism.body == contact
						.getFixtureB().getBody())) {
			physicsMechanism.beginContact(contact);
		}
		for (int i = 0; i < entities.size(); ++i) {
			if (!entities.get(i).alive) {
				continue;
			}
			entities.get(i).beginContact(contact);
		}
	}

	public void clearGroups() {
		if (groups == null) {
			return;
		}
		for (int c = 0; c < groups.length; c++) {
			world.getGroups()[groups[c]].remove(this);
			world.getGroupsAlives()[groups[c]].remove(this);
		}
	}

	public Entity create(Entity ent) {
		ent.parrent = this;
		ent.setAlive(false);
		entities.add(ent);
		if(ent.getSystem(ActionMechanism.class)==null)ent.addMechanism(new ActionMechanism(ent));
		addTagged(ent);
		return ent;
	}
	

	public void die() {
		for (int c = 0; groups != null && c < groups.length; c++) {
			world.getGroupsAlives()[groups[c]].remove(this);
		}
		for (int c = 0; c < mechanisms.size(); c++) {
			mechanisms.get(c).die();
		}
	}
	public void getColRectangle(Rectangle rec){
		rec.set(pos.x-colScale.x/2, pos.y-colScale.y/2, colScale.x, colScale.y);
	}
	public void endContact(Contact contact) {
		if (physicsMechanism != null
				&& (physicsMechanism.body == contact.getFixtureA().getBody() || physicsMechanism.body == contact
						.getFixtureB().getBody())) {
			physicsMechanism.endContact(contact);
		}
		for (int i = 0; i < entities.size(); ++i) {
			if (!entities.get(i).alive) {
				continue;
			}
			entities.get(i).endContact(contact);
		}
	}

	public Entity findCloser(int group, int tag) {
		Entity closer = null;
		float closerDis = 1000;
		for (int c = 0; c < world.getGroupsAlives()[group].size(); c++) {
			Entity en = world.getGroupsAlives()[group].get(c);
			if (en.tag == tag) {
				float dis = en.getPos().dis2(getPos());
				if (closer == null) {
					closer = en;
					closerDis = dis;
				} else {
					if (dis < closerDis) {
						closerDis = dis;
						closer = en;
					}
				}
			}
		}
		this.closerDis = closerDis;
		return closer;
	}
	public Entity findCloser(int group) {
		Entity closer = null;
		float closerDis = 1000;
		for (int c = 0; c < world.getGroupsAlives()[group].size(); c++) {
			Entity en = world.getGroupsAlives()[group].get(c);
			float dis = en.getPos().dis2(getPos());
			if (closer == null) {
				closer = en;
				closerDis = dis;
			} else {
				if (dis < closerDis) {
					closerDis = dis;
					closer = en;
				}
			}
		}
		this.closerDis = closerDis;
		return closer;
	}
	public Action getActionTemp() {
		return actionTemp;
	}

	public float getAngle() {
		return rotation.z;
	}

	public int getClassId() {
		return classId;
	}

	public CollitionMechanism getCollisionSystem() {
		return collisionMechanism;
	}

	public gColor getColor() {
		return color;
	}

	public Vector getColScale() {
		return colScale;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public Entity getEntity(int id) {
		return entities.get(id);
	}
	
	public Entity getEntityByTag(int tag) {
		for(int c=0;c<entities.size();c++){
			if(entities.get(c).tag!=tag || !entities.get(c).alive)continue;
			return entities.get(c);
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public int getIndexID() {
		return indexID;
	}

	public Entity getParrent() {
		return parrent;
	}

	public Vector getPos() {
		return pos;
	}

	public gColor getRealColor() {
		return realColor;
	}

	public Vector getRealScale() {
		return realScale;
	}

	public float getRealSpeed() {
		return realSpeed;
	}

	public Vector getRotation() {
		return rotation;
	}

	public Vector getScale() {
		return scale;
	}

	public float getSpeed() {
		return speed;
	}

	public <T extends WorldMechanism> T getSystem(Class<T> clazz) {
		for (int c = 0; c < mechanisms.size(); c++) {
			if (clazz.isInstance(mechanisms.get(c))) {
				return clazz.cast(mechanisms.get(c));
			}
		}
		return null;
	}

	public List<WorldMechanism> getMechanisms() {
		return mechanisms;
	}

	public int getTag() {
		return tag;
	}

	public Vector getVel() {
		return vel;
	}

	public gWorld getWorld() {
		return world;
	}

	public void init() {
		for (int c = 0; groups != null && c < groups.length; c++) {
			world.getGroupsAlives()[groups[c]].add(this);
		}
		for (int c = 0; c < mechanisms.size(); c++) {
			mechanisms.get(c).init();
		}
		if (parrent != null) {
			parrent.empty = false;
		}
		speed=realSpeed;
		rotation.z=0;
	}
	// get/set

	public void initColor(float r, float g, float b, float a) {
		color.set(r, g, b, a);
		realColor.set(r, g, b, a);
	}

	public void initColScale(float x, float y) {
		colScale.set(x, y);
	}

	public void initPos(float x, float y) {
		pos.set(x, y);
	}

	public void initScale(float x, float y) {
		scale.set(x, y);
		realScale.set(x, y);
	}
	public void initScale(float x, float y,float z) {
		scale.set(x, y,z);
		realScale.set(x, y,z);
	}
	public void initSpeed(float speed) {
		this.speed = speed;
		realSpeed = speed;
	}

	public void initVel(float x, float y) {
		vel.set(x, y);
	}

	public boolean isAlive() {
		return alive;
	}

	public void killChilds() {
		for (int i = 0; i < entities.size(); ++i) {
			if (entities.get(i).alive) {
				world.sendAction(entities.get(i).id, Actions.ACTION_DIE);
			}
		}
	}

	public void killChilds(int tag) {
		for (int i = 0; i < entities.size(); ++i) {
			if (entities.get(i).alive && entities.get(i).tag == tag) {
				world.sendAction(entities.get(i).id, Actions.ACTION_DIE);
			}
		}
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		if (physicsMechanism != null
				&& (physicsMechanism.body == contact.getFixtureA().getBody() || physicsMechanism.body == contact
						.getFixtureB().getBody())) {
			physicsMechanism.postSolve(contact, impulse);
		}
		for (int i = 0; i < entities.size(); ++i) {
			if (!entities.get(i).alive) {
				continue;
			}
			entities.get(i).postSolve(contact, impulse);
		}
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		if (physicsMechanism != null
				&& (physicsMechanism.body == contact.getFixtureA().getBody() || physicsMechanism.body == contact
						.getFixtureB().getBody())) {
			physicsMechanism.preSolve(contact, oldManifold);
		}
		for (int i = 0; i < entities.size(); ++i) {
			if (!entities.get(i).alive) {
				continue;
			}
			entities.get(i).preSolve(contact, oldManifold);
		}
	}

	public final void render(WorldRenderer renderer) {
		
		if(renderAfterKids){
			for (int i = 0, len = entities.size(); i < len; ++i) {
				if (!entities.get(i).alive && entities.get(i).empty) {
					continue;
				}
				entities.get(i).render(renderer);
			}
			if (alive) {
				for (int c = 0, len = mechanisms.size(); c < len; c++) {
					mechanisms.get(c).render(renderer);
				}
			}
		}else{
			if (alive) {
				for (int c = 0, len = mechanisms.size(); c < len; c++) {
					mechanisms.get(c).render(renderer);
				}
			}
			for (int i = 0, len = entities.size(); i < len; ++i) {
				if (!entities.get(i).alive && entities.get(i).empty) {
					continue;
				}
				entities.get(i).render(renderer);
			}
		}
	}

	public void sendAction(Action action) {
		world.receiveAction(action);
	}

	public void sendAction(byte action) {
		sendAction(action, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public void sendAction(byte action, float par1, float par2, float par3,
			float par4) {
		sendAction(action, par1, par2, par3, par4, 0, 0, 0, 0);
	}

	public void sendAction(byte action, float par1, float par2, float par3,
			float par4, float par5, float par6, float par7, float par8) {
		if (parrent != null) {
			actionTemp.id = parrent.id;
			actionTemp.action = Actions.ACTION_CHILD;
			actionTemp.par1 = id;
			actionTemp.par2 = action;
		} else {
			actionTemp.id = id;
			actionTemp.par1 = par1;
			actionTemp.action = action;
			actionTemp.par2 = par2;
		}
		actionTemp.time = world.getTime();
		actionTemp.par3 = par3;
		actionTemp.par4 = par4;
		actionTemp.par5 = par5;
		actionTemp.par6 = par6;
		actionTemp.par7 = par7;
		actionTemp.par8 = par8;
		world.receiveAction(actionTemp);

	}

	public void setActionTemp(Action actionTemp) {
		this.actionTemp = actionTemp;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setAngle(float angle) {
		rotation.z = angle;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public void setCollisionSystem(CollitionMechanism collisionSystem) {
		this.collisionMechanism = collisionSystem;
	}

	public void setColor(gColor color) {
		this.color = color;
	}

	public void setColScale(Vector colScale) {
		this.colScale = colScale;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public void setGroups(int... groups) {
		this.groups = groups;
		for (int c = 0; groups != null && c < groups.length; c++) {
			world.getGroups()[groups[c]].add(this);
		}
	}

	public void setGroupsAndAlives(int... groups) {
		this.groups = groups;
		for (int c = 0; groups != null && c < groups.length; c++) {
			world.getGroups()[groups[c]].add(this);
			world.getGroupsAlives()[groups[c]].add(this);
		}
	}

	public void setId(short id) {
		this.id = id;
	}

	public void setIndexID(int indexID) {
		this.indexID = indexID;
	}

	public void setParrent(Entity parrent) {
		this.parrent = parrent;
	}

	public void setPos(Vector pos) {
		this.pos = pos;
	}

	public void setRealColor(gColor realColor) {
		this.realColor = realColor;
	}

	public void setRealScale(Vector realScale) {
		this.realScale = realScale;
	}

	public void setRealSpeed(float realSpeed) {
		this.realSpeed = realSpeed;
	}

	public void setScale(Vector scale) {
		this.scale = scale;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setmechanisms(List<WorldMechanism> mechanisms) {
		this.mechanisms = mechanisms;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public void setVel(Vector vel) {
		this.vel = vel;
	}

	public void setVel(Vector2 vel) {
		this.vel.x = vel.x;
		this.vel.y = vel.y;
	}

	public void setWorld(gWorld world) {
		this.world = world;
	}

	public void touch(Input input) {
		if (alive) {
			for (int c = 0, len = mechanisms.size(); c < len; c++) {
				mechanisms.get(c).touch(input);
			}
		}
		for (int i = 0; i < entities.size(); ++i) {
			if (!entities.get(i).alive && entities.get(i).empty) {
				continue;
			}
			entities.get(i).touch(input);
		}
	}

	public void touchDown(int x, int y, int pointer) {
		if (alive) {
			for (int c = 0; c < mechanisms.size(); c++) {
				mechanisms.get(c).touchDown(x, y, pointer);
			}
		}
		for (int i = 0; i < entities.size(); ++i) {
			if (!entities.get(i).alive && entities.get(i).empty) {
				continue;
			}
			entities.get(i).touchDown(x, y, pointer);
		}
	}

	public void touchUp(int x, int y, int pointer) {
		if (alive) {
			for (int c = 0; c < mechanisms.size(); c++) {
				mechanisms.get(c).touchUp(x, y, pointer);
			}
		}
		for (int i = 0; i < entities.size(); ++i) {
			if (!entities.get(i).alive && entities.get(i).empty) {
				continue;
			}
			entities.get(i).touchUp(x, y, pointer);
		}
	}

	public final void update() {
		if (alive) {
			for (int c = 0, len = mechanisms.size(); c < len; c++) {
				mechanisms.get(c).update();
			}
		}

		empty = true;
		for (int i = 0, len = entities.size(); i < len; ++i) {
			if (!entities.get(i).alive && entities.get(i).empty) {
				continue;
			}
			entities.get(i).update();
			empty = false;
		}

	}

}
