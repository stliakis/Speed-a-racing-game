package tools.world.mechanisms;

import tools.Director;
import tools.general.Vector;
import tools.world.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class CollitionMechanism extends WorldMechanism {
	public int checkInterval = 0;
	public CollisionEvent colEvent;
	float lastUpdate = 0;
	private static Rectangle rec1=new Rectangle(),rec2=new Rectangle();
	public Vector colScale;
	public int targetGroup = -1;

	public CollitionMechanism(Entity entity) {
		super(entity);
		colEvent = new CollisionEvent();
		targetGroup = -1;
	}

	public CollitionMechanism(Entity entity, int targetGroup) {
		super(entity);
		this.targetGroup = targetGroup;
		colEvent = new CollisionEvent();
		checkInterval = 0;
		colScale=entity.colScale;
	}
	public CollitionMechanism(Entity entity, int targetGroup,Vector scale) {
		super(entity);
		this.targetGroup = targetGroup;
		colEvent = new CollisionEvent();
		checkInterval = 0;
		colScale=scale;
	}
	public CollitionMechanism(Entity entity, int targetGroup, int checkInterval) {
		super(entity);
		this.targetGroup = targetGroup;
		colEvent = new CollisionEvent();
		this.checkInterval = checkInterval;
		colScale=entity.colScale;
	}

	public CollisionEvent isCollide(Entity en) {
		// TODO Auto-generated method stub
		en.getColRectangle(rec1);
		entity.getColRectangle(rec2);
		colEvent.setCollideX(isCollideX(en));
		en.getColRectangle(rec1);
		entity.getColRectangle(rec2);
		colEvent.setCollideY(isCollideY(en));
		
		colEvent.setEntity(entity);
		if (colEvent.isCollideX() | colEvent.isCollideY()) {
			return colEvent;
		}
		return null;
	}

	public boolean isCollideX(Entity en) {
		rec1.x+= ((en.getVel().x) * en.getSpeed())* Director.delta;
		return rec2.overlaps(rec1);
	}

	public boolean isCollideY(Entity en) {
		rec1.y+= ((en.getVel().y) * en.getSpeed())	* Director.delta;
		return rec2.overlaps(rec1);
	}
	public void onCollide(CollisionEvent event) {
		
	}

	@Override
	public void update() {
		super.update();
		lastUpdate += Gdx.graphics.getDeltaTime() * 1000;

		lastUpdate = 0;
		if (targetGroup == -1) {
			return;
		}
		for (int c = 0; c < entity.world.getGroupsAlives()[targetGroup].size(); c++) {
			Entity en = entity.world.getGroupsAlives()[targetGroup].get(c);
			if (!en.alive || en == entity || en.collisionMechanism == null) {
				continue;
			}
			CollisionEvent ev = en.collisionMechanism.isCollide(entity);
			if (ev != null) {
				onCollide(ev);
			}
		}
	}
}
