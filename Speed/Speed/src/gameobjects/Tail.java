package gameobjects;

import java.util.Collections;

import tools.Action;
import tools.Actions;
import tools.Shader;
import tools.Shapes.Shape;
import tools.general.Pool;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class Tail extends Entity {
	public static int TAG = gWorld.getNextTag();
	public static int GROUP = gWorld.getNextGroup();
	public static byte ACTION_FADE_OUT = gWorld.getNextAction();

	public static class TailItem implements Comparable {
		public static int LAST_ADD_TIME = 0;
		public Vector pos = new Vector();
		public gColor color = new gColor(1, 1, 1, 1);
		public int addTime;

		public void set(float x, float y, float z, float a) {
			color.set(1, 1, 1, a);
			pos.set(x, y, z);
		}

		@Override
		public int compareTo(Object arg0) {
			if (((TailItem) arg0).addTime > addTime)
				return 1;
			else
				return -1;
		}
	}

	private int side;

	public Tail(gWorld world, int side) {
		super(world);
		this.side = side;
	}

	@Override
	public void create() {
		super.create();
		tag = TAG;

		initScale(1, 1, 1);

		final Pool<TailItem> tailItems = new Pool<TailItem>(50, TailItem.class);

		setGroups(GROUP);
		addMechanism(new ActionMechanism(this));

		addMechanism(new CollitionMechanism(this));

		addMechanism(new IntervalMechanism(this, 0) {
			@Override
			public void die() {
				super.die();
				tailItems.clear();
			}

			public void tick() {
				TailItem left = tailItems.getFree();
				if (left != null) {
					float z = parrent.pos.z - 0.02f;
					if (side == 1)
						z -= parrent.vel.x / 10;
					else
						z += parrent.vel.x / 10;

					if (side == 1) {
						z += parrent.vel.x / 20;
					} else
						z -= parrent.vel.x / 20;

					float alpha = Math.max(0.5f, 0.5f + (side == -1 ? parrent.vel.x : -parrent.vel.x)) * parrent.vel.y;

					left.set(parrent.pos.x + side * 0.1f - 0.05f + parrent.vel.x / 15, parrent.pos.y - 0.4f, z, alpha);
					left.addTime = TailItem.LAST_ADD_TIME++;
				}
				TailItem right = tailItems.getFree();
				if (right != null) {
					float z = parrent.pos.z - 0.02f;
					if (side == 1)
						z -= parrent.vel.x / 10;
					else
						z += parrent.vel.x / 10;

					if (side == 1) {
						z += parrent.vel.x / 20;
					} else
						z -= parrent.vel.x / 20;

					float alpha = Math.max(0.5f, 0.5f + (side == -1 ? parrent.vel.x : -parrent.vel.x)) * parrent.vel.y;

					right.set(parrent.pos.x + side * 0.1f + 0.05f + parrent.vel.x / 15, parrent.pos.y - 0.4f, z, alpha);
					right.addTime = TailItem.LAST_ADD_TIME++;
				}

				Collections.sort(tailItems.getAlives());
			}
		});
		addMechanism(new IntervalMechanism(this, 0) {
			@Override
			public void init() {
				super.init();
				color.a = 1;
			}

			public void tick() {
				for (int c = 0; c < tailItems.size(); c++) {
					if (tailItems.get(c).color.a > 0) {
						tailItems.get(c).color.a -= Gdx.graphics.getDeltaTime() * 10;
						tailItems.get(c).color.checkRange();
					} else
						tailItems.release(c);
				}
			}
		});

		addMechanism(new MeshMechanism(this, new Shape(new float[1000]), Shader.getShader("shaders/tail.vert", "shaders/tail.frag"), GL20.GL_TRIANGLE_STRIP, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(
				Usage.Color, 4, "a_color")) {
			public float sortingValue() {
				return entity.pos.y;
			}

			public void actualRender() {
				float[] verts = shape.vertices;
				int index = 0;
				for (int c = 0; c < tailItems.size(); c++) {
					verts[index++] = tailItems.get(c).pos.x;
					verts[index++] = tailItems.get(c).pos.y;
					verts[index++] = tailItems.get(c).pos.z;
					verts[index++] = color.r;
					verts[index++] = color.g;
					verts[index++] = color.b;
					verts[index++] = tailItems.get(c).color.a;
				}
				mesh.setVertices(verts);
				mesh.render(currentShader.getShader(), primitive, 0, index / 7);
			}
		});
	}
}
