package tools.general;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import tools.FastMath;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

public class Tools {
	static Random rand;
	static {
		rand = new Random();
	}
	public static Vector2 tempVec2=new Vector2();
	public static void con(String string) {
		Gdx.app.log("CONSOLE:", string);
	}
	public static void e(String string) {
		Gdx.app.debug("ERROR:", string);
	}
	public static float per(float size, float percent) {
		return (percent / 100f) * size;
	}
	public static float round(float f,float prec)
	{
		return (float) (Math.floor(f*(1.0f/prec) + 0.5)/(1.0f/prec));
	}
	public static Object load(String filei){
		FileHandle file = Gdx.files.local(filei);
		if (!file.exists())return null;
		return Tools.deserialize(file.readBytes());
	}
	public static  void save(Object data,String filei){
		FileHandle file = Gdx.files.local(filei);
		byte[] bytes=Tools.serialize(data);
		file.writeBytes(bytes, false);
	}
    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o;
		try {
			o = new ObjectOutputStream(b);
		    o.writeObject(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        return b.toByteArray();
    }
    public static Object deserialize(byte[] bytes)  {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o;
		try {
			o = new ObjectInputStream(b);
			 return o.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
       return null;
    }
	public static float randf(float min, float max) {
		return FastMath.random() * (max - min) + min;
	}
	public static int randi(int low, int high) {
		return FastMath.random(low,high);
	}

	public static float rper(float size, float num) {
		return (num / size) * 100;
	}

	public static float x(float percent) {
		return (percent / 100f) * Gdx.graphics.getWidth();
	}

	public static Vector xy(float percentx, float percenty) {
		Vector ret = new Vector(x(percentx), y(percenty));
		return ret;
	}
	public static String resizeString(String string,int maxSize){
		if(string.length()<maxSize){
			for(int c=string.length();c<maxSize;c++){
				string+=" ";
			}
		}else{
			string=string.substring(0, maxSize);
			string=string.substring(0, string.length()-3);
			string+="...";
		}
		
		return string;
	}
	public static interface OnFinishEvent{public void onDone();}
	public static float range(float value,float min,float max){
		if(value>max)value=max;
		if(value<min)value=min;
		return value;
	}
	public static String get(String text, String attribute) {
		String[] parts = text.split(":");
		for (String part : parts) {
			if (part.split("=")[0].equals(attribute)) {
				return part.split("=")[1];
			}
		}
		return null;
	}

	public static boolean isInside(Vector[] points, Vector pos) {
		Vector.vector.x = pos.x;
		Vector.vector.y = pos.y;

		int i;
		int j;
		boolean result = false;
		for (i = 0, j = points.length - 1; i < points.length; j = i++) {
			if ((points[i].y > Vector.vector.y) != (points[j].y > Vector.vector.y)
					&& (Vector.vector.x < (points[j].x - points[i].x)
							* (Vector.vector.y - points[i].y)
							/ (points[j].y - points[i].y) + points[i].x)) {
				result = !result;
			}
		}
		return result;

	}

	public static float y(float percent) {
		return (percent / 100f) * Gdx.graphics.getHeight();
	}

	String[] split(String text, String c) {
		if (text.contains(c))
			return text.split(c);
		else
			return new String[] { text };
	}
}
