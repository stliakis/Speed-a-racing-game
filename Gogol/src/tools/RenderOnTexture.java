package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class RenderOnTexture {
	
	private float m_fboScaler = 1f;
	private boolean m_fboEnabled = true;
	private FrameBuffer m_fbo = null;
	public TextureRegion m_fboRegion = null;
	public RenderOnTexture(float scale) {
	    int width = (int) (Gdx.graphics.getWidth()*scale);
	    int height = (int) (Gdx.graphics.getHeight()*scale);
        m_fbo = new FrameBuffer(Format.RGB565, (int)(width * m_fboScaler), (int)(height * m_fboScaler), true);
        m_fboRegion = new TextureRegion(m_fbo.getColorBufferTexture());
        m_fboRegion.flip(false,false);
	}
	public RenderOnTexture(float scale,boolean flipx,boolean flipy) {
	    int width = (int) (Gdx.graphics.getWidth()*scale);
	    int height = (int) (Gdx.graphics.getHeight()*scale);
        m_fbo = new FrameBuffer(Format.RGB565, (int)(width * m_fboScaler), (int)(height * m_fboScaler), true);
        m_fboRegion = new TextureRegion(m_fbo.getColorBufferTexture());
        m_fboRegion.flip( flipx,flipy);
	}
	public void begin(){
	    if(m_fboEnabled)    
	    {                  
	        m_fbo.begin();
			Gdx.gl.glClearColor(0, 0,0,0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	    }
	}
	public void beginWithoutClear(){
	    if(m_fboEnabled)    
	    {                  
	        m_fbo.begin();
	    }
	}
	public TextureRegion end(){
	    if(m_fbo != null)
	    {
	        m_fbo.end();
	        return m_fboRegion;
	    }  
	    return m_fboRegion;
	}
}
