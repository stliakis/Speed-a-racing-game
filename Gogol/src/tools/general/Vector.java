package tools.general;

import com.badlogic.gdx.math.Vector3;

import tools.FastMath;

public class Vector {
	public static Vector vector = new Vector(0, 0);
	public static Vector vector2 = new Vector(0, 0);
	public static Vector vector3 = new Vector(0, 0);
	public static Vector vector4 = new Vector(0, 0);
	public static Vector vector5 = new Vector(0, 0);
	public static Vector vector6 = new Vector(0, 0);
	public static Vector3 vec3=new Vector3();
	public static float dis(float x, float y, float x2, float y2) {
		float distance_x = (x - x2);
		float distance_y = (y - y2);
		return FastMath
				.sqrt(((distance_x * distance_x) + (distance_y * distance_y)));
	}

	public static float dis2(float x, float y, float x2, float y2) {
		float distance_x = (x - x2);
		float distance_y = (y - y2);
		return (((distance_x * distance_x) + (distance_y * distance_y)));
	}

	public static Vector vector() {
		vector.set(0, 0, 0);
		return vector;
	}
	public Vector mul(float val){
		x*=val;
		y*=val;
		return this;
	}

	public static Vector vector(float x, float y) {
		vector.set(x, y, 0);
		return vector;
	}

	public float x, y, z;

	public Vector() {
		x = y = z = 0;
	}

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(Vector vector) {
		set(vector);
	}

	public float angle() {
		return ((FastMath.atan2(y, x) * 180 / 3.14156535f)) - 90;
	}

	public float angle(float xi, float yi) {
		return ((FastMath.atan2(yi - y, xi - x) * 180 / 3.14156535f));
	}

	public float angle(Vector vec) {
		return ((FastMath.atan2(vec.y - y, vec.x - x) * 180 / 3.14156535f));
	}

	public float dis(float x, float y) {
		float distance_x = (this.x - x);
		float distance_y = (this.y - y);
		return FastMath
				.sqrt(((distance_x * distance_x) + (distance_y * distance_y)));
	}

	public float dis(Vector vector) {
		float distance_x = (x - vector.x);
		float distance_y = (y - vector.y);
		return FastMath.sqrt(((distance_x * distance_x) + (distance_y * distance_y)));
	}

	public float dis2(float x, float y) {
		float distance_x = (this.x - x);
		float distance_y = (this.y - y);
		return (((distance_x * distance_x) + (distance_y * distance_y)));
	}

	public float dis2(Vector vector) {
		float distance_x = (x - vector.x);
		float distance_y = (y - vector.y);
		return (((distance_x * distance_x) + (distance_y * distance_y)));
	}

	public float dis3d(Vector vector) {
		float distance_x = (x - vector.x);
		float distance_y = (y - vector.y);
		float distance_z = (z - vector.z);
		return FastMath.sqrt(((distance_x * distance_x)+ (distance_y * distance_y) + (distance_z * distance_z)));
	}

	public float disFast(Vector vector) {
		float distance_x = (x - vector.x);
		float distance_y = (y - vector.y);
		return (((distance_x * distance_x) + (distance_y * distance_y)));
	}

	public boolean equals(Vector vec) {
		if (vec.x == x && vec.y == y) {
			return true;
		}
		return false;
	}
	public boolean equals(Vector vec,float range) {
		if (Math.abs(vec.x - x)<range && Math.abs(vec.y- y)<range) {
			return true;
		}
		return false;
	}
	public boolean equals(float xi,float yi,float range) {
		if (Math.abs(xi - x)<range && Math.abs(yi- y)<range) {
			return true;
		}
		return false;
	}
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Vector plus(Vector vector) {
		x += vector.x;
		y += vector.y;
		z += vector.z;
		return this;
	}
	public Vector minus(Vector vector) {
		x -= vector.x;
		y -= vector.y;
		z -= vector.z;
		return this;
	}
	public Vector plus(Vector vector,float mul) {
		x += vector.x*mul;
		y += vector.y*mul;
		z += vector.z*mul;
		return this;
	}
	public Vector plus(float x,float y) {
		this.x +=x;
		this.y +=y;
		return this;
	}
	public Vector reverse() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public Vector set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	public Vector setXZ(float x, float z) {
		this.x = x;
		this.z = z;
		return this;
	}
	public Vector set(float v) {
		this.x = v;
		this.y = v;
		return this;
	}
	public Vector set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector set(Vector vector) {
		x = vector.x;
		y = vector.y;
		z = vector.z;
		return this;
	}

	public Vector setByAngle(float angle, float speed) {
		x = FastMath.cos((angle) * 0.0174532925f) * speed;
		y = FastMath.sin((angle) * 0.0174532925f) * speed;
		
		if(x>0 && x<0.00001f)x=0;
		if(y>0 && y<0.00001f)y=0;
		return this;
	}

	public Vector setVelocity(Vector target, Vector from, float speed) {
		float velx = target.x - from.x;
		float vely = target.y - from.y;
		float length = FastMath.sqrt(velx * velx + vely * vely);
		if (length != 0) {
			velx = velx / length;
			vely = vely / length;
		}
		x = velx * speed;
		y = vely * speed;
		return this;
	}

	public Vector setVelocity3d(Vector target, Vector from, float speed) {
		float velx = target.x - from.x;
		float vely = target.y - from.y;
		float velz = target.z - from.z;
		float length = FastMath.sqrt(velx * velx + vely * vely + velz * velz);
		if (length != 0) {
			velx = velx / length;
			vely = vely / length;
			velz = velz / length;
		}
		x = velx * speed;
		y = vely * speed;
		z = velz * speed;

		return this;
	}

	public Vector setVelocity3d(Vector target, Vector from, float speed,
			float length) {
		float velx = target.x - from.x;
		float vely = target.y - from.y;
		float velz = target.z - from.z;
		if (length != 0) {
			velx = velx / length;
			vely = vely / length;
			velz = velz / length;
		}
		x = velx * speed;
		y = vely * speed;
		z = velz * speed;

		return this;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "x:" + x + ",y:" + y;
	}
}
