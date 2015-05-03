package tools;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

/**
 * Provides an easier way to declare Box2D Bodies.
 * 
 * @author acoppes
 */
public class JointBuilder {

	public class DistanceJointBuilder {

		public DistanceJointDef distanceJointDef;
		public InternalJointBuilder internalJointBuilder = new InternalJointBuilder();

		public DistanceJointBuilder bodyA(Body bodyA) {
			internalJointBuilder.bodyA(bodyA);
			return this;
		}

		public DistanceJointBuilder bodyB(Body bodyB) {
			internalJointBuilder.bodyB(bodyB);
			return this;
		}

		public Joint build() {
			return world.createJoint(distanceJointDef);
		}

		public DistanceJointBuilder collideConnected(boolean collideConnected) {
			internalJointBuilder.collideConnected(collideConnected);
			return this;
		}

		public DistanceJointBuilder dampingRatio(float dampingRatio) {
			distanceJointDef.dampingRatio = dampingRatio;
			return this;
		}

		public DistanceJointBuilder frequencyHz(float frequencyHz) {
			distanceJointDef.frequencyHz = frequencyHz;
			return this;
		}

		public DistanceJointBuilder length(float length) {
			distanceJointDef.length = length;
			distanceJointDef.dampingRatio = 1;
			distanceJointDef.frequencyHz = 2;
			return this;
		}

		public DistanceJointBuilder localAnchorA(float x, float y) {
			distanceJointDef.localAnchorA.set(x, y);
			return this;
		}

		public DistanceJointBuilder localAnchorB(float x, float y) {
			distanceJointDef.localAnchorB.set(x, y);
			return this;
		}

		private void reset() {
			// TODO: reuse the same distance Joint by setting default values
			// here..
			distanceJointDef = new DistanceJointDef();
			internalJointBuilder.setJointDef(distanceJointDef);
		}

	}

	private class InternalJointBuilder {

		JointDef jointDef;

		public InternalJointBuilder bodyA(Body bodyA) {
			jointDef.bodyA = bodyA;
			return this;
		}

		public InternalJointBuilder bodyB(Body bodyB) {
			jointDef.bodyB = bodyB;
			return this;
		}

		public InternalJointBuilder collideConnected(boolean collideConnected) {
			jointDef.collideConnected = collideConnected;
			return this;
		}

		public void setJointDef(JointDef jointDef) {
			this.jointDef = jointDef;
		}
	}

	public class RevoluteJointBuilder {

		private InternalJointBuilder internalJointBuilder = new InternalJointBuilder();
		private RevoluteJointDef revoluteJointDef;

		public RevoluteJointBuilder bodyA(Body bodyA) {
			internalJointBuilder.bodyA(bodyA);
			return this;
		}

		public RevoluteJointBuilder bodyB(Body bodyB) {
			internalJointBuilder.bodyB(bodyB);
			return this;
		}

		public Joint build() {
			return world.createJoint(revoluteJointDef);
		}

		public RevoluteJointBuilder collideConnected(boolean collideConnected) {
			internalJointBuilder.collideConnected(collideConnected);
			return this;
		}

		public RevoluteJointBuilder length(float length) {
			revoluteJointDef.enableMotor = true;
			revoluteJointDef.maxMotorTorque = 1;
			revoluteJointDef.motorSpeed = 0.1f;
			return this;
		}

		private void reset() {
			// TODO: reuse the same distance Joint by setting default values
			// here..
			revoluteJointDef = new RevoluteJointDef();
			internalJointBuilder.setJointDef(revoluteJointDef);
		}

	}

	public class RopeJointBuilder {

		private InternalJointBuilder internalJointBuilder = new InternalJointBuilder();
		public RopeJointDef ropeJointDef;

		public RopeJointBuilder bodyA(Body bodyA) {
			internalJointBuilder.bodyA(bodyA);
			return this;
		}

		public RopeJointBuilder bodyA(Body bodyA, float lx, float ly) {
			internalJointBuilder.bodyA(bodyA);
			ropeJointDef.localAnchorA.set(lx, ly);
			return this;
		}

		public RopeJointBuilder bodyB(Body bodyB) {
			internalJointBuilder.bodyB(bodyB);
			return this;
		}

		public RopeJointBuilder bodyB(Body bodyB, float lx, float ly) {
			internalJointBuilder.bodyB(bodyB);
			ropeJointDef.localAnchorB.set(lx, ly);
			return this;
		}

		public Joint build() {
			return world.createJoint(ropeJointDef);
		}

		public RopeJointBuilder collideConnected(boolean collideConnected) {
			internalJointBuilder.collideConnected(collideConnected);
			return this;
		}

		public RopeJointBuilder maxLength(float maxLength) {
			ropeJointDef.maxLength = maxLength;
			return this;
		}

		private void reset() {
			// TODO: reuse the same distance Joint by setting default values
			// here..
			ropeJointDef = new RopeJointDef();
			internalJointBuilder.setJointDef(ropeJointDef);
		}

	}

	public DistanceJointBuilder distanceJointBuilder;
	public RevoluteJointBuilder revoluteJointBuilder;
	public RopeJointBuilder ropeJointBuilder;
	private final World world;

	public JointBuilder(World world) {
		this.world = world;
		distanceJointBuilder = new DistanceJointBuilder();
		ropeJointBuilder = new RopeJointBuilder();
		revoluteJointBuilder = new RevoluteJointBuilder();
	}

	public DistanceJointBuilder distanceJoint() {
		distanceJointBuilder.reset();
		return distanceJointBuilder;
	}

	public RevoluteJointBuilder revoluteJoint() {
		revoluteJointBuilder.reset();
		return revoluteJointBuilder;
	}

	public RopeJointBuilder ropeJointBuilder() {
		ropeJointBuilder.reset();
		return ropeJointBuilder;
	}
}