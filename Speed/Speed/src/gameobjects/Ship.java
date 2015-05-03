package gameobjects;

import tools.Actions;
import tools.Director;
import tools.Shader;
import tools.Shapes.Shape;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.WorldRenderer;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.CollisionEvent;
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;
import tools.world.mechanisms.MovementMechanism;
import tools.world.mechanisms.SoundMechanism;
import tools.world.mechanisms.SpriteMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Ship extends Entity {
	public static int TAG = gWorld.getNextTag();
	public static int GROUP = gWorld.getNextGroup();
	public float distance = 0;
	public float time = 0;
	public float nitroPercent=0;
	public boolean nitroEnabled=false;
	public Ship(final gWorld world, String shapeName,final boolean burners) {
		super(world);
		tag = TAG;

		initScale(0.5f, 0.5f, 0.5f);
		initColScale(0.8f, 0.5f);
		initColor(1, 1, 1, 1);
		initSpeed(0.1f);

		final Entity leftTail = create(new Tail(world, -1));
		final Entity rightTail = create(new Tail(world, 1));
		final Entity leftBurner = create(new ShipBurner(world, -1));
		final Entity rightBurner = create(new ShipBurner(world, 1));
		

		addMechanism(new ActionMechanism(this));
		setGroups(Scene.GROUP_CONCRETE, GROUP);
		final SoundMechanism sm = (SoundMechanism) addMechanism(new SoundMechanism(this, "sounds/engine.wav", "sounds/sparks.wav", "sounds/gameover.mp3") {
			@Override
			public void init() {
				super.init();
				// playLooping(0);

			}
		});
		addMechanism(new IntervalMechanism(this, 0) {
			boolean rising = false;

			@Override
			public void tick() {
				
				if(nitroPercent<1)nitroPercent+=Gdx.graphics.getDeltaTime()*0.02f*vel.y;
				
				if (rising) {
					if (pos.z < 1.5f)
						pos.z += Gdx.graphics.getDeltaTime() / 5;
					else
						rising = false;
				} else {
					if (pos.z > 0.5f)
						pos.z -= Gdx.graphics.getDeltaTime() / 5;
					else
						rising = true;
				}
			}
		});
		addMechanism(new IntervalMechanism(this, 0) {
			@Override
			public void init() {
				super.init();
				vel.set(0, 0);
				distance = 0;
				time = 0;
				Scene.SCORE = 0;

				if(burners){
					world.sendAction(leftBurner.id, Actions.ACTION_CREATE);
					world.sendAction(rightBurner.id, Actions.ACTION_CREATE);
	
					world.sendAction(leftTail.id, Actions.ACTION_CREATE);
					world.sendAction(rightTail.id, Actions.ACTION_CREATE);
				}

				
				entity.pos.set(0, -entity.world.cameraPos.y - 3, 0.25f);
			}

			@Override
			public void die() {
				// TODO Auto-generated method stub
				super.die();
				sm.muteAllSounds();
				world.sendAction(leftBurner.id, Actions.ACTION_CREATE_VEL);
				world.sendAction(rightBurner.id, Actions.ACTION_CREATE_VEL);
				world.sendAction(leftTail.id, Actions.ACTION_CREATE_VEL);
				world.sendAction(rightTail.id, Actions.ACTION_CREATE_VEL);

				world.sendAction(leftBurner.id, Actions.ACTION_DIE);
				world.sendAction(rightBurner.id, Actions.ACTION_DIE);

				world.sendAction(leftTail.id, Actions.ACTION_DIE);
				world.sendAction(rightTail.id, Actions.ACTION_DIE);
			}

			public void tick() {
				distance += vel.y;
				if (distance < Scene.RACE_LENGTH)
					time += Gdx.graphics.getDeltaTime();

				sm.setVolume(0, vel.y / 2);

				leftBurner.pos.set(pos).z -= 0.025f;
				leftBurner.pos.y -= 0.4f;
				leftBurner.pos.x += -0.09f + vel.x / 10;
				leftBurner.pos.z += vel.x / 20;
				leftBurner.color.a = Math.max(0.2f, 0.5f + vel.x) * vel.y;
				leftBurner.color.checkRange();
				leftBurner.scale.x = Math.max(0.15f, (0.15f * leftBurner.color.a));
				leftBurner.scale.y = Math.max(0.15f, (0.15f * leftBurner.color.a));

				rightBurner.pos.set(pos).z -= 0.025f;
				rightBurner.pos.y -= 0.4f;
				rightBurner.pos.x += +0.09f + vel.x / 10;
				rightBurner.pos.z -= vel.x / 20;
				rightBurner.color.a = Math.max(0.2f, 0.5f - vel.x) * vel.y;
				rightBurner.color.checkRange();
				rightBurner.scale.x = Math.max(0.15f, (0.15f * rightBurner.color.a));
				rightBurner.scale.y = Math.max(0.15f, (0.15f * rightBurner.color.a));
				
				
				if(nitroEnabled){
					rightTail.color.r+=(0.6f-rightTail.color.r)*Gdx.graphics.getDeltaTime()*10;
					rightTail.color.g+=(0.6f-rightTail.color.g)*Gdx.graphics.getDeltaTime()*10;
					rightTail.color.b+=(1-rightTail.color.b)*Gdx.graphics.getDeltaTime()*10;
				}else{
					rightTail.color.r+=(1-rightTail.color.r)*Gdx.graphics.getDeltaTime()*10;
					rightTail.color.g+=(1-rightTail.color.g)*Gdx.graphics.getDeltaTime()*10;
					rightTail.color.b+=(1-rightTail.color.b)*Gdx.graphics.getDeltaTime()*10;
				}
	
				leftBurner.color.set(rightTail.color);
				rightBurner.color.set(rightTail.color);
				leftTail.color.set(rightTail.color);
				
				vel.x += (0 - vel.x) / 20;

				Scene.SCORE += vel.y;

				rotation.y = vel.x * 20;

				vel.z += (-1 - vel.z) / 20;
				if ((pos.z - scale.z / 2) + vel.z * speed <= 0) {
					vel.z = 0;
				}
			}
		});

		addMechanism(new CollitionMechanism(this, Scene.GROUP_CONCRETE) {
			@Override
			public void onCollide(CollisionEvent event) {
				super.onCollide(event);

				if (event.collideX)
					vel.x = 0;
				else if (event.entity.tag != Ship.TAG)
					vel.y = 0;
				else if (event.entity.tag == Ship.TAG) {
					if (vel.y > 0.5f)
						vel.y -= Gdx.graphics.getDeltaTime() * 40;
				}

				if (event.isCollideY() && event.entity.tag != Ship.TAG) {
					world.sendAction(id, Actions.ACTION_DIE);
					Entity explosion = world.sendAction(world.getRoot().add(Explosion.TAG), Actions.ACTION_CREATE);
					if(explosion==null)return;
					explosion.color.set(1, 0, 0, 1);
					explosion.pos.set(pos);
					ParticleSystem rs = (ParticleSystem) world.getEntityByTag(ParticleSystem.TAG);
					for (int c = 0; c < 400; c++) {
						rs.add(pos.x, pos.y, pos.z, Tools.randf(-2, 2), Tools.randf(-2, 2), Tools.randf(-2, 2));
					}
					sm.play(2);
				}
				if (event.isCollideX()) {
					ParticleSystem rs = (ParticleSystem) world.getEntityByTag(ParticleSystem.TAG);
					if(rs==null)return;
					for (int c = 0; c < 5; c++) {
						ParticleSystem.Particle particle = rs.add(pos.x, pos.y - 0.2f, pos.z - 0.1f, Tools.randf(-0.25f, .25f), Tools.randf(-0.5f, -1), Tools.randf(-.25f, .25f));
						if (event.getEntity().tag == Map.TAG) {
							particle.pos.x += event.getEntity().pos.x > pos.x ? -scale.x / 1.5f : scale.x / 1.5f;
						} else {
							particle.pos.x += event.getEntity().pos.x > pos.x ? 0.35f : -0.35f;
						}
					}
				}
			}
		});

		addMechanism(new MovementMechanism(this));

		addMechanism(new MeshMechanism(this, Shape.getShape(shapeName), Shader.getShader("shaders/player.vert", "shaders/player.frag"), GL20.GL_TRIANGLES, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(
				Usage.Color, 3, "a_color")) {
			@Override
			public float sortingValue() {
				return entity.pos.y;
			}
		});

		addMechanism(new SpriteMechanism(this, "sprites/shipShadow.png", 0, 0, 128, new Vector(), new Vector(scale).mul(2), rotation, new gColor(0.5f, 0.5f, 0.5f, 1), "shaders/shipShadow.vert", "shaders/shipShadow.frag", true) {
			@Override
			public void setShaderParams(ShaderProgram shader) {
				shader.setUniformf("fog_color", Director.CLEAR_COLOR.r, Director.CLEAR_COLOR.g, Director.CLEAR_COLOR.b, Director.CLEAR_COLOR.a);
			}

			@Override
			public void render(WorldRenderer renderer) {
				System.out.println("rendeting'");
				this.pos.set(entity.pos);
				this.pos.z = 0;
				super.render(renderer);
			}
		});
	}

	static {
		Shape.registerShape("ship", new float[] { -0.75f, -0.75f, -0.2f, 0.3f, 0.3f, 0.3f, 0, -0.75f, 0.2f, 0.3f, 0.3f, 0.3f, 0.75f, -0.75f, -0.2f, 0.3f, 0.3f, 0.3f,

		-0.75f, -0.75f, -0.2f, 1, 1, 0.5f, 0, 0.75f, -0.2f, 1, 1, 0.5f, 0, -0.75f, 0.2f, 1, 1, 0.5f,

		0.75f, -0.75f, -0.2f, 1, 1, 0.5f, 0, 0.75f, -0.2f, 1, 1, 0.5f, 0, -0.75f, 0.2f, 1, 1, 0.5f, }, null);

		Shape.registerShape("shipBlue", new float[] { -0.75f, -0.75f, -0.2f, 0.3f, 0.3f, 0.3f, 0, -0.75f, 0.2f, 0.3f, 0.3f, 0.3f, 0.75f, -0.75f, -0.2f, 0.3f, 0.3f, 0.3f, -0.75f, -0.75f, -0.2f, 0.5f, 0.5f, 1, 0, 0.75f, -0.2f, 0.5f, 0.5f, 1,
				0, -0.75f, 0.2f, 0.5f, 0.5f, 1, 0.75f, -0.75f, -0.2f, 0.5f, 0.5f, 1, 0, 0.75f, -0.2f, 0.5f, 0.5f, 1, 0, -0.75f, 0.2f, 0.5f, 0.5f, 1, }, null);

		Shape.registerShape("shipRed", new float[] { -0.75f, -0.75f, -0.2f, 0.3f, 0.3f, 0.3f, 0, -0.75f, 0.2f, 0.3f, 0.3f, 0.3f, 0.75f, -0.75f, -0.2f, 0.3f, 0.3f, 0.3f, -0.75f, -0.75f, -0.2f, 1, 0.5f, 0.5f, 0, 0.75f, -0.2f, 1, 0.5f, 0.5f,
				0, -0.75f, 0.2f, 1, 0.5f, 0.5f, 0.75f, -0.75f, -0.2f, 1, 0.5f, 0.5f, 0, 0.75f, -0.2f, 1, 0.5f, 0.5f, 0, -0.75f, 0.2f, 1, 0.5f, 0.5f, }, null);

		Shape.registerShape("shipGreen", new float[] { -0.75f, -0.75f, -0.2f, 0.3f, 0.3f, 0.3f, 0, -0.75f, 0.2f, 0.3f, 0.3f, 0.3f, 0.75f, -0.75f, -0.2f, 0.3f, 0.3f, 0.3f, -0.75f, -0.75f, -0.2f, 0.5f, 1, 0.5f, 0, 0.75f, -0.2f, 0.5f, 1,
				0.5f, 0, -0.75f, 0.2f, 0.5f, 1, 0.5f, 0.75f, -0.75f, -0.2f, 0.5f, 1, 0.5f, 0, 0.75f, -0.2f, 0.5f, 1, 0.5f, 0, -0.75f, 0.2f, 0.5f, 1, 0.5f }, null);
	}

}
