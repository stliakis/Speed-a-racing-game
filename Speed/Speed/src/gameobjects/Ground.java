package gameobjects;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import tools.Action;
import tools.Actions;
import tools.Director;
import tools.Shapes;
import tools.world.Entity;
import tools.world.gWorld;
import tools.world.mechanisms.ActionMechanism;
import tools.world.mechanisms.IntervalMechanism;
import tools.world.mechanisms.MeshMechanism;
import tools.world.mechanisms.SpriteMechanism;

public class Ground extends Entity {
	public static int TAG = gWorld.getNextTag();

	public Ground(gWorld world) {
		super(world);
	}

	@Override
	public void create() {
		super.create();
		tag = TAG;

		initScale(1000000, 1000000);
		initColor(0.5f, 0.5f, 1, 1);

		addMechanism(new ActionMechanism(this));

		addMechanism(new SpriteMechanism(this, "sprites/map.png", "shaders/ground.vert", "shaders/ground.frag", true) {
			@Override
			public void setShaderParams(ShaderProgram shader) {
				shader.setUniformf("fog_color", Director.CLEAR_COLOR.r, Director.CLEAR_COLOR.g, Director.CLEAR_COLOR.b, Director.CLEAR_COLOR.a);
			}
		});
	}

}
