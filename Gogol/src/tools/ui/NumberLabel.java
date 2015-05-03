package tools.ui;

import tools.Text;
import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NumberLabel extends UIitem {

	int align = 0;
	long number;
	public boolean visible = true;

	public NumberLabel(long num, float x, float y, float sizes, int align,
			Screen screen) {
		super(screen);
		pos = new Vector(x, y);
		size = new Vector(sizes, sizes);
		color = new gColor(1, 1, 1, 1);
		number = num;
		this.align = align;
	}
	@Override
	public void Render(SpriteBatch sb) {
		if (!visible) {
			return;
		}
		Text.setColor(color.r, color.g, color.b, color.a);
		Text.drawNumber(number, sb, pos.x, pos.y, size.x, 1, 1, 1, 1,align);
	}

	@Override
	public boolean Touch(Input input) {

		return false;
	}

	@Override
	public void Update() {
		// color.r=colormb.r*0.05f+color.r*0.95f;
		// color.g=colormb.g*0.05f+color.g*0.95f;
		// color.b=colormb.b*0.05f+color.b*0.95f;
		// color.a=colormb.a*0.05f+color.a*0.95f;
	}

}
