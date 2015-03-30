package tools;

import java.util.HashMap;
import java.util.Map;

import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Text2 {
	
	
	
	public static int ALIGN_CENTER = 0;
	public static int ALIGN_RIGHT = 1;
	public static int ALIGN_LEFT = 2;
	static float r = 1, g = 1, b = 1, a = 1;
	public static TextureRegion tr;
	private static  Map<String,BitmapFont> fonts=new HashMap<String,BitmapFont>();
	public static class FontParrameters{
		gColor color;
		float size;
		String fontName;
		public FontParrameters(String font,float size,float r,float g,float b){
			this.fontName=font;
			this.size=size;
			this.color=new gColor(r,g,b,1);
		}
		public gColor getColor() {
			return color;
		}
		public float getSize() {
			return size;
		}
		public void setSize(float size) {
			this.size = size;
		}
		public String getFontName() {
			return fontName;
		}
	}
	static{
		loadFonts();
	}
	public static void loadFonts(){
		registerFont("default","fonts/defaultFont.ttf",128);
	}
	public static void registerFont(String name,String fileName,int size){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fileName));
		FreeTypeFontParameter parrameters=new FreeTypeFontParameter();
		parrameters.size=size;
		BitmapFont font = generator.generateFont(parrameters);
		generator.dispose();
		fonts.put(name, font);
	}
	public static BitmapFont getFont(String name){
		return fonts.get(name);
	}
	public static void drawText(SpriteBatch sb,FontParrameters fontp, String text, Vector pos,float scale, int align) {
		BitmapFont font;
		if(fontp==null)font=fonts.get("default");
		else font=fonts.get(fontp.getFontName());
		
		if(fontp!=null)font.setColor(fontp.color.r,fontp.color.g,fontp.color.b,fontp.color.a);
		else font.setColor(r,g,b,a);
		
		font.setScale(scale);
		TextBounds bounds = font.getBounds(text);
		if (align == ALIGN_CENTER) {
			font.drawMultiLine(sb, text, pos.x - (bounds.width / 2), pos.y+ (bounds.height / 2));
		}
		if (align == ALIGN_RIGHT) {
			font.drawMultiLine(sb, text, pos.x, pos.y + (bounds.height / 2));
		}
		if (align == ALIGN_LEFT) {
			font.drawMultiLine(sb, text, pos.x- bounds.width, pos.y + (bounds.height / 2));
		}
	}
	public static void drawTextMultiline(SpriteBatch sb,FontParrameters fontp, String text, Vector pos,float scale, int align,float lineHeight) {
		BitmapFont font;
		if(fontp==null)font=fonts.get("default");
		else font=fonts.get(fontp.getFontName());
		
		if(fontp!=null)font.setColor(fontp.color.r,fontp.color.g,fontp.color.b,fontp.color.a);
		else font.setColor(r,g,b,a);
		
		String[] lines=text.split("\\n");
		
		for(int c=0;c<lines.length;c++){
			font.setScale(scale);
			TextBounds bounds = font.getBounds(lines[c]);
			if (align == ALIGN_CENTER) {
				font.drawMultiLine(sb, lines[c], pos.x - (bounds.width / 2), pos.y+ (bounds.height / 2));
			}
			if (align == ALIGN_RIGHT) {
				font.drawMultiLine(sb,  lines[c], pos.x, pos.y + (bounds.height / 2));
			}
			if (align == ALIGN_LEFT) {
				font.drawMultiLine(sb,  lines[c], pos.x- bounds.width, pos.y + (bounds.height / 2));
			}
			pos.y-=lineHeight;
		}

	}
	static char charsArray[]=new char[50];
	public static void drawNumber(long number,SpriteBatch sb,float x,float y,float size){
		drawNumber(number, sb,null, x, y, size, '.',Text2.ALIGN_CENTER);
	}
	public static void drawNumber(long number,SpriteBatch sb,FontParrameters fontName,float x,float y,float size){
		drawNumber(number, sb,fontName, x, y, size, '.',Text2.ALIGN_CENTER);
	}
	public static void drawNumber(long number,SpriteBatch sb,float x,float y,float size,int align){
		drawNumber(number, sb,null, x, y, size, '.',align);
	}
	public static void drawNumber(long number,SpriteBatch sb,FontParrameters fontName,float x,float y,float size,int align){
		drawNumber(number, sb,fontName, x, y, size, '.',align);
	}
	public static void drawNumber(long number,SpriteBatch sb,FontParrameters fontName,float x,float y,float size,char seperator,int align){
		
		BitmapFont font;
		if(fontName==null){
			font=fonts.get("default");
		}
		else {
			font=fonts.get(fontName.getFontName());
		}
		
		tr=font.getRegion();
		if(number<10){
			Glyph glyph=font.getData().getGlyph(((char)(number+48)));
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
			Glyph glyph=font.getData().getGlyph(charsArray[c]);
			width+=glyph.width*size;
			height+=glyph.height*size;
		}
		height/=chars-1;
		for(int c=chars-1;c>0;c--){
				Glyph glyph=font.getData().getGlyph(charsArray[c]);
				tr.setRegion(glyph.u, glyph.v, glyph.u2, glyph.v2);
				tr.flip(false, true);
				sb.setColor(r, g, b, a);
				float sizeX=glyph.width*size;
				float sizeY=glyph.height*size;
				if(align==Text2.ALIGN_CENTER)sb.draw(tr, x+xofset-width/2, y-height/2, sizeX, sizeY);
				else if(align==Text2.ALIGN_RIGHT)sb.draw(tr, x+xofset, y-height/2, sizeX, sizeY);
				else if(align==Text2.ALIGN_LEFT)sb.draw(tr,  x+xofset-width, y-height/2, sizeX, sizeY);
				xofset+=sizeX;
		}
	}
	public static void drawNumberWithZ(long number,tools.SpriteBatch sb,FontParrameters fontName,float x,float y,float z,float size,char seperator,int align){
		
		BitmapFont font;
		if(fontName==null){
			font=fonts.get("default");
		}
		else {
			font=fonts.get(fontName.getFontName());
		}
		
		tr=font.getRegion();
		if(number<10){
			Glyph glyph=font.getData().getGlyph(((char)(number+48)));
			tr.setRegion(glyph.u, glyph.v, glyph.u2, glyph.v2);
			tr.flip(false, true);
			sb.setColor(r, g, b, a);
			float sizeX=glyph.width*size;
			float sizeY=glyph.height*size;
			sb.draw(tr, x-sizeX/2, y-sizeY/2,z, sizeX, sizeY);
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
			Glyph glyph=font.getData().getGlyph(charsArray[c]);
			width+=glyph.width*size;
			height+=glyph.height*size;
		}
		height/=chars-1;
		for(int c=chars-1;c>0;c--){
				Glyph glyph=font.getData().getGlyph(charsArray[c]);
				tr.setRegion(glyph.u, glyph.v, glyph.u2, glyph.v2);
				tr.flip(false, true);
				sb.setColor(r, g, b, a);
				float sizeX=glyph.width*size;
				float sizeY=glyph.height*size;
				if(align==Text2.ALIGN_CENTER)sb.draw(tr, x+xofset-width/2, y-height/2,z, sizeX, sizeY);
				else if(align==Text2.ALIGN_RIGHT)sb.draw(tr, x+xofset, y-height/2,z, sizeX, sizeY);
				else if(align==Text2.ALIGN_LEFT)sb.draw(tr,  x+xofset-width, y-height/2,z, sizeX, sizeY);
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
