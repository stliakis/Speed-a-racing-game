package tools.world;

import tools.SpriteBatch;
import tools.general.Vector;
import tools.general.gColor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SpriteWorldRenderer extends WorldRenderer{
	private Camera camera;
	ShaderProgram defaultShader;

	private ShaderProgram shader;
	private SpriteBatch spritebatch;
	public SpriteWorldRenderer(Camera camera) {
		super(camera);
		this.camera = camera;
		spritebatch = new SpriteBatch(2000);
		spritebatch.setProjectionMatrix(camera.combined);
		spritebatch.setBlendFunction(BLEND_FUN1, BLEND_FUN2);

		ShaderProgram.pedantic = false;
		defaultShader = new ShaderProgram(Gdx.files.internal(
				"shaders/default.ver").readString(), Gdx.files.internal(
				"shaders/default.frag").readString());
		setDefaultShader();
	}

	public void DrawCenter(Sprite sprite, Vector pos, Vector scale,
			gColor color, float angle) {
		if (color.a <= 0.01f) {
			return;
		}
		sprite.setSize(scale.x, scale.y);
		sprite.setPosition(pos.x - scale.x / 2, pos.y - scale.y / 2);
		sprite.setColor(color.r, color.g, color.b, color.a);
		sprite.setRotation(angle);
		spritebatch.draw(sprite.getTexture(), sprite.getVertices(), pos.z);

	}

	public void DrawLine(Sprite sprite, float x, float y, float x1, float y1,
			gColor color, float width) {
		Vector.vector.set(Vector.dis(x, y, x1, y1), width);
		Vector.vector2.set(x1, y1);
		sprite.setOrigin(Vector.vector.x / 2, Vector.vector.y / 2);
		DrawCenter(sprite, Vector.vector2, Vector.vector, color, Vector.vector3
				.set(x, y).angle(x1, y1));
	}

	public Camera getCamera() {
		return camera;
	}

	public ShaderProgram getCurShader() {
		return shader;
	}

	public ShaderProgram getDefaultShader() {
		return defaultShader;
	}

	public ShaderProgram getShader() {
		return shader;
	}

	public SpriteBatch getSpritebatch() {
		return spritebatch;
	}

	public void RenderEnd() {
		spritebatch.end();
		
	}

	public void RenderStart() {
		spritebatch.setProjectionMatrix(camera.combined);
		spritebatch.enableBlending();
		spritebatch.setBlendFunction(BLEND_FUN1, BLEND_FUN2);
		spritebatch.setProjectionMatrix(camera.combined);
		spritebatch.begin();
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public void setDefaultShader() {
		if (!defaultShader.isCompiled()) {
			Gdx.app.log("Problem loading shader:", defaultShader.getLog());
		}
		shader = defaultShader;
		spritebatch.setShader(defaultShader);
	}

	public void setDefaultShader(ShaderProgram defaultShader) {
		this.defaultShader = defaultShader;
	}

	public void setShader(ShaderProgram shader) {
		if (!shader.isCompiled()) {
			Gdx.app.log("Problem loading shader:", shader.getLog());
		}
		this.shader = shader;
		spritebatch.setShader(shader);
	}

	public void setSpritebatch(SpriteBatch spritebatch) {
		this.spritebatch = spritebatch;
	}

}
