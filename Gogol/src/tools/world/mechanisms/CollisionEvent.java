package tools.world.mechanisms;

import tools.world.Entity;

public class CollisionEvent {
	public boolean collideX, collideY;
	public Entity entity;

	public Entity getEntity() {
		return entity;
	}

	public boolean isCollideX() {
		return collideX;
	}

	public boolean isCollideY() {
		return collideY;
	}

	public void setCollideX(boolean collideX) {
		this.collideX = collideX;
	}

	public void setCollideY(boolean collideY) {
		this.collideY = collideY;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
