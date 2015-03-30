package tools.general;

import java.util.ArrayList;
import java.util.List;

public class Pool< T >
{
	private List<T> notAlives=new ArrayList<T>();
	private List<T> alives=new ArrayList<T>();
	
	public List<T> getNotAlives() {
		return notAlives;
	}
	public void setNotAlives(List<T> notAlives) {
		this.notAlives = notAlives;
	}
	public List<T> getAlives() {
		return alives;
	}
	public void setAlives(List<T> alives) {
		this.alives = alives;
	}
	public Pool(int size,Class<T> a){
		for(int c=0;c<size;c++){
			try {
				notAlives.add(a.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	public boolean contains(Object b){
		return alives.contains(b);
	}
	public T getFree(){
		if(notAlives.size()==0)return null;
		T object=notAlives.get(0);
		notAlives.remove(object);
		alives.add(object);
		return (T)object;
	}
	public void release(T t){
		if(!alives.contains(t))return;
		notAlives.add(t);
		alives.remove(t);
	}
	public void release(int c){
		if(alives.size()<=c)return;
		notAlives.add(alives.get(c));
		alives.remove(c);
	}
	public T get(int c){
		return alives.get(c);
	}
	public int capacity(){
		return notAlives.size()+alives.size();
	}
	public int size(){
		return alives.size();
	}
	public void clear(){
		for(int c=0;c<alives.size();c++){
			notAlives.add(alives.get(c));
		}
		alives.clear();
	}
}
