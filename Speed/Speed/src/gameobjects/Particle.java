package gameobjects;

import gameobjects.mechanisms.BeatMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;

import tools.Action;
import tools.Actions;
import tools.Shader;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.CollisionEvent;
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;
import tools.world.mechanisms.MovementMechanism;
import tools.world.mechanisms.Shapes;
import tools.world.mechanisms.Shapes.Shape;

public class Particle extends Entity {
	public static int TAG=gWorld.getNextTag();
	public static int GROUP=gWorld.getNextGroup();
	
	static{
		Shape.registerShape("particleGlow", new float[]{
				-0.5f,0,-0.5f,0,0,
				0.5f,0,-0.5f,1,0,
				0.5f,0,0.5f,1,1,
				-0.5f,0,0.5f,0,1
			},null
		);

	}
	public boolean glow;
	public Particle(gWorld world) {
		super(world);
	}
	@Override
	public void create() {
		super.create();
		tag=TAG;
		
		initScale(0.5f,0.5f,0.5f);
		initSpeed(0.02f);;
		
		setGroups(GROUP);
		addMechanism(new ActionMechanism(this));
		
		addMechanism(new IntervalMechanism(this,0) {
			@Override
			public void init() {
				super.init();
				color.set(1,1,0, 1);
				glow=true;
			}
			public void tick() {
				if(pos.y-1<-world.cameraPos.y){
					if(color.a>0)color.a-=Gdx.graphics.getDeltaTime()*20;
					else world.sendAction(id, Actions.ACTION_DIE);
					color.checkRange();
				}
				if(timeon>200){
					if(color.a>0)color.a-=Gdx.graphics.getDeltaTime();
					else world.sendAction(id, Actions.ACTION_DIE);
				}
				vel.z-=Gdx.graphics.getDeltaTime();;
			}
		});
		
		addMechanism(new MovementMechanism(this));
		
		addMechanism(new MeshMechanism(this,
				new Shape(new float[6]),
				Shader.getShader("shaders/particle.vert", "shaders/particle.frag"),
				GL20.GL_LINES,
				 new VertexAttribute(Usage.Position, 3, "a_position"))
				{
					public void renderMesh(Camera camera) {
						float[] verts=shape.vertices;
						verts[0]=pos.x;
						verts[1]=pos.y;
						verts[2]=pos.z;
						verts[3]=pos.x+vel.x/10;
						verts[4]=pos.y+vel.y/10;
						verts[5]=pos.z+vel.z/10;
						mesh.setVertices(verts);
						Gdx.gl.glLineWidth(2);
						
						if(myShader!=currentShader){
							if(currentShader!=null)currentShader.getShader().end();
							currentShader=myShader;
							currentShader.getShader().begin();
						}
						Matrix4 m=camera.combined;
						currentShader.getShader().setUniformMatrix(uProj,m);
						currentShader.getShader().setUniformf(vColor,color.r,color.g,color.b,color.a);
						
						mesh.render(currentShader.getShader(),primitive);
					}
					public float sortingValue() {
						return pos.y;
					}
				}
		);
		
		
		addMechanism(new MeshMechanism(this,
				Shape.getShape("particleGlow"),
				Shader.getShader("shaders/burner.vert", "shaders/burner.frag"),
				"sprites/glow.png",new gColor(color),scale,new Vector(),
				GL20.GL_TRIANGLE_FAN,
				 new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0")
			){
			@Override
			public void renderMesh(Camera camera) {
				if(!glow )return;
				color.set(1,0,0);
				color.a=entity.color.a/5;;
				pos.set((entity.pos.x+(entity.pos.x+vel.x/10))/2, (entity.pos.y+(entity.pos.y+vel.y/10))/2,(entity.pos.z+(entity.pos.z+vel.z/10))/2);
				super.renderMesh(camera);
			}
				@Override
				public float sortingValue() {
					return pos.y;
				}
		});
	}
	

}
