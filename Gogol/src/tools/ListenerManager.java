package tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ListenerManager{
	List<Object> listeners=new ArrayList<Object>();
	int it;
	Class clazz;
	public <T extends Object> T begin(Class<T> clazz){
		it=-1;
		this.clazz=clazz;
		return next();
	}
	public <T extends Object> T next(){
		while((++it)<listeners.size()){
			if (clazz.isInstance(listeners.get(it))){
				return (T)listeners.get(it);
			}
		}
		return null;
	}
	public void add(Object listener){
		listeners.add(listener);
	}
	public void test(){

	}
}
