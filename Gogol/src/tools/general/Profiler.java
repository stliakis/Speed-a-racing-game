package tools.general;

public class Profiler {
	public static long time=0;
	public static void begin(){
		time=System.nanoTime();
	}
	public static void end(){
		long timen=System.nanoTime();
		Tools.con("total time:"+(timen-time)/1000000f+ " millis");
	}
}
