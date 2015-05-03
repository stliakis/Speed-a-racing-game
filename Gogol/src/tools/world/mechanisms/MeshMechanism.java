package tools.world.mechanisms;

import java.util.Comparator;

import tools.Director;
import tools.Shader;
import tools.Shapes.Shape;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class MeshMechanism extends WorldMechanism{
	public Texture texture;
	public Shape shape;
	public Mesh mesh;
	public gColor color;
	public Vector scale,pos;
	public Shader myShader;
	public boolean dynamicVertices=false;
	public MeshMechanism(Entity entity,Shape shape,Shader shader,String texture,int primitive,VertexAttribute...atributes){
		this(entity,shape,shader,texture,entity.color,entity.scale,entity.pos,primitive,atributes);
	}
	public MeshMechanism(Entity entity,Shape shape,Shader shader,int primitive,VertexAttribute...atributes){
		this(entity,shape,shader,null,entity.color,entity.scale,entity.pos,primitive,atributes);
	}
	
	public MeshMechanism(Entity entity,Shape shape,Shader shader,gColor color,Vector scale,int primitive,VertexAttribute...atributes){
		this(entity,shape,shader,null,color,scale,entity.pos,primitive,atributes);
	}
	public MeshMechanism(Entity entity,Shape shape,Shader shader,String texture,gColor color,Vector scale,int primitive,VertexAttribute...atributes){
		this(entity,shape,shader,texture,entity.color,entity.scale,entity.pos,primitive,atributes);
	}
	public MeshMechanism(Entity entity,Shape shape,Shader shader,String texture,gColor color,Vector scale,Vector pos,int primitive,VertexAttribute...atributes){
		super(entity);
		this.shape=shape;
		if(texture!=null){
			createSprite(texture);
		}
		this.myShader=shader;
		this.color=color;
		this.scale=scale;
		this.pos=pos;
		this.primitive=primitive;
		if(atributes==null || atributes.length==0){
			mesh = new Mesh(true, shape.vertices.length,shape.indices.length, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.Normal,
					3, "a_normal"),new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"), new VertexAttribute(Usage.Color, 4, "a_color"));
		}else{
			mesh = new Mesh(true, shape.vertices.length,shape.indices==null?0:shape.indices.length, atributes);
		}

		mesh.setVertices(shape.vertices);
		
		if(shape.indices!=null)mesh.setIndices(shape.indices);
		
		mesh.getVertexAttribute(Usage.Position).alias = "a_position";
				
	}
	public float sortingValue(){
		return pos.z;
	}
	public int primitive;
	public static String uProj="u_projTrans",vColor="v_color2";
	public void renderMesh(Camera camera) {
		if(myShader!=currentShader){
			if(currentShader!=null)currentShader.getShader().end();
			currentShader=myShader;
			currentShader.getShader().begin();
		}
		Matrix4 m=camera.combined;
		m.translate(pos.x,pos.y,pos.z);
		m.scale(scale.x, scale.y,scale.z);
		m.rotate(0, 1, 0, entity.rotation.y);
		m.rotate(0, 0, 1, entity.rotation.z);
		
		currentShader.getShader().setUniformMatrix(uProj,m);
		currentShader.getShader().setUniformf(vColor,color.r,color.g,color.b,color.a);
		setShaderParams(currentShader.getShader());
		
		actualRender();
		
		m.rotate(0, 0, 1, -entity.rotation.z);
		m.rotate(0, 1, 0,-entity.rotation.y);
		if(scale.z==0)m.scale(1/scale.x, 1/scale.y,0.001f);
		else m.scale(1/scale.x, 1/scale.y,1/scale.z);
		m.translate(-pos.x,-pos.y,-pos.z);
		
	}
	public void setShaderParams(ShaderProgram shader){
		
	}
	public void actualRender(){
		if(texture!=null)texture.bind();
		mesh.render(currentShader.getShader(),primitive);
	}

	public static Shader currentShader;
	public void createSprite(String textu){
		texture = Director.getAsset(textu, Texture.class);
	}
	
	
	public static void begin(Camera camera){
		currentShader=null;
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		 Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		 Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		 Gdx.gl.glDepthFunc(GL20.GL_LESS);
	}
	public static void end(){
		if(currentShader!=null)currentShader.getShader().end();
	}
	public static Comparator<MeshMechanism> compareMethod=new  Comparator<MeshMechanism>() {
		@Override
		public int compare(MeshMechanism arg0, MeshMechanism arg1) {
			if(arg0.sortingValue()>arg1.sortingValue()){
				return -1;
			}
			else if(arg0.sortingValue()<arg1.sortingValue()){
				return 1;
			}
			else return 0;
		}
	};
}
