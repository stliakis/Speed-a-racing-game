package gameobjects;

import java.util.ArrayList;
import java.util.List;

import tools.Director;
import tools.Shader;
import tools.Shapes.Shape;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.CollitionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Map extends Entity {
	public static int TAG = gWorld.getNextTag();

	public static class PathPoint extends Vector {
		public gColor color = new gColor(1, 1, 1, 1);
	}

	public List<PathPoint> pathPoints;
	public List<PathPoint> groundPath;
	public List<PathPoint> leftBorders;
	public List<PathPoint> rightBorders;

	public Map(gWorld world) {
		super(world);
	}

	@Override
	public void create() {
		super.create();
		tag = TAG;

		pathPoints = new ArrayList<PathPoint>();
		groundPath = new ArrayList<PathPoint>();
		leftBorders = new ArrayList<PathPoint>();
		rightBorders = new ArrayList<PathPoint>();

		initScale(1, 1, 1);
		initColScale(4.25f, 1000000000F);

		setGroups(Scene.GROUP_CONCRETE);

		addMechanism(new ActionMechanism(this));

		addMechanism(new CollitionMechanism(this) {
			@Override
			public boolean isCollideX(Entity en) {
				return !super.isCollideX(en);
			}

			@Override
			public boolean isCollideY(Entity en) {
				return !super.isCollideY(en);
			}
		});

		addMechanism(new IntervalMechanism(this, 0) {
			@Override
			public void createSystem() {
				super.createSystem();
				float x = 0;
				for (int c = 0; c < 10000; c += 1) {
					if (c > 100)
						c += 100;
					PathPoint pp = new PathPoint();
					pp.set(x, c, 0);
					pathPoints.add(pp);
				}

				for (int c = 0; c < pathPoints.size() - 1; c++) {
					PathPoint pp = pathPoints.get(c);
					PathPoint pp2 = pathPoints.get(c + 1);

					PathPoint newpp = new PathPoint();
					newpp.color.set(0.2f, 0.2f, 0.2f);
					newpp.setByAngle(pp.angle(pp2) + (side ? 90 : -90), 3);
					newpp.plus(pp);
					groundPath.add(newpp);
					side = !side;
				}

				for (int c = 0; c < pathPoints.size() - 1; c++) {
					PathPoint pp = pathPoints.get(c);
					PathPoint pp2 = pathPoints.get(c + 1);

					PathPoint newpp = new PathPoint();
					newpp.color.set(0, 1, 0);
					newpp.setByAngle(pp.angle(pp2) + 90, 3);
					newpp.plus(pp);
					if (side)
						newpp.z += 1;
					leftBorders.add(newpp);
					side = !side;
				}

				for (int c = 0; c < pathPoints.size() - 1; c++) {
					PathPoint pp = pathPoints.get(c);
					PathPoint pp2 = pathPoints.get(c + 1);

					PathPoint newpp = new PathPoint();
					newpp.color.set(0, 1, 0);
					newpp.setByAngle(pp.angle(pp2) - 90, 3);
					newpp.plus(pp);
					if (side)
						newpp.z += 1;
					rightBorders.add(newpp);
					side = !side;
				}
			}

			boolean side = false;

			@Override
			public void die() {
				// TODO Auto-generated method stub
				super.die();
			}

			@Override
			public void tick() {

			}
		});

		addMechanism(new MeshMechanism(this, new Shape(new float[10000]), Shader.getShader("shaders/map.vert", "shaders/map.frag"), GL20.GL_TRIANGLE_STRIP, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(
				Usage.Color, 4, "a_color")) {
			public void setShaderParams(ShaderProgram shader) {
				shader.setUniformf("fog_color", Director.CLEAR_COLOR.r, Director.CLEAR_COLOR.g, Director.CLEAR_COLOR.b, Director.CLEAR_COLOR.a);
			}

			public float sortingValue() {
				return 100000000;
			}

			public void actualRender() {
				float[] verts = shape.vertices;
				int index = 0;
				for (int c = 0; c < leftBorders.size(); c++) {
					verts[index++] = leftBorders.get(c).x;
					verts[index++] = leftBorders.get(c).y;
					verts[index++] = leftBorders.get(c).z;
					verts[index++] = leftBorders.get(c).color.r;
					verts[index++] = leftBorders.get(c).color.g;
					verts[index++] = leftBorders.get(c).color.b;
					verts[index++] = leftBorders.get(c).color.a;
				}
				mesh.setVertices(verts);
				mesh.render(currentShader.getShader(), primitive, 0, index / 7);

				index = 0;
				for (int c = 0; c < groundPath.size(); c++) {
					verts[index++] = groundPath.get(c).x;
					verts[index++] = groundPath.get(c).y;
					verts[index++] = groundPath.get(c).z;
					verts[index++] = groundPath.get(c).color.r;
					verts[index++] = groundPath.get(c).color.g;
					verts[index++] = groundPath.get(c).color.b;
					verts[index++] = groundPath.get(c).color.a;
				}
				mesh.setVertices(verts);
				mesh.render(currentShader.getShader(), primitive, 0, index / 7);

				index = 0;
				for (int c = 0; c < rightBorders.size(); c++) {
					verts[index++] = rightBorders.get(c).x;
					verts[index++] = rightBorders.get(c).y;
					verts[index++] = rightBorders.get(c).z;
					verts[index++] = rightBorders.get(c).color.r;
					verts[index++] = rightBorders.get(c).color.g;
					verts[index++] = rightBorders.get(c).color.b;
					verts[index++] = rightBorders.get(c).color.a;
				}
				mesh.setVertices(verts);
				mesh.render(currentShader.getShader(), primitive, 0, index / 7);
			}

		});
	}
}
