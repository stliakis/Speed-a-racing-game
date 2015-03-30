package tools.general;

public class gColor {
	public static int AQUA = 5;
	public static int BLUE = 2;
	public static gColor color = new gColor(0, 0, 0, 0),color2 = new gColor(0, 0, 0, 0);
	public static int GREEN = 1;
	public static int ORANGE = 4;
	public static int PINK = 3;
	public static int RED = 0;
	public static int WHITE = 6;

	public static gColor color(float r, float g, float b, float a) {
		color.set(r, g, b, a);
		return color;
	}

	public static gColor color(int id) {
		if (id == RED) {
			color.set(1, 0, 0, 1);
		}
		if (id == GREEN) {
			color.set(0, 1, 0, 1);
		}
		if (id == BLUE) {
			color.set(0, 0, 1, 1);
		}
		if (id == AQUA) {
			color.setRGB(0, 191, 255);
		}
		if (id == PINK) {
			color.setRGB(255, 20, 147);
		}
		if (id == ORANGE) {
			color.setRGB(255, 127, 0);
		}
		if (id == WHITE) {
			color.setRGB(255, 255, 255);
		}

		return color;
	}

	public static int colorToInt(gColor colori) {
		if (colori.compare(color.set(1, 0, 0, 1))) {
			return RED;
		}
		if (colori.compare(color.set(0, 1, 0, 1))) {
			return GREEN;
		}
		if (colori.compare(color.set(0, 0, 1, 1))) {
			return BLUE;
		}
		if (colori.compare(color.setRGB(0, 191, 255))) {
			return AQUA;
		}
		if (colori.compare(color.setRGB(255, 20, 147))) {
			return PINK;
		}
		if (colori.compare(color.setRGB(255, 127, 0))) {
			return ORANGE;
		}

		return -1;
	}

	public static gColor vector() {
		return color;
	}

	public float r, g, b, a;

	public gColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public gColor(gColor color) {
		set(color);
	}

	public gColor checkRange() {
		if (r > 1) {
			r = 1;
		}
		if (g > 1) {
			g = 1;
		}
		if (b > 1) {
			b = 1;
		}
		if (a > 1) {
			a = 1;
		}

		if (r < 0) {
			r = 0;
		}
		if (g < 0) {
			g = 0;
		}
		if (b < 0) {
			b = 0;
		}
		if (a < 0) {
			a = 0;
		}
		return this;
	}

	public boolean compare(gColor color) {
		if (r == color.r && g == color.g && b == color.b) {
			return true;
		}
		return false;
	}

	public float dif(float r, float g, float b, float a) {
		return (float) Math.sqrt(Math.pow(this.r - r, 2)
				+ Math.pow(this.g - g, 2) + Math.pow(this.b - b, 2)
				+ Math.pow(this.a - a, 2));
	}

	public float dif(gColor color) {
		return (float) Math.sqrt(Math.pow(r - color.r, 2)
				+ Math.pow(g - color.g, 2) + Math.pow(b - color.b, 2)
				+ Math.pow(a - color.a, 2));
	}

	public float getA() {
		return a;
	}

	public float getB() {
		return b;
	}

	public float getG() {
		return g;
	}

	public float getR() {
		return r;
	}

	public gColor plus(gColor color) {
		r += color.r;
		g += color.g;
		b += color.b;
		a += color.a;
		return this;
	}

	public gColor set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}
	public gColor set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		return this;
	}
	public gColor set(gColor color) {
		r = color.r;
		g = color.g;
		b = color.b;
		a = color.a;
		return this;
	}

	public gColor setA(float a) {
		this.a = a;
		return this;
	}

	public void setB(float b) {
		this.b = b;
	}

	public void setG(float g) {
		this.g = g;
	}

	public void setR(float r) {
		this.r = r;
	}

	public gColor setRGB(int r, int g, int b) {
		this.r = r / 255.0f;
		this.g = g / 255.0f;
		this.b = b / 255.0f;
		a = 1;
		return this;
	}

	public gColor setVelocity4d(gColor target, gColor from, float speed) {
		float velx = target.r - from.r;
		float vely = target.g - from.g;
		float velz = target.b - from.b;
		float velw = target.a - from.a;
		float length = (float) Math.sqrt(velx * velx + vely * vely + velz
				* velz + velw * velw);
		if (length != 0) {
			velx = velx / length;
			vely = vely / length;
			velz = velz / length;
			velw = velw / length;
		}
		r = velx * speed;
		g = vely * speed;
		b = velz * speed;
		a = velw * speed;
		return this;
	}

}
