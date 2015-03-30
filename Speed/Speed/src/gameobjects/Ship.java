package gameobjects;

import tools.Actions;
import tools.Shader;
import tools.general.Tools;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.CollisionEvent;
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;
import tools.world.mechanisms.MovementMechanism;
import tools.world.mechanisms.Shapes.Shape;
import tools.world.mechanisms.SoundMechanism;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class Ship extends Entity {
	public static int TAG=gWorld.getNextTag();
	public Ship(gWorld world) {
		super(world);
	}
	@Override
	public void create() {
		super.create();
		tag=TAG;
		
		initScale(0.35f,0.35f,0.35f);
		initColScale(0.5f,0.5f);
		initColor(1, 1, 1, 1);
		initSpeed(0.1f);
		
		final Entity leftTail=create(new Tail(world,-1));
		final Entity rightTail=create(new Tail(world,1));
		final Entity leftBurner=create(new ShipBurner(world,-1));
		final Entity rightBurner=create(new ShipBurner(world,1));
		
		addMechanism(new ActionMechanism(this));
		
		final SoundMechanism sm=(SoundMechanism) 
		addMechanism(new SoundMechanism(this, "sounds/engine.wav","sounds/sparks.wav","sounds/gameover.mp3"){
			@Override
			public void init() {
				super.init();
				playLooping(0);
			}
		});
		
		addMechanism(new IntervalMechanism(this,0) {
			@Override
			public void init() {
				super.init();
				vel.set(0, 0);
				
				Scene.SCORE=0;
				
				world.sendAction(leftBurner.id, Actions.ACTION_CREATE);
				world.sendAction(rightBurner.id, Actions.ACTION_CREATE);
				
				world.sendAction(leftTail.id, Actions.ACTION_CREATE);
				world.sendAction(rightTail.id, Actions.ACTION_CREATE);
			}
			@Override
			public void die() {
				// TODO Auto-generated method stub
				super.die();
				sm.muteAllSounds();
				world.sendAction(leftBurner.id,Actions.ACTION_CREATE_VEL);
				world.sendAction(rightBurner.id,Actions.ACTION_CREATE_VEL);
				world.sendAction(leftTail.id,Actions.ACTION_CREATE_VEL);
				world.sendAction(rightTail.id,Actions.ACTION_CREATE_VEL);
			}
			public void tick() {
				sm.setVolume(0, vel.y/2);
				
				leftBurner.pos.set(pos).z-=0.025f;
				leftBurner.pos.y-=0.18f;
				leftBurner.pos.x+=-0.075f+vel.x/10;
				leftBurner.pos.z+=vel.x/20;
				leftBurner.color.a=Math.max(0.2f, 0.5f+vel.x)*vel.y;
				leftBurner.color.checkRange();
				leftBurner.scale.x=Math.max(0.1f, (0.15f*leftBurner.color.a));
				leftBurner.scale.y=Math.max(0.1f, (0.15f*leftBurner.color.a));
				
				rightBurner.pos.set(pos).z-=0.025f;
				rightBurner.pos.y-=0.18f;
				rightBurner.pos.x+=+0.075f+vel.x/10;
				rightBurner.pos.z-=vel.x/20;
				rightBurner.color.a=Math.max(0.2f, 0.5f-vel.x)*vel.y;
				rightBurner.color.checkRange();
				rightBurner.scale.x=Math.max(0.1f, (0.15f*rightBurner.color.a));
				rightBurner.scale.y=Math.max(0.1f, (0.15f*rightBurner.color.a));
			
				vel.x+=(0-vel.x)/20;
				
				Scene.SCORE+=vel.y;
				
				rotation.y=vel.x*20;
				
				vel.z+=(-1-vel.z)/20;
				if((pos.z -scale.z/2)+vel.z*speed<=0){
					vel.z=0;
				}
			}
		});
		
		addMechanism(new CollitionMechanism(this,Block.GROUP){
			@Override
			public void onCollide(CollisionEvent event) {
				super.onCollide(event);
				if(event.collideX)vel.x=0;
				else vel.y=0;
				if(event.isCollideY()){
					world.sendAction(world.getRoot().id, Scene.GAMEOVER);
					world.sendAction(id, Actions.ACTION_DIE);
					world.sendAction(event.getEntity().id, Actions.ACTION_DIE);
					Entity explosion=world.sendAction(world.getRoot().add(Explosion.TAG), Actions.ACTION_CREATE);
					explosion.color.set(1, 0,0,1);
					explosion.pos.set(pos);
					for(int c=0;c<200;c++){
						Entity particle=world.sendAction(world.getRoot().add(Particle.TAG), Actions.ACTION_CREATE);
						if(particle!=null){
							particle.pos.set(pos);
							particle.vel.set(Tools.randf(-4, 4),Tools.randf(-4,4),Tools.randf(-4, 4));
						}
					}
					sm.play(2);
				}
				if(event.isCollideX()){
					sm.play(1,0.1f);
					for(int c=0;c<2;c++){
						Entity particle=world.sendAction(world.getRoot().add(Particle.TAG), Actions.ACTION_CREATE);
						if(particle!=null){
							particle.pos.set(pos);
							particle.pos.x+=event.getEntity().pos.x>pos.x?scale.x/1.25f:-scale.x/1.25f;
							particle.pos.z-=0.125f;
							particle.vel.set(Tools.randf(-0.25f, .25f),Tools.randf(-0.5f,-1),Tools.randf(-.25f, .25f));
						}
					}
				}
			}
		});
		

		
		addMechanism(new MovementMechanism(this));
		
		addMechanism(new MeshMechanism(this,
					Shape.getShape("ship"),
					Shader.getShader("shaders/player.vert", "shaders/player.frag"),
					GL20.GL_TRIANGLES,
					 new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.Color, 3, "a_color")
				){
			@Override
			public float sortingValue() {
				return entity.pos.y;
			}
		});
	}
	
	static{
		Shape.registerShape("ship", new float[]{
				-0.75f,-0.75f,-0.2f,	 0.3f,0.3f,0.3f,
				0,-0.75f,0.2f, 0.3f,0.3f,0.3f,
				0.75f,-0.75f,-0.2f, 0.3f,0.3f,0.3f,
				
				-0.75f,-0.75f,-0.2f, 1,1,0.5f,
				0,0.75f,-0.2f, 1,1,0.5f,
				0,-0.75f,0.2f, 1,1,0.5f,
				
				0.75f,-0.75f,-0.2f, 1,1,0.5f,
				0,0.75f,-0.2f, 1,1,0.5f,
				0,-0.75f,0.2f ,1,1,0.5f,
				},null
		);

	}
}
