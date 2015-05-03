package gameobjects;

import tools.Director;
import tools.Shader;
import tools.Shapes.Shape;
import tools.general.Pool;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class ParticleSystem extends Entity {

	public static class Particle {
		public Vector pos = new Vector(), vel = new Vector();
		public gColor color = new gColor(1, 1, 1, 1);
		public boolean subDrop = false;

		public void set(float x, float y, float z, float vx, float vy, float vz) {
			pos.set(x, y, z);
			vel.set(vx, vy, vz);
			if (Tools.randf(0, 100) < 85)
				color.set(1, 0, 0);
			else
				color.set(1, 1, 0);
			subDrop = false;
		}
	}

	public static int TAG = gWorld.getNextTag();
	Pool<Particle> drops;

	public ParticleSystem(gWorld world) {
		super(world);
	}

	@Override
	public void create() {
		super.create();
		tag = TAG;

		initSpeed(5);
		drops = new Pool<Particle>(10000, Particle.class);
		final float[] dropsVerts = new float[drops.capacity() * 2 * 7];
		addMechanism(new ActionMechanism(this));

		addMechanism(new IntervalMechanism(this, 0) {
			public void init() {
				super.init();
				drops.clear();
			}

			public void tick() {
				for (int c = 0, indx = 0; c < drops.size(); c++) {
					Particle drop = drops.get(c);
					drop.color.a = (1 - Math.abs(-world.cameraPos.y - drop.pos.y) / 50) / 2;

					drop.pos.x += drop.vel.x * speed * Gdx.graphics.getDeltaTime();
					drop.pos.y += drop.vel.y * speed * Gdx.graphics.getDeltaTime();
					drop.pos.z += drop.vel.z * speed * Gdx.graphics.getDeltaTime();

					drop.vel.z -= Gdx.graphics.getDeltaTime();

					dropsVerts[indx++] = drop.pos.x;
					dropsVerts[indx++] = drop.pos.y;
					dropsVerts[indx++] = drop.pos.z;
					dropsVerts[indx++] = drop.color.r;
					dropsVerts[indx++] = drop.color.g;
					dropsVerts[indx++] = drop.color.b;
					dropsVerts[indx++] = drop.color.a;

					float size = 0.2f;
					if (drop.subDrop)
						size = 0.15f;
					else
						size = 0.2f;

					dropsVerts[indx++] = drop.pos.x + drop.vel.x * size;
					dropsVerts[indx++] = drop.pos.y + drop.vel.y * size;
					dropsVerts[indx++] = drop.pos.z + drop.vel.z * size;
					dropsVerts[indx++] = drop.color.r;
					dropsVerts[indx++] = drop.color.g;
					dropsVerts[indx++] = drop.color.b;
					dropsVerts[indx++] = drop.color.a;

					if (drop.pos.z < 0) {
						drops.release(c);
						if (drop.subDrop == false) {
							for (int i = 0; i < 3; i++) {
								Particle subDrop = add(drop.pos.x, drop.pos.y, drop.pos.z + 0.05f, Tools.randf(-0.2f, 0.2f), Tools.randf(-0.2f, 0.2f), 0.4f);
								if (subDrop != null)
									subDrop.subDrop = true;
							}
						}
					}

				}
			}
		});

		addMechanism(new MeshMechanism(this, new Shape(dropsVerts), Shader.getShader("shaders/rainDrop.vert", "shaders/rainDrop.frag"), GL20.GL_LINES, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.Color,
				4, "a_color")) {
			public void renderMesh(Camera camera) {
				if (myShader != currentShader) {
					if (currentShader != null)
						currentShader.getShader().end();
					currentShader = myShader;
					currentShader.getShader().begin();
				}
				Gdx.gl.glLineWidth(3);
				Matrix4 m = camera.combined;
				currentShader.getShader().setUniformMatrix(uProj, m);
				currentShader.getShader().setUniformf(vColor, 1, 1, 1, 1);
				mesh.setVertices(shape.vertices);
				mesh.render(currentShader.getShader(), primitive, 0, drops.size() * 2);
			}

			public float sortingValue() {
				return 0;
			}

			public void setShaderParams(ShaderProgram shader) {
				shader.setUniformf("fog_color", Director.CLEAR_COLOR.r, Director.CLEAR_COLOR.g, Director.CLEAR_COLOR.b, Director.CLEAR_COLOR.a);
			}
		});
	}

	public Particle add(float x, float y, float z, float vx, float vy, float vz) {
		Particle drop = drops.getFree();
		if (drop == null)
			return null;
		drop.set(x, y, z, vx, vy, vz);
		return drop;
	}

}
