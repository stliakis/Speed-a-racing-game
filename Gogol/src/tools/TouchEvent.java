package tools;

import tools.general.Vector;

public class TouchEvent {

	public static int TYPE_MOVE = 0, TYPE_DOWN = 1, TYPE_UP = 2,
			TYPE_DOUBLE_TAP = 3;
	private int fingers;
	private Vector pos;
	private Vector[] posArray;
	private int type;

	public TouchEvent() {
		posArray = new Vector[2];

	}

	public int getFingers() {
		return fingers;
	}

	public Vector getPos() {
		return pos;
	}

	public Vector[] getPosArray() {
		return posArray;
	}

	public int getType() {
		return type;
	}

	public void Set(float x1, float y1, float x2, float y2, int type,
			int fingers) {
		this.type = type;
		posArray[0].set(x1, y1);
		posArray[1].set(x2, y2);
		pos.set(x1, y1);
		this.fingers = fingers;

	}

	public void setFingers(int fingers) {
		this.fingers = fingers;
	}

	public void setPos(Vector pos) {
		this.pos = pos;
	}

	public void setPosArray(Vector[] posArray) {
		this.posArray = posArray;
	}

	public void setType(int type) {
		this.type = type;
	}

}
