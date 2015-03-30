package tools.world.mechanisms;

import tools.general.Tools;
import tools.general.Vector;
import tools.world.Entity;
import tools.world.gWorld;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

public class TouchMechanism extends WorldMechanism {
	public TouchMechanism(Entity entity) {
		super(entity);
	}

	@Override
	public void touch(Input input) {
		Vector.vec3.set(entity.world.unproject(input.getX(), input.getY()));
		float x = -(Vector.vec3.x + entity.world.cameraPos.x);
		float y =- (Vector.vec3.y + entity.world.cameraPos.y);
		
		if (x > entity.pos.x - entity.colScale.x / 2
				&& x < entity.pos.x + entity.colScale.x / 2
				&& y > entity.pos.y - entity.colScale.y / 2
				&& y < entity.pos.y + entity.colScale.y / 2) {
			touchMe(input, x, y);
		}
		super.touch(input);
	}

	@Override
	public void touchDown(int x, int y, int pointer) {
		Vector.vec3.set(entity.world.unproject(x, y));
		float x1 = -(Vector.vec3.x + entity.world.cameraPos.x);
		float y1 = -(Vector.vec3.y + entity.world.cameraPos.y);
		if (x1 > entity.pos.x - entity.colScale.x / 2
				&& x1 < entity.pos.x + entity.colScale.x / 2
				&& y1 > entity.pos.y - entity.colScale.y / 2
				&& y1 < entity.pos.y + entity.colScale.y / 2) {
			touchMeDown(x, y, pointer, x1, y1);
		}
		super.touchDown(x, y, pointer);
	}

	public void touchMe(Input input, float localx, float localy) {

	}

	public void touchMeDown(int x, int y, int pointer, float localx,
			float localy) {

	}

	public void touchMeUp(int x, int y, int pointer, float localx, float localy) {

	}

	@Override
	public void touchUp(int x, int y, int pointer) {
		Vector.vec3.set(entity.world.unproject(x, y));
		float x1 = -(Vector.vec3.x + entity.world.cameraPos.x);
		float y1 = -(Vector.vec3.y + entity.world.cameraPos.y);
		if (x1 > entity.pos.x - entity.colScale.x / 2
				&& x1 < entity.pos.x + entity.colScale.x / 2
				&& y1 > entity.pos.y - entity.colScale.y / 2
				&& y1 < entity.pos.y + entity.colScale.y / 2) {
			touchMeUp(x, y, pointer, x1, y1);
		}
		super.touchUp(x, y, pointer);
	}

	public Vector toWorldCoord(float x, float y) {
		Vector.vec3.set(entity.world.unproject(x, y));
		float localx = Vector.vec3.x + entity.world.cameraPos.x;
		float localy = Vector.vec3.y + entity.world.cameraPos.y;
		return Vector.vector.set(-localx, -localy);
	}
	public Vector toWorldCoord(float x, float y,Vector vector) {
		Vector.vec3.set(entity.world.unproject(x, y));
		float localx = Vector.vec3.x + entity.world.cameraPos.x;
		float localy = Vector.vec3.y + entity.world.cameraPos.y;
		return vector.set(-localx, -localy);
	}
}
