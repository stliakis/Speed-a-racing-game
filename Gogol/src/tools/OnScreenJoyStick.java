package tools;

public class OnScreenJoyStick {

	private float joyStickRadius;
	private float knob_x;
	private float knob_y;
	private float knobRadius;
	private float origin_x;
	private float origin_y;

	public OnScreenJoyStick(float location_x, float location_y,
			float joyStickRadius, float knobRadius) {
		origin_x = location_x;
		origin_y = location_y;
		this.joyStickRadius = joyStickRadius;
		knob_x = location_x;
		knob_y = location_y;
		this.knobRadius = knobRadius;
	}

	public float angle() {
		return (float) (Math.atan2(deltaY(), deltaX()) * 180 / Math.PI);
	}

	private float deltaX() {
		return knob_x - origin_x;
	}

	private float deltaY() {
		return knob_y - origin_y;
	}

	public float getDrawKnobX() {
		return knob_x - knobRadius;
	}

	public float getDrawKnobY() {
		return knob_y - knobRadius;
	}

	public float getDrawOriginX() {
		return origin_x - joyStickRadius;
	}

	public float getDrawOriginY() {
		return origin_y - joyStickRadius;
	}

	public void resetKnob() {
		knob_x = origin_x;
		knob_y = origin_y;
	}

	public void updateKnobPosition(float x, float y) {
		if ((x - origin_x) * (x - origin_x) + (y - origin_y) * (y - origin_y) <= joyStickRadius
				* joyStickRadius) {
			knob_x = x;
			knob_y = y;
		} else {
			resetKnob();
		}
	}

	public float x_Normalized() {
		return deltaX() / joyStickRadius;
	}

	public float y_Normalized() {
		return deltaY() / joyStickRadius;
	}
}