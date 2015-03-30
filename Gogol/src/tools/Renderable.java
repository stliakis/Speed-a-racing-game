package tools;

public class Renderable {
	float x, y, width, height;

	public Renderable(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void Set(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
