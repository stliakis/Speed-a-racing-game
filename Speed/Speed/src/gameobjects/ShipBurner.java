package gameobjects;

import tools.Action;
import tools.Actions;
import tools.Director;
import tools.Director.ActionListener;
import tools.Shapes.Shape;
import tools.Shader;
import tools.general.Tools;
import tools.general.Vector;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.CollisionEvent;
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;
import tools.world.mechanisms.MovementMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;

public class ShipBurner extends Entity {
	public static int TAG = gWorld.getNextTag();
	private int side;

	public ShipBurner(gWorld world, int side) {
		super(world);
		this.side = side;
	}

	static {
		Shape.registerShape("texturedQuad", new float[] { -0.5f, -0.5f, 0, 0, 0, 0.5f, -0.5f, 0, 1, 0, 0.5f, 0.5f, 0, 1, 1, -0.5f, 0.5f, 0, 0, 1 }, null);
	}

	@Override
	public void create() {
		super.create();
		tag = TAG;

		initScale(0.15f, 0.15f, 0.15f);
		initColor(1, 1, 1, 1);

		addMechanism(new ActionMechanism(this));

		addMechanism(new IntervalMechanism(this, 0) {
			public void tick() {
				rotation.x = 90;
				rotation.z += 33;
			}
		});

		addMechanism(new MeshMechanism(this, Shape.getShape("texturedQuad"), Shader.getShader("shaders/burner.vert", "shaders/burner.frag"), "sprites/burner.png", GL20.GL_TRIANGLE_FAN, new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0")) {
			@Override
			public void renderMesh(Camera camera) {
				if (myShader != currentShader) {
					if (currentShader != null)
						currentShader.getShader().end();
					currentShader = myShader;
					currentShader.getShader().begin();
				}
				Matrix4 m = camera.combined;
				m.translate(entity.pos.x, entity.pos.y, entity.pos.z);
				m.rotate(1, 0, 0, rotation.x);
				m.rotate(0, 0, 1, rotation.z);
				m.scale(scale.x, scale.y, scale.z);

				currentShader.getShader().setUniformMatrix(uProj, m);
				currentShader.getShader().setUniformf(vColor, color.r, color.g, color.b, color.a);

				texture.bind();
				mesh.render(currentShader.getShader(), primitive);
				m.translate(0, 0, -1);
				mesh.render(currentShader.getShader(), primitive);
				m.translate(0, 0, 1);

				m.scale(1 / scale.x, 1 / scale.y, 1 / scale.z);
				m.rotate(0, 0, 1, -rotation.z);
				m.rotate(1, 0, 0, -rotation.x);
				m.translate(-entity.pos.x, -entity.pos.y, -entity.pos.z);
			}

			@Override
			public float sortingValue() {
				return pos.y;
			}
		});
	}

}
