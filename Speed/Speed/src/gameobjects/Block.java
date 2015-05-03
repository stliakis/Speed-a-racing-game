package gameobjects;

import tools.Action;
import tools.Actions;
import tools.Director;
import tools.Shader;
import tools.Shapes.Shape;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Block extends Entity {
	public static int TAG = gWorld.getNextTag();
	public static int GROUP = gWorld.getNextGroup();
	static {
		gColor frontAndBack = new gColor(0.5f, 1, 0.5f, 1);
		gColor leftAndRight = new gColor(0.25f, 1, 0.25f, 1);
		Shape.registerShape("block", new float[] { -0.5f, 0.5f, 0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b, 0.5f, 0.5f, 0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b, 0.5f, 0.5f, -0.5f, frontAndBack.r, frontAndBack.g,
				frontAndBack.b, -0.5f, 0.5f, 0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b, -0.5f, 0.5f, -0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b, 0.5f, 0.5f, -0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b,

				-0.5f, -0.5f, 0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b, 0.5f, -0.5f, 0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b, 0.5f, -0.5f, -0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b, -0.5f, -0.5f, 0.5f,
				frontAndBack.r, frontAndBack.g, frontAndBack.b, -0.5f, -0.5f, -0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b, 0.5f, -0.5f, -0.5f, frontAndBack.r, frontAndBack.g, frontAndBack.b,

				-0.5f, 0.5f, 0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, -0.5f, -0.5f, 0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, -0.5f, -0.5f, -0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, -0.5f, -0.5f,
				-0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, -0.5f, 0.5f, -0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, -0.5f, 0.5f, 0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b,

				0.5f, 0.5f, 0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, 0.5f, -0.5f, 0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, 0.5f, -0.5f, -0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, 0.5f, -0.5f, -0.5f,
				leftAndRight.r, leftAndRight.g, leftAndRight.b, 0.5f, 0.5f, -0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, 0.5f, 0.5f, 0.5f, leftAndRight.r, leftAndRight.g, leftAndRight.b, }, null);
	}

	public Block(gWorld world) {
		super(world);
	}

	boolean fading = false;

	@Override
	public void create() {
		super.create();
		tag = TAG;

		initScale(Tools.randf(1, 2), Tools.randf(1, 3), Tools.randf(10, 10));
		initColScale(scale.x, scale.y);
		initColor(0.5f, 0.5f, 1, 1);

		setGroups(GROUP, Scene.GROUP_CONCRETE, Scene.DESTRUCTABLE);
		addMechanism(new ActionMechanism(this) {
			@Override
			public void onReceive(Action action) {
				if (action.action == Scene.ACTION_FADE_OUT) {
					fading = true;
				} else
					super.onReceive(action);
			}
		});

		addMechanism(new CollitionMechanism(this));

		addMechanism(new IntervalMechanism(this, 0) {
			@Override
			public void init() {
				super.init();
				color.a = 1;
				fading = false;
			}

			public void tick() {
				if (pos.y < -world.cameraPos.y - 20 || fading) {
					if (color.a > 0)
						color.a -= Gdx.graphics.getDeltaTime() * 2;
					else
						world.sendAction(id, Actions.ACTION_DIE);
					color.checkRange();
				}
				pos.z = scale.z / 2;
			}
		});

		addMechanism(new MeshMechanism(this, Shape.getShape("block"), Shader.getShader("shaders/block.vert", "shaders/block.frag"), GL20.GL_TRIANGLES, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.Color,
				3, "a_color")) {
			@Override
			public float sortingValue() {
				return pos.y;
			}
		});

		addMechanism(new MeshMechanism(this, Shape.getShape("block"), Shader.getShader("shaders/block.vert", "shaders/block.frag"), new gColor(color).setA(0.2f), new Vector(scale), GL20.GL_TRIANGLES, new VertexAttribute(Usage.Position, 3,
				"a_position"), new VertexAttribute(Usage.Color, 3, "a_color")) {
			@Override
			public void renderMesh(Camera camera) {
				scale.set(entity.scale).mul(1.2f);
				color.a = entity.color.a / 5;
				super.renderMesh(camera);
			}
			@Override
			public void setShaderParams(ShaderProgram shader) {
				shader.setUniformf("fog_color", Director.CLEAR_COLOR.r, Director.CLEAR_COLOR.g, Director.CLEAR_COLOR.b, Director.CLEAR_COLOR.a);
			}

			@Override
			public float sortingValue() {
				return pos.y;
			}
		});
	}
}
