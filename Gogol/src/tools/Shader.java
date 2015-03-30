package tools;

import java.util.HashMap;

import tools.world.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {
	String frag, vert;
	ShaderProgram shader;
	public ShaderParrametersListener listener;
	public int type;
	public static int SCENE_SHADER=0,SCREEN_SHADER=1,DEFAULT=2;
	
	private static HashMap<String,HashMap<String,Shader>> staticShaders=new HashMap<String,HashMap<String,Shader>>();
	public static Shader getShader(String vert, String frag){
		if(staticShaders.get(vert)==null || staticShaders.get(vert).get(frag)==null){
			Shader shader =new Shader(vert,frag);
			staticShaders.put( vert, new HashMap<String, Shader>(1));
			staticShaders.get(vert).put(frag, shader);
			return shader;
		}else{
			return staticShaders.get(vert).get(frag);
		}
	}
	public Shader(String vert, String frag) {
		this.frag = frag;
		this.vert = vert;
		shader = new ShaderProgram(Gdx.files.internal(vert).readString(),	Gdx.files.internal(frag).readString());
		System.out.println(shader.getLog());
		type=SCENE_SHADER;
	}
	public Shader(String vert, String frag,int type){
		this(vert,frag);
		this.type=type;
	}
	public Shader(String vert, String frag,int type,ShaderParrametersListener spl){
		this(vert,frag);
		this.listener=spl;
		this.type=type;
	}
	
	
	public Shader(int type){
		this.vert = "shaders/default.ver";
		this.frag = "shaders/default.frag";
		shader=SpriteBatch.createDefaultShader();
		
		this.type=type;
	}
	public boolean compare(String frag, String vert) {
		if (this.frag.equals(frag) && this.vert.equals(vert)) {
			return true;
		}
		return false;
	}
	
	public String getFrag() {
		return frag;
	}

	public ShaderProgram getShader() {
		return shader;
	}

	public String getVert() {
		return vert;
	}

	public void setShader(ShaderProgram shader) {
		this.shader = shader;
	}
	
	
	public ShaderParrametersListener getListener() {
		return listener;
	}

	public void setListener(ShaderParrametersListener listener) {
		this.listener = listener;
	}


	public abstract static class ShaderParrametersListener{
		public Entity entity;
		abstract public void setParameters(Shader shader);
		public ShaderParrametersListener(){};
		//public ShaderParrametersListener(Entity entity){this.entity=entity;}
	}

}
