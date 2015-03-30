package tools.world.mechanisms;

import tools.world.Entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class PhysiscsMechanism extends WorldMechanism {
	public static class UserData {
		public boolean readyToBeDeleted;
	}

	public static int SHAPE_CIRCLE = 1;
	public static int SHAPE_SQUARE = 0;
	public Body body;
	public BodyDef bodyDef;
	public FixtureDef fixtureDef;
	public PolygonShape playerShape;
	public int shape;
	public UserData userData;

	// Circel
	public PhysiscsMechanism(Entity entity, BodyType bodyType, int shape,
			float friction, float density, float restitution) {
		super(entity);
		this.shape = shape;
		if (shape == SHAPE_CIRCLE) {
			// generate bob's box2d body
			if (entity.world.getBox2dWorld() == null) {
				entity.world.initBox2dWorld();
			}
			userData = new UserData();

			bodyDef = new BodyDef();
			bodyDef.type = bodyType;

			fixtureDef = new FixtureDef();
			fixtureDef.density = density;
			fixtureDef.friction = friction;
			fixtureDef.restitution = restitution;
			fixtureDef.shape = new CircleShape();
			fixtureDef.shape.setRadius(entity.getColScale().x / 2);
		} else if (shape == SHAPE_SQUARE) {
			if (entity.world.getBox2dWorld() == null) {
				entity.world.initBox2dWorld();
			}
			userData = new UserData();

			// generate bob's box2d body
			float w = entity.getColScale().x / 2f;
			float h = entity.getColScale().y / 2f;

			bodyDef = new BodyDef();
			bodyDef.type = bodyType;

			playerShape = new PolygonShape();
			playerShape.setAsBox(w, h);

			fixtureDef = new FixtureDef();
			fixtureDef.shape = playerShape;
			fixtureDef.density = density;
			fixtureDef.friction = friction;
			fixtureDef.restitution = restitution;
		}

	}

	public void beginContact(Contact contact) {

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		super.die();
		userData.readyToBeDeleted = true;
	}

	public void endContact(Contact contact) {

	}

	public Entity getEntityFromBody(Body body) {
		for (int c = 0; c < entity.world.getAllEntities().size(); c++) {
			if (!entity.world.getAllEntities().get(c).alive) {
				continue;
			}
			Entity en = entity.world.getAllEntities().get(c);
			if (en.physicsMechanism != null && en.physicsMechanism.body == body) {
				return en;
			}
		}
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		if (shape == SHAPE_CIRCLE) {
			fixtureDef.shape.setRadius(entity.getColScale().x / 2);
		} else {
			float w = entity.getColScale().x / 2f;
			float h = entity.getColScale().y / 2f;
			playerShape.setAsBox(w, h);
		}
		userData.readyToBeDeleted = false;

		bodyDef.angle = entity.getAngle();
		bodyDef.position.set(entity.getPos().x, entity.getPos().y);
		body = entity.world.getBox2dWorld().createBody(bodyDef);
		body.setUserData(userData);
		body.createFixture(fixtureDef);
		body.setLinearVelocity(entity.vel.x, entity.vel.y);

	}

	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void update() {
		super.update();
		entity.getPos().set(body.getPosition().x, body.getPosition().y);
		entity.setAngle((float) (body.getAngle() * 180 / 3.14156535));
		entity.setVel(body.getLinearVelocity());
	}
}
