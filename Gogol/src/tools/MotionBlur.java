package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;

public class MotionBlur {
	private int currentFrame = 0;
	private int framesSize;
	private TextureRegion m_fboRegion = null;
	private FrameBuffer m_fbos[] = null;
	private float m_fboScaler = 1.5f;

	public MotionBlur(int framesSize) {
		this.framesSize = framesSize;
	}

	public void beginRender() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		if (m_fbos == null) {
			m_fbos = new FrameBuffer[framesSize];
			for (int c = 0; c < m_fbos.length; c++) {
				m_fbos[c] = new FrameBuffer(Format.RGB565,
						(int) (width * m_fboScaler),
						(int) (height * m_fboScaler), false);
			}
			m_fboRegion = new TextureRegion();
		}

		m_fbos[currentFrame].begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public void endRender(SpriteBatch sb, Camera camera) {
		if (m_fbos != null) {
			m_fbos[currentFrame].end();

			camera.position.set(0, 0, 1);
			camera.update();

			Vector3 botleft = new Vector3(0, Gdx.graphics.getHeight(), 0);
			camera.unproject(botleft);

			Vector3 size = new Vector3(Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight(), 0);
			camera.unproject(size);

			sb.setProjectionMatrix(camera.combined);
			sb.begin();

			for (int c = 0; c < framesSize; c++) {
				if (c == currentFrame) {
					continue;
				}
				m_fboRegion.setTexture(m_fbos[c].getColorBufferTexture());
				m_fboRegion.setRegion(0, 0, m_fbos[c].getWidth(),
						m_fbos[c].getHeight());
				m_fboRegion.flip(false, true);
				float alpha;
				alpha = (c - currentFrame);
				if (alpha > 0) {
					alpha = 10 - c;
				} else {
					alpha = 10 + alpha;
				}

				sb.setColor(1, 1, 1, alpha / 10.0f);
				sb.draw(m_fboRegion, botleft.x, botleft.y,
						Math.abs(size.x) * 2, Math.abs(size.y) * 2);
			}
			m_fboRegion
					.setTexture(m_fbos[currentFrame].getColorBufferTexture());
			m_fboRegion.setRegion(0, 0, m_fbos[currentFrame].getWidth(),
					m_fbos[currentFrame].getHeight());
			m_fboRegion.flip(false, true);

			sb.setColor(1, 1, 1, 1);
			sb.draw(m_fboRegion, botleft.x, botleft.y, Math.abs(size.x) * 2,
					Math.abs(size.y) * 2);

			sb.end();

			currentFrame++;
			if (currentFrame == 10) {
				currentFrame = 0;
			}
		}

	}
}
