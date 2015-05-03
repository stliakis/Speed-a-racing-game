package tools;

import tools.general.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Text {
	public static int ALIGN_CENTER = 0;
	public static int ALIGN_RIGHT = 1;
	public static BitmapFont font;
	public static TextBounds bounds;
	static float r = 1, g = 1, b = 1, a = 1;
	public static TextureRegion tr;
	public static BitmapFontData fontData;
	private static TextBounds timeBounds;
	
	static {
		Texture texture = new Texture(Gdx.files.internal("deprecated/oldFont.png"));
	    fontData=new BitmapFontData(Gdx.files.internal("deprecated/oldFont.fnt"), false);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(fontData, new TextureRegion(
				texture), false);
		tr=new TextureRegion(texture);
		timeBounds = font.getBounds("00:00");
	}

	public static void drawText(SpriteBatch sb, String text, Vector pos,
			float scale, int align) {
		scale /= 3;
		font.setColor(r, g, b, a);
		font.setScale(scale);
		bounds = font.getBounds(text);
		if (align == ALIGN_CENTER) {
			font.drawMultiLine(sb, text, pos.x - (bounds.width / 2), pos.y
					+ (bounds.height / 2));
		}
		if (align == ALIGN_RIGHT) {
			font.drawMultiLine(sb, text, pos.x, pos.y + (bounds.height / 2));
		}
	}
	public static void drawTextCenter(SpriteBatch sb, String text, Vector pos,
			float scale) {
		drawText(sb,text,pos,scale*3,Text.ALIGN_CENTER);
	}
	public static void drawTextLeft(SpriteBatch sb, String text, Vector pos,
			float scale) {
		font.setColor(r, g, b, a);
		font.setScale(scale);
		bounds = font.getBounds(text);
		font.draw(sb, text, pos.x - (bounds.width), pos.y + (bounds.height / 2));
	}
	public static void drawTime(SpriteBatch sb,long millis,Vector pos,float size) {
		font.setColor(r, g, b, a);
		font.setScale(size);
		int minutes=(int) (millis/60);
		int seconds=(int) (millis%60);
		String text="";
		float x=0;
	
		if(minutes<10){
			x+=drawChar('0', sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
			x+=drawChar((char)(minutes+48), sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
		}
		else{
			x+=drawChar((char)(minutes/10+48), sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
			x+=drawChar((char)(minutes%10+48), sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
		}
		x+=drawChar(':', sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
		x+=timeBounds.width/5;
		if(seconds<10){
			x+=drawChar('0', sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
			x+=drawChar((char)(seconds+48), sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
		}
		else{
			x+=drawChar((char)(seconds/10+48), sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
			x+=drawChar((char)(seconds%10+48), sb, pos.x+x, pos.y+ (timeBounds.height / 2), size, 1,1,1,1);
		}
	}
	
	public static void drawTextRight(SpriteBatch sb, String text, Vector pos,
			float scale) {
		font.setColor(r, g, b, a);
		font.setScale(scale);
		bounds = font.getBounds(text);
		font.draw(sb, text, pos.x, pos.y + (bounds.height / 2));
	}
	static char charsArray[]=new char[50];
	public static void drawNumber(long number,SpriteBatch sb,float x,float y,float size,float r,float g,float b,float a){
		drawNumber(number, sb, x, y, size, r, g, b, a, '.',Text.ALIGN_CENTER);
	}
	public static void drawNumber(long number,SpriteBatch sb,float x,float y,float size,float r,float g,float b,float a,int align){
		drawNumber(number, sb, x, y, size, r, g, b, a, '.',align);
	}
	public static float drawChar(char chari,SpriteBatch sb,float x,float y,float size,float r,float g,float b,float a){
		Glyph glyph=fontData.getGlyph((char)(chari));
		tr.setRegion(glyph.u, glyph.v, glyph.u2, glyph.v2);
		tr.flip(false, true);
		sb.setColor(r, g, b, a);
		float sizeX=glyph.width*size;
		float sizeY=glyph.height*size;
		sb.draw(tr, x-sizeX/2, y-sizeY/2, sizeX, sizeY);
		return sizeX;
	}
	public static void drawNumber(long number,SpriteBatch sb,float x,float y,float size,float r,float g,float b,float a,char seperator,int align){
		
		if(number<10){
			Glyph glyph=fontData.getGlyph((char)(number+48));
			tr.setRegion(glyph.u, glyph.v, glyph.u2, glyph.v2);
			tr.flip(false, true);
			sb.setColor(r, g, b, a);
			float sizeX=glyph.width*size;
			float sizeY=glyph.height*size;
			sb.draw(tr, x-sizeX/2, y-sizeY/2, sizeX, sizeY);
			return;
		}
		
		int chars=0;
		while(number!=0)
		{
			if(chars%4==0)charsArray[chars++]=seperator;
			else{
				charsArray[chars++]=(char)((int) (number%10)+48); 
			    number=number/10;
			}
		}
		float xofset=0;
		float width=0;
		float height=0;
		for(int c=chars-1;c>0;c--){
			Glyph glyph=fontData.getGlyph(charsArray[c]);
			width+=glyph.width*size;
			height+=glyph.height*size;
		}
		height/=chars-1;
		for(int c=chars-1;c>0;c--){

				Glyph glyph=fontData.getGlyph(charsArray[c]);
				tr.setRegion(glyph.u, glyph.v, glyph.u2, glyph.v2);
				tr.flip(false, true);
				sb.setColor(r, g, b, a);
				float sizeX=glyph.width*size;
				float sizeY=glyph.height*size;
				if(align==Text.ALIGN_CENTER)sb.draw(tr, x+xofset-width/2, y-height/2, sizeX, sizeY);
				else if(align==Text.ALIGN_RIGHT)sb.draw(tr, x+xofset, y-height/2, sizeX, sizeY);
				xofset+=sizeX;
		}
	}
	public static void setColor(float ri, float gi, float bi, float ai) {
		r = ri;
		g = gi;
		b = bi;
		a = ai;

	}
}
