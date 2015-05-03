package tools.ui;

import tools.Text;
import tools.Text2;
import tools.Text2.FontParrameters;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Label extends UIitem {

	int align = 0;
	public String label;
	public boolean visible = true;
	private FontParrameters fontPars;

	public Label(String text, float x, float y, float sizes, int align,
			Screen screen) {
		super(screen);
		pos = new Vector(x, y);
		size = new Vector(sizes, sizes);
		color = new gColor(1, 1, 1, 1);
		realPos=new Vector(x,y);
		label = text;
		this.align = align;
	}
	public Label(String text, float x, float y, float sizes, int align,FontParrameters font,Screen screen) {
		super(screen);
		pos = new Vector(x, y);
		size = new Vector(sizes, sizes);
		color = new gColor(1, 1, 1, 1);
		label = text;
		realPos=new Vector(x,y);
		this.align = align;
		this.fontPars=font;
	}
	
	@Override
	public void Render(SpriteBatch sb) {
		if (!visible) {
			return;
		}

		
		if(fontPars==null){
			Text.setColor(color.r, color.g, color.b, color.a);
			Text.drawText(sb, label, pos, size.x, align);
		}else {
			float ca=fontPars.getColor().a;
			fontPars.getColor().a=color.a;
			Text2.setColor(fontPars.getColor().r, fontPars.getColor().g, fontPars.getColor().b, fontPars.getColor().a);
			Text2.drawText(sb,fontPars, label, getPos(),size.x,align);
			fontPars.getColor().a=ca;
		}
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
